package Commands;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Jerremy extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		e.getChannel().sendMessage("such a stinky boy need febreze").queue();
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "jerremy"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Reminder for Jerremy that he needs some Febreze";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Jerremy Command";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("Just enter " + Ref.prefix + "jerremy");
	}

}
