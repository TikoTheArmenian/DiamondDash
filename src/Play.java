
import java.util.ArrayList;

public class Play {
    public static void main(String[] args)
    {
        //instance of should be kept within the miner itself
        //TODO: to fix
        //rng for value of score diamond
        //fix top or bottom always has advantage
        //add a coal cap
        ArrayList<Bot> bots = new ArrayList<Bot>();


        bots.add(new ExampleBotStoneMiner());
        bots.add(new ExampleBotStoneMiner());
        bots.add(new ExampleBotStoneMiner());



        Display display = new Display(200 , 32, bots,160); // height MUST be divisible by 8 and width must be greater than 1/5 of height
        display.run();
    }
}
