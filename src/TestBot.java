import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TestBot implements Bot {

    private static final String[] ADJECTIVES = {"Awesome", "Brave", "Clever", "Daring", "Elegant", "Fierce", "Gleaming", "Intelligent", "Jaunty", "Keen", "Lively"};
    private static final String[] NOUNS = {"Android", "Bot", "Cyborg", "Droid", "Gynoid", "Machine", "Robot", "Synthetic", "Transformer", "Unit", "Warrior"};



    @Override
    public void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers) {


    }



    /*
    "MOVE" --> Moves bot in direction it is facing if it's empty in front of it
    "TURN_RIGHT" --> Turns bot right
    "TURN_LEFT" --> Turns bot left
    "MINE" --> mines whatever is in front of it

    objectsDetected:
    [0, 1, 2,
     3,Bot 4,
     5, 6, 7]
     */

    @Override
    public String newTurn(Location currentLocation, String[] objectsDetected, int numCoal, int numEmerald, HashMap<Location,String> vision) {

        if(objectsDetected[4].equals("EMPTY"))
            return "MOVE";
        return "MINE";
    }

    @Override
    public String getName()
    {
        Random random = new Random();
        int adjIndex = random.nextInt(ADJECTIVES.length);
        int nounIndex = random.nextInt(NOUNS.length);
        return ADJECTIVES[adjIndex] + " " + NOUNS[nounIndex];

    }
}
