import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.Collection;

public class World {

    final boolean print = false;
    final double rockSpawnRate = .45;
    final double diamondSpawnRate = .05;

    final double coalSpawnRate = .3;

    final double emeraldSpawnRate = 0.2;

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

                    bots.remove(0);
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


//        if (x == 0 || y == 0) objectsArround[0] = "MAPS_EDGE";
//        else if (sprites[x - 1][y - 1] == null) objectsArround[0] = "EMPTY";
//        else if (sprites[x - 1][y - 1] instanceof Miner) objectsArround[0] = "BOT:"+sprites[x - 1][y - 1].toString();
//        else objectsArround[0] = sprites[x - 1][y - 1].toString();
//        if (y == 0) objectsArround[1] = "MAPS_EDGE";
//        else if (sprites[x][y - 1] == null) objectsArround[1] = "EMPTY";
//        else if (sprites[x][y - 1] instanceof Miner) objectsArround[1] = "BOT:"+sprites[x][y - 1].toString();
//        else objectsArround[1] = sprites[x][y - 1].toString();
//        if (y == 0 || x == gridWidth - 1) objectsArround[2] = "MAPS_EDGE";
//        else if (sprites[x + 1][y - 1] == null) objectsArround[2] = "EMPTY";
//        else if (sprites[x + 1][y - 1] instanceof Miner) objectsArround[2] = "BOT:"+sprites[x + 1][y - 1].toString();
//        else objectsArround[2] = sprites[x + 1][y - 1].toString();
//        if (x == 0) objectsArround[3] = "MAPS_EDGE";
//        else if (sprites[x - 1][y] == null) objectsArround[3] = "EMPTY";
//        else if (sprites[x - 1][y] instanceof Miner) objectsArround[3] = "BOT:"+sprites[x - 1][y].toString();
//        else objectsArround[3] = sprites[x - 1][y].toString();
//        if (x == gridWidth - 1) objectsArround[4] = "MAPS_EDGE";
//        else if (sprites[x + 1][y] == null) objectsArround[4] = "EMPTY";
//        else if (sprites[x + 1][y] instanceof Miner) objectsArround[4] = "BOT:"+sprites[x + 1][y].toString();
//        else objectsArround[4] = sprites[x + 1][y].toString();
//        if (x == 0 || y == gridHeight - 1) objectsArround[5] = "MAPS_EDGE";
//        else if (sprites[x - 1][y + 1] == null) objectsArround[5] = "EMPTY";
//        else if (sprites[x - 1][y + 1] instanceof Miner) objectsArround[5] = "BOT:"+sprites[x - 1][y + 1].toString();
//        else objectsArround[5] = sprites[x - 1][y + 1].toString();
//        if (y == gridHeight - 1) objectsArround[6] = "MAPS_EDGE";
//        else if (sprites[x][y + 1] == null) objectsArround[6] = "EMPTY";
//        else if (sprites[x][y + 1] instanceof Miner) objectsArround[6] = "BOT:"+sprites[x][y + 1].toString();
//        else objectsArround[6] = sprites[x][y + 1].toString();
//        if (x == gridWidth - 1 || y == gridHeight - 1) objectsArround[7] = "MAPS_EDGE";
//        else if (sprites[x + 1][y + 1] == null) objectsArround[7] = "EMPTY";
//        else if (sprites[x + 1][y + 1] instanceof Miner) objectsArround[7] = "BOT:"+sprites[x + 1][y + 1].toString();
//        else objectsArround[7] = sprites[x + 1][y + 1].toString();


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
            for (int i = 0; i < gridWidth; i++)
                for (int j = 0; j < gridHeight; j++) {
                    actions[i][j] = "";
                    Sprite sprite = sprites[i][j];
                    if (sprite != null) {
                        if (sprite instanceof Miner) {
                            String action = ((Miner) sprite).turn(i, j, getObjectsArround(i, j));
                            int dir = ((Miner) sprite).getDir();
                            //add a new action that is not mine, maybe just make it so that you can use coal to instant mine?
                            if (action.equals("MOVE")) {
                                if (dir == 0 && i != gridWidth - 1 && sprites[i + 1][j] == null)
                                    actions[i][j] = "r";
                                else if ((dir == 1 && j != 0 && sprites[i][j - 1] == null))
                                    actions[i][j] = "u";
                                else if ((dir == 2 && i != 0 && sprites[i - 1][j] == null))
                                    actions[i][j] = "l";
                                else if ((dir == 3 && j != gridHeight - 1 && sprites[i][j + 1] == null))
                                    actions[i][j] = "d";
                            } else if (action.equals("MINE")) {
                                if (dir == 0 && i != gridWidth - 1 && (sprites[i + 1][j] instanceof Stone || sprites[i + 1][j] instanceof Diamond || sprites[i + 1][j] instanceof Bomb || sprites[i + 1][j] instanceof Coal || sprites[i + 1][j] instanceof Emerald))
                                    actions[i][j] = "r_MINE";
                                else if (dir == 3 && j != 0 && ((sprites[i][j - 1] instanceof Stone || sprites[i][j - 1] instanceof Diamond || sprites[i][j - 1] instanceof Bomb || sprites[i + 1][j] instanceof Coal || sprites[i + 1][j] instanceof Emerald)))
                                    actions[i][j] = "u_MINE";
                                else if (dir == 2 && i != 0 && ((sprites[i - 1][j] instanceof Stone || sprites[i - 1][j] instanceof Diamond || sprites[i - 1][j] instanceof Bomb || sprites[i + 1][j] instanceof Coal || sprites[i + 1][j] instanceof Emerald)))
                                    actions[i][j] = "l_MINE";
                                else if (dir == 1 && (j != gridHeight - 1 && (sprites[i][j + 1] instanceof Stone || sprites[i][j + 1] instanceof Diamond || sprites[i][j + 1] instanceof Bomb || sprites[i + 1][j] instanceof Coal || sprites[i + 1][j] instanceof Emerald)))
                                    actions[i][j] = "d_MINE";
                            }
                           //THE ONLY PROBLEM WITH THIS IS IF YOU DO VISION WITH LESS Than 5 automatically loose a turn
                            else if (action.equals("VISION")) {
                                if(((Miner) sprite).getEmerald()>5) {

                                    ArrayList<Location> diamonds = new ArrayList<Location>();
                                    for (int g = 0; g < gridWidth; g++) {
                                        for (int h = 0; h < gridHeight; h++) {
                                            if (sprites[g][h] instanceof Diamond) {
                                                diamonds.add(new Location(g, h));

                                            }
                                        }
                                    }
                                    //send the array to the bot
                                    ((Miner) sprite).vision(diamonds);
                                    ((Miner) sprite).setEmerald(((Miner) sprite).getEmerald() - 5);
                                }
                            }
                            else {
                                actions[i][j] = action;
                            }
                        } else
                            sprite.step(this);
                    }
                }
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
                                if (sprites[i + 1][j] != null) {
                                    int mine = sprites[i + 1][j].mine();
                                    if (mine != 0) sprites[i + 1][j] = null;
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(5, 8 + 1);
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
                                if (sprites[i][j - 1] != null) {
                                    int mine = sprites[i][j - 1].mine();
                                    if (mine != 0) sprites[i][j - 1] = null;
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal() + coalRNG);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        ((Miner) sprites[i][j]).setEmerald(((Miner) sprites[i][j]).getEmerald() + emeraldRNG);
                                    }
                                }
                            }
                            case "l_MINE" -> {
                                if (sprites[i - 1][j] != null) {
                                    int mine = sprites[i - 1][j].mine();
                                    if (mine != 0) sprites[i - 1][j] = null;
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal() + coalRNG);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        ((Miner) sprites[i][j]).setEmerald(((Miner) sprites[i][j]).getEmerald() + emeraldRNG);
                                    }
                                }
                            }
                            case "d_MINE" -> {
                                if (sprites[i][j + 1] != null) {
                                    int mine = sprites[i][j + 1].mine();
                                    if (mine != 0) sprites[i][j + 1] = null;
                                    if (mine == 1) {
                                        int diamondRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()) + diamondRNG);
                                    }
                                    if (mine == 2){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int coalRNG = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                                        ((Miner) sprites[i][j]).setCoal(((Miner) sprites[i][j]).getCoal() + coalRNG);
                                    }
                                    if (mine == 3){
                                        scores.put(((Miner) sprites[i][j]).getName(), scores.get(((Miner) sprites[i][j]).getName()));
                                        int emeraldRNG = ThreadLocalRandom.current().nextInt(1, 2 + 1);
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


                    g2.rotate(Math.toRadians(((Miner) sprite).getDir() * 90 + 90), (int) ((i - pan) * xScaler) + xScaler / 2, (int) (j * xScaler) + yScaler / 2);
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

        int textX = 5;
        int textY = 3;

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
            /*
            for (int i = 0; i < maths.size() / 2; i++) {
                String temp = maths.get(i);
                maths.set(i, maths.get(maths.size() - i - 1));
                maths.set(maths.size() - i - 1, temp);
            }
            */
            for (String name : maths) {
                String str = name + ": " + scores.get(name);
                if(textY + 30 >= height - 4 * height / 5 - 15){
                    textY = 3;
                    textX += longestText + 5;
                }
                if (fontMetrics.getStringBounds(str, g).getWidth() > longestText)
                    longestText = fontMetrics.getStringBounds(str, g).getWidth();
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
                g.drawString(str.substring(1), textX, height - textY);
                textY += 18;
                //TODO: add in display diamond and coal
                for (Miner m : miners) {
                    String toDisplay = (m).getName().substring(1) + ": " + scores.get(m.getName()) + " diamonds, " + (m).getCoal() + " coal, " + (m).getEmerald() + " emerald";
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
}