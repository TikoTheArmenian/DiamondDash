public class Diamond extends Sprite {

    public int getHealth() {
        return health;
    }

    public boolean mine() {

        return health == 0;
    }

    private int health;
    Diamond(double left, double top, int width, int height)
    {

        super(left, top, width, height, "images/stone0.png");
        health = 5;
    }

    public String toString()
    {
        return "STONE";
    }


}
