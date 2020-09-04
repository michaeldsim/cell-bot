package Music;

import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class Join extends MusicCommand {

	public Join(AudioPlayerManager playerManager, Map<String, GuildMusicManager> musicManagers) {
		super(playerManager, musicManagers);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		Guild guild = e.getGuild();
		if (args.length == 1) // No channel name was provided to search for.
		{
			guild.getAudioManager().setSendingHandler(mng.sendHandler);
			guild.getAudioManager().openAudioConnection(channel);
			e.getChannel().sendMessage("I have joined the " + channel.getName() + " channel!").queue();
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
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return null;
	}

}
