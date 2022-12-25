public class Diamond extends Sprite {

    public int getHealth() {
        return health;
    }

    public int mine() {
        health-=1;
        switch (health) {
            case 3 -> setImage("images/Diamond1.png");
            case 2 -> setImage("images/Diamond2.png");
            case 1 -> setImage("images/Diamond3.png");
        }
        if(health <=0)
            return 1;
        else
            return 0;
    }

    private int health;
    Diamond(double left, double top, int width, int height)
    {

        super(left, top, width, height, "images/Diamond0.png");
        health = 4;
    }

    public String toString()
    {
        return "STONE";
    }


}
