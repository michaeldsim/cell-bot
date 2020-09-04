package Commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import discordbot.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Avatar extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub

		MessageBuilder message = new MessageBuilder();
		EmbedBuilder embed = new EmbedBuilder();
		InputStream file;
		try {
			URL url = new URL(e.getMessage().getMentionedUsers().get(0).getAvatarUrl());
			URLConnection uc = url.openConnection();
			uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			uc.connect();
			file = uc.getInputStream();
			embed.setImage("attachment://thumbnail.png");
			message.setEmbed(embed.build());
			e.getChannel().sendFile(file, "thumbnail.png", message.build()).queue();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "avatar");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Shows the avatar of the user";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Avatar";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "[@user] - shows the avatar of the current user"));
	}

}
