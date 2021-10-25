import java.util.*;

/* The Following are 4 classes at present, for search algorithms and corresponding classes for nodes for the search algorithms. When the programme
ran, an algorithm can be selected and ran to compare the 2*/


class AStarAlgorithm {
    int[][] mazeMatrix = MazePanel.mazeMatrix;
    MazePanel mazePanel = GamePanel.mazePanel;
    AStarNode startAStarNode;
    AStarNode endAStarNode;
    public AStarAlgorithm() {
        //Gets x and y positions of start and end nodes in matrix
        for (int y = 0; y < mazeMatrix.length; y++) {
            for (int x = 0; x < mazeMatrix.length; x++) {
                if (mazeMatrix[x][y] == 2) startAStarNode = new AStarNode(x, y);
                if (mazeMatrix[x][y] == 3) endAStarNode = new AStarNode(x, y);
            }
        }

        List<AStarNode> openAStarNodes =new ArrayList<>();
        openAStarNodes.add(startAStarNode);
        List<AStarNode> closedAStarNodes =new ArrayList<>();
        AStarNode currentAStarNode;
        AStarNode previousAStarNode = null;
        while(openAStarNodes.size()>0){
            currentAStarNode=openAStarNodes.get(0);

            //Searches through open points for one with lowest f cost
            for(int i=1;i<openAStarNodes.size();i++){
                //updates current node if better than currently selected node from open nodes
                if((openAStarNodes.get(i).fCost())<currentAStarNode.fCost()||(openAStarNodes.get(i).fCost()==currentAStarNode.fCost()&&openAStarNodes.get(i).hCost<currentAStarNode.hCost)){
                    currentAStarNode=openAStarNodes.get(i);
                }
            }
            openAStarNodes.remove(currentAStarNode);
            closedAStarNodes.add(currentAStarNode);

            //checks if reached end node
            if(currentAStarNode.x==endAStarNode.x&&currentAStarNode.y==endAStarNode.y){
                //What happens when reached end point
                endAStarNode.parent=previousAStarNode;
                List<AStarNode> path = retracePath(startAStarNode, previousAStarNode);
                for (AStarNode aStarNode : path) {
                    MazePanel.mazeMatrix[aStarNode.x][aStarNode.y] = 5;
                }
                mazePanel.repaint();
                return;
            }
            //checks each neighbour of current node that isn't a wall
            for (AStarNode neighbour:getNeighbours(currentAStarNode.x, currentAStarNode.y, closedAStarNodes)){
                //If neighbour already visited
                if(neighbourInList(closedAStarNodes, neighbour)){
                    continue;
                }
                int newCostToNeighbour = currentAStarNode.gCost+getDistance(currentAStarNode, neighbour);
                if(newCostToNeighbour<neighbour.gCost||!openAStarNodes.contains(neighbour)){
                    //updates values of neighbour
                    neighbour.gCost=newCostToNeighbour;
                    neighbour.hCost=getDistance(neighbour, endAStarNode);
                    neighbour.parent=currentAStarNode;
                    //adds neighbour to open set
                    if(!neighbourInList(openAStarNodes, neighbour))openAStarNodes.add(neighbour);
                }
            }
            previousAStarNode = currentAStarNode;
        }

    }

