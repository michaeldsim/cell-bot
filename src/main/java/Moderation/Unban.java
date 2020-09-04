package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Unban extends ModCommand {
	// TODO ALL

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		if (e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			if (args.length == 2) {
				e.getGuild().getController().unban(e.getMessage().getMentionedMembers().get(0).getUser()).queue();
				e.getChannel()
						.sendMessage("The user: **" + e.getMessage().getMentionedMembers().get(0).getEffectiveName()
								+ "** has been kicked from the server!")
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
		return Arrays.asList((Ref.prefix + "unban"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Unbans a user from the server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Unban";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(
				(Ref.prefix + "unban [@user] to unban a user and delete messages by them from x amount of days\n"));
	}
}