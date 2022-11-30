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

import data.Location;
import data.MazeData;
  
/**
 * TODO
 * Handle Exceptions properly
 * Navigation logic
 * GET maze information
 * Display maze information
 * Parse location information into Location object
 */


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

    /**
     * Creates a maze then handles the logic for navigating the maze and displaying information in the terminal
     * @throws Exception Can throw IOException, ParseException and InterruptedException.  Will handle exceptions in methods in the future
     */
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
            System.out.println(getMazeData());
            Location location = getLocation();
            if(location.on == "coin"){
                pickupCoin();
            }
            if(coins == 3){
                if(checkDoorAdjacency(location)){
                    move();
                }
            }
            done = true; // Temporary fix to avoid infinite loop
        }
        deleteMaze(); // Gives up the current maze run.  This is here to prevent large numbers of abandoned games on server during testing
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

        id = Integer.parseInt(newMazeResponse.body());  // Make API call to create a new maze and save the returned maze id;
    }

    // Gets the information about the tile that the bot is on and the surrounding tiles
    public Location getLocation() throws Exception{
        Location location = new Location();
        HttpRequest locationRequest = HttpRequest.newBuilder()
        .uri(URI.create(baseUri + id + "/steps"))
        .GET()
        .build();

        HttpResponse<String> locationResponse = client.send(locationRequest, BodyHandlers.ofString());

        JSONObject locationData = (JSONObject) new JSONParser().parse(locationResponse.body());
        System.out.println("Location Data");

        location.north = (String) locationData.get("NORTH");
        location.east = (String) locationData.get("EAST");
        location.south = (String) locationData.get("SOUTH");
        location.west = (String) locationData.get("WEST");
        location.on = (String) locationData.get("ON");

        return location;
    }

    /**
     * Gets general information about the maze such as id, coins gathered, time left, etc
     */
    public MazeData getMazeData() throws Exception{
        MazeData mazeData = new MazeData();
        HttpRequest dataRequest = HttpRequest.newBuilder()
        .uri(URI.create(baseUri + id))
        .GET()
        .build();

        HttpResponse<String> dataResponse = client.send(dataRequest, BodyHandlers.ofString());

        JSONObject data = (JSONObject) new JSONParser().parse(dataResponse.body());
        System.out.println(data); 
        
        mazeData.bumps = Integer.parseInt((String) data.get("bumps"));

        return mazeData;
    }

    /**
     * Turns the bot either to the right or left depending on whether it is still searching for coins or heading to the exit
     */
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
            System.out.println("Invalid value in facing, don't know how this happened but the application is broken");
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
            System.out.println("Invalid value in facing, don't know how this happened but the application is broken");
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
            System.out.println("Invalid value in facing, don't know how this happened but the application is broken");
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
        if(location.north == "EXIT"){
            facing = "NORTH";
            return true;
        }
        else if(location.east == "EXIT"){
            facing = "EAST";
            return true;
        }
        else if(location.south == "EXIT"){
            facing = "SOUTH";
            return true;
        }
        else if(location.west == "EXIT"){
            facing = "WEST";
            return true;
        }
        return false;
    }

    public void deleteMaze() throws Exception{
        HttpRequest deleteMazeRequest = HttpRequest.newBuilder()
        .uri(URI.create(baseUri + id))
        .DELETE()
        .build();

        client.send(deleteMazeRequest, BodyHandlers.ofString());
    }
}