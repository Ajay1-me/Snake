import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;



public class GamePanel extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];     //HOLDS THE BODY PARTS IN THE X-COORDINATE including the head of the snake: by setting the array x equal to game units we are saying that the snake wont geting bigger than the game itself 
    final int y[] = new int[GAME_UNITS];     //HOLDS THE BODY PARTS IN THE Y-COORDINATE
    int bodyParts = 6;                       //the game will start with 6 body parts 
    int applesEaten;
    int appleX;                              //x coordinate of where the apple is located; will appear randomly
    int appleY;                              //y coordinate of apples location
    char direction = 'R';
    boolean running = false;                //initial the snake will be stopped so = false
    Timer timer;
    Random random;
    
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground((Color.black));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            
            //the for loop below displays the grid line but for some reason it doesn't work when run 
            for(int i = 0; i <SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++){     //this for loop iterates through all the body parts of the snake 
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);        //g is the graphics that we are receiving with the parameter
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];      //shifting body parts by 1 that's why we are decrementing
            y[i] = y[i-1];
        }

        switch(direction) {                 //This would allow us to move the snake
            case 'U': 
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;        //increments the bodyparts because the apple was eaten
            applesEaten++;      //counts the apple eaten
            newApple();         //by calling this method we are generating a new apple
        }
    }

    public void checkCollisions() {
        //checks if the head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if the head touches the left border
        if(x[0] < 0) {
            running = false;
        }
        //check if the head touches the right border
        if(x[0] >= SCREEN_WIDTH ) {
            running = false;
        }
        //check if the head touches top border
        if(y[0] < 0) {
            running = false;
        }
        //check if the head touches bottom border
        if(y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }


    public void gameOver(Graphics g){
        //Display GAME OVER sign only
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("GAME Over"))/2, SCREEN_HEIGHT/2); 
        
        //Display score along with the GAME OVER sign
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{   //make sure to import this
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:  //for LEFT key
                    if(direction != 'R') {  //limit the user to a 90 degree turn
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:  //for RIGHT key
                    if(direction != 'L') {  //limit the user to a 90 degree turn
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:  //for UP key
                    if(direction != 'D') {  //limit the user to a 90 degree turn
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:  //for DOWN key
                    if(direction != 'U') {  //limit the user to a 90 degree turn
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
