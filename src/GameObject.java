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

    GameObject(Image _img)
    {
        img = _img;
    }

}
