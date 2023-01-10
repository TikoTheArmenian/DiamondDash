public class Emerald extends Sprite {

    public int getHealth() {
        return health;
    }
    //TODO: Tiko should we balance this more to make it easier to mine coal?
    public int mine() {
        health-=1;
        switch (health) {
            case 3 -> setImage("images/Emerald1.png");
            case 2 -> setImage("images/Emerald2.png");
            case 1 -> setImage("images/Emerald3.png");
        }
        if(health <=0)
            return 3;
        else
            return 0;
    }

    private int health;
    Emerald(double left, double top, int width, int height)
    {

        super(left, top, width, height, "images/Emerald0.png");
        health = 4;
    }

    public String toString()
    {
        return "EMERALD";
    }


}
