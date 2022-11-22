

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
        while(!done){
            Location location = getLocation();
            if(location.on == "coin"){
                pickupCoin();
            }
            done = true; // Temporary fix to avoid infinite loop
        }
    }

    public void move(){

    }

    public Location getLocation(){


        return new Location();
    }

    public void turnRight(){

    }

    public void turnLeft(){

    }

    public void turnAround(){

    }

    public void pickupCoin(){

    }
}