import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(50, 50, this);
    List<Missile> missiles = new ArrayList<Missile>();

    //这是一张虚拟图片
    Image offScreenImage = null;

    //The paint method does not need to be called and will be automatically called once it is to be redrawn
    public void paint(Graphics g) {
//看出容器中装了多少炮弹
        g.drawString("missiles count: " + missiles.size(), 10, 50);

//将容器中的炮弹逐个画出来
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.draw(g);

//if(!m.isLive()) missiles.remove(m);
//else m.draw(g);

        }
        myTank.draw(g);
    }

    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH,
                    GAME_HEIGHT);
        }
//拿到这个图片上的画笔

        Graphics gOffScreen = offScreenImage.getGraphics();

        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.ORANGE);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        print(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void launchFrame() {
        this.setLocation(300, 50);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);

        this.addKeyListener(new KeyMonitor());
        setVisible(true);
        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }

    private class PaintThread implements Runnable {

        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private class KeyMonitor extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            myTank.KyePressed(e);
        }

        public void keyReleased(KeyEvent e) {
            myTank.kyeReleased(e);
        }

    }

}