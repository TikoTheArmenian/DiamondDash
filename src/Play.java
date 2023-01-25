
import java.util.ArrayList;

public class Play {
    public static void main(String[] args)
    {

        ArrayList<Bot> bots = new ArrayList<Bot>();
        bots.add(new TestBot3());
        bots.add(new TestBot3());

        Display display = new Display(200 , 32, bots,120); // height MUST be divisible by 8 and width must be greater than 1/5 of height
        display.run();
    }
}
