import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeFiles {

    int[][] mazeMatrix = new int[ControlPanel.mazeDimensions][ControlPanel.mazeDimensions];
    static int numOfMazes=0;

    MazeFiles(int mazeNumber) throws FileNotFoundException {

        //for(int i=1;fileExists;i++){
            File checkFile = new File("src/main/resources/Maze" + mazeNumber +  ".txt");
            if (checkFile.isFile()) {
                //File exists:
                MazeFiles.numOfMazes++;
                Scanner reader = new Scanner(checkFile);
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
        //}
    }
    }

