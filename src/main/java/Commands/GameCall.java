package Commands;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GameCall extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		MessageBuilder msgBuilder = new MessageBuilder();

		msgBuilder
				.append(e.getJDA().getRolesByName(args[1], true).get(0).getAsMention() + ", Does anyone want to play?");
		Message msg = msgBuilder.build();

		e.getChannel().sendMessage(msg).queue();

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "lfg"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Will mention a role asking if they want to play";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Looking for group";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "lfg") + "[role name] will ask if anyone wants to play");
	}

}
