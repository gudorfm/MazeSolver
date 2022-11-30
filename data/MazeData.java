package data;

public class MazeData {
    public int bumps;
    public int coins;
    public int id;
    public int secondsLeft;
    public int size;
    public String status;
    public String username;

    @Override
    public String toString(){
        return ("MazeData/nBumps: " + bumps + " Coins: " + coins + " Seconds Left: " + secondsLeft);
    }
}
