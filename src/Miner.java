public class Miner extends Sprite {

    private Bot bot;

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

    int gridX;
    int gridY;

    Miner(double left, double top, int width, int height, String image, Bot bot, int gridX, int gridY)
    {
        super(left, top, width, height, image);
        this.gridX = gridX;
        this.gridY = gridY;
        this.bot = bot;
        dir = 0; //0: right, 1: up, 2: left, 3: down
    }



    public String turn(int gridX, int gridY, String[] objectsDetected)
    {
        return bot.newTurn(new Location(gridX,gridY),objectsDetected);
    }

    @Override
    public String toString() {
        return bot.getName();
    }
}
