import java.util.Random;

public class ComplexBot implements Bot {

    private static final String[] ADJECTIVES = {
            "Awesome", "Brave", "Clever", "Daring", "Elegant", "Fierce", "Gleaming", "Intelligent", "Jaunty", "Keen", "Lively"
    };
    private static final String[] NOUNS = {
            "Android", "Bot", "Cyborg", "Droid", "Gynoid", "Machine", "Robot", "Synthetic", "Transformer", "Unit", "Warrior"
    };
    private int facing = 1;
    /*
         0
        3 1
         2
     */


    //All bots start facing to the right
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
     "EMPTY"
     "STONE"
     "MAPS_EDGE"
     "DIAMOND"
     "BOT:[Name of bot]"
     "MAPS_EDGE"
     */

    @Override
    public String newTurn(Location currentLocation, String[] objectsDetected, int numCoal) {
//        System.out.println("[ " + objectsDetected[0] + ", " + objectsDetected[1] + ", " + objectsDetected[2] + "\n"
//                + objectsDetected[3] + ", " + "BOT" + ", " + objectsDetected[4] + "\n"
//                + objectsDetected[5] + ", " + objectsDetected[6] + ", " + objectsDetected[7] + "]");
//        System.out.println();
//        System.out.println();

        String moveOrMine;
        int coal = numCoal;

        if(facing == 1)
            if(objectsDetected[4].equals("EMPTY"))
                //if(numCoal>3)
                //    moveOrMine = "COAL";
                //else
                    moveOrMine = "MOVE";
            else
                moveOrMine="MINE";
        else if (facing == 2)
            if(objectsDetected[6].equals("EMPTY"))
               // if(numCoal>3)
               //     moveOrMine = "COAL";
               // else
                    moveOrMine = "MOVE";
            else
                moveOrMine="MINE";
        else if (facing == 3)
            if(objectsDetected[3].equals("EMPTY"))
                //if(numCoal>3)
               //     moveOrMine = "COAL";
               // else
                    moveOrMine = "MOVE";
            else
                moveOrMine="MINE";
        else
            if(objectsDetected[1].equals("EMPTY"))
                //if(numCoal>3)
               //     moveOrMine = "COAL";
               // else
                    moveOrMine = "MOVE";
            else
                moveOrMine="MINE";

        if (objectsDetected[1].equals("DIAMOND"))
            return doUp(moveOrMine);
        if (objectsDetected[4].equals("DIAMOND"))
            return doRight(moveOrMine);
        if (objectsDetected[6].equals("DIAMOND"))
            return doDown(moveOrMine);
        if (objectsDetected[3].equals("DIAMOND"))
            return doLeft(moveOrMine);
        if (objectsDetected[0].equals("DIAMOND"))
            return doUp(moveOrMine);
        if (objectsDetected[2].equals("DIAMOND"))
            return doRight(moveOrMine);
        if (objectsDetected[5].equals("DIAMOND"))
            return doDown(moveOrMine);
        if (objectsDetected[7].equals("DIAMOND"))
            return doRight(moveOrMine);

        return doRight(moveOrMine);
    }



    public String doRight(String whatToDo) {
        if (facing == 1)
            return whatToDo;
        if (facing == 2) {
            facing = (facing - 1) % 4;
            return "TURN_LEFT";
        }

        if (facing == 3) {
            facing = (facing - 1) % 4;
            return "TURN_LEFT";
        }
            facing = 1;
            return "TURN_RIGHT";

    }

    public String doLeft(String whatToDo) {
        if (facing == 1) {
            facing = 0;
            return "TURN_LEFT";
        }

        if (facing == 2) {
            facing = 3;
            return "TURN_RIGHT";
        }

        if (facing == 3)
            return whatToDo;
        facing = 4;
        return "TURN_LEFT";

    }

    public String doUp(String whatToDo) {
        if (facing == 1) {
            facing = 0;
            return "TURN_LEFT";
        }

        if (facing == 2) {
            facing = 3;
            return "TURN_RIGHT";
        }

        if (facing == 3) {
            facing = 0;
            return "TURN_RIGHT";
        }
        return whatToDo;
    }

    public String doDown(String whatToDo) {
        if (facing == 1) {
            facing = (facing + 1) % 4;
            return "TURN_RIGHT";
        }

        if (facing == 2)
            return whatToDo;
        {
            facing = (facing - 1) % 4;
            return "TURN_LEFT";
        }

    }

    @Override
    public String getName() {
        Random random = new Random();
        int adjIndex = random.nextInt(ADJECTIVES.length);
        int nounIndex = random.nextInt(NOUNS.length);
        return ADJECTIVES[adjIndex] + " " + NOUNS[nounIndex];

    }
}
