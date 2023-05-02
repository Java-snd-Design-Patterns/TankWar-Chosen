import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Missile {
    public static final int XSPEED = 10;


    public static final int YSPEED = 10;

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    private boolean good;




    private int x, y;
    Tank.Direction dir;

    //判断子弹是否活着
    private boolean live = true;

    TankClient tc;

    public Missile(int x, int y, Tank.Direction ptDir, TankClient tkw, boolean good) {
        this.x = x;
        this.y = y;
        this.dir = ptDir;
        this.tc = tkw;
        this.good = good;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }


    public Missile(int x, int y, Tank.Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, Tank.Direction dir, TankClient tc) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tc = tc;
    }

    public void draw(Graphics g) {

        if (!live) {
            tc.missiles.remove(this);
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 10, 10);
        g.setColor(c);

        move();
    }

    //getRect()可以正好拿到包在坦克外面的方块
    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitTanks(List<Tank> tanks) {

        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) {
                return true;
            }
        }
        return false;
    }



    public boolean hitTank(Tank t) {
        if(this.live &&  this.getRect().intersects(t.getRect()) && t.isLive()
                && this.good != t.isGood()) {
            t.setLive(false);
            this.live = false;
            Explode e = new Explode(t.getX(), t.getY(), tc);
            tc.explodes.add(e);
            return true;
        }
        return false;
    }



    private void move() {
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
        }
        if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
            live = false;
            tc.missiles.remove(this);
        }//

    }
}