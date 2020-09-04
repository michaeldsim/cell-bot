package Moderation;

import static discordbot.Ref.errorMessageParameter;
import static discordbot.Ref.errorMessagePermission;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Mute extends ModCommand {
	// TODO ALL

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Called");
		if (e.getMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
			System.out.println("Valid perms");
			if (e.getMessage().getMentionedMembers().get(0).isOwner()) {
				e.getChannel().sendMessage("**ERROR:** You cannot mute the server owner!").queue();
			} else if (e.getMessage().getMentionedMembers().get(0).getVoiceState().isGuildMuted()) {
				e.getGuild().getController().setMute(e.getMessage().getMentionedMembers().get(0), false).queue();
				;
			} else if (!e.getMessage().getMentionedMembers().get(0).getVoiceState().isGuildMuted()) {
				e.getGuild().getController().setMute(e.getMessage().getMentionedMembers().get(0), true).queue();
				;
				System.out.println("should mute " + e.getMessage().getMentionedMembers().get(0).getEffectiveName());
			} else {
				errorMessageParameter(e.getTextChannel(), e.getAuthor());
			}
		} else {
			System.out.println("invalid perms");
			errorMessagePermission(e.getTextChannel(), e.getAuthor());
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "mute"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Mute a user";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mute";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "mute [@user] to toggle the mute on the user!"));
	}

}
