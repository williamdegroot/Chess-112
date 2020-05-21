//========================================================================================
// class MediumComputer
//========================================================================================

// IMPORTS
import java.util.ArrayList;

public class Computer {

    // MEMBER VARIABLES

    Team team;

    Board board;

    //========================================================================================

    public Computer(Team _team, Board _board) {

        board = _board;
        team = _team;

    }//Computer ()

    //========================================================================================

    public Team computerMove(Board board, int depth, int pruneValue){
        // Update computer's board to current board
        this.board = board;

        Square startSquare = null;
        Square endSquare = null;

        // Create a copy of the current board
        // Neceessary to test possible moves and successor boards without overwriting game board
        Board copyBoard = board.copyBoard();

        // Create MiniMax object
        // Minimax will prune according to pruneValue
        MiniMax mediumMove = new MiniMax(board, pruneValue);

        // Run minimax on the current board to find the best play
        // Pair stores score and board object
        // Minimax runs to the specified depth
        Node optimalMove = mediumMove.minimax(new Node(null, new Pair( board.evaluateBoard("BLACK"),
                board)), depth, true);

        // Trace up the tree to get the optimal move
        // Try/Catch is used to prevent error when checkmate/stalemate occurs and return is null
        try {
            while(optimalMove.parent.parent != null) {
                optimalMove = optimalMove.parent;
            }

        } catch (NullPointerException e){};

        // Save the optimal board to copyBoard
        try {
            copyBoard = optimalMove.state.board;

        } catch (NullPointerException e) {
            return board.white;
        }

        // If a piece present on the optimal board is not present on the current board
        // That piece has moved, and is the end square
        for (Piece p : copyBoard.black.teamPieces) {
            if (!board.black.pieceFoundInTeam(p)) {
                endSquare = board.getSquare(p.row, p.col);
            }
        }

        // If a piece present on the current board is not present on the optimal boad
        // That piece is the moving piece, and is the start square
        for (Piece p : board.black.teamPieces) {
            if (!copyBoard.black.pieceFoundInTeam(p)) {
                startSquare = board.getSquare(p.row, p.col);
            }
        }

        // Make the optimal move
        startSquare.occPiece.move(startSquare, endSquare, board, true);

        // Update teams
        board.white.updateTeam();
        board.black.updateTeam();

        // Switch turns
        return board.white;
    }

//========================================================================================
} // class COMPUTER
//========================================================================================
