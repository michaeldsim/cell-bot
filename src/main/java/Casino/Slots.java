package Casino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Slots extends CasinoGame {

	public Slots(String player, String guild, int bet, Connection conn) {
		super(player, guild, bet, conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		String user = e.getMember().getUser().getId();
		String guild = e.getGuild().getId();

		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			String query = "SELECT EXISTS(SELECT 1 FROM money WHERE user_id='" + user + "' AND guild_id='" + guild
					+ "') AS found";
			System.out.println(query);
			PreparedStatement pst = connection.prepareStatement(query);

			ResultSet rs = pst.executeQuery();

			if (rs.getInt("found") == 1) { // if found then the user already exists
				System.out.println("User balance was found in the database");
				query = "SELECT amount FROM money WHERE user_id='" + user + "' AND guild_id='" + guild + "')";
				System.out.println(query);
				pst = connection.prepareStatement(query);
				rs = pst.executeQuery();
				int balance = rs.getInt("amount");
				System.out.println(balance);
			} else { // if not found will create new user entry with default values for each column
				e.getChannel().sendMessage("No balance was found!\nCreating a balance now...");
				System.out.println("User balance was not found in the database\nCreating a new entry...");
				statement.executeUpdate("insert into money values('" + e.getGuild().getId() + "',  '"
						+ e.getMember().getUser().getId() + "', 1000)");
				System.out.println("User balance was created!");
				e.getChannel().sendMessage("User balance has been created!");
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

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "slot");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Slot machine";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Slot machine";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("temp");
	}

	public void win(int x, String user, String guild, Connection conn) {
		String query = "SELECT EXISTS(SELECT 1 FROM money WHERE user_id='" + user + "' AND guild_id='" + guild
				+ "') AS found";

		try {
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

		} catch (SQLException ex) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				// connection close failed.
				System.err.println(ex);
			}
		}
	}
}
