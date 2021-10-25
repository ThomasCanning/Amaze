import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeFiles {

    int[][] mazeMatrix = new int[ControlPanel.mazeDimensions][ControlPanel.mazeDimensions];

    MazeFiles() throws FileNotFoundException {


        boolean fileExists=true;
        int i=1;
        //for(int i=1;fileExists;i++){
            File checkFile = new File("src/main/resources/Maze" + i +  ".txt");
            if (checkFile.isFile()) {
                //File exists:
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

            } else {
                fileExists=false;
            }
        //}
    }

}
