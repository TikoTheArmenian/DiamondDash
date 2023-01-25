import java.util.HashMap;
import java.util.Random;

public class TestBot2 implements Bot {

    private static final String[] ADJECTIVES = {"Awesome", "Brave", "Clever", "Daring", "Elegant", "Fierce", "Gleaming", "Intelligent", "Jaunty", "Keen", "Lively"};
    private static final String[] NOUNS = {"Android", "Bot", "Cyborg", "Droid", "Gynoid", "Machine", "Robot", "Synthetic", "Transformer", "Unit", "Warrior"};

    private int t;

    @Override
    public void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers) {
    t = 0;

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
        t++;
        if(t<=10) {
            if (objectsDetected[4].equals("EMPTY"))
                return "MOVE";
            return "MINE";
        }
        else if (t<=11) {
            return "TURN_RIGHT";
        }
        if(t<=17) {
            if (objectsDetected[6].equals("EMPTY"))
                return "MOVE";
            return "MINE";
        }
        else if (t<=18) {
            return "TURN_RIGHT";
        }
        if(t<=23) {
            if (objectsDetected[3].equals("EMPTY"))
                return "MOVE";
            return "MINE";
        }
        else if (t<=24) {
            return "TURN_RIGHT";
        }
        if(t<=29) {
            if (objectsDetected[1].equals("EMPTY"))
                return "MOVE";
            return "MINE";
        }
        else if (t<=30) {
            return "TURN_RIGHT";
        }
        if(t<=36) {
            if (objectsDetected[4].equals("EMPTY"))
                return "MOVE";
            return "MINE";
        }


        t=0;
        if (objectsDetected[4].equals("EMPTY"))
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
