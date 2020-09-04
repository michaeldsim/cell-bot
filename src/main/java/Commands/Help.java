package Commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import Moderation.ModCommand;
import discordbot.Ref;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command {
	private static final String NO_NAME = "No name provided for this command. Sorry!";
	private static final String NO_DESCRIPTION = "No description has been provided for this command. Sorry!";
	private static final String NO_USAGE = "No usage instructions have been provided for this command. Sorry!";

	private TreeMap<String, Command> commands;
	private TreeMap<String, ModCommand> modCommands;

	public Help() {
		commands = new TreeMap<>();
		modCommands = new TreeMap<>();
	}

	public Command registerCommand(Command command) {
		commands.put(command.getAliases().get(0), command);
		return command;
	}

	public ModCommand registerCommand(ModCommand command) {
		modCommands.put(command.getAliases().get(0), command);
		return command;
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (!e.isFromType(ChannelType.PRIVATE)) {
			e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor())
					.append(": Help information was sent as a private message.").build()).queue();
			if (e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				sendPrivateMod(e.getAuthor().openPrivateChannel().complete());
			}
		}

		sendPrivate(e.getAuthor().openPrivateChannel().complete(), args);
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList((Ref.prefix + "help"), (Ref.prefix + "commands"));
	}

	@Override
	public String getDescription() {
		return "Command that helps use all other commands. Use help [command name] to see more info for each command (except the music commands)!";
	}

	@Override
	public String getName() {
		return "Help Command";
	}

	@Override
	public List<String> getUsageInstructions() {
		return Collections.singletonList(Ref.prefix + "help   **OR** " + Ref.prefix + "help *<command>*\n" + Ref.prefix
				+ "help - returns the list of commands along with a simple description of each.\n" + Ref.prefix
				+ "help <command> - returns the name, description, aliases and usage information of a command.\n"
				+ "   - This can use the aliases of a command as input as well.\n" + "__Example:__ " + Ref.prefix
				+ "help ann");
	}

	private void sendPrivateMod(PrivateChannel channel) {
		StringBuilder ms = new StringBuilder();
		for (ModCommand c : modCommands.values()) {
			String description = c.getDescription();
			description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

			ms.append("**").append(c.getAliases().get(0)).append("** - ");
			ms.append(description).append("\n");
		}
		channel.sendMessage(
				new MessageBuilder().append("**The following Moderation commands are supported by the bot**\n")
						.append(ms.toString()).build())
				.queue();
	}

	private void sendPrivate(PrivateChannel channel, String[] args) {
		if (args.length < 2) {
			StringBuilder s = new StringBuilder();
			for (Command c : commands.values()) {
				String description = c.getDescription();
				description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;

				s.append("**").append(c.getAliases().get(0)).append("** - ");
				s.append(description).append("\n");
			}

			channel.sendMessage(new MessageBuilder().append("**The following commands are supported by the bot**\n")
					.append(s.toString()).build()).queue();

			channel.sendMessage(
					"\n\n**The following commands are used to control the music player.** \nIt will play audio from YouTube, SoundCloud, Vimeo, Twitch, and BandCamp\n"
							+ "**" + Ref.prefix + "join [channel name]** - Join the channel of provided name\n" + "**"
							+ Ref.prefix + "leave** - Leaves the Music channel\n" + "**" + Ref.prefix
							+ "play [url]** - Adds song to the queue and will begin playing if it wasn't already doing so.\n"
							+ "**" + Ref.prefix
							+ "pplay [url]** - Adds a playlist to the queue and will begin playing if it wasn't already doing so.\n"
							+ "**" + Ref.prefix
							+ "play** - Plays song from the current queue. Resumes audio playback if it was paused.\n"
							+ "**" + Ref.prefix + "pause** - Pauses the current song. " + Ref.prefix
							+ "play will resume playback.\n" + "**" + Ref.prefix
							+ "stop** - Completely stop audio playback and skip the rest of the current song.\n" + "**"
							+ Ref.prefix + "nowplaying** or **" + Ref.prefix
							+ "np** - Shows the current song in the queue along with the time stamp\n" + "**"
							+ Ref.prefix
							+ "skip** - Skips the current song and automatically starts the next in queue\n" + "**"
							+ Ref.prefix + "list** - Shows all the songs in the current queue\n" + "**" + Ref.prefix
							+ "volume** - Set the volume of the music player [10-100]\n" + "**" + Ref.prefix
							+ "restart** - Restarts the current playing song or the previous one if there no song currently playing.\n"
							+ "**" + Ref.prefix
							+ "repeat** - Repeat the currently playing song after it has finished.\n" + "**"
							+ Ref.prefix
							+ "reset** - Completely resets the player and the queue. Good for fixing any errors that may occur\n")
					.queue();
			channel.sendMessage("**" + Ref.prefix + "fwd [seconds]** - fowards the video a certain number of seconds\n"
					+ "**" + Ref.prefix + "rwd [seconds]** - rewinds the video a certain number of seconds\n" + "**"
					+ Ref.prefix + "goto [seconds]**" + " or" + "**" + Ref.prefix
					+ " goto [mins] [seconds]** - will skip to a certain time in the video").queue();
		} else {
			String command = args[1].charAt(0) == Ref.prefix.charAt(0) ? args[1] : Ref.prefix + args[1]; // If there is
																											// not a
																											// preceding
																											// .
			for (Command c : commands.values()) { // attached to the command we are
				if (c.getAliases().contains(command)) { // search, then prepend one.
					String name = c.getName();
					String description = c.getDescription();
					List<String> usageInstructions = c.getUsageInstructions();
					name = (name == null || name.isEmpty()) ? NO_NAME : name;
					description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
					usageInstructions = (usageInstructions == null || usageInstructions.isEmpty())
							? Collections.singletonList(NO_USAGE)
							: usageInstructions;

					// TODO: Replace with a PrivateMessage
					channel.sendMessage(new MessageBuilder().append("**Name:** " + name + "\n")
							.append("**Description:** " + description + "\n")
							.append("**Alliases:** " + StringUtils.join(c.getAliases(), ", ") + "\n")
							.append("**Usage:** ").append(usageInstructions.get(0)).build()).queue();
					for (int i = 1; i < usageInstructions.size(); i++) {
						channel.sendMessage(
								new MessageBuilder().append("__" + name + " Usage Cont. (" + (i + 1) + ")__\n")
										.append(usageInstructions.get(i)).build())
								.queue();
					}
					return;
				}
			}

			for (ModCommand mc : modCommands.values()) { // attached to the command we are
				if (mc.getAliases().contains(command)) { // search, then prepend one.
					String name = mc.getName();
					String description = mc.getDescription();
					List<String> usageInstructions = mc.getUsageInstructions();
					name = (name == null || name.isEmpty()) ? NO_NAME : name;
					description = (description == null || description.isEmpty()) ? NO_DESCRIPTION : description;
					usageInstructions = (usageInstructions == null || usageInstructions.isEmpty())
							? Collections.singletonList(NO_USAGE)
							: usageInstructions;

					// TODO: Replace with a PrivateMessage
					channel.sendMessage(new MessageBuilder().append("**Name:** " + name + "\n")
							.append("**Description:** " + description + "\n")
							.append("**Alliases:** " + StringUtils.join(mc.getAliases(), ", ") + "\n")
							.append("**Usage:** ").append(usageInstructions.get(0)).build()).queue();
					for (int i = 1; i < usageInstructions.size(); i++) {
						channel.sendMessage(
								new MessageBuilder().append("__" + name + " Usage Cont. (" + (i + 1) + ")__\n")
										.append(usageInstructions.get(i)).build())
								.queue();

					}
					return;
				}
			}
			channel.sendMessage(new MessageBuilder().append("The provided command '**" + args[1]
					+ "**' does not exist. Use " + Ref.prefix + "help to list all commands.").build()).queue();
		}
	}
}