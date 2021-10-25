import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class Main {
    static final String NAME = "Amaze";
    static final Dimension MIN_FRAME_SIZE = new Dimension(800, 600);

    public static void main(String[] args){

        //Adds panel component to frame
        JFrame frame = new JFrame(NAME);
        GamePanel panel = null;
        try {
            panel = new GamePanel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        frame.add(panel);

        //Sets up frame
        frame.setPreferredSize(MIN_FRAME_SIZE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }
}
