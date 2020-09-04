package Commands;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import discordbot.Ref;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RandomUser extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		Random rand = new Random();
		List<Member> members = e.getGuild().getMembers();

		int max = e.getGuild().getMembers().size();
		int num = rand.nextInt(max);

		Member chosen = members.get(num);

		if (chosen.getNickname() == null) {
			e.getChannel().sendMessage("**" + chosen.getEffectiveName() + "** has been chosen!").queue();
		} else {
			e.getChannel().sendMessage("**" + chosen.getNickname() + "** has been chosen!").queue();
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "random"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Rolls a random user from the server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Random user generator";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "random will generate a random user as a reply"));
	}

}
