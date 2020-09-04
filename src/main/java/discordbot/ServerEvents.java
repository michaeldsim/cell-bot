package discordbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ServerEvents extends ListenerAdapter {
	// create a database connection

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		e.getGuild().getTextChannels().get(0).sendMessage(e.getUser().getAsMention()
				+ " has just joined the server! Welcome to the " + e.getGuild().getName() + "!").queue();
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
		e.getGuild().getTextChannels().get(0)
				.sendMessage(e.getMember().getEffectiveName() + " (" + e.getUser().getName() + "#"
						+ e.getUser().getDiscriminator() + ")"
						+ "thinks that we smell too bad and has left the server!")
				.queue();
	}

	@Override
	public void onUserUpdateGame(UserUpdateGameEvent e) {
		// User user = e.getMember().getUser();

		// if the bot and the user is part of multiple servers it will send x amount of
		// messages per server so here we only check if the member is a part of our
		// server
		if (e.getMember().getGuild().getId().equals("95627288946675712")) {
			if (e.getNewGame() == null) {
				// ignore
			} else if (e.getNewGame().getType() == GameType.STREAMING) {
				System.out.println("User is streaming!");
				if (e.getOldGame().isRich() || e.getOldGame().getName().equals("League of Legends")) {
					// do nothing if last game is league of legends because discord rich presence is
					// shit and will have it override streaming status so that every time they exit
					// game it will apply the streaming status again which constantly triggers a new
					// message

					System.out.println("Old game" + e.getOldGame());
					System.out.println("Is rich?" + e.getOldGame());
				} else {
					e.getJDA().getGuildById("95627288946675712").getTextChannels().get(0)
							.sendMessage(e.getMember().getEffectiveName()
									+ " has just started streaming! Check them out at: " + e.getNewGame().getUrl())
							.queue();
				}

				// load();
				// if ((boolean) users.get(user.getId()).get(0)) {
				// for (Guild guild : (ArrayList<Guild>) users.get(user.getId()).get(1)) {
				// guild.getTextChannels().get(0)
				// .sendMessage(e.getMember().getEffectiveName()
				// + " has just started streaming! Check them out at: " +
				// e.getNewGame().getUrl())
				// .queue();
				// }
			} else {
				// DEBUG STATEMENTS
				// System.out.println("Old Game: " + e.getOldGame());
				// System.out.println("New Game: " + e.getNewGame().getName());
				// System.out.println("Boolean Test: " + e.getNewGame().getName().equals("League
				// of Legends"));
			}
		} else {
			// ignore
		}
	}

	@Override
	public void onGuildJoin(GuildJoinEvent e) {

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			String query = "SELECT EXISTS(SELECT 1 FROM guilds WHERE id='" + e.getGuild().getId() + "') AS found";
			// query to check if guild is new
			PreparedStatement pst = connection.prepareStatement(query);

			ResultSet rs = pst.executeQuery(); // will return 1 if user found

			if (rs.getInt("found") == 1) { // if found then the user already exists
				System.out.println("Guild was found in the database");
			} else { // if not found will create new user entry with default values for each column
				System.out.println("Guild was not found in the database\nCreating a new entry...");
				statement.executeUpdate("insert into guilds values('" + e.getGuild().getId() + "')");
				System.out.println("Guild was created!");
			}
		} catch (SQLException ex) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException ex) {
				// connection close failed.
				System.err.println(ex);
			}
		}
	}

}
//
// public void save() {
// prop.putAll(users);
// try {
// prop.store(new FileOutputStream("twitchAlerts.properties", false), null);
// } catch (IOException e) {
// System.out.println("There was an error with saving!");
// e.printStackTrace();
// }
// }
//
// public void load() {
// try {
// prop.load(new FileInputStream("twitchAlerts.properties"));
// Map props = new Properties();
// props = prop;
//
// users = new HashMap<String, ArrayList<Object>>(props);
// } catch (IOException e) {
// // TODO Auto-generated catch block
// System.out.println("There was an error with loading!");
// e.printStackTrace();
// }
// }
//
// public Map<String, ArrayList<Object>> getUsers() {
// return users;
// }
//
// public void setUsers(Map<String, ArrayList<Object>> users) {
// this.users = users;
// }
//
// public static Properties getProp() {
// return prop;
// }
//
// public static void setProp(Properties prop) {
// UserEvents.prop = prop;
// }
