package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MassBan extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		int num = e.getMessage().getMentionedMembers().size();
		StringBuilder users = new StringBuilder();

		if (e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			if (args.length == num + 2) {

				for (Member member : e.getMessage().getMentionedMembers()) {
					e.getGuild().getController().ban(member, 1).queue();
					users.append(member.getEffectiveName() + ", ");
				}

				e.getChannel().sendMessage("The users: **" + users.toString() + "** have been banned from the server!")
						.queue();

			} else if (args.length > num + 2) {
				String reason = Ref.createString(args, num + 2);

				for (Member member : e.getMessage().getMentionedMembers()) {
					e.getGuild().getController().ban(member, 1, reason).queue();
					users.append(member.getEffectiveName() + ", ");
				}

				e.getChannel().sendMessage(
						"The user: **" + users.toString() + "** has been kicked from the server! Reason: " + reason)
						.queue();
			} else {
				Ref.errorMessageParameter(e.getTextChannel(), e.getAuthor());
			}
		} else {
			Ref.errorMessagePermission(e.getTextChannel(), e.getAuthor());
		}

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "mban"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Ban multiple users from the server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Mass Ban";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix
				+ "ban [@user] [daysOfMessages] to ban a user and delete messages by them from x amount of days\n "
				+ Ref.prefix
				+ "[@user] [daysOfMessages] [reason] to ban a user and delete messages by them from x amount of days with a provided reason"));
	}
}