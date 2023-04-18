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
    private boolean bL = false, bU = false, bR = false, bD = false;

    //成员变量：方向
    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    private Direction dir = Direction.STOP;


    //PtDir represents the direction of the barrel, with the default direction pointing downwards
    private Direction ptDir = Direction.D;

    private boolean good;

    public Tank(int x, int y, boolean good, TankClient tc) {
        this.x = x;
        this.y = y;
        this.good = good;
        this.tc = tc;
    }


    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
//以不同的颜色来显示不同的坦克
        if (good) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);
        move();
//判断出炮筒的方向，并模拟方向来画出炮筒
        switch (ptDir) {
            case L:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT / 2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y);
                break;
            case RU:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT / 2);
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT

                        / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH / 2, y + Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x, y + Tank.HEIGHT);
                break;
            case STOP:
                break;
        }

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

//将坦克的方向传给炮筒，使炮筒与坦克方向一致
        if (this.dir != Direction.STOP) {
            this.ptDir = this.dir;
        }

        if (x < 0) x = 0;
        if (y < 25) y = 25;
        if (x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

    }

    public void KyePressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
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
//按下Ctrl时作出动作

//只有当键被抬起来时，这枚炮弹才被发出去
//避免了一直按下Ctrl而使得子弹过多的问题
            case KeyEvent.VK_CONTROL:
                fire();
                break;
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


    //由按键来定位方向
    void locateDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.RU;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (bL && !bU && !bR && bD) dir = Direction.LD;
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    public Missile fire() {

//保证子弹从Tank的中间出现
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.WIDTH / 2;
//将Tank现在的位置和方向传递给子弹
//And now the initialization of bullets is no longer determined by the tank, but by the barrel.
        Missile m = new Missile(x, y, ptDir, tc);
        tc.missiles.add(m);
        return m;
    }

}