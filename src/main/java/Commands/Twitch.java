package Commands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import discordbot.Ref;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Twitch extends Command {

	Map<String, String> twitch = new HashMap<String, String>();
	Properties prop = new Properties();

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		load();
		if (args[1].equals("add")) {
			if (Ref.checkAdmin(e.getMember())) {
				if (args.length <= 3 || args.length > 4) {
					Ref.errorMessageParameter(e.getTextChannel(), e.getAuthor());
				} else {
					twitch.put(args[2], args[3]);
					save();
					e.getChannel().sendMessage("Twitch link has been saved!").queue();
				}
			} else {
				Ref.errorMessagePermission(e.getTextChannel(), e.getAuthor());
			}
		} else if (args[1].equals("remove")) {
			if (Ref.checkAdmin(e.getMember())) {
				if (args.length > 3 || args.length < 3) {
					Ref.errorMessageParameter(e.getTextChannel(), e.getAuthor());
				} else {
					twitch.remove(args[2]);
					prop.remove(args[2]);

					e.getChannel().sendMessage("The user " + args[2] + " has been removed!").queue();
					save();
				}
			}

		} else if (args[1].equals("list")) {
			MessageBuilder msgBuild = new MessageBuilder();
			msgBuild.append("**__Registered Users:__**\n");
			for (String key : twitch.keySet()) {
				msgBuild.append("\n**" + key + ":** " + twitch.get(key));
			}

			Message msg = msgBuild.build();

			e.getChannel().sendMessage(msg).queue();

		} else {
			if (twitch.get(args[1]) == null) {
				e.getChannel().sendMessage("Twitch link has not been registered for that user!").queue();
			} else {
				e.getChannel().sendMessage("https://www.twitch.tv/" + twitch.get(args[1])).queue();
			}
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "twitch"), (Ref.prefix + "t"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Shows the twitch link of certain registered users";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Twitch";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "twitch")
				+ "[name] will return the twitch link of the user if they are registered\n " + (Ref.prefix + "twitch")
				+ "[add] [name] [twitch username] will associate the twitch link with the name"
				+ "[remove] [name] [twitch username] will remove the name and the link associated with it");
	}

	public void save() {
		prop.putAll(twitch);
		try {
			prop.store(new FileOutputStream("data.properties", false), null);
		} catch (IOException e) {
			System.out.println("There was an error with saving!");
			e.printStackTrace();
		}
	}

	public void load() {
		try {
			prop.load(new FileInputStream("data.properties"));
			Map props = new Properties();
			props = prop;

			twitch = new HashMap<String, String>(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("There was an error with loading!");
			e.printStackTrace();
		}
	}

}
