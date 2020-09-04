package Moderation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import discordbot.Ref;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Clear extends ModCommand {

	@Override
	public void onCommand(MessageReceivedEvent event, String[] args) {
		TextChannel textChannel = event.getTextChannel();
		if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			if (args.length >= 3) {
				ArrayList<Message> msgs = new ArrayList<Message>();

				int i = Integer.parseInt(args[1]); // number of deleted messages
				int p = 0; // passes

				for (Message message : textChannel.getIterableHistory().cache(false)) {
					if (message.getAuthor().getId()
							.equals(event.getMessage().getMentionedMembers().get(0).getUser().getId())) {

						if (message.getId().equals(event.getMessage().getId())) {
							// skip the message that calls the command
						} else {
							msgs.add(message);
							i--;
						}

						if (i <= 0 || p >= 100) { // once hits target or searches 100 messages
							if (p >= 100 && i == Integer.parseInt(args[1])) {
								event.getChannel().sendMessage(String.format(
										"I have checked the last 100 messages and none of them match the user: **%s**",
										event.getMessage().getMentionedMembers().get(0)));
							}
							break;
						}
					}
					p++;
				}

				event.getTextChannel().deleteMessages(msgs).queue();

				Message msg = textChannel
						.sendMessage(
								new MessageBuilder()
										.setContent("Cleared `" + args[1] + "` message(s) from user: **"
												+ event.getMessage().getMentionedUsers().get(0).getName() + "**")
										.build())
						.complete();

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						msg.delete().queue();
						event.getMessage().delete().queue();
					}
				}, 3000);

			} else if (args.length == 2) {
				MessageHistory history = new MessageHistory(event.getTextChannel());
				List<Message> msgs = history.retrievePast(Integer.parseInt(args[1]) + 1).complete();
				event.getTextChannel().deleteMessages(msgs).queue();

				Message msg = textChannel
						.sendMessage(new MessageBuilder().setContent("Cleared `" + args[1] + "` message(s).").build())
						.complete();

				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						msg.delete().queue();
						event.getMessage().delete().queue();
					}

				}, 3000);
			}
		} else {
			Ref.errorMessagePermission(event.getTextChannel(), event.getAuthor());
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "clear"), (Ref.prefix + "purge"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Deletes any given amount of previous messages";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Clear message";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix
				+ "clear [num] will clear any given amount of messages in the given channel (up to 100)\n" + Ref.prefix
				+ "clear [num] [@user] will search the past 100 messages and delete x amount of messages by the user");
	}

}
