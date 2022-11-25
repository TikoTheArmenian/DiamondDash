public interface Bot
{



    void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers);
    String newTurn(Location currentLocation, String[] objectsDetected);

    String getName();

}