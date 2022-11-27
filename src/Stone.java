public class Stone extends Sprite {

    public int getHealth() {
        return health;
    }

    public boolean mine() {
        health-=1;
        switch (health) {
            case 4: setImage("images/stone1.png");
                break;
            case 3: setImage("images/stone2.png");
                break;
            case 2: setImage("images/stone3.png");
        }
        return health == 0;
    }

    private int health;
    Stone(double left, double top, int width, int height)
    {

        super(left, top, width, height, "images/stone0.png");
        health = 5;
    }

    public String toString()
    {
        return "STONE";
    }


}
