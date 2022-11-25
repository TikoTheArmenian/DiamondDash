public class MobileSprite extends Sprite  //TODO EXTENDS MIGHT ME WRONG
{
  private double vx;
  private double vy;
  
  public MobileSprite(double left, double top, int width, int height, String image,
                      double velocityX, double velocityY)
  {
    super(left, top, width, height, image);
    vx = velocityX;
    vy = velocityY;

  }

  public MobileSprite() {
    super();
  }

  public void moveTowards(Sprite s, double speed)
  {
    double x =  (s.getLeft()-s.getWidth()/2) - (getLeft()-getWidth()/2);
    double y = (s.getTop()-s.getHeight()/2) - (getTop()-getHeight()/2);

    double theta = Math.atan2(y,x);
    setVX(speed*Math.cos(theta));
    setVY(speed*Math.sin(theta));

  }

  public double getVY()
  {
    return vy;
  }
  
  public void setVY(double velocityY)
  {
    vy = velocityY;
  }

  public double getVX()
  {
    return vx;
  }

  public void setVX(double velocityX)
  {
    vx = velocityX;
  }

  public void shootTo(double xPos, double yPos, double speed) {
    double x = xPos - getLeft()-getWidth()/2;
    double y = yPos - getTop()-getHeight()/2;

    double theta = Math.atan2(y,x);
    setVX(speed*Math.cos(theta));
    setVY(speed*Math.sin(theta));
  }

  public void step(World world)
  {
    if (getLeft() < 0)
      vx = Math.abs(vx);
    if (getLeft() + getWidth() > world.getWidth())
      vx = -Math.abs(vx);
    if (getTop() + getHeight() > world.getHeight())
      vy = -Math.abs(vy);


    setLeft(getLeft()+vx);
    setTop(getTop()+vy);



  }
}