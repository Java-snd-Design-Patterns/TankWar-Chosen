import java.awt.*;

public class Explode {
    int x, y;
    private boolean live = true;

    private TankClient tc;
    //这个爆炸的半径
    int[] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};
    //现在画到了第几步
    int step = 0;

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(Graphics g) {
        if (!live) {
            tc.missiles.remove(this);
            return;
        }

        if (step == diameter.length) {
            live = false;
            step = 0;
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.DARK_GRAY);
        g.fillOval(x, y, diameter[step], diameter[step]);
        g.setColor(c);
        step++;
    }

}
