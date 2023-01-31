
import java.util.ArrayList;

public class Play {
    public static void main(String[] args)
    {

        ArrayList<Bot> bots = new ArrayList<Bot>();
        //bots.add(new TestBot());
        bots.add(new ComplexBot());
        //bots.add(new ComplexBot());
        //bots.add(new ComplexBot());
        Display display = new Display(200 , 32, bots,20); // height MUST be divisible by 8 and width must be greater than 1/5 of height
        display.run();
    }
}
