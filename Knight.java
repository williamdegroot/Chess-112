//========================================================================================
// class Knight
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class Knight extends Piece {

	// MEMBER VARIABLES

	// File name for White Knight icon
	String white_filename = "src/white_knight.png";

	// File name for Black Knight icon
	String black_filename = "src/black_knight.png";

	//========================================================================================

	// CONSTRUCTOR
	// Each knight keeps track of its team and location
	public Knight(Team a, int row, int col) {

		// Extends Piece class
		super(a, row, col);

	} // Knight ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	// Knight is able to move in an 'L' shape
	@Override
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {
		
		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		int startRow = square.getRow();
		int startColumn = square.getColumn();

		// Threatened square located two squares above and one square to the right
		if ((startRow - 2 >= 0) && (startColumn != 7)) {
			threatenedSquares.add(board.getSquare((startRow - 2), (startColumn + 1)));
		}

		// Threatened square located one square above and two squares to the right
		if ((startRow != 0) && (startColumn + 2 <= 7)) {
			threatenedSquares.add(board.getSquare((startRow - 1), (startColumn + 2)));
		}

		// Threatened square located two squares above and one square to the left
		if ((startRow - 2 >= 0) && (startColumn != 0)) {
			threatenedSquares.add(board.getSquare((startRow - 2), (startColumn - 1)));
		}

		// Threatened squares located one square above and two squares to the left
		if ((startRow != 0) && (startColumn - 2 >= 0)) {
			threatenedSquares.add(board.getSquare((startRow - 1), (startColumn - 2)));
		}
			
		// Threatened square located two squares below and one square to the right
		if ((startRow + 2 <= 7) && (startColumn != 7)) {
			threatenedSquares.add(board.getSquare((startRow + 2), (startColumn + 1)));
		}

		// Threatened square located one square below and two squares to the right
		if ((startRow != 7) && (startColumn + 2 <= 7)) {
			threatenedSquares.add(board.getSquare((startRow + 1), (startColumn + 2)));
		}

		// Threatened square located two squares below and one square to the left
		if ((startRow + 2 <= 7) && (startColumn != 0)) {
			threatenedSquares.add(board.getSquare((startRow + 2), (startColumn - 1)));
		}

		// Threatened square located one square below and two squares to the left
		if ((startRow != 7) && (startColumn - 2 >= 0)) {
			threatenedSquares.add(board.getSquare((startRow + 1), (startColumn - 2)));
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

				else if (s.occPiece.team != square.occPiece.team  && !s.occPiece.getType().equals("KING")) {
					possibleMoves.add(s);
				}
			}
		}

		return possibleMoves;

	} // getPossibleMoves ()

	//========================================================================================

	// SOURCE
	// http://www.enpassant.dk/chess/fonteng.htm
	// Retrieve image of White or Black Knight
	@Override
	public String getImage() {

		if (super.team.color == Color.WHITE) {
			return white_filename;
		}

		if (super.team.color == Color.BLACK) {
			return black_filename;
		
		} else {
			return "";
		}

	} // getImage ()

	//========================================================================================

	// Return Knight type
	public String getType() {

		return "KNIGHT";

	} // getType ()

	//========================================================================================

	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		Knight copyKnight = new Knight(a, row, col);
		copyKnight.numMoves = numMoves;
		return copyKnight;

	} // copyPiece ()

//========================================================================================
} // class KNIGHT
//========================================================================================


