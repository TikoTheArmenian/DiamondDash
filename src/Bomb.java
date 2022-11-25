public class Bomb extends Sprite {

    public int getHealth() {
        return health;
    }

    public void mine() {
        health-=1;
        switch (health) {
            case 4: setImage("images/stone1.png");
                break;
            case 3: setImage("images/stone2.png");
                break;
            case 2: setImage("images/stone3.png");
        }
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
