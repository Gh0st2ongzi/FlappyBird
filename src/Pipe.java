import java.awt.*;

class Pipe extends GameObject{
    private int pipeX = 360;
    private int pipeY = 0;
    private int pipeWidth = 64;     //scaled by 1/6
    private int pipeHeight = 512;

    public boolean passed = false;

    Pipe(Image _img) {
        super(_img);
        setSize(pipeWidth, pipeHeight);
        setPosition(pipeX, pipeY);
    }

}