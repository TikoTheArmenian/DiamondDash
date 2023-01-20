import java.util.HashMap;

public class ExampleBotStoneMiner implements Bot{

    Integer moveDir=15;
    public void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers){
        System.out.println("New Game");
    }
    /*
    objectsDetected

    [0, 1, 2,
    3 BOT 4
    5  6  7]
     */
    public String newTurn(Location currentLocation, String[] objectsDetected, int numCoal, int numEmerald, HashMap<Location,String> vision){
        int currentDirection = 1;
        if(moveDir>0)
        {
            moveDir--;
            return "MOVE";
        }
        else if(moveDir==0)
        {
            moveDir--;
            return "TURN_LEFT";
        }

        if(objectsDetected[2].equals("EMPTY"))
        {
            return "MOVE";
        }
        else
        {
            //System.out.println("this is called");
            return "MINE";
        }
    }

    public String getName(){
        return "ExampleBotStoneMiner";
    }
}
