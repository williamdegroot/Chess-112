//========================================================================================
// class Queen
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class Queen extends Piece {

	// MEMBER VARIABLES

	// File name for White Queen icon
	String white_filename = "src/white_queen.png";

	// File name for Black Queen icon
	String black_filename = "src/black_queen.png";

	//========================================================================================

	// Constructor
	// Each queen keeps track of its team and location
	public Queen(Team a, int row, int col) {

		// Extends Piece class
		super(a, row, col);

	} // Queen ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	// Queen is able to move like the rook and bishop combined (i.e., in any direction)
	@Override
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		int startRow = square.getRow();
		int startColumn = square.getColumn();

		//initialize fake rook and bishop
		Rook rook = new Rook(null, -1, -1);
		Bishop bishop = new Bishop(null, -1, -1);

		// Determine all threatened squares given rook and bishop functionality
		threatenedSquares.addAll(rook.getThreatenedSquares(square, board));
		threatenedSquares.addAll(bishop.getThreatenedSquares(square, board));

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
	// Retrieve image of White or Black Queen
	@Override
	public String getImage() {

		if (super.team.color == Color.WHITE){
			return white_filename;
		}

		if (super.team.color == Color.BLACK){
			return black_filename;
		
		} else{
			return "";
		}

	} // getImage ()

	//========================================================================================

	// Return Queen type
	public String getType() {

		return "QUEEN";

	} // getType ()

	//========================================================================================

	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		Queen copyQueen = new Queen(a, row, col);
		copyQueen.numMoves = numMoves;
		return copyQueen;

	} // copyPiece ()

//========================================================================================
} // class QUEEN
//========================================================================================

