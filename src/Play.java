
import java.util.ArrayList;

public class Play {
    public static void main(String[] args)
    {
        ArrayList<Bot> bots = new ArrayList<Bot>();
        bots.add(new testBot());
        bots.add(new testBot());
        bots.add(new testBot());
        Display display = new Display(15, 8, bots);
        display.run();
    }
}
