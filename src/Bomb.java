public class Bomb extends Sprite {

    public int getHealth() {
        return health;
    }

    public int mine() {
        return 0;
    }

    private int health;
    Bomb(double left, double top, int width, int height)
    {

        super(left, top, width, height, "images/stone0.png");
        health = 5;
    }

    public String toString()
    {
        return "STONE";
    }


}
