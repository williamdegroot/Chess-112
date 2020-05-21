//========================================================================================
// class Piece
//========================================================================================

// Parent class for each piece type

// IMPORTS
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//========================================================================================

public class Piece {

	//MEMBER VARIABLES

	//Piece's team
	public Team team;

	// Piece's number of moves
	public int numMoves = 0;

	// Location of piece on grid
	public int row;
	public int col;

	//========================================================================================

	// CONSTRUCTOR - creates pieces
	// Each piece has a team
	// Each piece keeps track of its own row and column
	public Piece(Team a, int row, int col) {
		// Piece's team
		team = a;

		// Piece's location
		this.row = row;
		this.col = col;

	} // Piece ()

	//========================================================================================

	// Check if move attempted by player is valid
	public boolean checkMove(Square startSquare, Square endSquare, Board board) {

		// Determines each possible move for the piece selected
		// Evaluates if move is allowed given the piece selected and its location

		for (Square s : getPossibleMoves(startSquare, board)) {
			if (endSquare.equals(s)) {
				return true;
			}
		}

		return false;

	} //checkMove ()

	//========================================================================================

	// If play is valid, piece is moved
	public int move(Square startSquare, Square endSquare, Board board, Boolean aiGame) {

		//if there is a piece on the end square, then that piece is taken
		//removes taken piece from its team array

		// If opponent's piece is taken, it is removed from it's team

		Team team;
		Piece piece;

		try {
			team = endSquare.occPiece.team;
			piece = endSquare.occPiece;
			team.teamPieces.remove(piece);
		} catch(Exception e){};



		// Transfer piece from it's starting square to it's new square
		endSquare.occPiece = startSquare.occPiece;

		// Remove piece from the start square
		Piece.kill(startSquare);
		
		// Add 1 to number of moves for the piece moved
		endSquare.occPiece.numMoves += 1;


		// Update row and column for the moved piece
		endSquare.occPiece.row = endSquare.getRow();
		endSquare.occPiece.col = endSquare.getColumn();

        // If pawn reaches opposite side of board, allowed to promote
        Pawn pawn = null;
		if (endSquare.occPiece.getType().equals("PAWN")) { 
			if (endSquare.occPiece.row == 0 || endSquare.occPiece.row == 7) {
				pawn = (Pawn) endSquare.occPiece;
				pawn.readyForPromotion = true;
				if (aiGame){
					Pawn.promote(pawn, board, endSquare.occPiece.team, 'q');
				}
			}
		}

		// En Passant
		if (endSquare.occPiece.getType().equals("PAWN")){
			if (endSquare.occPiece.team.color == Color.WHITE){
				if (endSquare.getRow() == 2){
					if (board.getSquare((endSquare.getRow() + 1), endSquare.getColumn()).occPiece != null){
						if (board.getSquare((endSquare.getRow() + 1), endSquare.getColumn()).occPiece.getType().equals("PAWN")){
							if (((Pawn) (board.getSquare((endSquare.getRow() + 1), endSquare.getColumn()).occPiece)).enPassantRisk){
								board.black.teamPieces.remove(board.getSquare((endSquare.getRow() + 1), endSquare.getColumn()).occPiece);
								Piece.kill(board.getSquare((endSquare.getRow() + 1), endSquare.getColumn()));
							}
						}
					}
				}
			}
			if (endSquare.occPiece.team.color == Color.BLACK){
				if (endSquare.getRow() == 5){
					if (board.getSquare((endSquare.getRow() - 1), endSquare.getColumn()).occPiece != null){
						if (board.getSquare((endSquare.getRow() - 1), endSquare.getColumn()).occPiece.getType().equals("PAWN")){
							if (((Pawn) (board.getSquare((endSquare.getRow() - 1), endSquare.getColumn()).occPiece)).enPassantRisk){
								board.white.teamPieces.remove(board.getSquare((endSquare.getRow() - 1), endSquare.getColumn()).occPiece);
								Piece.kill(board.getSquare((endSquare.getRow() - 1), endSquare.getColumn()));
							}
						}
					}
				}
			}
		}

		for (Pawn p : board.white.getPawn()){
			p.enPassantRisk = false;
		}
		for (Pawn p : board.black.getPawn()){
			p.enPassantRisk = false;
		}

		if (endSquare.occPiece.getType().equals("PAWN")) { 
			if ((endSquare.occPiece).numMoves == 1){
				if ((endSquare.getRow() == 3) || (endSquare.getRow() == 4)){
					((Pawn) (endSquare.occPiece)).enPassantRisk = true;
				}
			}	
		}

		// Switch turns
		Team other;
		if (endSquare.occPiece.team == board.white) other = board.black;
		else other = board.white;

		// Check if game is over
		if (board.checkCheckMate(other)) return 10; //checkmate
		if (board.checkStaleMate(other)) return -10; //stalemate

		return 1;

	} // move ()

