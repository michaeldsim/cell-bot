package Other;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Weather extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		URL url;

		String city;
		String key = "&APPID=" + Ref.OPENWEATHER_API_KEY;
		String units = "";

		if (args[1].equals("f")) {
			units = "&units=imperial";
		} else if (args[1].equals("c")) {
			units = "&units=metric";
		}

		if (isInteger(args[2]) && args[2].length() == 5) {
			city = "zip=" + args[2];
		} else {
			city = "q=" + args[2];
		}

		try {
			url = new URL("http://api.openweathermap.org/data/2.5/weather?" + city + units + key);
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream inStream = conn.getInputStream();
			String json = new Scanner(inStream, "UTF-8").useDelimiter("\\Z").next();

			System.out.println(json);

			JSONObject obj = new JSONObject((String) json);
			JSONArray weather = obj.getJSONArray("weather");

			String name = obj.getString("name");
			String country = obj.getJSONObject("sys").getString("country");
			String weatherMain = weather.getJSONObject(0).getString("main");
			String weatherDesc = weather.getJSONObject(0).getString("description");

			double temp = obj.getJSONObject("main").getDouble("temp");

			e.getChannel().sendMessage("City: " + name + ", " + country + "\nTemperature: " + temp + " "
					+ args[1].toUpperCase() + "\nWeather: " + weatherMain + ", " + weatherDesc).queue();

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
		return Arrays.asList(Ref.prefix + "weather");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Shows the weather for a certain location";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Weather";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(
				Ref.prefix + "weather [units] [zip code] to show the temperature and weather for a certain city");
	}

	public static boolean isInteger(String s) {
		Scanner sc = new Scanner(s.trim());
		if (sc.hasNextInt()) {
			return true;
		} else {
			return false;
		}
	}

}
