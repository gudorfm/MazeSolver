import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
  
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
  

public class Solver {

    public String facing = "SOUTH"; 
    public String baseUri = "https://coding-challanges.herokuapp.com/api/mazes/";
    public int coins = 0;
    public int id = 0;
    public boolean foundDoor = false;
    public HttpClient client;
    public String username = "";
    public String password = "";
    public String difficulty = "BEGINNER";

    public static void main(String args[]) throws Exception{
        Solver solver = new Solver();
        solver.run();
    }

    public void run() throws Exception{
        
        getLoginInfo();

        client = HttpClient.newBuilder()
            .authenticator(new Authenticator() {
                @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        })
            .build();
        

        createNewMaze();

        boolean done = false;
        while(!done){
            Location location = getLocation();
            if(location.On == "coin"){
                pickupCoin();
            }
            if(coins == 3){
                if(checkDoorAdjacency(location)){
                    move();
                }
            }
            done = true; // Temporary fix to avoid infinite loop
        }
    }

    /***
     * Reads login.json to get the username and password
     */
     public void getLoginInfo(){
        try {
        Object input = new JSONParser().parse(new FileReader("login.json"));
        JSONObject json = (JSONObject) input;

        username = (String) json.get("Username");
        password = (String) json.get("Password");
        difficulty = (String) json.get("Difficulty");

        }
        catch(FileNotFoundException e){
            System.out.println("login file not found.  Make sure login.json is present in the project folder.");
        }
        catch(Exception e){
            e.printStackTrace();
        }
     }

    // Creates a new maze and stores the maze id for future api calls
    public void createNewMaze() throws Exception{
        HttpRequest newMazeRequest = HttpRequest.newBuilder()
        .uri(URI.create(baseUri + "?=" + difficulty))
        .POST(BodyPublishers.ofString("{\"Accept\": \"*/*\"}"))
        .build();

        HttpResponse<String> newMazeResponse = client.send(newMazeRequest, BodyHandlers.ofString());

        System.out.println(newMazeResponse);
        id = Integer.parseInt(newMazeResponse.body());  // Make API call to create a new maze and save the returned maze id;
    }

    // Gets the information about the tile that the bot is on and the surrounding tiles
    public Location getLocation(){
        Location location = new Location();
        // Make api call for location information
        // Parse json into location object
        return location;
    }

    // Allows the bot to switch from right hand rule to left hand rule
    public void turn(){
        if(coins == 3 && foundDoor){
            turnLeft();
        }
        else turnRight();
    }

    // Using the "right hand rule" we always turn right when available to search the maze
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

    /***
     * If the bot turns around they can use the "left hand rule" to retrace their steps
     */
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

    /** Turns the bot to face the opposite direction*/ 
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

        // Code below will be implemented later, will allow the bot to turn around if it has three coins and it knows it has already passed the door
        //This will save time so the bot doesn't have to do a complete circuit of the maze, then start over looking for the exit
        /*if(coins == 3 && foundDoor){
            turnAround();
        }*/
    }


    /***
     * Checks each tile around the bot to see if the bot is currently next to the exit.  If it is, turns the bot to face the exit.
     * @param location The tile the bot currently occupies
     * @return True if the bot is next to the exit, false otherwise
     */
    public boolean checkDoorAdjacency(Location location){
        if(location.North == "EXIT"){
            facing = "NORTH";
            return true;
        }
        else if(location.East == "EXIT"){
            facing = "EAST";
            return true;
        }
        else if(location.South == "EXIT"){
            facing = "SOUTH";
            return true;
        }
        else if(location.West == "EXIT"){
            facing = "WEST";
            return true;
        }
        return false;
    }
}