import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class GamePanel extends JPanel {

    public static MazePanel mazePanel;
    static {
        try {
            mazePanel = new MazePanel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    ControlPanel controlPanel;

    public GamePanel() throws FileNotFoundException {
        this.setBackground(Color.red);
        this.setLayout(new BorderLayout());

        this.add(mazePanel, BorderLayout.CENTER);

        controlPanel = new ControlPanel();
        this.add(controlPanel, BorderLayout.WEST);

        this.setVisible(true);
    }

}


