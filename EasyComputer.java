//========================================================================================
// class EasyComputer
//========================================================================================

// IMPORTS
import java.awt.*;
import java.util.ArrayList;

//========================================================================================

public class EasyComputer extends Computer{

	//========================================================================================

	// CONSTRUCTOR
	public EasyComputer(Team _team, Board _board) {

		super(_team,_board);

	} // EasyComputer ()

	//========================================================================================

	// Easy computer uses random moves
	public Team easyComputerMove() {

		// All of the pieces on the computer's team
		ArrayList<Piece> remainingPieces = copyList(team.getTeamPieces());

		Piece selected = null;
		int index = -1;
		Square startSquare = null;
		Square endSquare = null;

		try {
			while (true) {

				// Select a random piece
				index = (int) (Math.random() * (remainingPieces.size() - 1));
				selected = remainingPieces.get(index);

				// Determine possible moves for the piece selected
				ArrayList<Square> possibleMoves = selected.getPossibleMoves(board.getSquare(selected.row,
						selected.col), board);

				// If selected piece has no possible moves, select another random piece
				if (possibleMoves.size() == 0) {
					remainingPieces.remove(selected);
					continue;
				}

				// Location of the piece selected
				startSquare = board.getSquare(selected.row, selected.col);

				// Select a random move
				endSquare = possibleMoves.get((int) (Math.random() * (possibleMoves.size() - 1)));

				break;
			}

			// Randomly Move the random piece
			startSquare.occPiece.move(startSquare, endSquare, board, true);

		} catch(Exception e){};

		// Switch turns
		if (team.color == Color.WHITE) {
			return board.black;

		} else {
			return board.white;
		}

	} // easyComputerMove ()

	//========================================================================================

	// Copy of the computer's pieces
	// Necessary in order to remove pieces from the random list without removing from the team
	private ArrayList<Piece> copyList(ArrayList<Piece> original) {

		ArrayList<Piece> copy = new ArrayList<Piece>();
		for (Piece a : original) {
			copy.add(a);
		}

		return copy;

	} // copyList ()

//========================================================================================
} // class EASYCOMPUTER
//========================================================================================

