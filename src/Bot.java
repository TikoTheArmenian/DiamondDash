import java.util.ArrayList;

public interface Bot
{



    void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers);
    String newTurn(Location currentLocation, String[] objectsDetected, int numCoal, int numEmerald, ArrayList<Location> vision);

    String getName();

}