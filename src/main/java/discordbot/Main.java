package discordbot;

import javax.security.auth.login.LoginException;

import Casino.Create;
import Commands.Avatar;
import Commands.GameCall;
import Commands.Help;
import Commands.Jerremy;
import Commands.Ping;
import Commands.RandomUser;
import Commands.Roll;
import Commands.Spongebob;
import Commands.Test;
import Commands.Twitch;
import Moderation.Ban;
import Moderation.ChangePrefix;
import Moderation.Clear;
import Moderation.Gag;
import Moderation.Kick;
import Moderation.Mute;
import Moderation.ResetPrefix;
import Moderation.Unban;
import Music.Play;
import Other.Urban;
import Other.Weather;
import Stats.Fortnite;
import Stats.LoL;
import Stats.Overwatch;
import Stats.RB6;
import Stats.Top;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JDABuilder jda = new JDABuilder(AccountType.BOT).setToken(Ref.TEST_BOT_TOKEN);
		jda.setGame(Game.playing("ataraxia is my dad"));
		Help help = new Help();

		jda.addEventListener(help.registerCommand(help));
		jda.addEventListener(help.registerCommand(new Roll()));
		jda.addEventListener(help.registerCommand(new Jerremy()));
		jda.addEventListener(help.registerCommand(new Ping()));
		jda.addEventListener(help.registerCommand(new GameCall()));
		jda.addEventListener(help.registerCommand(new LoL()));
		jda.addEventListener(help.registerCommand(new Clear()));
		jda.addEventListener(help.registerCommand(new Spongebob()));
		jda.addEventListener(help.registerCommand(new ChangePrefix()));
		jda.addEventListener(help.registerCommand(new ResetPrefix()));
		jda.addEventListener(help.registerCommand(new RB6()));
		jda.addEventListener(help.registerCommand(new Fortnite()));
		jda.addEventListener(help.registerCommand(new Urban()));
		jda.addEventListener(help.registerCommand(new Twitch()));
		jda.addEventListener(help.registerCommand(new RandomUser()));
		jda.addEventListener(help.registerCommand(new Overwatch()));
		jda.addEventListener(help.registerCommand(new Kick()));
		jda.addEventListener(help.registerCommand(new Ban()));
		jda.addEventListener(help.registerCommand(new Mute()));
		jda.addEventListener(help.registerCommand(new Unban()));
		jda.addEventListener(help.registerCommand(new Gag()));
		jda.addEventListener(help.registerCommand(new Avatar()));
		jda.addEventListener(help.registerCommand(new Weather()));
		jda.addEventListener(help.registerCommand(new Top()));
		jda.addEventListener(help.registerCommand(new Create()));
		// jda.addEventListener(help.registerCommand(new DBTest()));
		jda.addEventListener(new Test());
		jda.addEventListener(new ServerEvents());
		// jda.addEventListener(new SuperslyEvent());

		// Music
		jda.addEventListener(new Play());
		try {
			jda.buildAsync();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}