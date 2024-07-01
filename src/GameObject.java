import java.awt.*;

public class GameObject {
    int x ;
    int y ;
    int width;
    int height;
    Image img;

    GameObject(int _x, int _y, int _width, int _height, Image _img)
    {
        x = _x;
        y = _y;
        width = _width ;
        height = _height;
        img = _img;
    }

    public void setSize(int _width, int _height)
    {
        width = _width ;
        height = _height;
    }

    public void setPosition(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    GameObject(Image _img)
    {
        img = _img;
    }

}
