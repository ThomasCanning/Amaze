
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.miginfocom.swing.MigLayout;


public class ControlPanel extends JPanel {

    public MazePanel maze = GamePanel.mazePanel;

    //GUI objects -----
    JButton saveButton;
    JButton resetSolveButton;
    JButton resetButton;
    JButton wallPieceButton;
    JButton startPieceButton;
    JButton endPieceButton;
    JButton clearMazeButton;

    JComboBox algorithmSelection;
    JButton solveButton;
    static JToggleButton toggleOpen;
    JLabel timeDisplay;

    Color silverColor;
    Insets smallInset;
    //-----

    String[] algorithms = {"A* Search", "Depth First Search", "Random Search"};

    static int mazeDimensions = 25;
    static int currentPiece=1;
    static int currentMaze = 1;

    public ControlPanel() throws FileNotFoundException {
        silverColor = new Color(192, 192, 192);
        smallInset = new Insets(0, 0, 0, 0);

        //Sets up panel settings
        this.setBackground(new Color(119, 136, 143));
        this.setBorder(new LineBorder(Color.black, 2, true));
        this.setPreferredSize(new Dimension(220, 100));
        this.setLayout(new MigLayout("fillx", "",
                "[50][50][50][50][50][50][50][50][50][50][50][50][50][50]"));

        //Creates components
        saveButton = new JButton("Save");
        saveButton.setBackground(silverColor);
        clearMazeButton = new JButton("Clear");
        clearMazeButton.setBackground(silverColor);
        resetButton = new JButton("Reset");
        resetButton.setBackground(silverColor);
        resetSolveButton = new JButton("Reset Solve");
        resetSolveButton.setBackground(silverColor);
        wallPieceButton = new JButton("Wall");
        wallPieceButton.setBackground(Color.BLACK);
        startPieceButton = new JButton("Start");
        startPieceButton.setBackground(Color.green);
        endPieceButton = new JButton("End");
        endPieceButton.setBackground(Color.red);
        algorithmSelection = new JComboBox(algorithms);
        solveButton = new JButton("Solve Maze");
        solveButton.setBackground(silverColor);
        toggleOpen = new JToggleButton("Paint closed");
        toggleOpen.setBackground(silverColor);
        timeDisplay = new JLabel("Time");
        timeDisplay.setFont(new Font("Arial", Font.BOLD, 30));

        //Adds components to control panel
        this.add(saveButton, "w 100!, h 40!, cell 0 1, align c, span 2, wrap");
        this.add(clearMazeButton, "w 100!, h 40!, cell 0 2, align c, span 2, wrap");
        this.add(resetButton, "w 100!, h 40!, cell 0 3, align c, span 2, wrap");
        this.add(wallPieceButton, "w 80!, h 40!, cell 0 5, span 2, align c");
        this.add(startPieceButton, "w 80!, h 40!, cell 0 6, span 2, align c");
        this.add(endPieceButton, "w 80!, h 40!, cell 0 7, span 2, align c");
        this.add(algorithmSelection, "w 150!, h 50!, cell 0 9, span 2, align center, wrap");
        this.add(solveButton, "w 120!, h 40!, cell 0 10, span 2, align c");
        this.add(resetSolveButton, "w 120!, h 40!, cell 0 11, span 2, align c");
        this.add(toggleOpen, "w 120!, h 40!, cell 0 12, span 2, align c");
        this.add(timeDisplay, "cell 0 13, span 2, align c");

        clearMazeButton.addActionListener(e -> {
            for(int row = 0; row < MazePanel.mazeMatrix.length;row++){
                for(int col = 0;col<MazePanel.mazeMatrix[row].length;col++){
                    MazePanel.mazeMatrix[row][col]=0;
                }
            }
            maze.repaint();
            System.out.println("clear");
        });

        saveButton.addActionListener(e -> {
            //Save Current Maze:
            try {
                FileWriter writer = new FileWriter("src/main/resources/Maze1.txt");
                for (int y = 0; y < ControlPanel.mazeDimensions; y++) {
                    for (int x = 0; x < ControlPanel.mazeDimensions; x++) {
                        if(MazePanel.mazeMatrix[x][y]<=3) writer.write(String.valueOf(MazePanel.mazeMatrix[x][y]));
                        else writer.write('0');
                    }
                    writer.write("\r\n");
                }
                writer.close();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }


        });

        resetButton.addActionListener(e -> {
            //Resets the maze in the maze panel
            MazeFiles mazeFiles = null;
            try {
                mazeFiles = new MazeFiles(currentMaze);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            MazePanel.mazeMatrix= mazeFiles.mazeMatrix;
            maze.repaint();

        });

        resetSolveButton.addActionListener(e -> {
            for(int row = 0; row < MazePanel.mazeMatrix.length;row++){
                for(int col = 0;col<MazePanel.mazeMatrix[row].length;col++){
                    if(MazePanel.mazeMatrix[row][col]==4||MazePanel.mazeMatrix[row][col]==5) MazePanel.mazeMatrix[row][col]=0;
                }
            }
            maze.repaint();
            System.out.println("reset");
        });

        wallPieceButton.addActionListener(e -> {
            //Set current piece to wall
            currentPiece = 1;
        });
        startPieceButton.addActionListener(e -> {
            //Set current piece to start piece
            currentPiece = 2;
        });
        endPieceButton.addActionListener(e -> {
            currentPiece = 3;
            //Set current piece to end
        });
        //-----

        //Solving controls
        solveButton.addActionListener(e -> {
            Start();
            //AStarAlgorithm solveSmart = new AStarAlgorithm();
            //depthFirstAlgorithm solveDepth = new depthFirstAlgorithm();
            //Code that runs an algorithm that solves maze

            String algorithm = String.valueOf(algorithmSelection.getSelectedItem());
            System.out.println(algorithm);

            switch(algorithm) {
                case "A* Search":
                    new AStarAlgorithm();
                    break;

                case "Depth First Search":
                    new depthFirstAlgorithm(false);
                    break;

                case "Random Search":
                    System.out.println("test");
                    new depthFirstAlgorithm(true);
                    break;

            }



            End();
            timeDisplay.setText(solveTime+" ns");
        });

    }

        //Method and variables for calculating time solving algorithm takes
        long startTime;
        long solveTime;
        void Start() {startTime = System.nanoTime();
        }
        void End() {solveTime = System.nanoTime()-startTime;
        }
}
