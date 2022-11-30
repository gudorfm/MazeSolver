package data;

public class MazeData {
    public long bumps;
    public long coins;
    public long id;
    public long secondsLeft;
    public long size;
    public String status;
    public String username;

    @Override
    public String toString(){
        return ("MazeData\nBumps: " + bumps + " Coins: " + coins + " Seconds Left: " + secondsLeft);
    }
}
