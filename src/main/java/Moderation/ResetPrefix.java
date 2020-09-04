package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ResetPrefix extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		if (Ref.checkAdmin(e.getMember())) {
			Ref.prefix = "!";

			e.getChannel().sendMessage("Server prefix has been reset to '!'").queue();

		} else {
			Ref.errorMessagePermission(e.getTextChannel(), e.getAuthor());
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList("!resetprefix");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Resets the channel prefix to !";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Reset prefix";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("Use to reset the channel prefix to '!'");
	}

}
