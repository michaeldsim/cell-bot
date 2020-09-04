package Moderation;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChangePrefix extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		if (Ref.checkAdmin(e.getMember())) {
			if (args.length == 1) {
				e.getChannel().sendMessage("Prefix: " + Ref.prefix);
			} else {
				e.getChannel().sendMessage("Old prefix: " + Ref.prefix).queue();
				Ref.prefix = args[1];

				e.getChannel().sendMessage("New prefix: " + Ref.prefix).queue();
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
		return Arrays.asList((Ref.prefix + "prefix"), (Ref.prefix + "changeprefix"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Change the prefix";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Change prefix";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("Changes the prefix");
	}
}
