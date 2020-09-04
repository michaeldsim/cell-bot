package Commands;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ping extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		e.getChannel().sendMessage("Pong! `" + e.getJDA().getPing() + "ms`").queue();
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "ping"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Shows the bot's ping to the server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Ping";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("Just enter " + Ref.prefix + "ping");
	}
}
