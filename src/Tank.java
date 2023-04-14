import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

//保留TankClient的引用，更方便地使用其中的成员变量

    TankClient tc = null;

    private int x, y;

//是否按下了4个方向键

    private boolean bL = false,
            bU = false, bR = false, bD = false;

    //成员变量：方向
    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    ;

    private Direction dir = Direction.STOP;


    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, TankClient tc) {
//调用其它的构造方法this(x, y);
        this.tc = tc;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        move();
    }

    void move() {
        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;

                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }
    }

    public void KyePressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
//按下Ctrl时作出的动作
            case KeyEvent.VK_CONTROL:
                tc.m = fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }

    public void kyeReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        locateDirection();
    }


    void locateDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.RU;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (bL && !bU && !bR && bD) dir = Direction.LD;
        else if (!bL && !bU && !bR && !bD) dir =
                Direction.STOP;
    }

    public Missile fire() {
//保证子弹从Tank的中间出现
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.WIDTH / 2;
//将Tank现在的位置和方向传递给子弹
        Missile m = new Missile(x, y, dir);
        return m;
    }
}