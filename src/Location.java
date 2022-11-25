public class Location implements Comparable<Location>
{
    private int x;
    private int y;

    public Location(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static Location StringToLocation(String s)
    {
        s=s.replace(";","");
        int comma= s.indexOf(",");
        return new Location((int)Double.parseDouble(s.substring(comma+1)),
                            (int)Double.parseDouble(s.substring(0,comma)));
    }

    public int getX()
    {
        return x;
    }

    public String toString()
    {
        return "(" + x + "," + y + ")";
    }

    public int getY()
    {
        return y;
    }

    public boolean equals(Object obj)
    {
        Location other = (Location)obj;
        return x == other.getX() && y == other.getY();
    }

    // allows Location to be used in TreeSet or TreeMap
    public int compareTo(Location other)
    {
        if (x == other.getX())
            return y - other.getY();
        else
            return x - other.getX();
    }

    // allows Location to be used in HashSet or HashMap
    public int hashCode()
    {
        return x * 10 + y;
    }
}