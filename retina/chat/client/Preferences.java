package retina.chat.client;



public class Preferences
{
    public static final String SMALL = "Small";
    public static final String MEDIUM = "Medium";
    public static final String LARGE = "Large";
    public static final String EXTRA_LARGE = "Extra Large";

    public static final String BLACK = "Black";
    public static final String BLUE = "Blue";
    public static final String GREEN = "Green";
    public static final String RED = "Red";

    private String color;
    private String size;

    /** Creates a new instance of Preferences */
    public Preferences()
    {
    }

    public Preferences(String newColor, String newSize)
    {
        color = newColor;
        size = newSize;
    }

    public void setColor(String newColor)
    {
        color = newColor;
    }

    public String getColor()
    {
        return color;
    }

    public void setSize(String newSize)
    {
        size = newSize;
    }

    public String getSize()
    {
        return size;
    }
}
