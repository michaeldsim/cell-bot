package Music;

import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class MusicCommand extends ListenerAdapter {
	public static final int DEFAULT_VOLUME = 35; // (0 - 150, where 100 is default max volume)

	private AudioPlayerManager playerManager;
	private Map<String, GuildMusicManager> musicManagers;

	public MusicCommand(AudioPlayerManager playerManager, Map<String, GuildMusicManager> musicManagers) {
		this.playerManager = playerManager;
		this.musicManagers = musicManagers;
	}

	public abstract void onCommand(MessageReceivedEvent e, String[] args);

	public abstract List<String> getAliases();

	public abstract String getDescription();

	public abstract String getName();

	public abstract List<String> getUsageInstructions();

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if (e.getAuthor().isBot() && !respondToBots())
			return;
		if (containsCommand(e.getMessage()))
			onCommand(e, commandArgs(e.getMessage()));

		Guild guild = e.getGuild();
		GuildMusicManager mng = getMusicManager(guild);
		AudioPlayer player = mng.player;
		TrackScheduler scheduler = mng.scheduler;
		VoiceChannel channel;

		channel = e.getMember().getVoiceState().getChannel();
	}

	protected boolean containsCommand(Message message) {
		return getAliases().contains(commandArgs(message)[0].toLowerCase());
	}

	protected String[] commandArgs(Message message) {
		return commandArgs(message.getContentDisplay());
	}

	protected String[] commandArgs(String string) {
		return string.split(" ");
	}

	protected Message sendMessage(MessageReceivedEvent e, Message message) {
		if (e.isFromType(ChannelType.PRIVATE))
			return e.getPrivateChannel().sendMessage(message).complete();
		else
			return e.getTextChannel().sendMessage(message).complete();
	}

	protected Message sendMessage(MessageReceivedEvent e, String message) {
		return sendMessage(e, new MessageBuilder().append(message).build());
	}

	protected boolean respondToBots() {
		return false;
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

}
