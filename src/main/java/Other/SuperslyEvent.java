package Other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import discordbot.Ref;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.champion.dto.Champion;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameInfo;
import net.rithms.riot.api.endpoints.spectator.dto.CurrentGameParticipant;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

public class SuperslyEvent extends ListenerAdapter {

	ApiConfig config = new ApiConfig().setKey(Ref.RIOT_API_KEY);
	RiotApi api = new RiotApi(config);
	public String michael = "95644480685735936";
	public String corey = "96106301649600512";
	public ArrayList<String> ids = new ArrayList<String>();
	final String URL = "https://www.probuilds.net/champions/details/";

	@Override
	public void onUserUpdateGame(UserUpdateGameEvent e) {
		ids.add(michael);
		ids.add(corey);

		Map<String, String> users = new HashMap<String, String>();
		users.put("95644480685735936", "Seibah");
		users.put("96106301649600512", "Supersly");

		Summoner summoner;

		try {
			summoner = api.getSummonerByName(Platform.NA, users.get(e.getMember().getUser().getId()));
			if (e.getGuild().getId().equals("95627288946675712")) {
				if (ids.contains(e.getMember().getUser().getId())) {

					System.out.println(e.getNewGame().getName());
					if (e.getNewGame() == null) {
						// do nothing
					} else if (e.getNewGame().getName().equals("League of Legends")) {
						CurrentGameInfo game = api.getActiveGameBySummoner(Platform.NA, summoner.getId());
						CurrentGameParticipant ingame = game.getParticipantByParticipantId(summoner.getId());
						Champion champion = api.getChampion(Platform.NA, ingame.getChampionId());

						Map<String, String> champs = JSONParse.getList();
						String name = champs.get(champion.toString());
						String removeSpaces = name.replaceAll(" ", "");

						System.out.println(removeSpaces);
						System.out.println(URL + removeSpaces);

						String newURL = URL + removeSpaces;

						e.getGuild().getTextChannelById("436720988286353409")
								.sendMessage(e.getMember().getUser().getAsMention() + "\nProbuilds Link for "
										+ removeSpaces + ": " + newURL)
								.queue();

					}
				}
			}
		} catch (RiotApiException e1) {
			// TODO Auto-generated catch block
		} catch (NullPointerException e2) {

		}

	}

}
