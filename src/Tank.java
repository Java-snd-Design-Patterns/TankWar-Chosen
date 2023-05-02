import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


public class Tank {
    private int x, y;
    int oldX, oldY;

    int step;
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    TankClient tc = null;

    private static Random r = new Random();
    Direction[] dirs = Direction.values();

    int rn = r.nextInt(dirs.length);

    private boolean bL = false, bU = false, bR = false, bD = false;

    public void setLive(boolean b) {
        this.live = b;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isGood() {
        return good;
    }

    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}
    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private int life = 100;
    private Direction dir = Direction.D;

    private Direction ptDir = Direction.D;

    private boolean good;

    private boolean live = true;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, TankClient tc) {
        this(x, y);
        this.tc = tc;
    }

    public Tank(int x, int y, boolean good, TankClient tc) {
        this(x, y);
        this.tc = tc;
        this.good = good;
    }

    public boolean isLive() {
        return live;
    }


    public void draw(Graphics g) {
        if (!live) return;

        Color c = g.getColor();
        if (this.good) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.RED);
        }

        g.fillOval(x, y, 30, 30);
        g.setColor(c);
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
                g.drawLine(x + Tank.WIDTH / 2, y + Tank.HEIGHT / 2, x + Tank.WIDTH, y + Tank.HEIGHT);
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

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    void move() {
        if (!good) {
            this.oldX = x;
            this.oldY = y;
//Direction.values()将这个枚举类型转为数组Direction[] dirs = Direction.values(); if(step == 0) {
            step = r.nextInt(12) + 3;
            int rn = r.nextInt(dirs.length);
            dir = dirs[rn];
        }
        step--;

        if (r.nextInt(40) > 38) this.fire();

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
        if (this.dir != Direction.STOP) {
            this.ptDir = this.dir;
            if (x < 0) x = 0;
            if (y < 25) y = 25;
            if (x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
            if (y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
        }
    }

    public boolean collidesWithWall(Wall w) {
        if (this.getRect().intersects(w.getRect()) &&
                this.live) {
            this.stay();
            return true;
        }
        return false;
    }

    private void stay() {
        x = oldX;
        y = oldY;
    }

    public boolean collidesWithTanks(java.util.List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if (this != t) {
                if (this.live && t.isLive() &&
                        this.getRect().intersects(t.getRect())) {
                    t.stay();
                    this.stay();
                    return true;
                }
            }
        }
        return false;
    }

    public void KeyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL:
                fire();
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

    public void keyReleased(KeyEvent e) {
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
            case KeyEvent.VK_A:
                superFire();
                break;
        }
        locateDirection();
    }

    public Missile fire() {
        if(!live) return null;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile m = new Missile(x, y, ptDir, tc, this.good);
        tc.missiles.add(m);
        return m;//
    }

    public Missile fire(Direction dir) {
        if (!live) return null;
//保证子弹从Tank的中间出现
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.WIDTH / 2;
//将Tank现在的位置和方向传递给子弹
//并且现在子弹的初始化不再是由坦克决定，而是由炮筒决定
        Missile m = new Missile(x, y, dir, this.tc, good);
        tc.missiles.add(m);
        return m;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        for(int i = 0; i < 8; i++) { fire(dirs[i]);
        }
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
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }
}