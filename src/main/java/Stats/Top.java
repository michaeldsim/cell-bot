package Stats;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Top extends Command {
	
	
	
	static {
		System.setProperty("java.awt.headless", "true");
	}
	
	String requestURL=null;
	


	Font font = null;
	MessageBuilder message = new MessageBuilder();
	EmbedBuilder embed = new EmbedBuilder();

		

	DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		String playerName = "";


		
		try {
			for(int i=1;i<args.length;i++) { // the for loop here goes through all the names starting from args[1] for now (until we add more parameters) and does a http request to get each
											//individual json and then saves it inside of a dataset type to be put in the chart
				
				if(args[i].toLowerCase().equals("michael")) {
					playerName="\u0391" + "taraxia";
				} else {
					playerName = args[i];
				}
				requestURL = "https://fortnite.y3n.co/v2/player/" + playerName;
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
	
				JSONObject solo = stats.getJSONObject("solo");
				JSONObject duo = stats.getJSONObject("duo");
				JSONObject squad = stats.getJSONObject("squad");
				
				int sKills = solo.getInt("kills"), dKills = duo.getInt("kills"), sqKills = squad.getInt("kills");
				double sWinP = solo.getDouble("winRate"), dWinP = duo.getDouble("winRate"),
						sqWinP = squad.getDouble("winRate");
				int sWins = solo.getInt("wins"), dWins = duo.getInt("wins"), sqWins = squad.getInt("wins");
				int sMatchesPlayed = solo.getInt("matchesPlayed"), dMatchesPlayed = duo.getInt("matchesPlayed"),
						sqMatchesPlayed = squad.getInt("matchesPlayed");
				
				dataset.setValue(sKills, "kills", (args[i].equals(("\u0391" + "taraxia"))?"Ataraxia":args[i])); // at the end of getting the individual json, the name of the person and their kills is saved
																												//here. the ternary if statement just changes michael's special name to Ataraxia so that it
																												//will appear normal in the chart since Burbank.ttf font does not have the special character.
																												//if the person's name is not michael's then it just puts their regular name there assuming they 
																												// have no special characters.
	
				
			}
		}
		catch (Exception exe) {
			System.out.println("error: message here:    " + exe.getMessage());
			exe.printStackTrace(System.err);
			e.getChannel().sendMessage("**ERROR:** Player not found! ").queue();
		}
		

		
		JFreeChart chart = ChartFactory.createBarChart("Fortnite Solo Kills", "PlayerNames" , "kills", dataset, PlotOrientation.VERTICAL, false, true, false);
		chart.setBackgroundPaint(Color.YELLOW);
		try {
			chart.setBackgroundImage(ImageIO.read(new File("libs/images/fortnite.jpg")));
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		CategoryPlot p = chart.getCategoryPlot();
		
		p.setRangeGridlinePaint(Color.BLUE); // EDIT THIS TO CHANGE THE COLOR OF THE GRID LINES
		p.setRangeGridlineStroke(new BasicStroke(5)); // EDIT THIS TO CHANGE THE GRIDLINE WIDTH
		
		//ChartFrame frame = new ChartFrame ("Bar Chart for Parameters",chart);
		p.setBackgroundPaint(null);
		chart.setBackgroundPaint(null);
		//p.setForegroundAlpha(.01f); opacity of bars
		
		Member target = e.getMember();
		List<Role> tYugH = e.getGuild().getMemberById("96106301649600512").getRoles();
		Role m = e.getGuild().getRoleById(tYugH.get(0).getId());
		Icon yyu = null;
		try {
			yyu = yyu.from(new File("libs/images/fortnite.jpg"));
			System.out.println("WORKING: " + yyu.getEncoding());
		} catch (IOException e2) {
			System.out.println(e.getMessageId());
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		e.getGuild().getController().createEmote("fn", yyu, m);
		e.getGuild().getController().addSingleRoleToMember(target, m).queue();
		for(Role r:tYugH) {
			System.out.println(r.getId() + " " + r.getName());
		}
		

		
		GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("libs/Burbank.ttf")); //FORTNITE FONT
			font = font.deriveFont(20f);
			genv.registerFont(font);
		} catch (FontFormatException | IOException e1) {
			System.out.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		chart.getTitle().setPaint(Color.YELLOW); //CHANGE THE COLOR OF THE TITLE
		font=font.deriveFont(80f); //CHANGE THE FONT SIZE OF THE TITLE
		chart.getTitle().setFont(font);
		
		 ValueAxis vAxis = p.getRangeAxis();
		 
		 CategoryAxis cAxis = p.getDomainAxis();
		 font=font.deriveFont(35f); //CHANGE THE FONT SIZE OF THE VERTICAL AXIS
		 vAxis.setTickLabelFont(font);
		 vAxis.setTickLabelPaint(Color.YELLOW); //V AXIS TEXT COLOR
		 font=font.deriveFont(50f);//CHANGE THE FONT SIZE OF THE HORIZONTAL AXIS
		 cAxis.setTickLabelFont(font);
		 cAxis.setTickLabelPaint(Color.YELLOW);	 //H AXIS TEXT COLOR
		 
		 
		
		//frame.setVisible(true);
		//frame.setSize(1920,1080);
		
		BufferedImage image = chart.createBufferedImage(1920, 1080);
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

	} 
	

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "top"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
