package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ban extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		if (e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			if (args.length == 2) {
				e.getGuild().getController().ban(e.getMessage().getMentionedMembers().get(0), 1).queue();
				e.getChannel()
						.sendMessage("The user: **" + e.getMessage().getMentionedMembers().get(0).getEffectiveName()
								+ "** has been banned from the server!")
						.queue();

			} else if (args.length > 2) {
				String reason = Ref.createString(args, 2);

				e.getGuild().getController().ban(e.getMessage().getMentionedMembers().get(0), 1, reason).queue();
				e.getChannel()
						.sendMessage("The user: **" + e.getMessage().getMentionedMembers().get(0).getEffectiveName()
								+ "** has been banned from the server! Reason: " + reason)
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
		return Arrays.asList((Ref.prefix + "ban"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Ban a user from the server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Ban";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "ban [@user]  to ban a user and delete messages by them within the day\n "
				+ Ref.prefix
				+ "[@user] [reason] to ban a user and delete messages by them within the day with a provided reason"));
	}
}