	//========================================================================================

	// Determine legal moves for piece
	public ArrayList<Square> getPossibleMoves(Square square, Board board) {

		ArrayList<Square> possibleSquares = new ArrayList<Square>();
		return possibleSquares;

	} // getPossibleMoves ()

	//========================================================================================

	// Determine squares that are threatened by a piece
	// Includes moves that leave own King in check
	public ArrayList<Square> getThreatenedSquares(Square square, Board board) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		return threatenedSquares;

	} // getTreatenedSquares ()

	//========================================================================================

	// Determine if move puts or leaves own King in check
	public boolean kingThreatened(Square startSquare, Square endSquare, Board board) {
		//Piece holder = endSquare.occPiece;
		//Team holderTeam = endSquare.occ
		//boolean murder = false;

		if (endSquare.occPiece != null){
			if (endSquare.occPiece.getType() == "KING"){
				return true;
			}
		}
	

		Team team;
		Piece piece;
		Board copyBoard = board.copyBoard();
		Square copiedEndSquare = copyBoard.getSquare(endSquare.getRow(), endSquare.getColumn());
		Square copiedStartSquare = copyBoard.getSquare(startSquare.getRow(), startSquare.getColumn());

		try {
			team = copiedEndSquare.occPiece.team;
			piece = copiedEndSquare.occPiece;
			team.teamPieces.remove(piece);
			//murder = true;
		} catch(Exception e){};

		// Simulate piece move
		copiedEndSquare.occPiece = copiedStartSquare.occPiece;
		Piece.kill(copiedStartSquare);

		copiedEndSquare.occPiece.row = copiedEndSquare.getRow();
		copiedEndSquare.occPiece.col = copiedEndSquare.getColumn();

	
		boolean kingInCheck = copyBoard.isYourKingInCheck(copiedEndSquare.occPiece.team);

		// Return piece to original position
		// copiedStartSquare.occPiece = copiedEndSquare.occPiece;
		// copiedEndSquare.occPiece = holder;

		// startSquare.occPiece.row = startSquare.getRow();
		// startSquare.occPiece.col = startSquare.getColumn();

		// if (murder){
		// 	endSquare.occPiece.team.addToTeam(endSquare.occPiece);
		// }
		// Return true if move puts or leaves own King in check
		return kingInCheck;

	} // keepYourKingSafe ()

	//========================================================================================

	// Return the team of a piece
	public Team getTeam(Piece piece) {

		return piece.team;

	} // getTeam ()

	//========================================================================================

	// Remove a piece from its square
	public static void kill(Square square) {

		square.occPiece = null;

	} // kill ()

	//========================================================================================
	
	// Get filename for piece image
	public String getImage() {

		return "";

	} // getImage ()

	//========================================================================================

	// Return piece type
	public String getType() {

		return null;

	} // getType ()

	//========================================================================================

	// Copy piece's team and location
	public Piece copyPiece(Team a) {

		return null;

	} // copyPiece ()

//========================================================================================
} // class Piece
//========================================================================================
