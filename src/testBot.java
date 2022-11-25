public class testBot implements Bot {

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
     */

    @Override
    public String newTurn(Location currentLocation, String[] objectsDetected) {


        if(objectsDetected[4].equals("EMPTY"))
            return "MOVE";
        return "MINE";
    }

    @Override
    public String getName()
    {
        return "Test Bot";
    }
}
