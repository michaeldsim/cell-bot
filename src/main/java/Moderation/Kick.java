package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Kick extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Called");
		if (e.getMessage().getMember().hasPermission(Permission.KICK_MEMBERS)) {
			System.out.println("valid perms");
			if (args.length == 2) {
				e.getGuild().getController().kick(e.getMessage().getMentionedMembers().get(0)).queue();
				System.out
						.println("should kick user " + e.getMessage().getMentionedMembers().get(0).getEffectiveName());
				e.getChannel()
						.sendMessage("The user: **" + e.getMessage().getMentionedMembers().get(0).getEffectiveName()
								+ "** has been kicked from the server!")
						.queue();
			} else if (args.length > 2) {
				String reason = Ref.createString(args, 2);

				e.getGuild().getController().kick(e.getMessage().getMentionedMembers().get(0), reason).queue();
				e.getChannel()
						.sendMessage("The user: **" + e.getMessage().getMentionedMembers().get(0).getEffectiveName()
								+ "** has been kicked from the server! Reason: " + reason)
						.queue();

				System.out.println("should kick user " + e.getMessage().getMentionedMembers().get(0).getEffectiveName()
						+ " for " + reason);
			} else {
				e.getChannel().sendMessage(e.getAuthor().getName() + ", **ERROR:** Invalid parameters!").queue();
			}
		} else {
			e.getChannel().sendMessage(
					e.getAuthor().getName() + ", **ERROR:** You do not have the permissions to perform this command!")
					.queue();
		}

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "kick"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Kick a user from the server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Kick";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "kick [@user] to kick a user\n " + Ref.prefix
				+ "[@user] [reason] to kick user with a provided reason"));
	}

}
