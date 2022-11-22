

public class Solver {

    public String facing = "SOUTH"; 
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
        if(facing == "NORTH"){
            facing = "EAST";
        }
        else if(facing == "EAST"){
            facing = "SOUTH";
        }
        else if(facing == "SOUTH"){
            facing = "WEST";
        }
        else if(facing == "WEST"){
            facing = "NORTH";
        }else{
            System.out.println("Invalid value in facing, don't know how this happened but the apllication is broken");
        }
    }

    public void turnLeft(){
        if(facing == "NORTH"){
            facing = "WEST";
        }
        else if(facing == "EAST"){
            facing = "NORTH";
        }
        else if(facing == "SOUTH"){
            facing = "EAST";
        }
        else if(facing == "WEST"){
            facing = "SOUTH";
        }else{
            System.out.println("Invalid value in facing, don't know how this happened but the apllication is broken");
        }
    }

    public void turnAround(){
        if(facing == "NORTH"){
            facing = "SOUTH";
        }
        else if(facing == "EAST"){
            facing = "WEST";
        }
        else if(facing == "SOUTH"){
            facing = "NORTH";
        }
        else if(facing == "WEST"){
            facing = "EAST";
        }else{
            System.out.println("Invalid value in facing, don't know how this happened but the apllication is broken");
        }
    }

    public void move(){
        // Make API call to step in the direction of facing
    }

    public void pickupCoin(){
        // Make API call to pickup a coin
        coins++;
        if(coins == 3 && foundDoor){
            turnAround();
        }
    }
}