import java.util.*;

/* The Following are 4 classes at present, for search algorithms and corresponding classes for nodes for the search algorithms. When the programme
ran, an algorithm can be selected and ran to compare the 2*/

class SearchClass{ //Parent class of search algorithms with methods for retracing and checking if neighbour is already visited, and a node class

    int[][] mazeMatrix = MazePanel.mazeMatrix;MazePanel mazePanel = GamePanel.mazePanel;
    Node startNode;
    Node endNode;

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
    public boolean neighbourInList(List<Node> nodeList, Node neighbour){
        int x = neighbour.x;
        int y = neighbour.y;
        boolean equals = false;
        for (Node node : nodeList) {
            if (node.x == x && node.y == y) {
                equals = true;
                break;
            }
        }
        return equals;
    }
    //Checks neighbours of a given node
    public List<Node> getNeighbours(int x, int y, List<Node> closedNodes){
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

class AStarAlgorithm extends SearchClass{
    public AStarAlgorithm() {
        //Gets x and y positions of start and end nodes in matrix
        for (int y = 0; y < mazeMatrix.length; y++) {
            for (int x = 0; x < mazeMatrix.length; x++) {
                if (mazeMatrix[x][y] == 2) startNode = new Node(x, y);
                if (mazeMatrix[x][y] == 3) endNode = new Node(x, y);
            }
        }

        List<Node> openNodes =new ArrayList<>();
        openNodes.add(startNode);
        List<Node> closedNodes =new ArrayList<>();
        Node currentNode;
        Node previousNode = null;
        while(openNodes.size()>0){ //Iteratively uses A* algorithm to find shortest path
            currentNode=openNodes.get(0);

            //Searches through open points for one with lowest f cost
            for(int i=1;i<openNodes.size();i++){
                //updates current node if better than currently selected node from open nodes
                if((openNodes.get(i).fCost())<currentNode.fCost()||(openNodes.get(i).fCost()==currentNode.fCost()&&openNodes.get(i).hCost<currentNode.hCost)){
                    currentNode=openNodes.get(i);
                }
            }
            openNodes.remove(currentNode);//Switches current node to closed list
            closedNodes.add(currentNode);

            //checks if reached end node
            if(currentNode.x==endNode.x&&currentNode.y==endNode.y){
                //What happens when reached end point
                endNode.parent=previousNode;
                List<Node> path = retracePath(startNode, previousNode);
                for (Node aStarNode : path) {
                    MazePanel.mazeMatrix[aStarNode.x][aStarNode.y] = 5;
                }
                mazePanel.repaint();
                return;
            }
            //checks each neighbour of current node that isn't a wall
            for (Node neighbour:getNeighbours(currentNode.x, currentNode.y, closedNodes)){
                //If neighbour already visited ignore
                if(neighbourInList(closedNodes, neighbour)){
                    continue;
                }
                //Finds old route to neighbour
                int oldCostToNeighbour = currentNode.gCost+getDistance(currentNode, neighbour);
                //If not on open list, add it to open list and set g and h costs and assign parent
                if(!neighbourInList(openNodes, neighbour)){
                    openNodes.add(neighbour);
                    neighbour.gCost=oldCostToNeighbour;
                    neighbour.hCost=getDistance(neighbour, endNode);
                    neighbour.parent=currentNode;
                    //Paint squares on open list to make it more visual as to what is going on
                    if(mazeMatrix[neighbour.x][neighbour.y]==0&&ControlPanel.toggleOpen.isSelected())mazeMatrix[neighbour.x][neighbour.y]=4;
                }
                //If it is on open list already, check to see if new path is better
                else if(neighbour.gCost<oldCostToNeighbour){
                    System.out.println("test");

                    //updates values of neighbour to shorter distances
                    neighbour.gCost=getDistance(neighbour, startNode);
                    neighbour.hCost=getDistance(neighbour, endNode);
                    neighbour.parent=currentNode;
                    //adds neighbour to open set
                }
            }
            previousNode = currentNode;
        }

    }

    //returns distance between 2 nodes
    private int getDistance(Node nodeA, Node nodeB) {
        return (int)(Math.sqrt(Math.pow(nodeA.x-nodeB.x,2)+Math.pow(nodeA.y-nodeB.y,2))*10);
    }
}

class depthFirstAlgorithm extends SearchClass {
    Random random = new Random();
    List<Node> closedNodes;
    Stack<Node> pathStack;

    public depthFirstAlgorithm(boolean randomEnabled) {
        //Gets x and y positions of start and end nodes in matrix
        for (int y = 0; y < mazeMatrix.length; y++) {
            for (int x = 0; x < mazeMatrix.length; x++) {
                if (mazeMatrix[x][y] == 2) startNode = new Node(x, y);
                if (mazeMatrix[x][y] == 3) endNode = new Node(x, y);
            }
        }

        closedNodes = new ArrayList<>();
        Node currentNode = startNode;
        pathStack = new Stack();
        pathStack.push(currentNode);

        searchDeep(currentNode, closedNodes, null, randomEnabled);
    }

    private void searchDeep(Node currentNode, List<Node> closedNodes, Node previousNode, boolean randomEnabled) {
        if (currentNode.x == endNode.x && currentNode.y == endNode.y) { //Checks if reached end node and retraces path
            //What happens when reached end point
            endNode.parent = previousNode;
            List<Node> path = retracePath(startNode, previousNode);
            for (Node node : path) {
                MazePanel.mazeMatrix[node.x][node.y] = 5;
            }
            mazeMatrix[endNode.x][endNode.y] = 3;
            mazeMatrix[startNode.x][startNode.y] = 2;
            mazePanel.repaint();

        } else {
            previousNode = currentNode;
            currentNode = getNeighbour(currentNode, closedNodes, randomEnabled); //Gets a neighbour of current node excluding closed nodes
            mazePanel.repaint();
            pathStack.push(previousNode);
            pathStack.push(currentNode);
            if (!neighbourInList(closedNodes, currentNode)) closedNodes.add(currentNode);
            currentNode.parent = previousNode;

            searchDeep(currentNode, closedNodes, currentNode.parent, randomEnabled); //Recursively call search algorithm
        }
    }

    private Node getNeighbour(Node currentNode, List<Node> closedNodes, boolean randomEnabled) {
        int x = currentNode.x;
        int y = currentNode.y;
        boolean atTop = (y <= 0);
        boolean atRight = (x >= mazeMatrix.length - 1);
        boolean atBottom = (y >= mazeMatrix.length - 1);
        boolean atLeft = (x <= 0);
        ArrayList<Node> validNodes = new ArrayList<>();
        if (!atTop) {
            if (mazeMatrix[x][y - 1] != 1 && !neighbourInList(closedNodes, new Node(x, y - 1))) {
                validNodes.add(new Node(x, y - 1));
            }
        }
        if (!atRight) {
            if (mazeMatrix[x + 1][y] != 1 && !neighbourInList(closedNodes, new Node(x + 1, y))) {
                validNodes.add(new Node(x + 1, y));
            }
        }
        if (!atBottom) {
            if (mazeMatrix[x][y + 1] != 1 && !neighbourInList(closedNodes, new Node(x, y + 1))) {
                validNodes.add(new Node(x, y + 1));
            }
        }
        if (!atLeft) {
            if (mazeMatrix[x - 1][y] != 1 && !neighbourInList(closedNodes, new Node(x - 1, y))) {
                validNodes.add(new Node(x - 1, y));
            }
        }
        if (validNodes.size() > 0 && randomEnabled) {
            return validNodes.get(random.nextInt(validNodes.size()));
        }
        else if (validNodes.size()>0 && !randomEnabled) {
            return validNodes.get(0);
        }

        return getNeighbour(pathStack.pop(), closedNodes, randomEnabled);

    }
}

class breadthFirstAlgorithm extends SearchClass {
    Random random = new Random();
    List<Node> closedNodes;
    Queue<Node> pathQueue;
    Node previousNode;

    public breadthFirstAlgorithm() {
        //Gets x and y positions of start and end nodes in matrix
        for (int y = 0; y < mazeMatrix.length; y++) {
            for (int x = 0; x < mazeMatrix.length; x++) {
                if (mazeMatrix[x][y] == 2) startNode = new Node(x, y);
                if (mazeMatrix[x][y] == 3) endNode = new Node(x, y);
            }
        }

        closedNodes = new ArrayList<>();
        Node currentNode = startNode;
        pathQueue = new LinkedList<>();
        pathQueue.add(currentNode);
        closedNodes.add(currentNode);
        int x=0;
        while (!pathQueue.isEmpty()) {
            x++;
            currentNode=pathQueue.poll();
                System.out.println("Loop: "+currentNode.x);
                //checks each neighbour of current node that isn't a wall
                previousNode = currentNode;
                for (Node neighbour : getNeighbours(currentNode.x, currentNode.y, closedNodes)) {
                    //If neighbour already visited ignore
                    if (neighbourInList(closedNodes, neighbour)) {
                        continue;
                    }
                    if (currentNode.x == endNode.x && currentNode.y == endNode.y) { //Checks if reached end node and retraces path
                        System.out.println("reached end");
                        //What happens when reached end point
                        endNode.parent = previousNode;
                        List<Node> path = retracePath(startNode, previousNode);
                        for (Node node : path) {
                            MazePanel.mazeMatrix[node.x][node.y] = 5;
                        }
                        mazeMatrix[endNode.x][endNode.y] = 3;
                        mazeMatrix[startNode.x][startNode.y] = 2;
                        mazePanel.repaint();
                        return;
                    }
                    closedNodes.add(neighbour);
                    pathQueue.add(neighbour);
                    neighbour.parent = currentNode;
                    //Paint squares on open list to make it more visual as to what is going on
                    if (mazeMatrix[neighbour.x][neighbour.y] == 0 && ControlPanel.toggleOpen.isSelected())
                        mazeMatrix[neighbour.x][neighbour.y] = 4;
                }
                mazePanel.repaint();
            }
        }
    }
