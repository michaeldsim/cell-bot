package discordbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import Commands.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DBTest extends Command {
	// all default values for each column
	// current columns in db are id, talerts
	final String DEFAULT_VALUES = "false";

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			String query = "SELECT EXISTS(SELECT 1 FROM users WHERE id='" + e.getMember().getUser().getId()
					+ "') AS found"; // query to check if user already exists
			PreparedStatement pst = connection.prepareStatement(query);

			ResultSet rs = pst.executeQuery(); // will return 1 if user found

			if (rs.getInt("found") == 1) { // if found then the user already exists
				System.out.println("User was found in the database");
			} else { // if not found will create new user entry with default values for each column
				System.out.println("User was not found in the database\nCreating a new entry...");
				statement.executeUpdate(
						"insert into user values('" + e.getMember().getUser().getId() + "', " + DEFAULT_VALUES + ")");
				System.out.println("User was created!");
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
		return Arrays.asList((Ref.prefix + "db"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "dbtest";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "dbtest";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList("dbtest");
	}
}