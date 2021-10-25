import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeSolveAlgorithm {
    int[][] mazeMatrix = MazePanel.mazeMatrix;
    MazePanel mazePanel = GamePanel.mazePanel;
    Node startNode;
    Node endNode;
    public MazeSolveAlgorithm(){
        //Gets x and y positions of start and end nodes in matrix
        for (int y = 0; y < mazeMatrix.length; y++) {
            for (int x = 0; x < mazeMatrix.length; x++) {
                if (mazeMatrix[x][y] == 2) startNode = new Node(x, y);
                if (mazeMatrix[x][y] == 3) endNode = new Node(x, y);
            }
        }
    }
}

class AStarAlgorithm extends MazeSolveAlgorithm{
    public AStarAlgorithm() {
        MazeSolveAlgorithm maze = new MazeSolveAlgorithm();
        List<Node> openNodes =new ArrayList<>();
        openNodes.add(startNode);
        List<Node> closedNodes =new ArrayList<>();
        Node currentNode;
        Node previousNode = null;
        while(openNodes.size()>0){
            currentNode=openNodes.get(0);

            //Searches through open points for one with lowest f cost
            for(int i=1;i<openNodes.size();i++){
                //updates current node if better than currently selected node from open nodes
                if((openNodes.get(i).fCost())<currentNode.fCost()||(openNodes.get(i).fCost()==currentNode.fCost()&&openNodes.get(i).hCost<currentNode.hCost)){
                    currentNode=openNodes.get(i);
                }
            }
            openNodes.remove(currentNode);
            closedNodes.add(currentNode);

            //checks if reached end node
            if(currentNode.x==endNode.x&&currentNode.y==endNode.y){
                //What happens when reached end point
                endNode.parent=previousNode;
                List<Node> path = retracePath(startNode, previousNode);
                for (int i=0;i<path.size();i++) {
                    MazePanel.mazeMatrix[path.get(i).x][path.get(i).y]=5;
                }
                maze.mazePanel.repaint();
                return;
            }
            //checks each neighbour of current node that isn't a wall
            for (Node neighbour:getNeighbours(currentNode.x, currentNode.y, closedNodes)){
                //If neighbour already visited
                if(neighbourInList(closedNodes, neighbour)){
                    continue;
                }
                int newCostToNeighbour = currentNode.gCost+getDistance(currentNode, neighbour);
                if(newCostToNeighbour<neighbour.gCost||!openNodes.contains(neighbour)){
                    //updates values of neighbour
                    neighbour.gCost=newCostToNeighbour;
                    neighbour.hCost=getDistance(neighbour, endNode);
                    neighbour.parent=currentNode;
                    /*if(MazePanel.mazeMatrix[neighbour.x][neighbour.y]!=3) MazePanel.mazeMatrix[neighbour.x][neighbour.y]=4;
                    maze.mazePanel.repaint();*/
                    //adds neighbour to open set
                    if(!neighbourInList(openNodes, neighbour))openNodes.add(neighbour);
                }
            }
            previousNode = currentNode;
        }

    }

    public List retracePath(Node startNode, Node endNode){
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;
        while (currentNode!=startNode){
            path.add(currentNode);
            MazePanel.mazeMatrix[currentNode.x][currentNode.y]=3;
            currentNode=currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public boolean neighbourInList(List<Node> nodeList, Node neighbour){
        boolean equals = false;
        if(nodeList.size()==0)equals = false;
        else{
            for(int i=0;i<nodeList.size();i++){
                if(nodeList.get(i).x==neighbour.x&&nodeList.get(i).y==neighbour.y){
                    equals = true;
                }
            }
        }
        return equals;
    }

    //returns distance between 2 nodes
    private int getDistance(Node nodeA, Node nodeB) {
        return (int)(Math.sqrt(Math.pow(nodeA.x-nodeB.x,2)+Math.pow(nodeA.y-nodeB.y,2))*10);
    }

    //Checks neighbours of a given node
    private List<Node> getNeighbours(int x, int y, List<Node> closedNodes){
        List<Node> neighbours = new ArrayList<>();
        boolean atTop = (y<=0);
        boolean atRight = (x>= mazeMatrix.length-1);
        boolean atBottom = (y>= mazeMatrix.length-1);
        boolean atLeft = (x<=0);
        if(!atTop) {
            if (mazeMatrix[x][y - 1] != 1 && !closedNodes.contains(new Node(x, y - 1))) {
                neighbours.add(new Node(x, y - 1));
            }
        }
        if(!atRight) {
            if (mazeMatrix[x + 1][y] != 1 && !closedNodes.contains(new Node(x + 1, y))) {
                neighbours.add(new Node(x + 1, y));
            }
        }
        if(!atBottom) {
            if (mazeMatrix[x][y + 1] != 1 && !closedNodes.contains(new Node(x, y + 1))) {
                neighbours.add(new Node(x, y + 1));
            }
        }
        if(!atLeft) {
            if (mazeMatrix[x - 1][y] != 1 && !closedNodes.contains(new Node(x - 1, y))) {
                neighbours.add(new Node(x - 1, y));
            }
        }
        return neighbours;
    }
}

class Node{
    int x;
    int y;
    int gCost;
    int hCost;
    Node parent;

public Node(int x, int y){
    this.x=x;
    this.y=y;
}
    //Heuristic for A* algorithm - gcost+hcost
    public int fCost(){
        return gCost+hCost;
    }
}

    class depthFirstAlgorithm{

    }

