package Casino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import Commands.Command;

public abstract class CasinoGame extends Command {

	private int balance;
	private String player;
	private String guild;
	private Connection connection;

	public CasinoGame(String player, String guild, int bet, Connection conn) {
		this.player = player;
		this.guild = guild;
		connection = conn;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
			ResultSet rs = conn
					.prepareStatement(
							"SELECT amount FROM money WHERE user_id='" + player + "' AND guild_id='" + guild + "'")
					.executeQuery();
			balance = rs.getInt("amount");

			if (bet > balance) {
				System.out.println("Bet is higher than balance!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR CONNECTING TO DATABASE");
		}
	}

	public void win(int x) { // add exceptions making bets higher than balacne
		balance += x;
	}

	public void lose(int x) {
		balance -= x;
	}
}
