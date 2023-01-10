import java.util.ArrayList;

public class Miner extends Sprite {

    private Bot bot;

    int coal;

    int emerald;

    private ArrayList<Location> vision;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getDir() {
        return dir;}

    private int dir; //0: right, 1: up, 2: left, 3: down

    public Bot getBot() {
        return bot;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getCoal() { return coal; }

    public void setCoal(int coal) { this.coal = coal; }

    public int getEmerald() { return emerald; }

    public void setEmerald(int emerald) { this.emerald = emerald; }

    public void vision(ArrayList<Location> vision){this.vision = vision;}

    int gridX;
    int gridY;

    Miner(double left, double top, int width, int height, String image, Bot bot, int gridX, int gridY, String name)
    {
        super(left, top, width, height, image);
        this.gridX = gridX;
        this.gridY = gridY;
        this.bot = bot;
        this.name = name;
        dir = 0; //0: right, 1: up, 2: left, 3: down
    }



    public String turn(int gridX, int gridY, String[] objectsDetected)
    {
        return bot.newTurn(new Location(gridX,gridY),objectsDetected, coal, emerald, vision);
    }

    public void turnRight()
    {
        dir++;
        if(dir==4)
            dir=0;
    }
    public void turnLeft()
    {
        dir--;
        if(dir==-1)
            dir=3;
    }

    @Override
    public String toString() {
        return name;
    }
}
