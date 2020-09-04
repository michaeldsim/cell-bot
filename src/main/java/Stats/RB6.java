package Stats;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RB6 extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		String requestURL;
		URL data;
		InputStream inStream;
		HttpURLConnection connection;
		String json;

		String playerName = args[1]; // do you get what im saying? look at LoL class to see my example
		System.out.println(args[1] + args[1].equals("compare"));
		/*
		 * if(args[1].equals("compare")) { // arg0 = !fn arg1 = compare arg 2 =
		 * firstplayername
		 * if(args.length>=4) {
		 * ArrayList<JSONObject> players = new ArrayList<>();
		 * try {
		 * for(int i = 2; i<args.length-1;i++){
		 * requestURL = "https://api.r6stats.com/api/v1/players/" + args[i] +
		 * "/?platform=uplay";
		 * data = new URL(requestURL);
		 * connection = (HttpURLConnection) data.openConnection();
		 * connection.setDoOutput(true);
		 * connection.setInstanceFollowRedirects(false);
		 * connection.setRequestMethod("GET");
		 * connection.setRequestProperty("Content-Type", "application/json");
		 * connection.setRequestProperty("charset", "utf-8");
		 * connection.addRequestProperty("User-Agent", "Chrome");
		 * connection.connect();
		 * inStream = connection.getInputStream();
		 * json = new Scanner(inStream, "UTF-8").useDelimiter("\\Z").next();
		 * players.add(new JSONObject(json)); //saves json for each player in this handy
		 * dandy array list to be accessed later
		 * }
		 * JSONObject bestKd = players.get(0), bestWl=players.get(0);
		 * for(int i = 0; i<players.size()-2;i++) {
		 * if(
		 * players.get(i).getJSONObject("stats").getJSONObject("ranked").getDouble("kd")
		 * < players.get(i+1).getJSONObject("stats").getJSONObject("ranked").getDouble(
		 * "kd")) {
		 * bestKd = players.get(i+2);
		 * }
		 * if( players.get(i).getJSONObject("stats").getJSONObject("ranked").getDouble(
		 * "wlr") <
		 * players.get(i+1).getJSONObject("stats").getJSONObject("ranked").getDouble(
		 * "wlr")) {
		 * bestWl = players.get(i+2);
		 * }
		 * }
		 * String names ="";
		 * for(int i = 0; i<players.size();i++) {
		 * names += " *" +
		 * players.get(i).getJSONObject("player").getString("username")+"*";
		 * }
		 * e.getChannel()
		 * .sendMessage("***__COMPARING Rainbow Six Siege Statistics BETWEEN:__***" +
		 * names + "\n" + "__Best KD:__ *" +
		 * bestKd.getJSONObject("player").getString("username")
		 * +"* has the best KD at **" +
		 * bestKd.getJSONObject("stats").getJSONObject("ranked").getDouble("kd") + "**"
		 * + "\n" +
		 * "__Best W/L:__ *" +
		 * bestWl.getJSONObject("player").getString("username")
		 * +"* has the best W/L at **" +
		 * bestWl.getJSONObject("stats").getJSONObject("ranked").getDouble("wlr") +
		 * "**")
		 * .queue();
		 * }
		 * catch(Exception ex) {
		 * e.getChannel().
		 * sendMessage("***ERROR:*** One or more of the players entered may not exist!"
		 * );
		 * }
		 * } else {
		 * e.getChannel().
		 * sendMessage("Too few arguments for the ***compare*** command. Please list at least two names separated by spaces. \n __Example:__ !fn compare SlyTachyon Boptics"
		 * ).queue();
		 * }
		 * } else {
		 */
		try {
			requestURL = "https://r6db.com/api/v2/players?name=" + playerName + "&platform=pc&exact=false";
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet(requestURL);
			request.addHeader("x-app-id", Ref.R6DB_API_KEY);
			System.out.println(requestURL);
			HttpResponse response = httpclient.execute(request);

			InputStream input = response.getEntity().getContent();
			json = new Scanner(input, "UTF-8").useDelimiter("\\Z").next();
			json = "{ \"players\":" + json + "}";

			// System.out.println("json: " + json);

			JSONObject obj = new JSONObject(json);
			JSONArray players = obj.getJSONArray("players");
			String playerId = players.getJSONObject(0).getString("id"); // gets the first match or closest match

			requestURL = "https://r6db.com/api/v2/players/" + playerId;
			httpclient = new DefaultHttpClient();
			request = new HttpGet(requestURL);
			request.addHeader("x-app-id", Ref.R6DB_API_KEY);
			System.out.println("final request url with ID: " + requestURL);
			response = httpclient.execute(request);

			input = response.getEntity().getContent();
			json = new Scanner(input, "UTF-8").useDelimiter("\\Z").next();

			// System.out.println("final json: " + json);

			obj = new JSONObject(json);
			playerName = obj.getString("name");
			JSONObject ranked = obj.getJSONObject("rank").getJSONObject("ncsa");
			int currentRanked = ranked.getInt("rank");
			int maxRank = ranked.getInt("max_rank");
			double currentMmr = ranked.getDouble("mmr");
			double maxMmr = ranked.getDouble("max_mmr");
			int abandons = ranked.getInt("abandons");

			JSONObject stats = obj.getJSONObject("stats").getJSONObject("ranked");
			int kills = stats.getInt("kills");
			int deaths = stats.getInt("deaths");
			double kd = ((double) kills / deaths);
			int wins = stats.getInt("won");
			int lost = stats.getInt("lost");
			double wlr = ((double) wins / lost);
			int matchesPlayed = stats.getInt("played");

			// BufferedImage image = ImageIO.read(new FileInputStream("src/main/Images/"+));
			// ByteArrayOutputStream os = new ByteArrayOutputStream();
			// ImageIO.write(image, "png", os);
			// InputStream is = new ByteArrayInputStream(os.toByteArray());
			// e.getChannel().sendFile(is, "test").queue();

			// Image imgData = image.getScaledInstance(Constants.AVATAR_SIZE, -1,
			// Image.SCALE_SMOOTH);
			// BufferedImage bufferedImage = new BufferedImage(imgData.getWidth(null),
			// imgData.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			// bufferedImage.getGraphics().drawImage(imgData, 0, 0, null);*/ // resizing the
			// rank images
			// BufferedImage buffered = (BufferedImage) image;
			MessageBuilder message = new MessageBuilder();
			EmbedBuilder embed = new EmbedBuilder();

			ArrayList<TreeMap> ranks = new ArrayList<>();
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Copper IV", "c4.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Copper III", "c3.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Copper II", "c2.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Copper I", "c1.png");

			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Bronze IV", "b4.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Bronze III", "b3.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Bronze II", "b2.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Bronze II", "b1.png");

			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Silver IV", "s4.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Silver III", "s3.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Silver II", "s2.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Silver I", "s1.png");

			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Gold IV", "g4.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Gold III", "g3.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Gold II", "g2.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Gold I", "g1.png");

			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Platinum III", "p3.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Platinum II", "p2.png");
			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Platinum I", "p1.png");

			ranks.add((TreeMap) new TreeMap());
			ranks.get(ranks.size() - 1).put("Diamond", "d.png");
			/*
			 * File file = new File("src/main/Images/"
			 * + ranks.get(currentRanked - 1).get(ranks.get(currentRanked -
			 * 1).keySet().toArray()[0]));
			 * BufferedImage image = ImageIO.read(file);
			 * BufferedImage resized = resize(image, (image.getHeight() / 5),
			 * (image.getWidth() / 5));
			 * ByteArrayOutputStream newStream = new ByteArrayOutputStream();
			 * InputStream newImage = new ByteArrayInputStream(newStream.toByteArray());
			 * File output = new File("src/main/Images/temporary.png");
			 * ImageIO.write(resized, "png", output); // i shouldn't have to create a new
			 * image, but passing the
			 * // inputstream directly into the sendFile() works but doesnt print
			 * // out an
			 * // image for some reason. Even though there is a sendFile() overload with
			 * // inputstream as a parameter. So for now, we will overwrite temporary.png
			 * every
			 * // time.
			 * file = new File("src/main/Images/temporary.png");
			 * embed.setImage("attachment://thumbnail.png");
			 * message.setEmbed(embed.build());
			 * e.getChannel().sendMessage("***__Rainbow Six Siege Statistics__***" +
			 * "\n").queue();
			 * e.getChannel().sendFile(file, "thumbnail.png", message.build()).queue();
			 */
			e.getChannel()
					.sendMessage("```css\n" + "Name: " + playerName + "\n\n" + "Player Rank:"
							+ ranks.get(currentRanked - 1).keySet().toArray()[0] + "\n" + "Max Rank:"
							+ ranks.get(maxRank - 1).keySet().toArray()[0] + "\n" + "MMR: "
							+ String.format("%.0f", currentMmr) + "\n" + "Max MMR: " + String.format("%.0f", maxMmr)
							+ "\n\n" + "K/D: " + String.format("%.2f", kd) + "\n" + "Kills: " + kills + "\n"
							+ "Deaths: " + deaths + "\n\n" + "Win/Loss: " + String.format("%.2f", wlr) + "\n"
							+ "Games Played: " + matchesPlayed + "\n" + wins + " Wins " + lost + " losses ```")
					.queue();

			// e.getChannel().sendMessage(playerName + " has not played ranked
			// yet!").queue();

		}

		catch (FileNotFoundException ex) {
			System.out.println("im not supposed to be here");
			e.getChannel().sendMessage("**ERROR:** Player not found! ").queue();
		}

		catch (JSONException ex) {
			System.out.println("USER TYPED" + playerName);
			System.out.println("error message: " + ex.toString());
			e.getChannel().sendMessage("**ERROR:** Player not found!").queue();
			;
		}

		catch (Exception ex) {
			System.out.println("USER TYPED" + playerName);
			System.out.println("error message: " + ex.toString());
			e.getChannel().sendMessage("**ERROR:** Something went wrong :(").queue();
			;
		}

		// }
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "rb6"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Lists stats for Rainbow Six Siege";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Rainbow Six Siege stats lookup";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "rb6 [username] - will retrieve Rainbow Six Siege stats for the user");
	}

	public static BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}

}
