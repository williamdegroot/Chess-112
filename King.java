//========================================================================================
// class King
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class King extends Piece {

	// MEMBER VARIABLES

	// File name for White King icon
	String white_filename = "src/white_king.png";

	// File name for Black King icon
	String black_filename = "src/black_king.png";

	//========================================================================================

	// CONSTRUCTOR
	//each king keeps track of its team and location
	public King(Team a, int row, int col) {

		// Extends Piece class
		super(a, row, col);

	} // King ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	// King is able to move one square in any direction
	@Override
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		int startRow = square.getRow();
		int startColumn = square.getColumn();

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if ((i != 0) || (j != 0)) {
						threatenedSquares.add(board.getSquare((startRow + i), (startColumn + j)));
					}
				}
				catch(Exception e){};
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
				if (s.occPiece == null){
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
	// Retrieve image of White or Black King
	@Override
	public String getImage(){

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

	// Return King type
	public String getType() {

		return "KING";

	} // getType ()

	//========================================================================================

	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		King copyKing = new King(a, row, col);
		copyKing.numMoves = numMoves;
		return copyKing;

	} // copyPiece ()

//========================================================================================
} // class KING
//========================================================================================

