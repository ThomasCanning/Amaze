import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import static java.lang.Integer.min;

public class MazePanel extends JPanel {

    Dimension mazePanelDimensions;
    static int mazeNumberOfPieces = ControlPanel.mazeDimensions;
    int mazePieceSize;
    int mazeSideLength;
    static int[][] mazeMatrix = new int[mazeNumberOfPieces][mazeNumberOfPieces];
    Point clickedPoint;

    public MazePanel() throws FileNotFoundException {

        this.setBackground(Color.white);
        repaint();

        //Updates screen size variable when frame is resized
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                mazePanelDimensions = getSize();
                repaint();
            }
        });

        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);

        //Creates a Maze array from a text file
        MazeFiles mazeFiles = new MazeFiles(1);
        mazeMatrix = mazeFiles.mazeMatrix;

    }

    public static void loadMaze(int mazeLoad) throws FileNotFoundException {
        MazeFiles mazeFiles = new MazeFiles(mazeLoad);
        mazeMatrix = mazeFiles.mazeMatrix;
    }

    public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.BLACK);

            mazeSideLength=min(mazePanelDimensions.height, mazePanelDimensions.width)-20;
            mazePieceSize = ((mazeSideLength)/ mazeNumberOfPieces);

            //Draws grid for maze
        int rowsDrawn=0;
            for(int x=((mazePanelDimensions.width/2)-(mazeSideLength/2));x<mazeSideLength-10+((mazePanelDimensions.width/2)-(mazeSideLength/2));x+=mazePieceSize){
                for(int y=10;y<mazeSideLength-mazePieceSize;y+=mazePieceSize){
                    if(rowsDrawn<mazeNumberOfPieces) g2D.drawRect(x,y,mazePieceSize,mazePieceSize);
                }
                rowsDrawn++;
            }

            //Colors in maze pieces
            for (int y = 0; y < mazeNumberOfPieces; y++) {
                for (int x= 0; x < mazeNumberOfPieces; x++) {
                    switch(mazeMatrix[x][y]){
                        case 1:
                            g2D.setColor(Color.BLACK);
                            g2D.fillRect((((mazePanelDimensions.width/2)-(mazeSideLength/2))+x*mazePieceSize)+1, mazePieceSize*y+11, mazePieceSize-2,mazePieceSize-2);
                            break;
                        case 2:
                            g2D.setColor(Color.green);
                            g2D.fillRect((((mazePanelDimensions.width/2)-(mazeSideLength/2))+x*mazePieceSize)+1, mazePieceSize*y+11, mazePieceSize-2,mazePieceSize-2);
                            break;
                        case 3:
                            g2D.setColor(Color.red);
                            g2D.fillRect((((mazePanelDimensions.width/2)-(mazeSideLength/2))+x*mazePieceSize)+1, mazePieceSize*y+11, mazePieceSize-2,mazePieceSize-2);
                            break;
                        case 4:
                            g2D.setColor(Color.yellow);
                            g2D.fillRect((((mazePanelDimensions.width/2)-(mazeSideLength/2))+x*mazePieceSize)+1, mazePieceSize*y+11, mazePieceSize-2,mazePieceSize-2);
                            break;
                        case 5:
                            g2D.setColor(Color.orange);
                            g2D.fillRect((((mazePanelDimensions.width/2)-(mazeSideLength/2))+x*mazePieceSize)+1, mazePieceSize*y+11, mazePieceSize-2,mazePieceSize-2);
                            break;
                    }

                }
            }
        }
        private class ClickListener extends MouseAdapter {
          public void mousePressed(MouseEvent e) {
                int xIndex = 0;
                int yIndex =0;
                 int index = 0;
                int inGrid = 0;
                clickedPoint = e.getPoint(); //updates point to click position
                for (int y = 10; y < mazeSideLength; y+=mazePieceSize) {
                    if(y<=clickedPoint.y&&clickedPoint.y<=y+(mazePieceSize)) {
                       yIndex=index;
                       inGrid++;
                       break;
                    }
                    index++;
                }
                index=0;
                for (int x = (mazePanelDimensions.width/2)-(mazeSideLength/2); x < mazeSideLength+(mazePanelDimensions.width/2)-(mazeSideLength/2); x+=mazePieceSize) {
                    if(x<=clickedPoint.x&&clickedPoint.x<=x+(mazePieceSize)) {
                        xIndex=index;
                        inGrid++;
                        break;
                    }
                    index++;
                }
                if(xIndex>mazeNumberOfPieces-1)xIndex=mazeNumberOfPieces-1;
                if(yIndex>mazeNumberOfPieces-1)yIndex=mazeNumberOfPieces-1;
                if(inGrid==2) repaint();
                switch(ControlPanel.currentPiece){
                    case 1:
                        if(mazeMatrix[xIndex][yIndex]==0){
                            mazeMatrix[xIndex][yIndex]=1;
                        }
                        else if(mazeMatrix[xIndex][yIndex]==1){
                            mazeMatrix[xIndex][yIndex]=0;
                        }
                        break;
                    case 2:
                        if(mazeMatrix[xIndex][yIndex]==0){
                            mazeMatrix[xIndex][yIndex]=2;
                        }
                        else if(mazeMatrix[xIndex][yIndex]==2){
                            mazeMatrix[xIndex][yIndex]=0;
                        }
                        break;
                    case 3:
                        if(mazeMatrix[xIndex][yIndex]==0){
                            mazeMatrix[xIndex][yIndex]=3;
                        }
                        else if(mazeMatrix[xIndex][yIndex]==3){
                            mazeMatrix[xIndex][yIndex]=0;
                        }
                        break;
                }
            }
        }

    public static void newMaze(int newMazeSize){
        //Uses maze dimensions and maze size to repaint the maze grid so a maze can be drawn
        mazeNumberOfPieces =newMazeSize;
    }
}
