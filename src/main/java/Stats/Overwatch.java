package Stats;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Overwatch extends Command {

	Map<String, String> usernames = new HashMap<String, String>();
	Properties prop = new Properties();

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		load();
		String username;

		if (args[1].equals("add")) {
			if (args.length <= 4 || args.length > 5) {
				e.getChannel().sendMessage("**ERROR:** Invalid parameters!").queue();
				e.getChannel()
						.sendMessage(
								"Use " + Ref.prefix + "help [command] to learn how to correctly use this commmand!")
						.queue();
			} else {
				username = args[3] + "-" + args[4];
				usernames.put(args[2].toLowerCase(), username);
				save();
				e.getChannel().sendMessage("Overwatch username has been saved!").queue();
			}
		} else if (args[1].equals("remove")) {
			if (args.length > 3 || args.length < 3) {
				e.getChannel().sendMessage("**ERROR:** Invalid parameters!").queue();
			} else {
				usernames.remove(args[2]);
				prop.remove(args[2]);

				e.getChannel().sendMessage("The user **" + args[2] + "** has been removed!").queue();
				save();
			}
		} else if (args.length > 3) {
			e.getChannel().sendMessage("**ERROR:** Invalid parameters!").queue();
			e.getChannel()
					.sendMessage("Use " + Ref.prefix + "help [command] to learn how to correctly use this commmand!")
					.queue();
		} else if (args.length == 1) {
			e.getChannel().sendMessage("**ERROR:** Invalid parameters!").queue();
			e.getChannel()
					.sendMessage("Use " + Ref.prefix + "help [command] to learn how to correctly use this commmand!")
					.queue();
		} else if (args[1].equals("list")) {
			MessageBuilder msgBuild = new MessageBuilder();
			msgBuild.append("**__Registered Users:__**\n");
			for (String key : usernames.keySet()) {
				msgBuild.append("\n**" + key + ":** " + usernames.get(key).replace('-', '#'));
			}

			Message msg = msgBuild.build();

			e.getChannel().sendMessage(msg).queue();

		} else if (args.length == 3) {
			username = args[1] + "-" + args[2];
			getStats(username, e.getChannel());
		} else {
			if (usernames.get(args[1].toLowerCase()) == null) {
				e.getChannel()
						.sendMessage(
								"**ERROR: **Either the username has not been registered yet or incorrect parameters!")
						.queue();
			} else {
				username = usernames.get(args[1].toLowerCase());
				getStats(username, e.getChannel());
			}
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "ow"), (Ref.prefix + "overwatch"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Displays competitive statistics for NA PC Overwatch players";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Overwatch stats lookup";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays
				.asList((Ref.prefix + "ow [username] [account #] to display any user from NA PC Overwatch servers"));
	}

	public void save() {
		prop.putAll(usernames);
		try {
			prop.store(new FileOutputStream("owusers.properties", false), null);
		} catch (IOException e) {
			System.out.println("There was an error with saving!");
			e.printStackTrace();
		}

	}

	public void load() {
		try {
			prop.load(new FileInputStream("owusers.properties"));
			Map props = new Properties();
			props = prop;

			usernames = new HashMap<String, String>(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("There was an error with loading!");
			e.printStackTrace();
		}
	}

	public void getStats(String username, MessageChannel channel) {
		try {
			channel.sendMessage(
					"The current API used is very slow, but it shows the most information. Please wait a moment.")
					.queue();

			EmbedBuilder embed = new EmbedBuilder();
			InputStream file;
			String requestURL = "https://owapi.net/api/v3/u/" + username + "/blob";
			String displayName = username.replace('-', '#');
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet(requestURL);

			HttpResponse response = httpclient.execute(request);
			InputStream input = response.getEntity().getContent();

			String json = new Scanner(input, "UTF-8").useDelimiter("\\Z").next();

			JSONObject obj = new JSONObject((String) json);
			JSONObject stats = obj.getJSONObject("us").getJSONObject("stats").getJSONObject("competitive")
					.getJSONObject("overall_stats");

			double wr = stats.getDouble("win_rate");
			int sr = stats.getInt("comprank"), wins = stats.getInt("wins"), losses = stats.getInt("losses"),
					ties = stats.getInt("ties"), games = stats.getInt("games"), prestige = stats.getInt("prestige"),
					level = stats.getInt("level");
			String avatarURL = stats.getString("avatar");

			channel.sendMessage("**__Overwatch Statistics__**\n__Name:__ " + displayName + "\n").queue();

			file = new URL(avatarURL).openStream();
			embed.setImage("attachment://thumbnail.png");
			MessageBuilder message = new MessageBuilder();
			message.setEmbed(embed.build());
			channel.sendFile(file, "thumbnail.png", message.build()).queue();

			channel.sendMessage("__Player Level:__ Prestige " + prestige + " Level " + level + "\n__Current SR:__ " + sr
					+ " " + "\n" + "__Games Played:__  Total Games - " + games + "\n" + wins + " Wins " + losses
					+ " Losses WR: " + wr + "%").queue();

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
