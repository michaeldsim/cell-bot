package Commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import discordbot.Ref;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Spongebob extends Command {
	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		// TODO Auto-generated method stub
		String msg;
		String newText = "";
		String[] name;

		if (!e.getMessage().getMentionedMembers().isEmpty()) {
			name = e.getMessage().getMentionedMembers().get(0).getEffectiveName().split(" ");
			if ((args.length - name.length) == 1) {

				int p = 0;
				for (Message message : e.getChannel().getIterableHistory()) {
					if (message.getAuthor().getId().equals(e.getMessage().getMentionedUsers().get(0).getId())) {
						// searches for messages from @user
						String[] temp = message.getContentDisplay().split(" ");
						if (temp[0].startsWith(Ref.prefix)) {
							msg = Ref.createString(temp, 1);
							newText = formNew(msg);
						} else {
							msg = Ref.createString(temp, 0);
						}
						break;
					} else if (p >= 10) {
						e.getChannel()
								.sendMessage("I could not find a message by: **"
										+ e.getMessage().getMentionedUsers().get(0).getName()
										+ "** within the past 10 messages")
								.queue();
						break;
					}

					p++;
				}

			} else {
				msg = Ref.createString(args, 1);
				newText = formNew(msg);
			}
		} else {
			msg = Ref.createString(args, 1);
			newText = formNew(msg);
		}

		e.getChannel().sendMessage(newText).queue();
		MessageBuilder message = new MessageBuilder();
		EmbedBuilder embed = new EmbedBuilder();
		InputStream file;
		try {
			file = new URL("https://i.imgur.com/OdZwb33.jpg").openStream();
			embed.setImage("attachment://thumbnail.png");
			message.setEmbed(embed.build());
			e.getChannel().sendFile(file, "thumbnail.png", message.build()).queue();

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "spongebob"), (Ref.prefix + "sb"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Translates your message into Spongebob autism speak.";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Spongebob autism translator";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "sb [message] will translate the message into spongebob autism speak.");
	}

	public static String formNew(String s) {
		String newMessage = "";
		Character ch;

		Scanner read = new Scanner(s);

		while (read.hasNext()) {
			String word = read.next();
			for (int i = 0; i < word.length(); i++) {
				if (i % 2 == 0) {
					ch = Character.toLowerCase(word.charAt(i));
					newMessage += ch;
				} else {
					ch = Character.toUpperCase(word.charAt(i));
					newMessage += ch;
				}
			}
			newMessage += " ";
		}

		read.close();
		System.out.println(newMessage);
		return newMessage;
	}

}
