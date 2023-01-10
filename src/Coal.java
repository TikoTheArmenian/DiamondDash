public class Coal extends Sprite {

    public int getHealth() {
        return health;
    }
    //TODO: Tiko should we balance this more to make it easier to mine coal?
    public int mine() {
        health-=1;
        switch (health) {
            case 3 -> setImage("images/Coal1.png");
            case 2 -> setImage("images/Coal2.png");
            case 1 -> setImage("images/Coal3.png");
        }
        if(health <=0)
            return 2;
        else
            return 0;
    }

    private int health;
    Coal(double left, double top, int width, int height)
    {

        super(left, top, width, height, "images/Coal0.png");
        health = 4;
    }

    public String toString()
    {
        return "COAL";
    }


}
