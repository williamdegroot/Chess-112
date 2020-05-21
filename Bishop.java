//========================================================================================
// class Bishop
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class Bishop extends Piece {

	// MEMBER VARIABLES

	// File name for White Bishop icon
	String white_filename = "src/white_bishop.png";

	// File name for Black Bishop icon
	String black_filename = "src/black_bishop.png";

	//========================================================================================

	// CONSTRUCTOR
	// Each bishop keeps track of its team and location
	public Bishop(Team a, int row,int col) {

		// Extends Piece class
		super(a, row, col);

	} // Bishop ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	// Bishop is able to move along diagonals
	@Override
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		int startRow = square.getRow();
		int startColumn = square.getColumn();

		// Threatened squares in the down/right direction
		int i = 1;
		while (i <= (7 - startColumn) && i <= (7 - startRow)) {
			if (board.getSquare((startRow + i), (startColumn + i)).occPiece == null) {
				threatenedSquares.add(board.getSquare((startRow + i), (startColumn + i)));

			} else {
				if (square.occPiece.team != board.getSquare((startRow + i), 
					(startColumn + i)).occPiece.team) {
					threatenedSquares.add(board.getSquare((startRow + i), (startColumn + i)));
				}
				break;
			}
			i++;
		}

		// Threatened squares in the down/left direction
		int j = 1;
		while (j <= (startColumn) && j <= (7 - startRow)) {
			if (board.getSquare((startRow + j), (startColumn - j)).occPiece == null) {
				threatenedSquares.add(board.getSquare((startRow + j), (startColumn - j)));
			
			} else {
				if (square.occPiece.team != board.getSquare((startRow + j), 
					(startColumn - j)).occPiece.team) {
					threatenedSquares.add(board.getSquare((startRow + j), (startColumn - j)));
				}
				break;
			}
			j++;
		}

		// Threatened squares in the up/right direction
		int k = 1;
		while (k <= (7 - startColumn) && k <= (startRow)) {
			if (board.getSquare((startRow - k), (startColumn + k)).occPiece == null) {
				threatenedSquares.add(board.getSquare((startRow - k), (startColumn + k)));

			} else {
				if (square.occPiece.team != board.getSquare((startRow - k), 
					(startColumn + k)).occPiece.team) {
					threatenedSquares.add(board.getSquare((startRow - k), (startColumn + k)));
				}
				break;
			}
			k++;
		}

		// Threatened squares in the up/left direction
		int m = 1;
		while (m <= startColumn && m <= startRow) {
			if (board.getSquare((startRow - m), (startColumn - m)).occPiece == null) {
				threatenedSquares.add(board.getSquare((startRow - m), (startColumn - m)));

			} else {
				if (square.occPiece.team != board.getSquare((startRow - m), 
					(startColumn - m)).occPiece.team) {
					threatenedSquares.add(board.getSquare((startRow - m), (startColumn - m)));
				}
				break;
			}

			m++;
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
	// Retrieve image of White or Black Bishop
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

	// Return Bishop type
	public String getType() {

		return "BISHOP";

	} // getType ()

	//========================================================================================

	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		Bishop copyBishop = new Bishop(a, row, col);
		copyBishop.numMoves = numMoves;
		return copyBishop;

	} // copyPiece ()

//========================================================================================
} // class PAWN
//========================================================================================


