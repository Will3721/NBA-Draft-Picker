import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import org.jsoup.Jsoup;

/**
 * This class contains a method which takes in a year and outputs an ArrayList
 * of PlayerModels which are made up of a combination of the top 5 players from
 * each college basketball team that year. This class essentially allows us
 * to create a database of all "good" players from any given year.
 */

public class Scrape {

    public Scrape() {
    }

    public ArrayList<PlayerModel> scrapePlayerInfo(int year) {
        ArrayList<PlayerModel> output = new ArrayList();
        Document document = null;
        try {
            document = Jsoup.connect("https://www.sports-reference.com/cbb/seasons/"+year+"-school-stats.html").get();
            Elements outer = document.select("tbody");
            outer = outer.select("a");
            for (Element ele : outer) {
                String tempURL = (ele.absUrl("href"));
                Document teamPage = Jsoup.connect(tempURL).get();
                Elements actLinks = teamPage.select("table#per_game");
                actLinks = actLinks.select("tbody");
                actLinks = actLinks.select("tr");

                try {

                    for (int x = 0; x < 5; x++) {
                        Elements current = actLinks.get(x).select("td");
                        Element player = current.get(0).select("a").first();
                        String playerName = player.childNodes().get(0).toString();
                        String[] nameArray = playerName.split(" ");
                        playerName = nameArray[0].toCharArray()[0] + ". " + nameArray[1];
                        String points = current.select("[data-stat = pts_per_g]").text();
                        String rebounds = current.select("[data-stat = trb_per_g]").text();
                        String assists = current.select("[data-stat = ast_per_g]").text();
                        String fgp = current.select("[data-stat = fg_pct]").text();
                        if (fgp.equals("")) {
                            fgp = "0";
                        }
                        String ftp = current.select("[data-stat = ft_pct]").text();
                        if (ftp.equals("")) {
                            ftp = "0";
                        }
                        PlayerModel newPlayer = new PlayerModel(playerName, Double.parseDouble(points),
                                Double.parseDouble(rebounds), Double.parseDouble(assists),
                                Double.parseDouble(fgp) * 100, Double.parseDouble(ftp) * 100);
                        output.add(newPlayer);

                    }

                }
                catch (IndexOutOfBoundsException ex) {
                }
            }
        } catch (IOException ex) {
            System.out.println("404: Not Found");
        }
        return output;
    }

}