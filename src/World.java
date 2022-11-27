import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class World
{

  final boolean print = false;
  final double rockSpawnRate = .5;
  final double diamondSpawnRate = .05; //TODO

  private ArrayList<Bot> bots;



  private Sprite[][] sprites;

  private ArrayList<String> robotNames;
  private int width;
  private int height;
  private int mouseX;
  private int mouseY;


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



  public World(int w, int h, ArrayList<Bot> bots)
  {
    sprites = new Sprite[w][h];
    gridWidth = w; //15
    gridHeight = h; //8

    this.bots = bots;

    width = 1200;
    height = 800;

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



    turn  = 0;
    turnsPlayed = 0;

    xScaler = ((double)width)/(gridWidth);
    yScaler = ((double)height)/(gridHeight+2);


    if(bots.size()>gridHeight*gridWidth/5) throw new RuntimeException("Too many bots for given map size");

    int iterator = 1;
    for(int i = 0; i<gridWidth/5; i++) {
      for (int j = 0; j < gridHeight; j++) {
        if (bots.isEmpty()) break;
        if (bots.size() / ((double) gridWidth/5 * gridHeight - iterator) > Math.random()) {

          sprites[i][j] = new Miner(i*((double)width/gridWidth), j*((double)height/(h+2)),
                  width / w, height / (h+2), getNextName(), bots.get(0), i, j);
          bots.remove(0);
        }
        iterator++;
      }
    }
    for(int i = gridWidth/5; i<gridWidth; i++)
      for (int j = 0; j < gridHeight; j++) {
        if(Math.random()<rockSpawnRate)
          sprites[i][j] = new Stone(i*((double)width/gridWidth), j*((double)height/(h+2)),
                  width / w, height / (h+2));
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

  public String[] getObjectsArround(int x, int y)
  {
    String[] objectsArround = new String[8];


    if(x==0 || y==0)                      objectsArround[0] = "MAPS_EDGE";
    else if (sprites[x-1][y-1] ==null)    objectsArround[0] = "EMPTY";
    else                                  objectsArround[0] = sprites[x-1][y-1].toString();
    if(y==0)                              objectsArround[1] = "MAPS_EDGE";
    else if (sprites[x][y-1] ==null)      objectsArround[1] = "EMPTY";
    else                                  objectsArround[1] = sprites[x][y-1].toString();
    if(y==0 || x==gridWidth-1)            objectsArround[2] = "MAPS_EDGE";
    else if (sprites[x+1][y-1] ==null)    objectsArround[2] = "EMPTY";
    else                                  objectsArround[2] = sprites[x+1][y-1].toString();
    if(x==0)                              objectsArround[3] = "MAPS_EDGE";
    else if (sprites[x-1][y] ==null)      objectsArround[3] = "EMPTY";
    else                                  objectsArround[3] = sprites[x-1][y].toString();
    if(x==gridWidth-1)                    objectsArround[4] = "MAPS_EDGE";
    else if (sprites[x+1][y] ==null)      objectsArround[4] = "EMPTY";
    else                                  objectsArround[4] = sprites[x+1][y].toString();
    if(x==0 || y== gridHeight-1)          objectsArround[5] = "MAPS_EDGE";
    else if (sprites[x-1][y+1] ==null)    objectsArround[5] = "EMPTY";
    else                                  objectsArround[5] = sprites[x-1][y+1].toString();
    if(y==gridHeight-1)                   objectsArround[6] = "MAPS_EDGE";
    else if (sprites[x][y+1] ==null)      objectsArround[6] = "EMPTY";
    else                                  objectsArround[6] = sprites[x][y+1].toString();
    if(x==gridWidth-1 || y==gridHeight-1) objectsArround[7] = "MAPS_EDGE";
    else if (sprites[x+1][y+1] ==null)    objectsArround[7] = "EMPTY";
    else                                  objectsArround[7] = sprites[x+1][y+1].toString();

    return objectsArround;

  }
  
  public void stepAll()
  {
    //i --> x
    //j --> y
    //Iterates through each sprite once per turn
    if(turnsPlayed<turn) {
      String[][] actions = new String[gridWidth][gridHeight];
      for (int i = 0; i < gridWidth; i++)
        for (int j = 0; j < gridHeight; j++) {
          actions[i][j] = "";
          Sprite sprite = sprites[i][j];
          if (sprite != null) {
            if(sprite instanceof Miner) {
              String action = ((Miner) sprite).turn(i, j, getObjectsArround(i, j));
              int dir = ((Miner) sprite).getDir();

              if(action.equals("MOVE")) {
                if (dir == 0 && i != gridWidth - 1 && sprites[i + 1][j] == null)
                  actions[i][j] = "r";
                else if ((dir == 1 && j != 0 && sprites[i][j-1] == null))
                  actions[i][j] = "u";
                else if ((dir == 2 && i != 0 && sprites[i-1][j] == null))
                  actions[i][j] = "l";
                else if ((dir == 3 && j != gridHeight-1 && sprites[i][j+1] == null))
                  actions[i][j] = "d";
              }
              else if(action.equals("MINE")) {
                if (dir == 0 && i != gridWidth - 1 && (sprites[i + 1][j] instanceof Stone || sprites[i + 1][j] instanceof Diamond || sprites[i + 1][j] instanceof Bomb))
                  actions[i][j] = "r_MINE";
                else if (dir == 1 && j != 0 && (sprites[i][j - 1] instanceof Stone || sprites[i][j - 1] instanceof Diamond || sprites[i][j - 1] instanceof Bomb))
                  actions[i][j] = "u_MINE";
                else if (dir == 2 && i != 0 && (sprites[i - 1][j] instanceof Stone || sprites[i - 1][j] instanceof Diamond || sprites[i - 1][j] instanceof Bomb))
                  actions[i][j] = "l_MINE";
                else if (dir == 3 && j != gridHeight-1 && (sprites[i][j + 1] instanceof Stone || sprites[i][j + 1] instanceof Diamond || sprites[i][j + 1] instanceof Bomb))
                  actions[i][j] = "d_MINE";
              }
              else {
                actions[i][j] = action;
              }


            }
            else
              sprite.step(this);
          }
        }
      turnsPlayed++;
      for (int i = 0; i < gridWidth; i++)
        for (int j = 0; j < gridHeight; j++) {
          Sprite sprite = sprites[i][j];
          if(sprite instanceof Miner) {
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
              case "TURN_LEFT" -> ((Miner) sprites[i][j]).setDir((((Miner) sprites[i][j]).getDir() + 1) % 4);
              case "TURN_RIGHT" -> ((Miner) sprites[i][j]).setDir((((Miner) sprites[i][j]).getDir() - 1) % 4);
              case "r_MINE" -> {if(sprites[i + 1][j].mine()) sprites[i + 1][j] = null;}
              case "u_MINE" -> {if(sprites[i][j - 1].mine()) sprites[i][j - 1] = null;}
              case "l_MINE" -> {if(sprites[i - 1][j].mine()) sprites[i - 1][j] = null;}
              case "d_MINE" -> {if(sprites[i][j + 1].mine()) sprites[i][j + 1] = null;}
            }
          }
          if (sprite instanceof Miner){
            ((Miner) sprite).setGridX(i);
            ((Miner) sprite).setGridY(j);
          }
        }
    }




  }


  public int getWidth()
  {
    return width;
  }
  
  public int getHeight()
  {
    return height;
  }

  public String getNextName()
  {
    String name = robotNames.get(0);
    robotNames.add(robotNames.remove(0));
    return name;
  }


  
  public Sprite getSpriteAt(int x, int y)
  {
    return sprites[x][y];
  }

  public void mouseClicked(int x, int y)
  {
  }






  public void keyPressed(int key)
  {



  }




  public void keyReleased(int key)
  {
    if(key == KeyEvent.VK_SPACE)//space
    {
      turn++;
    }
  }

  public void mouseMoved(int x, int y)
  {
    mouseX = x;
    mouseY = y;
  }
  
  public String getTitle()
  {
    return "World";
  }



  //Total map 2:5
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

  public void paintComponent(Graphics g) {



    g.setColor(new Color(11, 173, 14));
    g.fillRect(0, 0, (int)(xScaler*3),(int)(yScaler*gridHeight));
    g.setColor(new Color(157, 103, 23));
    g.fillRect((int)(xScaler*3), 0, (int)(xScaler*gridWidth), (int)(yScaler*gridHeight));
    g.setColor(new Color(23, 23, 23));
    g.fillRect(0, (int)(yScaler*gridHeight), width, height);


    for(int i = 0; i < gridWidth; i++)
      for(int j = 0; j<gridHeight; j++)
      {

        g.drawRect((int)(i*xScaler),(int)(j*yScaler),(int)xScaler,(int)yScaler);


        Sprite sprite = sprites[i][j];
        if(sprite instanceof Miner)
        {

          Graphics2D g2 = (Graphics2D) g;
          AffineTransform old = g2.getTransform();
          g2.rotate(Math.toRadians(((Miner) sprite).getDir() * 90+90),(int)(i*xScaler)+sprite.getWidth()/2, (int)(j*xScaler)+sprite.getHeight()/2);
          g2.drawImage(Display.getImage(sprite.getImage()),
                  (int)(i*xScaler),
                  (int)(j*xScaler),
                  sprite.getWidth(), sprite.getHeight(), null);

          g2.setTransform(old);
        }
        else if(sprite != null && sprite.getImage()!=null)
        {
          g.drawImage(Display.getImage(sprite.getImage()),
                  (int)(i*xScaler),
                  (int)(j*xScaler),
                  sprite.getWidth(), sprite.getHeight(), null);


          if(sprite.touching(mouseX,mouseY)) {
            g.drawString("[" + i + ", "  +j + "]",10, height-height/5-10);
          }
        }
      }

  }
}