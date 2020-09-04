package Commands;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Test extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		System.out.print(e.getMessage().getMentionedMembers().toString().replaceAll("[\\[\\](){}]", ""));
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "test"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Test command";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Test";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("test command");
	}
}
