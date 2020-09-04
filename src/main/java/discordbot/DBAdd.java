package discordbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAdd {
	public static void main(String[] args) {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			String query = "SELECT EXISTS(SELECT 1 FROM user WHERE id='95644480685735936') AS found";
			PreparedStatement pst = connection.prepareStatement(query);

			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getInt("found"));
			}

			// ResultSet rs = statement.executeQuery("select id from user");
			//
			// while (rs.next()) {
			// System.out.println(rs.getString("id"));
			// }

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
