package Stats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.miginfocom.swing.MigLayout;

public class Fortnite extends Command {



	MessageBuilder message = new MessageBuilder();
	EmbedBuilder embed = new EmbedBuilder();
	String error = "";

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		String playerName = "";
		try {
			if (args.length == 2) {
				playerName = args[1];
				if (playerName.toLowerCase().equals("michael")) {
					playerName = "\u0391" + "taraxia";
				}
			} else if (args.length == 3) {
				playerName = args[2];
				if (playerName.toLowerCase().equals("michael")) {
					playerName = "\u0391" + "taraxia";
				}
			}
			String requestURL = "https://fortnite.y3n.co/v2/player/" + playerName;
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet(requestURL);
			request.addHeader("X-Key", Ref.FORTNITE_Y3N_API_KEY); // found a better database michael, it has data
																	// divided by squads/duos/solos and match history
			System.out.println(requestURL);
			HttpResponse response = httpclient.execute(request);

			InputStream input = response.getEntity().getContent();
			String json = new Scanner(input, "UTF-8").useDelimiter("\\Z").next();
			System.out.println("FORTNITE JSON: " + json);

			JSONObject obj = new JSONObject((String) json);
			playerName = obj.getString("displayName");
			if (playerName.equals("\u0391" + "taraxia")) {
				playerName = "Ataraxia";
			}
			JSONObject stats = obj.getJSONObject("br").getJSONObject("stats").getJSONObject("pc");

			if (args.length == 3) {
				if (args[1].equals("pc") || args[1].equals("xbox")) {
					stats = obj.getJSONObject("br").getJSONObject("stats").getJSONObject("pc");
				} else if (args[1].equals("ps4")) {
					stats = obj.getJSONObject("br").getJSONObject("stats").getJSONObject("ps4");
				}
			}

			JSONObject solo = stats.getJSONObject("solo");
			JSONObject duo = stats.getJSONObject("duo");
			JSONObject squad = stats.getJSONObject("squad");

			double sKd = 0, dKd = 0, sqKd = 0;

			if (!solo.isNull("kpd")) {
				sKd = solo.getDouble("kpd");
			}
			if (!duo.isNull("kpd")) {
				dKd = duo.getDouble("kpd");
			}
			if (!squad.isNull("kpd")) {
				sqKd = squad.getDouble("kpd");
			}
			// double sKd = solo.getDouble("kpd"), dKd = duo.getDouble("kpd"), sqKd =
			// squad.getDouble("kpd");
			int sKills = solo.getInt("kills"), dKills = duo.getInt("kills"), sqKills = squad.getInt("kills");
			double sWinP = solo.getDouble("winRate"), dWinP = duo.getDouble("winRate"),
					sqWinP = squad.getDouble("winRate");
			int sWins = solo.getInt("wins"), dWins = duo.getInt("wins"), sqWins = squad.getInt("wins");
			int sMatchesPlayed = solo.getInt("matchesPlayed"), dMatchesPlayed = duo.getInt("matchesPlayed"),
					sqMatchesPlayed = squad.getInt("matchesPlayed");
			// int sTop10 = solo.getInt("top10"), dTop10= duo.getInt("top10"), sqTop10=
			// squad.getInt("top10");
			/*
			 * e.getChannel()
			 * .sendMessage("***__Fortnite LifeTime Statistics__***" + "\n" + "__Name:__ " +
			 * playerName + "\n" +
			 * "**-----------------__Solos__ -----------------**" + "\n" + "__K/D:__  " +
			 * sKd + " " + "\n"
			 * + "__Kills:__  " + sKills + "\n" + "__Win/Loss:__  " + sWinP + "%\n"
			 * + "__Games Played:__  Total Games - " + sMatchesPlayed + "\n" + sWins +
			 * " Wins " + "\n" +
			 * // "__Top 10:__ " + sTop10 + "\n"+
			 * "**-----------------__Duos__ ------------------**" + "\n" + "__K/D:__  " +
			 * dKd + " " + "\n"
			 * + "__Kills:__  " + dKills + "\n" + "__Win/Loss:__  " + dWinP + "%\n"
			 * + "__Games Played:__  Total Games - " + dMatchesPlayed + "\n" + dWins +
			 * " Wins " + "\n" +
			 * // "__Top 10:__ " + dTop10 + "\n"+
			 * "**-----------------__Squads__ ----------------**" + "\n" + "__K/D:__  " +
			 * sqKd + " " + "\n"
			 * + "__Kills:__  " + sqKills + "\n" + "__Win/Loss:__  " + sqWinP + "%\n"
			 * + "__Games Played:__  Total Games - " + sqMatchesPlayed + "\n" + sqWins +
			 * " Wins " + "\n"
			 * // "__Top 10:__ " + sqTop10 + "\n"
			 * ).queue();
			 */

			System.out.println(java.awt.GraphicsEnvironment.isHeadless());

			Font font;
			ImagePanel picture = new ImagePanel("libs/Images/fortnite.png");
			Dimension dim = new Dimension(1920, 1080);
			picture.setSize(dim);
			picture.setPreferredSize(dim);
			picture.setMinimumSize(dim);
			picture.setMaximumSize(dim);
			font = picture.font();
			if (font == null) {
				System.out.println("FONT IS NULL HERE");
			}
			Toolkit tk = Toolkit.getDefaultToolkit();
			GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			boolean headless_check = genv.isHeadless();
			System.out.println("Headless mode : " + headless_check);
			genv.registerFont(font);
			MigLayout layout = new MigLayout("", // Layout Constraints
					"[][]20[]", // Column Constraints
					"[]20[]"); // Row Constratints
			picture.setLayout(layout);
			JLabel name = new JLabel(playerName);
			name.setFont(font);
			name.setSize(name.getPreferredSize());
			name.setLocation(750, 80);
			name.setForeground(Color.decode("#fdff8c"));
			picture.add(name);
			addOutline(name, picture, font, 750, 80);
			JLabel soloKills = new JLabel(String.format("%5s", Integer.toString(sKills)));
			System.out.println(Integer.toString(sKills) + "\n" + soloKills.getText());
			soloKills.setFont(font);
			soloKills.setSize(soloKills.getPreferredSize());
			soloKills.setLocation(200, 500);
			soloKills.setForeground(Color.decode("#fdff8c"));
			picture.add(soloKills);
			addOutline(soloKills, picture, font, 200, 500);
			JLabel duoKills = new JLabel(String.format("%5s", Integer.toString(dKills)));
			duoKills.setFont(font);
			duoKills.setSize(duoKills.getPreferredSize());
			duoKills.setLocation(835, 500);
			duoKills.setForeground(Color.decode("#fdff8c"));
			picture.add(duoKills);
			addOutline(duoKills, picture, font, 835, 500);
			JLabel squadKills = new JLabel(String.format("%5s", Integer.toString(sqKills)));
			squadKills.setFont(font);
			squadKills.setSize(squadKills.getPreferredSize());
			squadKills.setLocation(1475, 500);
			squadKills.setForeground(Color.decode("#fdff8c"));
			picture.add(squadKills);
			addOutline(squadKills, picture, font, 1475, 500);
			JLabel soloKD = new JLabel(String.format("%5.3s", Double.toString(sKd)));
			soloKD.setFont(font);
			soloKD.setSize(soloKD.getPreferredSize());
			soloKD.setLocation(200, 620);
			soloKD.setForeground(Color.decode("#fdff8c"));
			picture.add(soloKD);
			addOutline(soloKD, picture, font, 200, 620);
			JLabel duoKD = new JLabel(String.format("%5.3s", Double.toString(dKd)));
			duoKD.setFont(font);
			duoKD.setSize(duoKD.getPreferredSize());
			duoKD.setLocation(835, 620);
			duoKD.setForeground(Color.decode("#fdff8c"));
			picture.add(duoKD);
			addOutline(duoKD, picture, font, 835, 620);
			JLabel squadKD = new JLabel(String.format("%5.3s", Double.toString(sqKd)));
			squadKD.setFont(font);
			squadKD.setSize(squadKD.getPreferredSize());
			squadKD.setLocation(1475, 620);
			squadKD.setForeground(Color.decode("#fdff8c"));
			picture.add(squadKD);
			addOutline(squadKD, picture, font, 1475, 620);
			JLabel soloWins = new JLabel(String.format("%5s", Integer.toString(sWins)));
			soloWins.setFont(font);
			soloWins.setSize(soloWins.getPreferredSize());
			soloWins.setLocation(200, 750);
			soloWins.setForeground(Color.decode("#fdff8c"));
			picture.add(soloWins);
			addOutline(soloWins, picture, font, 200, 750);
			JLabel duoWins = new JLabel(String.format("%5s", Integer.toString(dWins)));
			duoWins.setFont(font);
			duoWins.setSize(duoWins.getPreferredSize());
			duoWins.setLocation(835, 750);
			duoWins.setForeground(Color.decode("#fdff8c"));
			picture.add(duoWins);
			addOutline(duoWins, picture, font, 835, 750);
			JLabel squadWins = new JLabel(String.format("%5s", Integer.toString(sqWins)));
			squadWins.setFont(font);
			squadWins.setSize(squadWins.getPreferredSize());
			squadWins.setLocation(1475, 750);
			squadWins.setForeground(Color.decode("#fdff8c"));
			picture.add(squadWins);
			addOutline(squadWins, picture, font, 1475, 750);
			JLabel soloWL = new JLabel(String.format("%5.3s", Double.toString(sWinP)));
			soloWL.setFont(font);
			soloWL.setSize(soloWL.getPreferredSize());
			soloWL.setLocation(200, 850);
			soloWL.setForeground(Color.decode("#fdff8c"));
			picture.add(soloWL);
			addOutline(soloWL, picture, font, 200, 850);
			JLabel duoWL = new JLabel(String.format("%5.3s", Double.toString(dWinP)));
			duoWL.setFont(font);
			duoWL.setSize(duoWL.getPreferredSize());
			duoWL.setLocation(835, 850);
			duoWL.setForeground(Color.decode("#fdff8c"));
			picture.add(duoWL);
			addOutline(duoWL, picture, font, 835, 850);
			JLabel squadWL = new JLabel(String.format("%5.3s", Double.toString(sqWinP)));
			squadWL.setFont(font);
			squadWL.setSize(squadWL.getPreferredSize());
			squadWL.setLocation(1475, 850);
			squadWL.setForeground(Color.decode("#fdff8c"));
			picture.add(squadWL);
			addOutline(squadWL, picture, font, 1475, 850);
			JLabel soloGames = new JLabel(String.format("%5.3s", Integer.toString(sMatchesPlayed)));
			soloGames.setFont(font);
			soloGames.setSize(soloGames.getPreferredSize());
			soloGames.setLocation(200, 960);
			soloGames.setForeground(Color.decode("#fdff8c"));
			picture.add(soloGames);
			addOutline(soloGames, picture, font, 200, 960);
			JLabel duoGames = new JLabel(String.format("%5s", Integer.toString(dMatchesPlayed)));
			duoGames.setFont(font);
			duoGames.setSize(duoGames.getPreferredSize());
			duoGames.setLocation(835, 960);
			duoGames.setForeground(Color.decode("#fdff8c"));
			picture.add(duoGames);
			addOutline(duoGames, picture, font, 835, 960);
			JLabel squadGames = new JLabel(String.format("%5s", Integer.toString(sqMatchesPlayed)));
			squadGames.setFont(font);
			squadGames.setSize(squadGames.getPreferredSize());
			squadGames.setLocation(1475, 960);
			squadGames.setForeground(Color.decode("#fdff8c"));
			picture.add(squadGames);
			addOutline(squadGames, picture, font, 1475, 960);
			System.out.println("number: " + picture.getComponentCount());
			picture.validate();
			picture.repaint();
			System.out.println("number: " + picture.getComponentCount());
			BufferedImage image = getScreenShot(picture);
			File file = new File("libs/Images/temporary.png");
			if (file.exists()) {
				System.out.println("exists");
			} else {
				System.out.println("does not exist");
			}

			try {
				ImageIO.write(image, "png", file);
				System.out.println("worked");
				embed.setImage("attachment://thumbnail.png");
				message.setEmbed(embed.build());
				e.getChannel().sendFile(file, "thumbnail.png", message.build()).queue();
			} catch (IOException e1) {
				System.out.println("did not work");
				e1.printStackTrace();
			}

		} catch (Exception exe) {
			System.out.println("error: " + error + " message here:    " + exe.getMessage());
			exe.printStackTrace(System.err);
			e.getChannel().sendMessage("**ERROR:** Player not found! ").queue();
		} catch (AbstractMethodError er) {
			System.out.println(er.getMessage());
		}

	}

	private BufferedImage getScreenShot(JPanel panel) {
		BufferedImage bi = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
		panel.paint(bi.getGraphics());
		return bi;
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "fn"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "List stats for Fornite account";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Fortnite stats lookup";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "fn [username] - will retrieve Fortnite stats from user");
	}

	public void addOutline(JLabel label, ImagePanel panel, Font font, int x, int y) {
		JLabel label1 = new JLabel(String.format("%5s", label.getText()));
		Dimension dim = new Dimension((int) label.getPreferredSize().getWidth() + 10,
				(int) label.getPreferredSize().getHeight() + 10);

		label1.setFont(font);
		label1.setSize(dim);
		label1.setLocation(x, y);
		label1.setForeground(Color.decode("#000000"));

		panel.add(label1);
	}

}




