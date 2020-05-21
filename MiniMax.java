//========================================================================================
// class MiniMax
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.util.Collections;

//========================================================================================

// SOURCE
// https://www.youtube.com/watch?v=l-hh51ncgDI
// Used for inspriation and clarity

public class MiniMax {

    // MEMBER VARIABLES

    // Number of successor nodes evaluated
    private int pruneValue;

    // Current game board
    private Board origBoard;

    //========================================================================================

    // CONSTRUCTOR
    // Determines the best move for the CPU
    public MiniMax(Board origBoard, int prune) {

        this.origBoard = origBoard;
        pruneValue = prune;

    } // MiniMax ()

    //========================================================================================

    // Run the minimax algorithm to determine best move given the current game board
    public Node minimax(Node node, int depth, boolean maximizingPlayer) {

        // Initialize terminal nodes
        Node checkmate = null;
        Node stalemate = null;

        // After reaching bottom of the tree, return the node
        if (depth == 0){
            return node;
        }

        // Nodes are a parent node reference and a pair
        // Pairs are the board score and board object
    
        ArrayList<Node> nodeSuccessors = new ArrayList<Node>();

        // Get all successor nodes from original board for a particular team
        if (maximizingPlayer){
            for (Board b : node.state.board.successorBoards(node.state.board.black)){
                
                // Terminal Test - if successorboard reaches checkmate, return node
                if (b.checkCheckMate(b.white)){
                    checkmate = new Node(node, new Pair(b.evaluateBoard("BLACK"), b));
                    return checkmate;
                }

                // If successorboard reaches stalemate, disincentivise move
                else if (b.checkStaleMate(b.white)){
                    stalemate = new Node(node, new Pair(-10000, b));
                    nodeSuccessors.add(stalemate);
                
                // Add successor nodes to arraylist
                } else {
                    nodeSuccessors.add(new Node(node, new Pair(b.evaluateBoard("BLACK"), b)));
                }
            } 

            // Run maxPair - finds the maximum of minimax of all of the nodeSuccessors
            return maxPair(nodeSuccessors, depth);

        } else {
            for (Board b : node.state.board.successorBoards(node.state.board.white)) {

                // Terminal Test - if successorboard reaches checkmate, return node
                if (b.checkCheckMate(b.black)){
                    checkmate = new Node(node, new Pair(b.evaluateBoard("WHITE"), b));
                    return checkmate;
                } 

                // Add successor nodes to arraylist
                nodeSuccessors.add(new Node(node, new Pair(b.evaluateBoard("WHITE"), b)));
            } 

             // Run maxPair - finds the minimum of minimax of all of the nodeSuccessors
            return minPair(nodeSuccessors, depth);
        }

    } // MiniMax ()

    //========================================================================================

    // Determines the maximum of minimax of all of successor nodes
    private Node maxPair(ArrayList<Node> inputNodes, int depth){

        ArrayList<Node> successorNodes = new ArrayList<Node>();
        ArrayList<Node> prunedInputNodes = new ArrayList<Node>();

        // Sort the successor nodes in descending order by board score
        Node.sort(inputNodes);

        // Prune list to only top five scoring successor boards
         // Try/Catch prevents error if less than 'prune value' number of successor boards
        try {
            for (int i = 0; i < pruneValue; i++) {
                prunedInputNodes.add(inputNodes.get(i));
             }
        }
        catch(IndexOutOfBoundsException e){};


        // Call minimax to fill successorNodes with minimax results
        for (Node a : prunedInputNodes){
            successorNodes.add(minimax(a, depth - 1, false));
        }

        // Determine maximum of successorNodes
        Node maxNode = null;
        try{
            maxNode = successorNodes.get(0);
            for (int i = 1; i < successorNodes.size(); i++){
                try{
                    if (successorNodes.get(i).state.score > maxNode.state.score){
                        maxNode = successorNodes.get(i);
                    }
                }
                catch(Exception e){};
            }
        }
        catch (IndexOutOfBoundsException e){};

        // Return maximum node
        return  maxNode;

    }

    //========================================================================================

    // Determines the minimum of minimax of all of successor nodes
    private Node minPair(ArrayList<Node> inputNodes, int depth){

        ArrayList<Node> successorNodes = new ArrayList<Node>();
        ArrayList<Node> prunedInputNodes = new ArrayList<Node>();

        // Sort the successor nodes in descending order by board score
        Node.sort(inputNodes);

        // Prune list to only bottom five scoring successor boards
        // Try/Catch prevents error if less than 'prune value' number of successor boards
        try {
            for (int i = 0; i < pruneValue; i++) {
                prunedInputNodes.add(inputNodes.get(inputNodes.size() - 1 - i));
            }

        } catch(IndexOutOfBoundsException e){};

        // Call minimax to fill successorNodes with minimax results
        for (Node a : prunedInputNodes) {
            successorNodes.add(minimax(a, depth - 1, true));
        }
        
         // Determine minimum of successorNodes
        Node minNode = null;
        try {  
            minNode = successorNodes.get(0);
            for (int i = 1; i < successorNodes.size(); i++) {
                try{
                    if (successorNodes.get(i).state.score < minNode.state.score){
                        minNode = successorNodes.get(i);
                    }
                }
                catch(Exception e){};
            }
        }
        catch(IndexOutOfBoundsException e){};

        // Return minimum node
        return  minNode;

    } // minPair ()

//========================================================================================
} // class MiniMax
//========================================================================================

//========================================================================================
// class Node
//========================================================================================

class Node implements Comparable<Node> {

    //MEMBER VARIABLES

    // Pair of board object and board score
    Pair state;

    // Parent node
    Node parent;

    //========================================================================================

    // CONSTRUCTOR
    // Each node has a parent node, and a pair that consists of a board object and board score
    public Node(Node parent, Pair state) {

        this.parent = parent;
        this.state = state;

    } // Node ()

    //========================================================================================

    // SOURCE
    // https://www.geeksforgeeks.org/collections-sort-java-examples/
    // Used to determine correct syntax for sorting arraylists
    // Sorts arraylist of nodes by board score in descending order
    public static ArrayList<Node> sort(ArrayList<Node> nodes) {

        Collections.sort(nodes);
        return nodes;

    } // sort ()

    //========================================================================================

    // Compares board scores to determine list of nodes
    public int compareTo(Node node) {

        if (node.state.score == this.state.score) {
            return 0;
        }

        if (node.state.score > this.state.score) {
            return 1;
        }
        return -1;

    } // compareTo ()

//========================================================================================
}// class Node
//========================================================================================


