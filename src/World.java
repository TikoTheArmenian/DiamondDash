import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class World {

    final boolean print = false;
    final double rockSpawnRate = .4;
    final double diamondSpawnRate = .05;

    final double coalSpawnRate = .1;

    final double emeraldSpawnRate = .02;

    private ArrayList<Bot> bots;


    private Sprite[][] sprites;
    private HashMap<String, Integer> scores;

    private ArrayList<String> robotNames;
    private int width;
    private int height;
    private int mouseX;
    private int mouseY;
    private int tickRate;
    private long timer;
    private boolean doTurn = true;

    private double pan;


    private int gridWidth;
    private int gridHeight;

    private int turn;
    private int turnsPlayed;

    private double xScaler;
    private double yScaler;


    //Total map 1:3
  /*
    0123456789ABCDE
  0 SSS____________
  1 SSS____________
  2 SSS____________
  3 SSS____________
  4 SSS____________
  5 DDDDDDDDDDDDDDD

  D-data
  S-starting zone
  _-mining zone

   */


    public World(int w, int h, ArrayList<Bot> bots, int tickRate) {
        sprites = new Sprite[w][h];
        scores = new HashMap<>();
        gridWidth = w; //15
        gridHeight = h; //8
        this.tickRate = tickRate;

        this.bots = bots;

        width = 1200;
        height = 800;

        pan = 0;
        timer = System.currentTimeMillis();

        robotNames = new ArrayList<String>();
        robotNames.add("images/bot_yellow.png");
        robotNames.add("images/bot_red.png");
        robotNames.add("images/bot_blue.png");
        robotNames.add("images/bot_green.png");
        robotNames.add("images/bot_purple.png");
        robotNames.add("images/bot_pink.png");
        robotNames.add("images/bot_black.png");
        robotNames.add("images/bot_white.png");
        robotNames.add("images/bot_orange.png");


        turn = 0;
        turnsPlayed = 0;


        xScaler = ((double) width) / (gridWidth);
        yScaler = ((double) height) / (gridHeight + gridHeight / 4);


        if (bots.size() > gridHeight * (((gridHeight / 8) * 15) / 5))
            throw new RuntimeException("Too many bots for given map size");

        int iterator = 1;
        for (int i = 0; i < ((gridHeight / 8) * 15) / 5; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (bots.isEmpty()) break;
                if (bots.size() / ((double) (((gridHeight / 8) * 15) / 5) * gridHeight - iterator) > Math.random()) {
                    String imageName = getNextName();
                    String intToAdd = "";
                    String colorToBeInserted;
                    if (imageName.contains("red"))
                        colorToBeInserted = "r";
                    else if (imageName.contains("blue"))
                        colorToBeInserted = "b";
                    else if (imageName.contains("green"))
                        colorToBeInserted = "g";
                    else if (imageName.contains("purple"))
                        colorToBeInserted = "p";
                    else if (imageName.contains("pink"))
                        colorToBeInserted = "i";
                    else if (imageName.contains("black"))
                        colorToBeInserted = "l";
                    else if (imageName.contains("white"))
                        colorToBeInserted = "w";
                    else if (imageName.contains("orange"))
                        colorToBeInserted = "o";
                    else
                        colorToBeInserted = "y";

                    sprites[i][j] = new Miner(i * ((double) width / gridWidth), j * ((double) height / (h + 2)),
                            width / w, height / (h + 2), imageName, bots.get(0), i, j, "mame");

                    while (scores.containsKey(colorToBeInserted + bots.get(0).getName() + intToAdd)) {

                        if (intToAdd.equals(""))
                            intToAdd = "0";
                        else
                            intToAdd = "" + (Integer.parseInt(intToAdd) + 1);
                    }

                    StringBuilder scoreKey = new StringBuilder(bots.get(0).getName() + intToAdd);
                    scoreKey.insert(0, colorToBeInserted);

                    ((Miner) sprites[i][j]).setName(scoreKey.toString());
                    scores.put(scoreKey.toString(), 0);
                    ((Miner) sprites[i][j]).setCoal(10);
                    bots.remove(0);
                    ((Miner) sprites[i][j]).newGame(new Location(i,j),gridWidth,gridHeight,bots.size());
                }
                iterator++;
            }
        }

        for (int i = (((gridHeight / 8) * 15) / 5); i < gridWidth; i++)
            for (int j = 0; j < gridHeight; j++) {
                if (Math.random() < rockSpawnRate)
                    sprites[i][j] = new Stone(i * ((double) width / gridWidth), j * ((double) height / (h + 2)),
                            width / w, height / (h + 2));
            }
        for (int i = (((gridHeight / 8) * 15) / 5); i < gridWidth; i++)
            for (int j = 0; j < gridHeight; j++) {
                if (Math.random() < diamondSpawnRate)
                    sprites[i][j] = new Diamond(i * ((double) width / gridWidth), j * ((double) height / (h + 2)),
                            width / w, height / (h + 2));
            }
        for (int i = (((gridHeight / 8) * 15) / 5); i < gridWidth; i++)
            for (int j = 0; j < gridHeight; j++) {
                if (Math.random() < coalSpawnRate)
                    sprites[i][j] = new Coal(i * ((double) width / gridWidth), j * ((double) height / (h + 2)),
                            width / w, height / (h + 2));
            }
        for (int i = (((gridHeight / 8) * 15) / 5); i < gridWidth; i++)
            for (int j = 0; j < gridHeight; j++) {
                if (Math.random() < emeraldSpawnRate)
                    sprites[i][j] = new Emerald(i * ((double) width / gridWidth), j * ((double) height / (h + 2)),
                            width / w, height / (h + 2));
            }

        if (print) {
            for (int i = 0; i < gridWidth / 5; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    System.out.print(sprites[i][j] + ", ");
                }
                System.out.println();
            }
            System.out.println();

            for (int i = 0; i < gridWidth / 5; i++) {
                for (int j = 0; j < gridHeight; j++) {
                    System.out.print("[" + (int) (i * ((double) width / gridWidth)) + ", " + (int) (j * ((double) height / gridHeight)) + "], ");
                }
                System.out.println();
            }
        } //prints initial starting positions of robots

    }

    public String[] getOreAround(int x, int y) {
        String[] objectsArround = new String[8];
        for (int i = 0; i < objectsArround.length; i++) {

            int dx = i % 3 - 1;
            int dy = i / 3 - 1;
            if(i>3)
            {
                dx = (i+1) % 3 - 1;
                dy = (i+1) / 3 - 1;
            }
            int newX = x + dx;
            int newY = y + dy;
            if (newX < 0 || newY < 0 || newX >= gridWidth || newY >= gridHeight) {
                objectsArround[i] = "MAPS_EDGE";
            } else if (sprites[newX][newY] == null) {
                objectsArround[i] = "EMPTY";
            } else if (sprites[newX][newY] instanceof Miner) {
                objectsArround[i] = "BOT:" + sprites[newX][newY].toString();
            } else {
                objectsArround[i] = sprites[newX][newY].toString();
            }
        }


        return objectsArround;

    }

    public String[] getObjectsArround(int x, int y) {
        String[] objectsArround = new String[8];

        for (int i = 0; i < objectsArround.length; i++) {

            int dx = i % 3 - 1;
            int dy = i / 3 - 1;
            if(i>3)
            {
                dx = (i+1) % 3 - 1;
                dy = (i+1) / 3 - 1;
            }
            int newX = x + dx;
            int newY = y + dy;
            if (newX < 0 || newY < 0 || newX >= gridWidth || newY >= gridHeight) {
                objectsArround[i] = "MAPS_EDGE";
            } else if (sprites[newX][newY] == null) {
                objectsArround[i] = "EMPTY";
            } else if (sprites[newX][newY] instanceof Miner) {
                objectsArround[i] = "BOT:" + sprites[newX][newY].toString();
            } else {
                objectsArround[i] = sprites[newX][newY].toString();
            }
        }


        return objectsArround;

    }

    public String[][] fixMovingActions(String[][] actions, ArrayList<Location> locs)
    {
        for(Location loc : locs) {
            if (sprites[loc.getX()][loc.getY()] == null) {
                ArrayList<Location> spotsMoving = new ArrayList<>();
                if (loc.getX() != 0 && actions[loc.getX() - 1][loc.getY()].equals("r"))
                    spotsMoving.add(new Location(loc.getX() - 1, loc.getY()));
                if (loc.getY() != gridHeight - 1 && actions[loc.getX()][loc.getY() + 1].equals("u"))
                    spotsMoving.add(new Location(loc.getX(), loc.getY() + 1));
                if (loc.getX() != gridWidth - 1 && actions[loc.getX() + 1][loc.getY()].equals("l"))
                    spotsMoving.add(new Location(loc.getX() + 1, loc.getY()));
                if (loc.getY() != 0 && actions[loc.getX()][loc.getY() - 1].equals("d"))
                    spotsMoving.add(new Location(loc.getX(), loc.getY() - 1));
                int mover = (int) (spotsMoving.size() * Math.random());
                for (int i = 0; i < spotsMoving.size(); i++)
                    if (i != mover)
                        actions[spotsMoving.get(i).getX()][spotsMoving.get(i).getY()] = "";
            }
        }
        return actions;
    }

    public String[][] fixMiningActions(String[][] actions, ArrayList<Location> locs)
    {
        for(Location loc : locs)
        {

                ArrayList<Location> spotsMining = new ArrayList<>();
                if (loc.getX() != 0 && actions[loc.getX() - 1][loc.getY()].equals("r_MINE"))
                    spotsMining.add(loc);
                if (loc.getY() != gridHeight - 1 && actions[loc.getX()][loc.getY() + 1].equals("u_MINE"))
                    spotsMining.add(loc);
                if (loc.getX() != gridWidth - 1 && actions[loc.getX() + 1][loc.getY()].equals("l_MINE"))
                    spotsMining.add(loc);
                if (loc.getY() != 0 && actions[loc.getX()][loc.getY() - 1].equals("d_MINE"))
                    spotsMining.add(loc);
                int miner = (int) (spotsMining.size() * Math.random());
                for (int i = 0; i < spotsMining.size(); i++)
                    if (i != miner)
                        actions[spotsMining.get(i).getX()][spotsMining.get(i).getY()] = "";
        }
        return actions;
    }



    public void stepAll() {
        //i --> x
        //j --> y
        //Iterates through each sprite once per turn

        if (System.currentTimeMillis() - timer >= tickRate && doTurn
        ) {
            turn++;
            timer = System.currentTimeMillis();
        }

        if (turnsPlayed < turn && doTurn) {
            String[][] actions = new String[gridWidth][gridHeight];
            ArrayList<Location> locsBeingMined= new ArrayList<>();
            ArrayList<Location> locsBeingMovedTo= new ArrayList<>();
            for (int i = 0; i < gridWidth; i++)
                for (int j = 0; j < gridHeight; j++) {
                    actions[i][j] = "";
                    Sprite sprite = sprites[i][j];
                    if (sprite != null) {
                        if (sprite instanceof Miner) {
                            String action = ((Miner) sprite).turn(i, j, getObjectsArround(i, j));
                            int dir = ((Miner) sprite).getDir();
                            //add a new action that is not mine, maybe just make it so that you can use coal to instant mine?
                            switch (action) {
                                case "MOVE":
                                    ((Miner) sprite).vision(null);
                                    if (dir == 0 && i != gridWidth - 1 && sprites[i + 1][j] == null) {
                                        actions[i][j] = "r";
                                        locsBeingMovedTo.add(new Location(i+1,j));
                                    }
                                    else if ((dir == 1 && j != 0 && sprites[i][j - 1] == null)) {
                                        actions[i][j] = "u";
                                        locsBeingMovedTo.add(new Location(i,j-1));
                                    }
                                    else if ((dir == 2 && i != 0 && sprites[i - 1][j] == null)) {
                                        actions[i][j] = "l";
                                        locsBeingMovedTo.add(new Location(i-1,j));
                                    }
                                    else if ((dir == 3 && j != gridHeight - 1 && sprites[i][j + 1] == null)) {
                                        actions[i][j] = "d";
                                        locsBeingMovedTo.add(new Location(i,j+1));
                                    }

                                    break;
                                case "MINE":
                                    ((Miner) sprite).vision(null);
                                    if (dir == 0 && i != gridWidth - 1 && (sprites[i + 1][j] instanceof Stone || sprites[i + 1][j] instanceof Diamond || sprites[i + 1][j] instanceof Bomb || sprites[i + 1][j] instanceof Coal || sprites[i + 1][j] instanceof Emerald)) {
                                        actions[i][j] = "r_MINE";
                                        locsBeingMined.add(new Location(i + 1, j));
                                    } else if (dir == 1 && j != 0 && ((sprites[i][j - 1] instanceof Stone || sprites[i][j - 1] instanceof Diamond || sprites[i][j - 1] instanceof Bomb || sprites[i][j -1] instanceof Coal || sprites[i][j -1] instanceof Emerald))) {
                                        actions[i][j] = "u_MINE";
                                        locsBeingMined.add(new Location(i, j - 1));
                                    } else if (dir == 2 && i != 0 && ((sprites[i - 1][j] instanceof Stone || sprites[i - 1][j] instanceof Diamond || sprites[i - 1][j] instanceof Bomb || sprites[i - 1][j] instanceof Coal || sprites[i - 1][j] instanceof Emerald))) {
                                        actions[i][j] = "l_MINE";
                                        locsBeingMined.add(new Location(i - 1, j));
                                    } else if (dir == 3 && (j != gridHeight - 1 && (sprites[i][j + 1] instanceof Stone || sprites[i][j + 1] instanceof Diamond || sprites[i][j + 1] instanceof Bomb || sprites[i][j + 1] instanceof Coal || sprites[i][j + 1] instanceof Emerald))) {
                                        actions[i][j] = "d_MINE";
                                        locsBeingMined.add(new Location(i, j + 1));
                                    }
                                    break;
                                case "VISION":
                                    if (((Miner) sprite).getEmerald() >= 3) {
                                        //System.out.println("new vision");
                                        HashMap<Location, String> vision = new HashMap<>();
                                        int l = ((Miner) sprite).getGridX() - 50;
                                        if (l < 0)
                                            l = 0;
                                        int k = ((Miner) sprite).getGridX() + 50;
                                        if (k > gridWidth)
                                            k = gridWidth;
                                        for (int g = l; g < k; g++) {
                                            for (int h = 0; h < gridHeight; h++) {
                                                if (sprites[g][h] == null)
                                                    vision.put(new Location(g, h), "EMPTY");
                                                else
                                                    vision.put(new Location(g, h), sprites[g][h].toString());
                                            }
                                        }
                                        /*
                                        System.out.println("see if this is the problem:");
                                        for (Location loc: vision.keySet()) {
                                            String obj = loc.toString();
                                            String value = vision.get(loc).toString();
                                            System.out.println(obj + " " + value);
                                        }
                                        */
                                        //BEFORE PUSHING UPDATE README WITH IT
                                        ((Miner) sprite).vision(vision);
                                        ((Miner) sprite).setEmerald(((Miner) sprite).getEmerald() - 3);
                                    }
                                    break;
                                default:
                                    actions[i][j] = action;
                                    break;
                            }
                        } else
                            sprite.step(this);

                    }
                }
            actions = fixMiningActions(actions,locsBeingMined);
            actions = fixMovingActions(actions,locsBeingMovedTo);

            turnsPlayed++;
            for (int i = 0; i < gridWidth; i++)
                for (int j = 0; j < gridHeight; j++) {
                    Sprite sprite = sprites[i][j];
                    if (sprite instanceof Miner) {
                        switch (actions[i][j]) {
                            case "r" -> {
                                sprites[i + 1][j] = sprites[i][j];
                                sprites[i][j] = null;
                            }
                            case "u" -> {
                                sprites[i][j - 1] = sprites[i][j];
                                sprites[i][j] = null;
                            }
                            case "l" -> {
                                sprites[i - 1][j] = sprites[i][j];
                                sprites[i][j] = null;
                            }
                            case "d" -> {
                                sprites[i][j + 1] = sprites[i][j];
                                sprites[i][j] = null;
                            }
                            case "TURN_LEFT" -> ((Miner) sprites[i][j]).turnLeft();
                            case "TURN_RIGHT" -> ((Miner) sprites[i][j]).turnRight();
                            case "r_MINE" -> {
                                if (sprites[i + 1][j] != null && ((Miner) sprites[i][j]).getCoal() > 0) {
                                    int mine = sprites[i + 1][j].mine();
                                    if (mine != 0) {
                                        sprites[i + 1][j] = null;
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal()-1);
                                    }
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(3, 5 + 1);
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal() + coalRNG);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setEmerald(((Miner) sprites[i][j]).getEmerald() + emeraldRNG);
                                    }
                                }
                            }
                            case "u_MINE" -> {
                                if (sprites[i][j - 1] != null && ((Miner) sprites[i][j]).getCoal() > 0) {
                                    int mine = sprites[i][j - 1].mine();
                                    if (mine != 0) {
                                        sprites[i][j - 1] = null;
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal()-1);
                                    }
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(3, 5 + 1);
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal() + coalRNG);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setEmerald(((Miner) sprites[i][j]).getEmerald() + emeraldRNG);
                                    }
                                }
                            }
                            case "l_MINE" -> {
                                if (sprites[i - 1][j] != null && ((Miner) sprites[i][j]).getCoal() > 0) {
                                    int mine = sprites[i - 1][j].mine();
                                    if (mine != 0) {
                                        sprites[i - 1][j] = null;
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal()-1);
                                    }
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(3, 5 + 1);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setEmerald(((Miner) sprites[i][j]).getEmerald() + emeraldRNG);
                                    }
                                }
                            }
                            case "d_MINE" -> {
                                if (sprites[i][j + 1] != null && ((Miner) sprites[i][j]).getCoal() > 0) {
                                    int mine = sprites[i][j + 1].mine();
                                    //System.out.println("d_MINE on " + sprites[i][j + 1].getImage() + " mine: " + mine);
                                    if (mine != 0) {
                                        sprites[i][j + 1] = null;
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal()-1);
                                    }
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(3, 5 + 1);
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal() + coalRNG);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setEmerald(((Miner) sprites[i][j]).getEmerald() + emeraldRNG);
                                    }
                                }
                            }
                        }
                    }
                    if (sprite instanceof Miner) {
                        ((Miner) sprite).setGridX(i);
                        ((Miner) sprite).setGridY(j);
                    }
                }
        }


    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getNextName() {
        String name = robotNames.get(0);
        robotNames.add(robotNames.remove(0));
        return name;
    }


    public Sprite getSpriteAt(int x, int y) {
        return sprites[x][y];
    }

    public void mouseClicked(int x, int y) {
        /*
        int viewWidth = (gridHeight / 8) * 15;
        double xScaler = ((double) width) / viewWidth;

        sprites[(int) (x / xScaler + pan)][(int) (y / yScaler)] = new Coal((x / xScaler + pan) * ((double) width / gridWidth), (y / yScaler) * ((double) height / (gridHeight + 2)),
                width / gridWidth, height / (gridHeight + 2));
    */
    }


    public void keyPressed(int key) {
        if (key == KeyEvent.VK_SPACE)
            doTurn = false;


    }

    // D C M C M D


    public void keyReleased(int key) {
        if (key == KeyEvent.VK_SPACE) {//space
            doTurn = true;
            timer = System.currentTimeMillis();
        }
        if (key == KeyEvent.VK_RIGHT)
            if (pan < gridWidth - (gridHeight / 8) * 15) pan += 2;
        if (key == KeyEvent.VK_LEFT)
            if (pan > 1) pan -= 2;
    }

    public void mouseMoved(int x, int y) {
        mouseX = x;
        mouseY = y;
        if (mouseX < 60 && pan >= .2)
            pan -= .2;
        if (mouseX < 30 && pan >= .2)
            pan -= .2;
        if (mouseX > 1140 && pan <= gridWidth - (gridHeight / 8) * 15 - .2)
            pan += .2;
        if (mouseX > 1170 && pan <= gridWidth - (gridHeight / 8) * 15 - .2)
            pan += .2;
        if (pan >= gridWidth - (gridHeight / 8) * 15 - .2)
            pan = gridWidth - (gridHeight / 8) * 15;
        if (pan <= .2)
            pan = 0;
    }

    public String getTitle() {
        return "World";
    }


    //Total view 15:8

    public void paintComponent(Graphics g) {

        int viewWidth = (gridHeight / 8) * 15;
        double xScaler = ((double) width) / viewWidth;
        ArrayList<Miner> miners = new ArrayList<>();
        Font font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);

        g.setColor(new Color(255, 0, 252));
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(11, 173, 14));
        g.fillRect(0, 0, (int) (xScaler * (((gridHeight / 8) * 15) / 5)), (int) (yScaler * gridHeight));
        g.setColor(new Color(157, 103, 23));
        g.fillRect((int) (xScaler * ((gridHeight / 8) * 15) / 5 - pan * xScaler), 0, (int) (xScaler * gridWidth) - (int) (xScaler * ((gridHeight / 8) * 15) / 5 - pan * xScaler), (int) (yScaler * gridHeight));
        g.setColor(new Color(23, 23, 23));
        g.fillRect(0, (int) (yScaler * gridHeight), width, height);


        for (int i = (int) pan; i < Math.min(viewWidth + pan, gridWidth); i++)
            for (int j = 0; j < gridHeight; j++) {
                g.setColor(new Color(94, 94, 94));
                g.drawRect((int) ((i - pan) * xScaler), (int) (j * yScaler), (int) xScaler, (int) yScaler);


                Sprite sprite = sprites[i][j];
                if (sprite instanceof Miner) {
                    Graphics2D g2 = (Graphics2D) g;
                    AffineTransform old = g2.getTransform();
                    Font font2 = new Font("Futura", Font.BOLD, 14);
                    g2.setFont(font2);
                    FontMetrics fontMetrics = g2.getFontMetrics();


                    if(((Miner) sprite).getDir() == 0) {
                        g2.rotate(Math.toRadians(90), (int) ((i - pan) * xScaler) + (int) xScaler / 2, (int) (j * yScaler) + (int) yScaler / 2);
                    }
                    else if (((Miner) sprite).getDir() == 1) {
                        g2.rotate(Math.toRadians(0), (int) ((i - pan) * xScaler) + (int) xScaler / 2, (int) (j * yScaler) + (int) yScaler / 2);

                    }
                    else if (((Miner) sprite).getDir() == 2) {
                        g2.rotate(Math.toRadians(-90), (int) ((i - pan) * xScaler) + (int) xScaler / 2, (int) (j * yScaler) + (int) yScaler / 2);

                    }
                    else if (((Miner) sprite).getDir() == 3) {
                        g2.rotate(Math.toRadians(-180), (int) ((i - pan) * xScaler) + (int) xScaler / 2, (int) (j * yScaler) + (int) yScaler / 2);
                    }
                   //g2.rotate(Math.toRadians(((Miner) sprite).getDir() * 90 + 90), (int) ((i - pan) * xScaler) + xScaler / 2, (int) (j * xScaler) + yScaler / 2);
                    g2.drawImage(Display.getImage(sprite.getImage()),
                            (int) ((i - pan) * xScaler),
                            (int) (j * yScaler),
                            (int) (yScaler), (int) xScaler, null);
                    g2.setTransform(old);
                    miners.add((Miner) sprite);
                } else if (sprite != null && sprite.getImage() != null) {
                    g.drawImage(Display.getImage(sprite.getImage()),
                            (int) ((i - pan) * xScaler),
                            (int) (j * yScaler),
                            (int) yScaler, (int) xScaler, null);
                }
            }
        //I added this
        for(int i=0;i<miners.size();i++){
            for(int j=i+1;j<miners.size();j++){
                Miner tempI=miners.get(i);
                Miner tempJ=miners.get(j);
                if(scores.get(tempI.getName())>scores.get(tempJ.getName())){
                    miners.set(i,tempJ);
                    miners.set(j,tempI);
                }
            }
        }
        //is this where you draw??
        g.setColor(new Color(255, 255, 255));
        g.drawString("[" + (int) (mouseX / xScaler + pan) + ", " + (int) (mouseY / yScaler) + "]", 5, height - height / 5 + 20);

        double longestText = 0;
        FontMetrics fontMetrics = g.getFontMetrics();
        font = new Font("Arial", Font.PLAIN, 14);
        g.setFont(font);
        Set<String> keySet = scores.keySet();
        ArrayList<String> maths = new ArrayList<String>();
        for(String name:keySet)
        {
            maths.add(name);
        }

            for (int i = 0; i < maths.size(); i++) {
                for (int j = i + 1; j < maths.size(); j++) {
                    String tempI = maths.get(i);
                    String tempJ = maths.get(j);
                    if (scores.get(tempI) > scores.get(tempJ)) {
                        maths.set(i, tempJ);
                        maths.set(j, tempI);
                    }
                }
            }
        drawScores(g, xScaler, miners, longestText, fontMetrics, maths);


    }

    private void drawScores(Graphics g, double xScaler, ArrayList<Miner> miners, double longestText, FontMetrics fontMetrics, ArrayList<String> maths) {
        int textX = 5;
        int textY = 20;
        for (String name : maths) {
            String str = name + ": " + scores.get(name);
            if(textY + 30 >= height - 4 * height / 5 - 15){
                textY = 20;
                textX += longestText + 5;
            }
            if (fontMetrics.getStringBounds(str, g).getWidth() > longestText)
                longestText = fontMetrics.getStringBounds(str, g).getWidth();
            setColor(g, name);
            g.drawString(str.substring(1), textX, height - textY);
            textY += 18;
            //TODO: add in display diamond and coal
            for (Miner m : miners) {
                String toDisplay = (m).getName().substring(1) + ": " + scores.get(m.getName()) + " diamond, " + (m).getCoal() + " coal, " + (m).getEmerald() + " emerald";
                // + " dir: " + ((Miner)m).getDir();
                double textWidth = fontMetrics.getStringBounds(toDisplay, g).getWidth();
                double textHeight = fontMetrics.getStringBounds(toDisplay, g).getHeight();
                int x = (int) ((m.getGridX() - pan) * xScaler);
                int y = (int) (m.getGridY() * yScaler);
                if (mouseX > x && mouseX < x + (int) xScaler && mouseY > y && mouseY < y + (int) (yScaler)) {
                    x -= textWidth / 2;
                    y += textHeight / 2;
                    g.setColor(Color.BLACK);
                    g.drawRect(x - 1, y - 1, (int) textWidth + 1, 1 + (int) textHeight);
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, (int) textWidth, (int) textHeight);
                    g.setColor(Color.BLACK);
                    g.drawString(toDisplay, x + 2, y + 12);
                }
            }
        }
    }

    private void setColor(Graphics g, String name) {
        switch (name.substring(0,1)) {
            case "y" -> g.setColor(Color.YELLOW);
            case "r" -> g.setColor(Color.RED);
            case "b" -> g.setColor(Color.BLUE);
            case "g" -> g.setColor(Color.GREEN);
            case "p" -> g.setColor(new Color(159, 1, 138));
            case "i" -> g.setColor(Color.pink);
            case "l" -> g.setColor(Color.GRAY);
            case "w" -> g.setColor(Color.WHITE);
            case "o" -> g.setColor(Color.ORANGE);
            default -> g.setColor(Color.CYAN);
        }
    }
}