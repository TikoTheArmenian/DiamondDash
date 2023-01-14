import java.util.HashMap;

public class ExampleBotStoneMiner implements Bot{
    public void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers){

    }
    /*
    objectsDetected

    [0, 1, 2,
    3 BOT 4
    5  6  7]
     */
    public String newTurn(Location currentLocation, String[] objectsDetected, int numCoal, int numEmerald, HashMap<Location,String> vision){
        if(objectsDetected[4].equals("EMPTY"))
            return "MOVE";
        else
            return "MINE";
    }

    public String getName(){
        return "ExampleBotStoneMiner";
    }
}
