import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class MazeFiles {

    int[][] mazeMatrix = new int[ControlPanel.mazeDimensions][ControlPanel.mazeDimensions];

    MazeFiles(int mazeNumber) throws FileNotFoundException {

            InputStream mazeFile = getClass().getResourceAsStream("Maze" + mazeNumber +  ".txt");
            Scanner reader = new Scanner(mazeFile);
            int y = 0;
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                for (int x = 0; x < data.length(); x++) {
                    mazeMatrix[x][y] = Integer.parseInt(String.valueOf(data.charAt(x)));
                }
                y++;
            }
            reader.close();
    }
    }

