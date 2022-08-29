import java.util.*;

/**
 * This class is used to compare players to a given hall of fame player model through cosine
 * similarity. It can also return an array list of players sorted by decreasing cosine similarity.
 */
public class SimilarityRanking {
    int numInRanking;

    /**
     * Constructor that instantiates the class with the number of players the user wants to rank
     * @param numInRanking the length of the returned list of players
     */
    public SimilarityRanking(int numInRanking) {
        this.numInRanking = numInRanking;
    }

    /**
     * Given two PlayerModels, one of the average hall of fame player during their college years
     * and the other of a college player we want to compare to the hall of fame player model,
     * create 2 vectors and return the cosine similarity of the 2 vectors.
     * @param hofP the PlayerModel for the average hall of fame player during their college years
     * @param colP the PlayerModel for the player we want to compare to hofP
     * @return the cosine similarity between the vectors created from hofP and colP
     */
    public double cosSimilarity(PlayerModel hofP, PlayerModel colP) {
        double[] hallOfFameVector = {hofP.getAssists(), hofP.getFieldGoalPercent(),
                hofP.getFreeThrowPercent(), hofP.getPoints(), hofP.getRebounds()};
        double[] collegeVector = {colP.getAssists(), colP.getFieldGoalPercent(),
                colP.getFreeThrowPercent(), colP.getPoints(), colP.getRebounds()};
        double dotProd = 0.0;
        double hofVectorMag = 0.0;
        double colVectorMag = 0.0;
        for (int i = 0; i < 5; i++) {
            dotProd += (hallOfFameVector[i] * collegeVector[i]);
            hofVectorMag += (hallOfFameVector[i] * hallOfFameVector[i]);
            colVectorMag += (collegeVector[i] * collegeVector[i]);
        }
        if (colVectorMag == 0 || hofVectorMag == 0) {
            return 0;
        }
        hofVectorMag = Math.sqrt(hofVectorMag);
        colVectorMag = Math.sqrt(colVectorMag);
        return dotProd / (hofVectorMag * colVectorMag);
    }

    /**
     * Given an ArrayList of PlayerModels and the model for the average hall of fame
     * player during their college years rankedPlayers will determine the cosine similarity
     * between all PlayerModels in allPlayers and hofP, organizing all this data in an array
     * list of tuples that contain both a players name and the calculated cosine similarity.
     * Then, the method will sort this array list by decreasing cosine similarity, then
     * return the names of the top players in an array list.
     * @param hofP the PlayerModel for the average hall of fame player during their college years
     * @param allPlayers the ArrayList containing all players we want to rank based on cosine
     *                   similarity to hofP
     * @return an ArrayList containing the names of the most highly cosine similar players
     */
    public ArrayList<String> rankedPlayers(ArrayList<PlayerModel> allPlayers, PlayerModel hofP) {
        ArrayList<Entry<String, Double>> allCosSim = new ArrayList<>();
        for (PlayerModel player : allPlayers) {
            allCosSim.add(new Entry<>(player.getName(), cosSimilarity(hofP, player)));
        }
        allCosSim.sort(new Comparator<Entry<String, Double>>() {
            @Override
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                if ((o1.value - o2.value) > 0) {
                    return -1;
                } else if ((o1.value - o2.value) < 0) {
                    return 1;
                }
                return 0;
            }
        });
        ArrayList<String> rankedNames = new ArrayList<>();
        for (int i = 0; i < Math.min(numInRanking, allCosSim.size()); i++) {
            rankedNames.add(allCosSim.get(i).key);
        }
        return rankedNames;
    }

    /**
     * This subclass serves the purpose of creating a map entry that allows for
     * the storing of a value (in our case cosine similarity value) keyed by player name
     */
    class Entry<K, V> {
        K key;
        V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
