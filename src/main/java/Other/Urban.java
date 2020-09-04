package Other;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Urban extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {

		URL url;

		String phrase = Ref.createString(args, 1).replaceAll(" ", "");
		System.out.println(phrase);

		try {
			url = new URL("http://api.urbandictionary.com/v0/define?term=" + phrase);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream inStream = conn.getInputStream();
			String json = new Scanner(inStream, "UTF-8").useDelimiter("\\Z").next();

			System.out.println(json);

			JSONObject obj = new JSONObject((String) json);
			JSONArray list = obj.getJSONArray("list");

			if (list.length() < 1) {
				e.getChannel()
						.sendMessage("No results were found for the phrase: **" + Ref.createString(args, 1) + "**")
						.queue();
			} else {
				String temp = list.getJSONObject(0).getString("word");
				String word = StringUtils.capitalize(temp);
				String definition = list.getJSONObject(0).getString("definition");
				String example = list.getJSONObject(0).getString("example");

				e.getChannel()
						.sendMessage("**" + word + "**\nDefinition: \n" + definition + "\n\nExample: \n" + example)
						.queue();
			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "urban");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Displays top definition from urban dictionary.";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Urban Dictionary lookup";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix) + "urban [search phrase] - will retrieve definition for the word/phrase");
	}

}
