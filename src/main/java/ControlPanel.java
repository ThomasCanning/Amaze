
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import net.miginfocom.swing.MigLayout;


public class ControlPanel extends JPanel {

    MazePanel maze = GamePanel.mazePanel;

    //GUI objects -----
    JComboBox mazeList;
    JButton newMazeButton;
    JLabel mazeSizeDisplay;
    JButton mazeSizeUp;
    JButton mazeSizeBigUp;
    JButton mazeSizeDown;
    JButton mazeSizeBigDown;
    JButton saveButton;
    JButton resetButton;
    JButton wallPieceButton;
    JButton startPieceButton;
    JButton endPieceButton;

    JComboBox algorithmSelection;
    JButton solveButton;
    JLabel timeDisplay;

    Color silverColor;
    Insets smallInset;
    //-----

    String[] mazes = {"Maze 1"}; //temporary
    String[] algorithms = {"A* Algorithm"};

    final int MAX_MAZE_DIMENSION = 25;
    static int mazeDimensions = 25;
    static int currentPiece=1;

    public ControlPanel() throws FileNotFoundException {

        silverColor = new Color(192, 192, 192);
        smallInset = new Insets(0, 0, 0, 0);

        //Sets up panel settings
        this.setBackground(new Color(119, 136, 143));
        this.setBorder(new LineBorder(Color.black, 2, true));
        this.setPreferredSize(new Dimension(220, 100));
        this.setLayout(new MigLayout("fillx",
                "[80][80]",
                "[200][50][60][50][60][70][70][70][100][70][80]"));

        //Creates components
        mazeList = new JComboBox(mazes);
        mazeList.setBackground(silverColor);
        newMazeButton = new JButton("New");
        newMazeButton.setBackground((silverColor));
        mazeSizeDisplay = new JLabel(mazeDimensions + "*" + mazeDimensions);
        mazeSizeDisplay.setFont(new Font("Arial", Font.BOLD, 30));
        mazeSizeDisplay.setForeground(Color.black);
        mazeSizeDisplay.setBackground(silverColor);
        mazeSizeUp = new JButton("↑");
        mazeSizeUp.setBackground(silverColor);
        mazeSizeUp.setMargin(smallInset);
        mazeSizeBigUp = new JButton("10↑");
        mazeSizeBigUp.setMargin(smallInset);
        mazeSizeBigUp.setBackground(silverColor);
        mazeSizeDown = new JButton("↓");
        mazeSizeDown.setBackground(silverColor);
        mazeSizeDown.setMargin(smallInset);
        mazeSizeBigDown = new JButton("10↓");
        mazeSizeBigDown.setMargin(smallInset);
        mazeSizeBigDown.setBackground(silverColor);
        saveButton = new JButton("Save");
        saveButton.setBackground(silverColor);
        resetButton = new JButton("Reset");
        resetButton.setBackground(silverColor);
        wallPieceButton = new JButton("Wall");
        wallPieceButton.setBackground(Color.BLACK);
        startPieceButton = new JButton("Start");
        startPieceButton.setBackground(Color.green);
        endPieceButton = new JButton("End");
        endPieceButton.setBackground(Color.red);
        algorithmSelection = new JComboBox(algorithms);
        solveButton = new JButton("Solve Maze");
        solveButton.setBackground(silverColor);
        timeDisplay = new JLabel("Time");
        timeDisplay.setFont(new Font("Arial", Font.BOLD, 30));

        //Adds components to control panel
        this.add(mazeList, "w 150!, h 50!, cell 0 0, span 2, align center, wrap");
        this.add(mazeSizeUp, "w 40!, h 30!, cell 0 1");
        this.add(mazeSizeBigUp, "w 40!, h 30!, cell 0 1, wrap");
        this.add(mazeSizeDisplay, "cell 0 2, align c");
        this.add(newMazeButton, "w 80!, h 40!, cell 1 2, wrap");
        this.add(mazeSizeDown, "w 40!, h 30!, cell 0 3");
        this.add(mazeSizeBigDown, "w 40!, h 30!, cell 0 3, wrap");
        this.add(saveButton, "w 80!, h 40!, cell 0 4");
        this.add(resetButton, "w 80!, h 40!, cell 1 4");
        this.add(wallPieceButton, "w 80!, h 40!, cell 0 5, span 2, align c");
        this.add(startPieceButton, "w 80!, h 40!, cell 0 6, span 2, align c");
        this.add(endPieceButton, "w 80!, h 40!, cell 0 7, span 2, align c");
        this.add(algorithmSelection, "w 150!, h 50!, cell 0 8, span 2, align center, wrap");
        this.add(solveButton, "w 120!, h 40!, cell 0 9, span 2, align c");
        this.add(timeDisplay, "cell 0 10, span 2, align c");

        //What happens when the selected maze is changed
        mazeList.addItemListener(e -> {

        });

        //Setting dimensions of new Maze: -----
        //Maze size up
        mazeSizeUp.addActionListener(e -> {
            if (mazeDimensions < MAX_MAZE_DIMENSION) mazeDimensions++;
            mazeSizeDisplay.setText(mazeDimensions + "*" + mazeDimensions);
        });
        //Big up
        mazeSizeBigUp.addActionListener(e -> {
            mazeDimensions += 10;
            if (mazeDimensions > MAX_MAZE_DIMENSION) mazeDimensions = MAX_MAZE_DIMENSION;
            mazeSizeDisplay.setText(mazeDimensions + "*" + mazeDimensions);
        });
        //Maze size down
        mazeSizeDown.addActionListener(e -> {
            if (mazeDimensions > 5) mazeDimensions--;
            mazeSizeDisplay.setText(mazeDimensions + "*" + mazeDimensions);
        });
        //Big down
        mazeSizeBigDown.addActionListener(e -> {
            mazeDimensions -= 10;
            if (mazeDimensions < 5) mazeDimensions = 5;
            mazeSizeDisplay.setText(mazeDimensions + "*" + mazeDimensions);
        });
        //New maze button pressed
        newMazeButton.addActionListener(e -> {
            //Save Current Maze
            //Create new maze and set to new maze
            MazePanel.newMaze(Integer.parseInt(mazeSizeDisplay.getText().substring(0,mazeSizeDisplay.getText().indexOf('*'))));
        });
        //-----

        saveButton.addActionListener(e -> {
            //Save Current Maze:
            try {
                FileWriter writer = new FileWriter("res/Maze1.txt");
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
                mazeFiles = new MazeFiles();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            MazePanel.mazeMatrix= mazeFiles.mazeMatrix;
            maze.repaint();

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
            AStarAlgorithm solveSmart = new AStarAlgorithm();
            //depthFirstAlgorithm solveDepth = new depthFirstAlgorithm();
            //Code that runs an algorithm that solves maze

            End();
            timeDisplay.setText(solveTime+" ns");
        });

        //----------
    }

        //Method and variables for calculating time solving algorithm takes
        long startTime;
        long solveTime;
        void Start() {startTime = System.nanoTime();
        }
        void End() {solveTime = System.nanoTime()-startTime;
        }


}
