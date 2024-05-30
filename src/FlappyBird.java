import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth =360;
    int boardHeight = 640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth =34;
    int birdHeight =24;

    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;     //scaled by 1/6
    int pipeHeight = 512;

    class Bird extends GameObject{
        Bird(int _x, int _y, int _width, int _height, Image _img) {
            super(_x, _y, _width, _height, _img);
        }
    }
    Bird bird;

    class Pipe extends GameObject{
        boolean passed = false;

        Pipe(Image _img) {
            super(pipeX, pipeY, pipeWidth, pipeHeight, _img);
        }
    }

    //game logic

    int velocityX = -4; //move pipes to the left speed(相对参考系)
    int velocityY = 0;
    int gravity= 1;

    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver = false;   //bird fall down || bird collides with one of the pipes
    int score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = SetImageFile("./flappybirdbg.png");
        birdImg = SetImageFile("./flappybird.png");
        topPipeImg = SetImageFile("./toppipe.png");
        bottomPipeImg = SetImageFile("./bottompipe.png");

        //bird
        bird = new Bird(birdX,birdY,birdWidth,birdHeight,birdImg);
        pipes = new ArrayList<>();

        //place pipes times
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        //game timer
        gameLoop = new Timer(1000/60,this);//1000/60 = 16.6
        gameLoop.start();

    }

    public void placePipes(){
        // (0-1) * pipeHeight /2 -> (0-256)
        //128
        //0 - 128 -(0-256) 00> pipeHeight/4 -> 3/4 pipeHeight

        int randomPipeY = (int)(pipeY - pipeHeight/4f - Math.random()*(pipeHeight/2f));
        int openingSpace = boardHeight/4;

        Pipe topPipe =new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
//        if(++i%60==0)
//        System.out.println(j++);
        //background
        g.drawImage(backgroundImg,0,0,boardWidth,boardHeight,null);

        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        //pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        ShowScore(g);
    }

    private void ShowScore(Graphics g) {
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: " + score, 10 , 35);
        }
        else{
            g.drawString(String.valueOf(score), 10 ,35);
        }
    }

    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);
        //bird.y = Math.min(bird.y,boardHeight-birdHeight);


        //pipes
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 5; //2 pipes consists a openSpacing
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if(bird.y > boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(GameObject a ,Pipe b){
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y +b.height &&
                a.y + a.height > b.y ;
    }
    // region #Function
    private void RestartGame() {
        //restart the game ,resetting the scene
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver =false;
        gameLoop.start();
        placePipesTimer.start();
    }

    public Image SetImageFile(String name) {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource(name))).getImage();
    }

    //endregion

    // region #Implement
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;}
            if(gameOver){
                RestartGame();
            }
    }



    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    //endregion
}
