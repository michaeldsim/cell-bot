package Commands;

import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Roll extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		int max;
		int min;
		int roll;

		if (args.length == 1) {
			max = 1;
			min = 6;
		} else if (args.length == 2) {
			min = 1;
			try {
				max = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				sendMessage(e, "The provided maximum is not an integer! Provided value: " + args[1]);
				return;
			}
		} else {
			try {
				min = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				sendMessage(e, "The provided minimum is not an integer! Provided value: " + args[1]);
				return;
			}

			try {
				max = Integer.parseInt(args[2]);
			} catch (NumberFormatException ex) {
				sendMessage(e, "The provided maximum is not an integer! Provided value: " + args[2]);
				return;
			}
		}
		// Instead of erroring when the upper and lower are out of order, just flip
		// them.
		if (min > max) {
			int temp = max;
			max = min;
			min = temp;
		}

		roll = (int) ((Math.random() * (max - min)) + min);
		sendMessage(e, "Rolled dice with range of [" + min + " - " + max + "] and got: **" + roll + "**");
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "roll"), (Ref.prefix + "dice"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Rolls the dice and generates a number value in between";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Dice Roll";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "roll [num] [num]. Order does not matter."));
	}
}
