//========================================================================================
// class Rook
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class Rook extends Piece {

	// MEMBER VARIABLES

	// File name for White Rook
	String white_filename = "src/white_rook.png";

	// File name for Black Rook
	String black_filename = "src/black_rook.png";

	//========================================================================================

	// CONSTRUCTOR
	// Each rook keeps track of its team and location
	public Rook(Team a, int row, int col) {
		
		// Extends Piece class
		super(a, row, col);

	} // Rook ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	// Rook is able to move straight (not diagonal) in any direction
	@Override
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		int startRow = square.getRow();
		int startColumn = square.getColumn();

		// Threatened squares in the up direction
		for (int i = (startRow - 1); i >= 0; i--) {
			if (board.getSquare(i, startColumn).occPiece == null) {
				threatenedSquares.add(board.getSquare(i, startColumn));
			
			} else {
				if (square.occPiece.team != board.getSquare(i, startColumn).occPiece.team) {
					threatenedSquares.add(board.getSquare(i, startColumn));
				}
				break;
			}
		}

		// Threatened squares in the down direction
		for (int i = (startRow + 1); i <= 7; i++) {
			if (board.getSquare(i, startColumn).occPiece == null) {
				threatenedSquares.add(board.getSquare(i, startColumn));
			
			} else {
				if (square.occPiece.team != board.getSquare(i, startColumn).occPiece.team){
					threatenedSquares.add(board.getSquare(i, startColumn));
				}
				break;
			}
		}

		// Threatened squares in the right direction
		for (int i = (startColumn - 1); i >= 0; i--) {
			if (board.getSquare(startRow, i).occPiece == null) {
				threatenedSquares.add(board.getSquare(startRow, i));
			
			} else {
				if (square.occPiece.team != board.getSquare(startRow, i).occPiece.team) {
					threatenedSquares.add(board.getSquare(startRow, i));
				}
				break;
			}
		}

		// Threatened squares in the left direction
		for (int i = (startColumn + 1); i <= 7; i++) {
			if (board.getSquare(startRow, i).occPiece == null) {
				threatenedSquares.add(board.getSquare(startRow, i));
			
			} else {
				if (square.occPiece.team != board.getSquare(startRow, i).occPiece.team) {
					threatenedSquares.add(board.getSquare(startRow, i));
				}
				break;
			}
		}
				
		return threatenedSquares;

	} // getThreatenedSquares ()

	//========================================================================================

	// Determine legal moves for piece
	@Override
	public ArrayList<Square> getPossibleMoves(Square square, Board board) {

		ArrayList<Square> possibleMoves = new ArrayList<Square>();

		for (Square s : getThreatenedSquares(square, board)) {
			
			if (!kingThreatened(square, s, board)) {
				if (s.occPiece == null) {
					possibleMoves.add(s);
				}

				else if (s.occPiece.team != square.occPiece.team && !s.occPiece.getType().equals("KING")) {
					possibleMoves.add(s);
				}
			}
		}

		return possibleMoves;

	} // getPossibleMoves ()
	
	//========================================================================================

	// SOURCE
	// http://www.enpassant.dk/chess/fonteng.htm
	// Retrieve image of White or Black Rook
	@Override
	public String getImage() {

		if (super.team.color == Color.WHITE) {
			return white_filename;
		}

		if (super.team.color == Color.BLACK) {
			return black_filename;
		}

		else {
			return "";
		}

	} // getImage ()

	//========================================================================================

	// Return Rook type
	public String getType() {

	 	return "ROOK";

	} // getType ()

	//========================================================================================

	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		Rook copyRook = new Rook(a, row, col);
		copyRook.numMoves = numMoves;
		return copyRook;

	} // copyPiece ()

//========================================================================================
} // class ROOK
//========================================================================================

