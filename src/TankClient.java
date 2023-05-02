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

    Tank myTank = new Tank(50, 50, true,this);

    List<Tank> tanks = new ArrayList<Tank>();
    List<Missile> missiles = new ArrayList<Missile>();
    List<Explode> explodes = new ArrayList<Explode>();

    Wall w1 = new Wall(250,250,250,10,this);
    Missile m = null;
    Image offScreenImage = null;

    public void paint(Graphics g) {
        g.drawString("missiles count: " + missiles.size(), 10, 50);
        g.drawString("explodes count: " + explodes.size(), 10,70);
        g.drawString("tanks count: " + tanks.size(), 10, 90);
        g.drawString("blood: " + myTank.getLife(), 10, 110);

        for(int i = 0; i < explodes.size(); i++) {
            Explode e = explodes.get(i);
            e.draw(g);
        }
        w1.draw(g);
        myTank.draw(g);
        myTank.collidesWithWall(w1);
        myTank.collidesWithTanks(tanks);

        for(int i = 0;i<tanks.size();i++){
            tanks.get(i).draw(g);
            tanks.get(i).collidesWithWall(w1);
            tanks.get(i).collidesWithTanks(tanks);
        }

        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.draw(g);

            if (m != null)
                m.draw(g);
        }
    }

    public void update (Graphics g){

        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.ORANGE);

        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        print(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void launchFrame () {

        for(int i = 0; i < 10; i++) {
            tanks.add(new Tank(50 + 40 * (i + 1), 50, false, this));
            tanks.get(i).collidesWithWall(w1);
        }

        this.setLocation(300, 50);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);
        this.setTitle("TankWar");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.ORANGE);

        this.addKeyListener(new KeyMonitor());
        setVisible(true);
        new Thread(new PaintThread()).start();
    }
    public static void main (String[]args){
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
            myTank.KeyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }

    }
}