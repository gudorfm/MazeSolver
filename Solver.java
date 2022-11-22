

public class Solver {

    public String facing = "south";  
    public int coins = 0;
    public int id = 0;
    public boolean foundDoor = false;
    public static void main(String args[]){
        Solver solver = new Solver();
        solver.run();
    }

    public void run(){
        boolean done = false;
        createNewMaze();

        while(!done){
            Location location = getLocation();
            if(location.on == "coin"){
                pickupCoin();
            }
            done = true; // Temporary fix to avoid infinite loop
        }
    }

    public void createNewMaze(){
        id = 0;  // Make API call to create a new maze and save the returned maze id;
    }

    public Location getLocation(){
        Location location = new Location();
        // Make api call for location information
        // Parse json into location object
        return location;
    }

    public void turn(){
        if(coins == 3 && foundDoor){
            turnLeft();
        }
        else turnRight();
    }

    public void turnRight(){

    }

    public void turnLeft(){

    }

    public void turnAround(){

    }

    public void move(){

    }

    public void pickupCoin(){
        // Make API call to pickup a coin
        coins++;
    }
}