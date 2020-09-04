package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import discordbot.Ref;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TwitchAlerts extends Command {
	// need to load from file

	public Map<String, ArrayList<Object>> users = new HashMap<>();
	public static Properties prop = new Properties();

	public static final ArrayList<Object> DEFAULT_SETTINGS = new ArrayList<>(
			Arrays.asList(false, new ArrayList<Guild>()));

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "ta"), (Ref.prefix + "twitchalerts"), (Ref.prefix + "talerts"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Sends an announcement that you are streaming to each guild this is enabled in";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Twitch Alerts";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "kick [@user] to kick a user\n " + Ref.prefix
				+ "[@user] [reason] to kick user with a provided reason"));
	}

}
