import java.util.ArrayList;
import java.util.HashMap;

public interface Bot
{



    void newGame(Location currentLocation, int mapWidth, int mapHeight, int numPlayers);
    String newTurn(Location currentLocation, String[] objectsDetected, int numCoal, int numEmerald, HashMap<Location,String> vision);

    String getName();

}