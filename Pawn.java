///========================================================================================
// class Pawn
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class Pawn extends Piece {

	// MEMBER VARIABLES

	// File name for White Pawn icon
	String white_filename = "src/white_pawn.png";

	// File name for Black Pawn icon
	String black_filename = "src/black_pawn.png";

	// Whether the pawn can be promoted
	public boolean readyForPromotion = false;

	// Whether the pawn is at risk of being taken by an 'en passant' move
	public boolean enPassantRisk = false;

	//========================================================================================

	// Constructor
	// Each pawn keeps track of its team and location
	public Pawn(Team a, int row, int col) {

		// Extends Piece class
		super(a, row, col);

	} // Pawn ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	@Override
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		int startRow = square.getRow();
		int startColumn = square.getColumn();

		// During first move, pawn is allowed to travel forward two spaces
		if (super.numMoves == 0) {
			if (super.team.color == Color.WHITE) {

				if ((board.getSquare(5, startColumn).occPiece == null) && (board.getSquare(4, 
					startColumn).occPiece == null)){
					threatenedSquares.add(board.getSquare(4, startColumn));
				}
			}

			if (super.team.color == Color.BLACK) {
				if ((board.getSquare(2, startColumn).occPiece == null) && board.getSquare(3, 
					startColumn).occPiece == null) {
					threatenedSquares.add(board.getSquare(3, startColumn));
				}
			}
		}
		
		// Regular pawn mobility for white team
		if (super.team.color == Color.WHITE) {
			
			// Pawn is able to move forward one square
			if (startRow != 0) {
				if (board.getSquare((startRow - 1), startColumn).occPiece == null) {
					threatenedSquares.add(board.getSquare((startRow - 1), startColumn));
				}
			}

			// Pawn is able to move diagonally one square to kill an opponent piece
			if (startColumn != 0 && startRow != 0) {
				if (board.getSquare((startRow - 1), (startColumn - 1)).occPiece != null) {
					threatenedSquares.add(board.getSquare((startRow - 1), (startColumn - 1)));
				}
			}
			if (startColumn != 7 && startRow != 0) {
				if (board.getSquare((startRow - 1), (startColumn + 1)).occPiece != null){
					threatenedSquares.add(board.getSquare((startRow - 1), (startColumn + 1)));
				}	
			}

			// En Passant Attack
			if (startRow == 3) {
				if (startColumn != 0) {
					if (board.getSquare(startRow, (startColumn - 1)).occPiece != null) {
						if (board.getSquare(startRow, (startColumn - 1)).occPiece.getType().equals("PAWN")) {
							if (((Pawn) (board.getSquare(startRow, (startColumn - 1)).occPiece)).enPassantRisk) {
								threatenedSquares.add(board.getSquare((startRow - 1), (startColumn - 1)));
							}
						}
					}
				}
				if (startColumn != 7) {
					if (board.getSquare(startRow, (startColumn + 1)).occPiece != null) {
						if (board.getSquare(startRow, (startColumn + 1)).occPiece.getType().equals("PAWN")) {
							if (((Pawn) (board.getSquare(startRow, (startColumn + 1)).occPiece)).enPassantRisk) {
								threatenedSquares.add(board.getSquare((startRow - 1), (startColumn + 1)));
							}
						}
					}
				}
			}
		}
		
		// Regular pawn mobility for black team
		if (super.team.color == Color.BLACK) {

			// Pawn is able to move forward one square
			if (startRow != 7) {
				if (board.getSquare((startRow + 1), startColumn).occPiece == null) {
					threatenedSquares.add(board.getSquare((startRow + 1), startColumn));
				}
			}

			// Pawn is able to move diagonally one square to kill an opponent piece
			if (startColumn != 0 && startRow != 7) {
				if (board.getSquare((startRow + 1), (startColumn - 1)).occPiece != null) {
					threatenedSquares.add(board.getSquare((startRow + 1), (startColumn - 1)));
				}
			}
			if (startColumn != 7 && startRow != 7) {
				if (board.getSquare((startRow + 1), (startColumn + 1)).occPiece != null) {
					threatenedSquares.add(board.getSquare((startRow + 1), (startColumn + 1)));
				}	
			}

			//En Passant Attack
			if (startRow == 4) {
				if (startColumn != 0) {
					if (board.getSquare(startRow, (startColumn - 1)).occPiece != null) {
						if (board.getSquare(startRow, (startColumn - 1)).occPiece.getType().equals("PAWN")) {
							if (((Pawn) (board.getSquare(startRow, (startColumn - 1)).occPiece)).enPassantRisk) {
								threatenedSquares.add(board.getSquare((startRow + 1), (startColumn - 1)));
							}
						}
					}
				}
				if (startColumn != 7) {
					if (board.getSquare(startRow, (startColumn + 1)).occPiece != null) {
						if (board.getSquare(startRow, (startColumn + 1)).occPiece.getType().equals("PAWN")) {
							if (((Pawn) (board.getSquare(startRow, (startColumn + 1)).occPiece)).enPassantRisk) {
								threatenedSquares.add(board.getSquare((startRow + 1), (startColumn + 1)));
							}
						}
					}
				}
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
			if (!kingThreatened(square, s, board)){
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

	// Promote pawn once it reaches the opposite end of the board
	public static void promote(Pawn pawn, Board board, Team team, char upgrade) {
		
		// Remove pawn from board and team
		team.teamPieces.remove(pawn);
		Piece.kill(board.getSquare(pawn.row, pawn.col));

		// Upgrade Pawn to Bishop
		if (upgrade == 'b') {
			Bishop bishop = new Bishop(team, pawn.row, pawn.col);
			board.getSquare(pawn.row, pawn.col).occPiece = bishop;
			team.addToTeam(bishop);
		}

		// Upgrade Pawn to Knight
		if (upgrade == 'k') {
			Knight knight = new Knight(team, pawn.row, pawn.col);
			board.getSquare(pawn.row, pawn.col).occPiece = knight;
			team.addToTeam(knight);
		}

		// Upgrade Pawn to Rook
		if (upgrade == 'r') {
			Rook rook = new Rook(team, pawn.row, pawn.col);
			board.getSquare(pawn.row, pawn.col).occPiece = rook;
			team.addToTeam(rook);
		}

		// Upgrade Pawn to Queen
		if (upgrade == 'q') {
			Queen queen = new Queen(team, pawn.row, pawn.col);
			board.getSquare(pawn.row, pawn.col).occPiece = queen;
			team.addToTeam(queen);
		}

	} // Promote ()

	//========================================================================================
	
	// SOURCE
	// http://www.enpassant.dk/chess/fonteng.htm
	// Retrieve image of White or Black Pawn
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

	// Return Pawn type
	public String getType() {

		return "PAWN";

	} // getType ()
 
	//========================================================================================
	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		Pawn copyPawn = new Pawn(a, row, col);
		copyPawn.numMoves = numMoves;
		return copyPawn;

	} // copyPiece ()

//========================================================================================
} // class PAWN
//========================================================================================

