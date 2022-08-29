import java.util.*;
import java.util.Scanner;

/**
 * This class contains the main method which is where all the objects get instantiated.
 * The user is able to input a year in which they want recommendations for.
 * A list of the top 10 recommendations is outputted, as well as the number of players on that list that
 * are considering all-time greats (which are determined by the AnnualTopPlayersParser class).
 */

public class DraftPickerMain {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        HOFParser hof = new HOFParser("https://en.hispanosnba.com/players/hall-of-fame/index");
        hof.setPlayers();

        System.out.println("Please give our program a few minutes to set up before we begin!");

        double points = hof.getAverage("PTS");
        System.out.println("20% done");
        double rebounds = hof.getAverage("TRB");
        System.out.println("40% done");
        double assists = hof.getAverage("AST");
        System.out.println("60% done");
        double FGP = hof.getAverage("FG%");
        System.out.println("80% done");
        double FTP = hof.getAverage("FT%");
        PlayerModel averageHOF = new PlayerModel("HOF", points, rebounds, assists, FGP, FTP);
        System.out.println("100% done");

        System.out.println("How many players (between 10 and 500, we recommend 100) " +
                "would you like us to rank? Please give our program a few minutes to collect all the data!");
        int numInList = scan.nextInt();
        scan.nextLine();

        System.out.println("Would you like to just straight into your own draft year pick, " +
                "or do you want to see our recommendations for the 2021-22 class first?" +
                "(Say 'yes' to see our 2021-22 recommendations, 'no' otherwise):");
        String answer = scan.nextLine();
        while (!answer.equals("yes") && !answer.equals("no")) {
            System.out.println("Try again! Remember, only 'yes' or 'no'.");
            answer = scan.nextLine();
        }

        if (answer.equals("yes")) {
            SimilarityRanking simR = new SimilarityRanking(numInList);
            Scrape scraper = new Scrape();
            ArrayList<PlayerModel> players = scraper.scrapePlayerInfo(2021);
            ArrayList<String> rankedPlayers = simR.rankedPlayers(players, averageHOF);
            System.out.println("Here are our picks for the top " + numInList +
                    " players in the upcoming draft class: ");
            System.out.println(rankedPlayers.toString());
        }

        System.out.println("Now it's year turn. " +
                "What year would you like draft recommendations for" +
                "? (please select a year between 2000 and 2020)");
        int year = scan.nextInt();
        while (year < 2000 || year > 2020) {
            System.out.println("Please enter a valid year.");
            year = scan.nextInt();
        }

        AnnualTopPlayersParser aP = new AnnualTopPlayersParser(year, 2021);
        SimilarityRanking sR = new SimilarityRanking(numInList);
        Scrape scraper = new Scrape();
        ArrayList<PlayerModel> players = scraper.scrapePlayerInfo(year);
        ArrayList<String> rankedPlayers = sR.rankedPlayers(players, averageHOF);
        System.out.println("Here are our picks for the top " + numInList +
                " players in " + year + ": ");
        System.out.println(rankedPlayers.toString());

        System.out.println("From the list we generated, we'll now be checking the accuracy " +
                "by seeing who has appeared in the annual NBA top 10 players list and how many times these " +
                "players have made the list since the year they were drafted: ");
        int numTop10 = aP.totalTopTenAppearances(rankedPlayers);
        if (numTop10 == 0) {
            System.out.println("Looks like none of the players we recommended " +
                    "made the top 10 list!");
        }
        System.out.println("Therefore the total number of top 10 appearances from our list is: "
                + numTop10);
    }
}

