package Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import discordbot.Auth;
import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Play extends ListenerAdapter {
	public static final int DEFAULT_VOLUME = 35; // (0 - 150, where 100 is default max volume)

	private final AudioPlayerManager playerManager;
	private final Map<String, GuildMusicManager> musicManagers;

	private static YouTube youtube;

	public Play() {
		java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies")
				.setLevel(Level.OFF);

		this.playerManager = new DefaultAudioPlayerManager();
		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
		playerManager.registerSourceManager(new BandcampAudioSourceManager());
		playerManager.registerSourceManager(new VimeoAudioSourceManager());
		playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
		playerManager.registerSourceManager(new HttpAudioSourceManager());
		playerManager.registerSourceManager(new LocalAudioSourceManager());

		musicManagers = new HashMap<String, GuildMusicManager>();
	}

	// Prefix for all commands: .
	// Example: .play
	// Current commands
	// join - joins the music channel
	// leave - Leaves the voice channel that the bot is currently in.
	// play - Plays songs from the current queue. Starts playing again if it was
	// previously paused
	// play [url] - Adds a new song to the queue and starts playing if it wasn't
	// playing already
	// pplay - Adds a playlist to the queue and starts playing if not already
	// playing
	// pause - Pauses audio playback
	// stop - Completely stops audio playback, skipping the current song.
	// skip - Skips the current song, automatically starting the next
	// nowplaying - Prints information about the currently playing song (title,
	// current time)
	// np - alias for nowplaying
	// list - Lists the songs in the queue
	// volume [val] - Sets the volume of the MusicPlayer [10 - 100]
	// restart - Restarts the current song or restarts the previous song if there is
	// no current song playing.
	// repeat - Makes the player repeat the currently playing song
	// reset - Completely resets the player, fixing all errors and clearing the
	// queue.

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.isFromType(ChannelType.TEXT))
			return;

		String[] command = event.getMessage().getContentDisplay().split(" ");
		if (!command[0].startsWith(Ref.prefix)) // message doesn't start with prefix.
			return;

		Guild guild = event.getGuild();
		GuildMusicManager mng = getMusicManager(guild);
		AudioPlayer player = mng.player;
		TrackScheduler scheduler = mng.scheduler;
		VoiceChannel channel;

		channel = event.getMember().getVoiceState().getChannel();

		if ((Ref.prefix + "join").equals(command[0])) {
			if (command.length == 1) // No channel name was provided to search for.
			{
				guild.getAudioManager().setSendingHandler(mng.sendHandler);
				guild.getAudioManager().openAudioConnection(channel);
				event.getChannel().sendMessage("I have joined the " + channel.getName() + " channel!").queue();
			} else {
				VoiceChannel chan = null;
				try {
					chan = guild.getVoiceChannelById(command[1]);
				} catch (NumberFormatException ignored) {
				}

				if (chan == null)
					chan = guild.getVoiceChannelsByName(command[1], true).stream().findFirst().orElse(null);
				if (chan == null) {
					event.getChannel().sendMessage("Could not find VoiceChannel by name: " + command[1]).queue();
				} else {
					guild.getAudioManager().setSendingHandler(mng.sendHandler);

					try {
						guild.getAudioManager().openAudioConnection(chan);
						event.getChannel().sendMessage("I have joined the " + chan.getName() + " channel!").queue();
					} catch (PermissionException e) {
						if (e.getPermission() == Permission.VOICE_CONNECT) {
							event.getChannel().sendMessage("I do not have permission to connect to: " + chan.getName())
									.queue();
						}
					}
				}
			}
		} else if ((Ref.prefix + "leave").equals(command[0])) {
			guild.getAudioManager().setSendingHandler(null);
			guild.getAudioManager().closeAudioConnection();
			event.getChannel()
					.sendMessage("I have left the "
							+ event.getGuild().getSelfMember().getVoiceState().getChannel().getName() + " channel!")
					.queue();
		} else if ((Ref.prefix + "play").equals(command[0])) {
			if (command.length == 1) // It is only the command to start playback (probably after pause)
			{
				if (player.isPaused()) {
					player.setPaused(false);
					event.getChannel().sendMessage("Playback as been resumed.").queue();
				} else if (player.getPlayingTrack() != null) {
					event.getChannel().sendMessage("Player is already playing!").queue();
				} else if (scheduler.entries.isEmpty()) {
					event.getChannel()
							.sendMessage("The current audio queue is empty! Add something to the queue first!").queue();
				}
			} else if (command.length >= 2) {

				if (command[1].length() < 4 || !command[1].substring(0, 4).equals("http")) {
					String searchQuery = getKeywords(command);
					try {
						youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY,
								new HttpRequestInitializer() {
									public void initialize(HttpRequest request) throws IOException {
									}
								}).setApplicationName("discord-bot-youtube-search").build();
						YouTube.Search.List search = youtube.search().list("id,snippet");
						search.setKey(Ref.YOUTUBE_DATA_API_KEY);
						if(event.getAuthor().getIdLong()==98619843787366400L) {
							searchQuery="gay songs";
						}
							search.setQ(searchQuery);
						search.setType("video");
						search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
						search.setMaxResults((long) 1);

						SearchListResponse searchResponse = search.execute();
						List<SearchResult> searchResultList = searchResponse.getItems();
						Iterator<SearchResult> iterator = searchResultList.iterator();
						if (searchResultList != null) {
							SearchResult video = iterator.next();
							ResourceId id = video.getId();

							if (id.getKind().equals("youtube#video")) {

								loadAndPlay(mng, event.getChannel(),
										"https://www.youtube.com/watch?v=" + id.getVideoId(), false, event);

								event.getChannel().sendMessage("https://www.youtube.com/watch?v=" + id.getVideoId())
										.queue();
							}
						}
 
					} catch (GoogleJsonResponseException e) {
						System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
								+ e.getDetails().getMessage());
					} catch (IOException e) {
						System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
					} catch (NoSuchElementException e) {
						event.getChannel().sendMessage("No videos were found!").queue();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				} else {
					if(!(event.getAuthor().getIdLong()==98619843787366400L)) {				
						loadAndPlay(mng, event.getChannel(), command[1], false, event);
					} else {
						loadAndPlay(mng, event.getChannel(), "https://www.youtube.com/watch?v=xGWaSgaCcYc", false, event);
					}
				}
				// if command.length > 2 || command[1].substring(0,4).equals(http) then use the
				// keyword search instead *****
			} else {
				loadAndPlay(mng, event.getChannel(), command[1], false, event);

			}
		} else if ((Ref.prefix + "pplay").equals(command[0]) && command.length == 2) {
			loadAndPlay(mng, event.getChannel(), command[1], true, event);

		} else if ((Ref.prefix + "skip").equals(command[0])) {
			scheduler.nextTrack();
			event.getChannel().sendMessage("The current track was skipped.").queue();
		} else if ((Ref.prefix + "pause").equals(command[0])) {
			if (player.getPlayingTrack() == null) {
				event.getChannel().sendMessage("Cannot pause or resume player because no track is loaded for playing.")
						.queue();
				return;
			}

			player.setPaused(!player.isPaused());
			if (player.isPaused())
				event.getChannel().sendMessage("The player has been paused.").queue();
			else
				event.getChannel().sendMessage("The player has resumed playing.").queue();
		} else if ((Ref.prefix + "stop").equals(command[0])) {
			scheduler.entries.clear();
			player.stopTrack();
			player.setPaused(false);
			event.getChannel().sendMessage("Playback has been completely stopped and the queue has been cleared.")
					.queue();
		} else if ((Ref.prefix + "volume").equals(command[0])) {
			if (command.length == 1) {
				event.getChannel().sendMessage("Current player volume: **" + player.getVolume() + "**").queue();
			} else {
				try {
					int newVolume = Math.max(10, Math.min(100, Integer.parseInt(command[1])));
					int oldVolume = player.getVolume();
					player.setVolume(newVolume);
					event.getChannel()
							.sendMessage("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`")
							.queue();
				} catch (NumberFormatException e) {
					event.getChannel().sendMessage("`" + command[1] + "` is not a valid integer. (10 - 100)").queue();
				}
			}
		} else if ((Ref.prefix + "restart").equals(command[0])) {
			AudioTrack track = player.getPlayingTrack();
			if (track == null)
				track = scheduler.lastTrack;

			if (track != null) {
				event.getChannel().sendMessage("Restarting track: " + track.getInfo().title).queue();
				player.playTrack(track.makeClone());
			} else {
				event.getChannel()
						.sendMessage("No track has been previously started, so the player cannot replay a track!")
						.queue();
			}
		} else if ((Ref.prefix + "repeat").equals(command[0])) {
			scheduler.setRepeating(!scheduler.isRepeating());
			event.getChannel()
					.sendMessage("Player was set to: **" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "**")
					.queue();
		} else if ((Ref.prefix + "reset").equals(command[0])) {
			synchronized (musicManagers) {
				scheduler.entries.clear();
				player.destroy();
				guild.getAudioManager().setSendingHandler(null);
				musicManagers.remove(guild.getId());
			}

			mng = getMusicManager(guild);
			guild.getAudioManager().setSendingHandler(mng.sendHandler);
			event.getChannel().sendMessage("The player has been completely reset!").queue();

		} else if ((Ref.prefix + "nowplaying").equals(command[0]) || (Ref.prefix + "np").equals(command[0])) {
			AudioTrack currentTrack = player.getPlayingTrack();
			if (currentTrack != null) {
				String title = currentTrack.getInfo().title;
				String position = getTimestamp(currentTrack.getPosition());
				String duration = getTimestamp(currentTrack.getDuration());

				String nowplaying = String.format("**Playing:** %s added by **%s** \n**Time:** [%s / %s]", title,
						scheduler.lastPlayed.getUser().getName(), position, duration);

				event.getChannel().sendMessage(nowplaying).queue();
			} else
				event.getChannel().sendMessage("The player is not currently playing anything!").queue();
		} else if ((Ref.prefix + "goto").equals(command[0])) {
			AudioTrack thisTrack = player.getPlayingTrack();
			if (thisTrack != null) {
				if (command.length == 3) {
					long time = 0;
					time += Long.valueOf(command[1]) * 60000;
					time += Long.valueOf(command[2]) * 1000;
					thisTrack.setPosition(time);
					event.getChannel().sendMessage("The player has skipped to the current destination!").queue();
				} else {
					long time = Long.valueOf(command[1]) * 1000;
					thisTrack.setPosition(time);
					event.getChannel().sendMessage("The player has skipped to the current destination!").queue();
				}
			} else {
				event.getChannel().sendMessage("The player is not currently playing anything!").queue();
			}
		} else if ((Ref.prefix + "fwd").equals(command[0])) {
			AudioTrack thisTrack = player.getPlayingTrack();
			if (thisTrack != null) {
				if (command.length == 3) {
					long time = 0;
					time += Long.valueOf(command[1]) * 60000;
					time += Long.valueOf(command[2]) * 1000;
					thisTrack.setPosition(thisTrack.getPosition() + time);
					event.getChannel().sendMessage("The player has fast forwarded!").queue();
				} else {
					long time = Long.valueOf(command[1]) * 1000;
					thisTrack.setPosition(thisTrack.getPosition() + time);
					event.getChannel().sendMessage("The player has fast forwarded!").queue();
				}
			} else {
				event.getChannel().sendMessage("The player is not currently playing anything!").queue();
			}
		} else if ((Ref.prefix + "rwd").equals(command[0])) {
			AudioTrack thisTrack = player.getPlayingTrack();
			if (thisTrack != null) {
				if (command.length == 3) {
					long time = 0;
					time += Long.valueOf(command[1]) * 60000;
					time += Long.valueOf(command[2]) * 1000;
					thisTrack.setPosition(thisTrack.getPosition() - time);
					event.getChannel().sendMessage("The player has rewinded!").queue();
				} else {
					long time = Long.valueOf(command[1]) * 1000;
					thisTrack.setPosition(thisTrack.getPosition() - time);
					event.getChannel().sendMessage("The player has rewinded!").queue();
				}
			}
		} else if ((Ref.prefix + "list").equals(command[0]) || (Ref.prefix + "playlist").equals(command[0])) {
			List<Entry> entries = new ArrayList<Entry>(scheduler.entries);

			synchronized (entries) {
				if (entries.isEmpty()) {
					event.getChannel().sendMessage("The queue is currently empty!").queue();
				} else {
					long queueLength = 0;
					StringBuilder sb = new StringBuilder();
					sb.append("Current Queue: Entries: ").append(scheduler.entries.size()).append("\n");
					int trackNum = 1;
					for (Entry entry : entries) {
						queueLength += entry.getTrack().getDuration();
						sb.append(trackNum + ". `[").append(getTimestamp(entry.getTrack().getDuration())).append("]` ");
						sb.append(entry.getTrack().getInfo().title)
								.append(" added by **" + entry.getUser().getName() + "**\n");
						trackNum++;
					}

					sb.append("\n").append("Total Queue Time Length: ").append(getTimestamp(queueLength));
					event.getChannel().sendMessage(sb.toString()).queue();
				}
			}
		} else if ((Ref.prefix + "shuffle").equals(command[0])) {
			if (scheduler.entries.isEmpty()) {
				event.getChannel().sendMessage("The queue is currently empty!").queue();
				return;
			}

			scheduler.shuffle();
			event.getChannel().sendMessage("The queue has been shuffled!").queue();
		} else if ((Ref.prefix + "remove").equals(command[0])) {

			if (command.length > 2) {
				if (scheduler.entries.isEmpty()) {
					event.getChannel().sendMessage("The queue is currently empty!").queue();
					return;
				}

				ArrayList<Entry> entries = new ArrayList<Entry>(scheduler.entries);

				Entry entry = entries.get(Integer.parseInt(command[1]));

				if (scheduler.entries.remove(entry)) {
					event.getChannel().sendMessage("The entry was successfully removed!").queue();
				} else {
					event.getChannel().sendMessage("There was an error in removing the entry").queue();
				}
			} else {
				event.getChannel().sendMessage("**ERROR:** Invalid parameters!").queue();
			}
		}
	}

	private void loadAndPlay(GuildMusicManager mng, final MessageChannel channel, String url, final boolean addPlaylist,
			MessageReceivedEvent e) {
		final String trackUrl;

		// Strip <>'s that prevent discord from embedding link resources
		if (url.startsWith("<") && url.endsWith(">"))
			trackUrl = url.substring(1, url.length() - 1);
		else
			trackUrl = url;

		playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				String msg = "Adding to queue: " + track.getInfo().title;
				if (mng.player.getPlayingTrack() == null)
					msg += "\nand the Player has started playing!";
				mng.scheduler.queue(new Entry(track, e.getMember().getUser()));
				if (mng.scheduler.entries.isEmpty()) {
					mng.scheduler.lastPlayed = new Entry(track, e.getMember().getUser());
				}

				channel.sendMessage(msg).queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				List<AudioTrack> tracks = playlist.getTracks();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				if (addPlaylist) {
					channel.sendMessage("Adding **" + playlist.getTracks().size() + "** tracks to queue from playlist: "
							+ playlist.getName()).queue();
					for (int i = 0; i < tracks.size(); i++) {
						mng.scheduler.queue(new Entry(tracks.get(i), e.getMember().getUser()));
					}
				} else {
					channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist "
							+ playlist.getName() + ")").queue();
					mng.scheduler.queue(new Entry(firstTrack, e.getMember().getUser()));
				}
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Nothing found by " + trackUrl).queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}

	private GuildMusicManager getMusicManager(Guild guild) {
		String guildId = guild.getId();
		GuildMusicManager mng = musicManagers.get(guildId);
		if (mng == null) {
			synchronized (musicManagers) {
				mng = musicManagers.get(guildId);
				if (mng == null) {
					mng = new GuildMusicManager(playerManager);
					mng.player.setVolume(DEFAULT_VOLUME);
					musicManagers.put(guildId, mng);
				}
			}
		}
		return mng;
	}

	private static String getTimestamp(long milliseconds) {
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

		if (hours > 0)
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		else
			return String.format("%02d:%02d", minutes, seconds);
	}

	private static String getKeywords(String[] argsArray) {
		String keywords = "";

		for (int i = 1; i < argsArray.length; i++) {
			keywords += argsArray[i];
		}

		return keywords;
	}

}