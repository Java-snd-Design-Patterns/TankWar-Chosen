import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame {
    //The paint method does not need to be called
    // and will be automatically called once it is to be redrawn
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(50, 50, 30, 30);
        g.setColor(c);
    }

    public void launchFrame() {

        this.setLocation(300, 50);
        this.setSize(800, 600);
        this.setTitle("TankWar");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GREEN);
        setVisible(true);
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.launchFrame();
    }
}