    public List retracePath(AStarNode startAStarNode, AStarNode endAStarNode){
        List<AStarNode> path = new ArrayList<>();
        AStarNode currentAStarNode = endAStarNode;
        while (currentAStarNode!=startAStarNode){
            path.add(currentAStarNode);
            MazePanel.mazeMatrix[currentAStarNode.x][currentAStarNode.y]=3;
            currentAStarNode=currentAStarNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public boolean neighbourInList(List<AStarNode> nodeList, AStarNode neighbour){
        boolean equals = false;
        for (AStarNode aStarNode : nodeList) {
            if (aStarNode.x == neighbour.x && aStarNode.y == neighbour.y) {
                equals = true;
                break;
            }
        }
        return equals;
    }

    //returns distance between 2 nodes
    private int getDistance(AStarNode nodeA, AStarNode nodeB) {
        return (int)(Math.sqrt(Math.pow(nodeA.x-nodeB.x,2)+Math.pow(nodeA.y-nodeB.y,2))*10);
    }

    //Checks neighbours of a given node
    private List<AStarNode> getNeighbours(int x, int y, List<AStarNode> closedAStarNodes){
        List<AStarNode> neighbours = new ArrayList<>();
        boolean atTop = (y<=0);
        boolean atRight = (x>= mazeMatrix.length-1);
        boolean atBottom = (y>= mazeMatrix.length-1);
        boolean atLeft = (x<=0);
        if(!atTop) {
            if (mazeMatrix[x][y - 1] != 1 && !closedAStarNodes.contains(new AStarNode(x, y - 1))) {
                neighbours.add(new AStarNode(x, y - 1));
            }
        }
        if(!atRight) {
            if (mazeMatrix[x + 1][y] != 1 && !closedAStarNodes.contains(new AStarNode(x + 1, y))) {
                neighbours.add(new AStarNode(x + 1, y));
            }
        }
        if(!atBottom) {
            if (mazeMatrix[x][y + 1] != 1 && !closedAStarNodes.contains(new AStarNode(x, y + 1))) {
                neighbours.add(new AStarNode(x, y + 1));
            }
        }
        if(!atLeft) {
            if (mazeMatrix[x - 1][y] != 1 && !closedAStarNodes.contains(new AStarNode(x - 1, y))) {
                neighbours.add(new AStarNode(x - 1, y));
            }
        }
        return neighbours;
    }
}

class AStarNode{
    int x;
    int y;
    int gCost;
    int hCost;
    AStarNode parent;

public AStarNode(int x, int y){
    this.x=x;
    this.y=y;
}
    //Heuristic for A* algorithm - gcost+hcost
    public int fCost(){
        return gCost+hCost;
    }
}

class DepthNode{
    int x;
    int y;
    DepthNode parent;

    public DepthNode(int x, int y){
        this.x=x;
        this.y=y;
    }

}

class depthFirstAlgorithm{
    int[][] mazeMatrix = MazePanel.mazeMatrix; //Matrix of walls, paths, start and end nodes
    MazePanel mazePanel = GamePanel.mazePanel;
    DepthNode startDepthNode;
    DepthNode endDepthNode;
    List<DepthNode> closedDepthNodes;
    Stack<DepthNode> pathStack;

    public depthFirstAlgorithm() {
        //Gets x and y positions of start and end nodes in matrix
        for (int y = 0; y < mazeMatrix.length; y++) {
            for (int x = 0; x < mazeMatrix.length; x++) {
                if (mazeMatrix[x][y] == 2) startDepthNode = new DepthNode(x, y);
                if (mazeMatrix[x][y] == 3) endDepthNode = new DepthNode(x, y);
            }
        }

        closedDepthNodes = new ArrayList<>();
        DepthNode currentDepthNode = startDepthNode;
        pathStack = new Stack();
        pathStack.push(currentDepthNode);

        searchDeep(currentDepthNode, closedDepthNodes, null);
    }

    public void searchDeep(DepthNode currentDepthNode, List<DepthNode> closedDepthNodes, DepthNode previousDepthNode) {
        if (currentDepthNode.x == endDepthNode.x && currentDepthNode.y == endDepthNode.y) {
            //What happens when reached end point
            endDepthNode.parent = previousDepthNode;
            List<DepthNode> path = retracePath(startDepthNode, previousDepthNode);
            for (DepthNode depthNode : path) {
                MazePanel.mazeMatrix[depthNode.x][depthNode.y] = 5;
            }
            mazeMatrix[endDepthNode.x][endDepthNode.y]=3;
            mazeMatrix[startDepthNode.x][startDepthNode.y]=2;
            mazePanel.repaint();

        } else {
            previousDepthNode = currentDepthNode;
            currentDepthNode = getNeighbour(currentDepthNode, closedDepthNodes);
            //MazePanel.mazeMatrix[currentDepthNode.x][currentDepthNode.y]=4;
            mazePanel.repaint();
            pathStack.push(previousDepthNode);
            pathStack.push(currentDepthNode);
            if(neighbourInList(closedDepthNodes, currentDepthNode.x, currentDepthNode.y)) closedDepthNodes.add(currentDepthNode);
            currentDepthNode.parent=previousDepthNode;

            searchDeep(currentDepthNode, closedDepthNodes, currentDepthNode.parent); //Recursively call search algorithm
        }
    }

    public List retracePath(DepthNode startDepthNode, DepthNode endDepthNode){
        List<DepthNode> path = new ArrayList<>();
        DepthNode currentDepthNode = endDepthNode;
        while (currentDepthNode!=startDepthNode){
            path.add(currentDepthNode);
            MazePanel.mazeMatrix[currentDepthNode.x][currentDepthNode.y]=3;
            currentDepthNode=currentDepthNode.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public boolean neighbourInList(List<DepthNode> nodeList, int x, int y){
        boolean equals = false;
            for (DepthNode depthNode : nodeList) {
                if (depthNode.x == x && depthNode.y == y) {
                    equals = true;
                    break;
                }
            }
        return !equals;
    }

    private DepthNode getNeighbour(DepthNode currentNode, List<DepthNode> closedDepthNodes){
        int x = currentNode.x;
        int y = currentNode.y;
        boolean atTop = (y<=0);
        boolean atRight = (x>= mazeMatrix.length-1);
        boolean atBottom = (y>= mazeMatrix.length-1);
        boolean atLeft = (x<=0);
        if(!atTop) {
            if (mazeMatrix[x][y - 1] != 1 && neighbourInList(closedDepthNodes, x, y - 1)) {
                return (new DepthNode(x, y - 1));
            }
        }
        if(!atRight) {
            if (mazeMatrix[x + 1][y] != 1 && neighbourInList(closedDepthNodes, x + 1, y)) {
                return (new DepthNode(x + 1, y));
            }
        }
        if(!atBottom) {
            if (mazeMatrix[x][y + 1] != 1 && neighbourInList(closedDepthNodes, x, y + 1)) {
                return (new DepthNode(x, y + 1));
            }
        }
        if(!atLeft) {
            if (mazeMatrix[x - 1][y] != 1 && neighbourInList(closedDepthNodes, x - 1, y)) {
                return (new DepthNode(x - 1, y));
            }
        }

        return getNeighbour(pathStack.pop(), closedDepthNodes);
    }

}
