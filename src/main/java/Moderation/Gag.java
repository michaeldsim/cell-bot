package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Gag extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		// dr gero 437478407081558026
		// if (args.length == 1 || args.length > 2) {
		// Ref.errorMessageParameter(e.getTextChannel(), e.getMember().getUser());
		// } else {
		System.out.println(args.length);
		if (e.getGuild().getId().equals("95627288946675712")) {
			Member target = e.getMessage().getMentionedMembers().get(0);
			Role muted = e.getGuild().getRoleById("462348938775625728");
			Role gero = e.getGuild().getRoleById("437478407081558026");

			if (e.getMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
				if (target.getRoles().contains(muted)) {
					if (target.getUser().getId().equals("96106301649600512")) {
						e.getGuild().getController().addSingleRoleToMember(target, gero).queue();
					}
					e.getGuild().getController().removeSingleRoleFromMember(target, muted).queue();
				} else {
					e.getGuild().getController().addSingleRoleToMember(target, muted).queue();
					if (target.getUser().getId().equals("96106301649600512")) {
						e.getGuild().getController().removeSingleRoleFromMember(target, gero).queue();
						;
					}
				}
			} else {
				Ref.errorMessagePermission(e.getTextChannel(), e.getMember().getUser());
			}
		} else {
		}
	}
	// }

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "gag"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "This will prevent users from typing in the chat";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Gag";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "gag [@user] to prevent the user from typing in the chat");
	}

}
