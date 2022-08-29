import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * The purpose of this class is to determine how successful a given draft class determined
 * by our algorithm is by seeing the number of times a player we recommended appeared in
 * the consensus NBA top 10 annual players.
 */

public class AnnualTopPlayersParser {
    private String baseURL;
    private Document currentDoc;
    private HashMap<String, Integer> players;

    /**
     * Constructor that given a base year and a current year will find all players that have
     * appeared in the NBA top 10 players list in addition to how many appearances each of
     * these players have had.
     * @param yearDrafted the year we will start the count of top 10 players from
     * @param currYear the year we will stop the count of top 10 players
     */
    public AnnualTopPlayersParser(int yearDrafted, int currYear) {
        baseURL = "https://www.basketball-reference.com/leaders/per_top_10.html";
        players = new HashMap<>();
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
            Elements th = currentDoc.select("tr");
            for (int i = 1; i <= currYear - yearDrafted + 1; i++) {
                Element year = th.get(i);
                Elements each = year.select("a");
                for (Element player : each) {
                    String name = player.text();
                    if (!name.equals("NBA")) {
                        if (players.containsKey(name)) {
                            players.put(name, players.get(name) + 1);
                        } else {
                            players.put(name, 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given an ArrayList of player names, sees how many times these players appear in the top 10
     * in total.
     * @param recommended the input ArrayList of player names
     * @return the count of the number of times all the given players have made the NBA top 10
     */
    public int totalTopTenAppearances(ArrayList<String> recommended) {
        int count = 0;
        for (String player : recommended) {
            if (players.containsKey(player)) {
                System.out.println(player + ": " + players.get(player));
                count += players.get(player);
            }
        }
        return count;
    }
}
