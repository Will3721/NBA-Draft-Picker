/**
 * This class allows us to represent a player or any generic player.
 * It has a field for the name, as well as fields for each of the 5 stats we are considering.
 * There are setters and getters for each as well.
 */

public class PlayerModel {

    private String name;
    private double points;
    private double rebounds;
    private double assists;
    private double fieldGoalPercent;
    private double freeThrowPercent;

    public PlayerModel(String name, double points, double rebounds, double assists,
                       double fieldGoalPercent, double freeThrowPercent) {
        this.name = name;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.fieldGoalPercent = fieldGoalPercent;
        this.freeThrowPercent = freeThrowPercent;
    }

    public String getName() {
        return name;
    }

    public double getPoints() {
        return points;
    }

    public double getRebounds() {
        return rebounds;
    }

    public double getAssists() {
        return assists;
    }

    public double getFieldGoalPercent() {
        return fieldGoalPercent;
    }

    public double getFreeThrowPercent() {
        return freeThrowPercent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void setRebounds(double rebounds) {
        this.rebounds = rebounds;
    }

    public void setAssists(double assists) {
        this.assists = assists;
    }

    public void setFieldGoalPercent(double fieldGoalPercent) {
        this.fieldGoalPercent = fieldGoalPercent;
    }

    public void setFreeThrowPercent(double freeThrowPercent) {
        this.freeThrowPercent = freeThrowPercent;
    }

    public String toString() {
        return name + ": " + points + ", " + rebounds + ", " + assists + ", " + fieldGoalPercent + "," + freeThrowPercent;
    }

}
