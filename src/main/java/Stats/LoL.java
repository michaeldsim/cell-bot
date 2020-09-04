package Stats;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import Commands.Command;
import discordbot.Ref;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.api.endpoints.league.constant.LeagueQueue;
import net.rithms.riot.api.endpoints.league.dto.LeaguePosition;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

public class LoL extends Command {

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		ApiConfig config = new ApiConfig().setKey(Ref.RIOT_API_KEY);
		RiotApi api = new RiotApi(config);

		try {
			Platform platform = Platform.getPlatformByName(args[1]);
			Summoner summoner = api.getSummonerByName(platform, args[2]);
			Set<LeaguePosition> leagues = api.getLeaguePositionsBySummonerId(platform, summoner.getId());
			int soloWins = 0;
			int soloLosses = 0;
			String soloRank = "";
			String soloTier = "Not placed yet";
			int soloLP = 0;

			int flexWins = 0;
			int flexLosses = 0;
			String flexRank = "";
			String flexTier = "Not placed yet";
			int flexLP = 0;

			List<ChampionMastery> mastery = api.getChampionMasteriesBySummoner(platform, summoner.getId());

			for (LeaguePosition league : leagues) {
				if (league.getQueueType().equalsIgnoreCase(LeagueQueue.RANKED_SOLO_5x5.toString())) {
					soloWins = league.getWins();
					soloLosses = league.getLosses();
					soloRank = league.getRank();
					soloTier = league.getTier();
					soloLP = league.getLeaguePoints();
				} else if (league.getQueueType().equalsIgnoreCase(LeagueQueue.RANKED_FLEX_SR.toString())) {
					flexWins = league.getWins();
					flexLosses = league.getLosses();
					flexRank = league.getRank();
					flexTier = league.getTier();
					flexLP = league.getLeaguePoints();
				}

			}
			e.getChannel().sendMessage("**__League of Legends Statistics__**\n__Name:__ " + summoner.getName() + "\n"
					+ "__Summoner Level:__ " + summoner.getSummonerLevel()
					+ "\n **-----------------__Solo Queue__ -----------------**\n" + "__Current Rank:__ " + soloTier
					+ " " + soloRank + "\n" + "__League Points:__  " + soloLP + "\n"
					+ "__Games Played:__  Total Games - " + (soloWins + soloLosses) + "\n" + soloWins + " Wins "
					+ soloLosses + " Losses WR: "
					+ String.format("%.2f",
							((double) ((double) soloWins / ((double) soloWins + (double) soloLosses)) * 100.0))
					+ "%" + "\n **-----------------__Flex Queue__ -----------------**\n" + "__Current Rank:__  "
					+ flexTier + " " + flexRank + "\n" + "__League Points:__  " + flexLP + "\n"
					+ "__Games Played:__  Total Games - " + (flexWins + flexLosses) + "\n" + flexWins + " Wins "
					+ flexLosses + " Losses WR: "
					+ String.format("%.2f",
							((double) ((double) flexWins / ((double) flexWins + (double) flexLosses)) * 100.0))
					+ "%").queue();
			e.getChannel()
					.sendMessage("**Highest Mastery Champion: **"
							+ api.getDataChampion(platform, mastery.get(0).getChampionId()).getName()
							+ "\n**Mastery Level**: " + mastery.get(0).getChampionLevel() + "\n**Mastery Points**: "
							+ String.format("%,d", mastery.get(0).getChampionPoints()))
					.queue();

		} catch (RiotApiException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			e.getChannel().sendMessage("Summoner could not be found!").queue();
		}

	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList((Ref.prefix + "lol"));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Look up ranks of any user on any server";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "League of Legends Rank Lookup";
	}

	@Override
	public List<String> getUsageInstructions() {
		// TODO Auto-generated method stub
		return Arrays.asList(Ref.prefix + "lol [region] [username] to look up ranks of anyone on any region\n"
				+ "**Regions: na, br, eune, euw, jp, kr, lan, las, oce, ru, tr**");
	}
}
