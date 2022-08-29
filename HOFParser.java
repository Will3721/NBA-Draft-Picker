import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.AbstractDocument;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import java.util.regex.*;

/**
 * This class parses data from https://en.hispanosnba.com/players/hall-of-fame/index to create a list
 * of all NBA hall of fame players in history. It then parses data from https://www.sports-reference.com/cbb/players/
 * to obtain the college stats of those hall of fame players. Then, it averages those stats from each of the
 * five categories.
 */

public class HOFParser {
    private String baseURL;
    private Document currentDoc;
    private List<String> players;

    public HOFParser(String url) {
        baseURL = url;
        players = new ArrayList<>();
        try {
            this.currentDoc = Jsoup.connect(this.baseURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //useful helper method for testing
    public void showCurrDoc() {
        System.out.println(currentDoc);
    }

    //creates a list of all NBA hall of fame players
    public void setPlayers() {
        Elements th = currentDoc.select("th.tdl");
        for (Element e : th) {
            if (e.child(0).text().equals("Nate Archibald")) {
                players.add("Tiny Archibald"); //edge case, Nate Archibald is more often called Tiny Archibald
            } else {
                players.add(e.child(0).text());
            }
        }
        //System.out.println(players);
    }

    //connects to the page that the last name starts with
    public void connectLastNamePage(String lastInitial) {
        try {
            currentDoc = Jsoup.connect("https://www.sports-reference.com/cbb/players/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String lastInitialLink = "";
        Elements aTag = currentDoc.select("a");
        for (Element e : aTag) {
            if (e.text().equals(lastInitial)) {
                lastInitialLink = e.attr("abs:href");
            }
        }
        try {
            currentDoc = Jsoup.connect(lastInitialLink).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns the average across all players of the list of the stat that gets passed in
    public double getAverage(String stat) {
        double average = 0;
        double total = 0;
        int count = 0;
        String playerLink = "";

        for (String player : players) {
            String[] firstAndLast = player.split(" ");
            String lastInitial = firstAndLast[firstAndLast.length - 1].substring(0, 1);
            connectLastNamePage(lastInitial);
            Elements aTag = currentDoc.select("a");
            for (Element a : aTag) {
                if (a.text().equals(player)) {
                    playerLink = a.attr("abs:href");
                    break;
                }
                playerLink = "";
            }
            if (!playerLink.equals("")) {
                try {
                    currentDoc = Jsoup.connect(playerLink).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements span = currentDoc.select("span.poptip");
                for (Element s : span) {
                    if (s.child(0).text().equals(stat)) {
                        String pointsToAdd = s.nextElementSibling().nextElementSibling().text();
                        if (!pointsToAdd.equals("-")) {
                            total += Double.parseDouble(pointsToAdd);
                            count++;
                        }
                    }
                }
            }

        }
        average = total / count;
        return average;
    }
}
