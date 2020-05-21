//========================================================================================
// class Team
//========================================================================================

// IMPORTS
import java.util.ArrayList;
import java.awt.Color;

//========================================================================================

public class Team {

	// MEMBER VARIABLES
	
	// Team color
	public Color color;

	// Team's pieces
	public ArrayList<Piece> teamPieces; 

	//========================================================================================

	// CONSTRUCTOR
	// Assigns color to each team, creates arraylist of team's pieces
	public Team(String teamColor) {
		
		if (teamColor == "WHITE") {
			color = Color.WHITE;
		}

		if (teamColor == "BLACK") {
			color = Color.BLACK;
		}

		teamPieces = new ArrayList<Piece>();

	} // Team ()

	//========================================================================================

	//Initialize team by adding its pieces to an array
	public void initializeTeam(Board board) {

		for (int row = 0; row<8;row++) {
        	for (int column = 0; column<8; column++) {
        		try {
        			if (board.grid[row][column].occPiece.team.color == color) {
        				this.addToTeam(board.grid[row][column].occPiece);
        			}
        		} catch(Exception NullPointerException){};
        	}
        }

	} // initializeTeam()

	//========================================================================================

	// Add piece to team pieces arraylist
	public void addToTeam(Piece piece) {

		teamPieces.add(piece);

	} // addToTeam ()
	
	//========================================================================================

	// Add arraylist of pieeces to team pieces
	public void addToTeam(ArrayList<Piece> a) {

		teamPieces.addAll(a);

	} // addToTeam ()

	//========================================================================================

	//Return array of a team's pieces
	public ArrayList<Piece> getTeamPieces() {
		
		return teamPieces;

	} // getTeamPieces ()

	//========================================================================================

	// Determine if a piece is in a team
	public boolean pieceFoundInTeam(Piece p) {

		for (Piece piece : this.teamPieces) {
			if ((p.getType().equals(piece.getType())) && (p.team.color == piece.team.color) 
				&& (p.row == piece.row) && (p.col == piece.col)) {
				return true;
			}
		}

		return false;

	} // pieceFoundInTeam ()

	//========================================================================================

	// Remove any pieces from team once they have been removed from team
	public void updateTeam() {

		int i = 0;
		for (Piece piece : teamPieces) {
			if (piece == null) {
				teamPieces.remove(i);
			}
			i++;
		}

	} // updateTeam ()

	//========================================================================================

	// Return arraylist of all pawns on a team
	public ArrayList<Pawn> getPawn() {

		ArrayList<Pawn> pawns = new ArrayList<Pawn>();
		
		for (Piece c : teamPieces) {
			if (c.getType() == "PAWN") {
				pawns.add((Pawn) c);
			}
		}

		return pawns;

	} // getPawn ()

	//========================================================================================

	// Return a team's king piece
	public King getKing() {

		this.updateTeam();
		for (Piece c : teamPieces) {
			if (c.getType() == "KING") return (King)c;
		}

		return null;

	} // getKing()

	//========================================================================================

	// Run when castling on the white team
	public Team castleWhite(Board board, int side) {

		// Create arraylist of all squares threatened by the opposing (black) team
		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		for (Piece p : board.black.getTeamPieces()) {
			for (Square s : p.getThreatenedSquares(board.getSquare(p.row, p.col), board)) {
				threatenedSquares.add(s);
			}
		}

		boolean cantPass = false;

		// Castling on the Queen's side
		if (side == 0) {

			// If the King and Rook have not yet been moved
			if ((board.getSquare(7, 0).occPiece != null) && (board.getSquare(7, 4).occPiece != null)) {
                if ((board.getSquare(7, 0).occPiece.numMoves == 0) && (board.getSquare(7, 4).occPiece.numMoves == 0)) {
                    
                    // If a piece is located between the King and Rook
                    // or if any squares inbetween the King and Rook are threatened by the opposing (black) team
                    for (int i = 1; i <= 3; i++) {
                        if ((board.getSquare(7, i).occPiece != null) || threatenedSquares.contains(board.getSquare(7, i))) {
                            cantPass = true;
                        }
                    }

                    // If castling is a valid move, castle
                    if (!cantPass) {
                        board.getSquare(7, 3).occPiece = board.getSquare(7, 0).occPiece; 
                        Piece.kill(board.getSquare(7, 0));
                        board.getSquare(7, 3).occPiece.row = 7;
                        board.getSquare(7, 3).occPiece.col = 3;

                        board.getSquare(7, 2).occPiece = board.getSquare(7, 4).occPiece; 
                        Piece.kill(board.getSquare(7, 4)); 
                        board.getSquare(7, 2).occPiece.row = 7;
                        board.getSquare(7, 2).occPiece.col = 2;                        

                        return board.black;

                    }
                }
            }
		}

		// Castling on King's side
		if (side == 1) { 

			// If the King and Rook have not yet been moved
			if ((board.getSquare(7, 7).occPiece != null) && (board.getSquare(7, 4).occPiece != null)) {
                if ((board.getSquare(7, 7).occPiece.numMoves == 0) && (board.getSquare(7, 4).occPiece.numMoves == 0)) {

                	// If a piece is located between the King and Rook
                    // or if any squares inbetween the King and Rook are threatened by the opposing (black) team
                    for (int i = 1; i < 3; i++){
                        if ((board.getSquare(7, (4 + i)).occPiece != null) || threatenedSquares.contains(board.getSquare(7, (4 + i)))) {
                            cantPass = true;
                        }
                    }

                    // If castling is a valid move, castle
                    if (!cantPass){
                        board.getSquare(7, 5).occPiece = board.getSquare(7, 7).occPiece; 
                        Piece.kill(board.getSquare(7, 7)); 
                        board.getSquare(7, 5).occPiece.row = 7;
                        board.getSquare(7, 5).occPiece.col = 5;

                        board.getSquare(7, 6).occPiece = board.getSquare(7, 4).occPiece; 
                        Piece.kill(board.getSquare(7, 4)); 
                        board.getSquare(7, 6).occPiece.row = 7;
                        board.getSquare(7, 6).occPiece.col = 6;

                        return board.black;

                    }
                }
            }
		}
		return board.white;

	} // castleWhite ()

	//========================================================================================

	public Team castleBlack(Board board, int side) {
		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		for (Piece p : board.white.getTeamPieces()) {
			for (Square s : p.getThreatenedSquares(board.getSquare(p.row, p.col), board)) {
				threatenedSquares.add(s);
			}
		}
		boolean cantPass = false;

		// Castling on Queen's side
		if (side == 0) { 

			// If the King and Rook have not yet been moved
			if ((board.getSquare(0, 0).occPiece != null) && (board.getSquare(0, 4).occPiece != null)) {
                if ((board.getSquare(0, 0).occPiece.numMoves == 0) && (board.getSquare(0, 4).occPiece.numMoves == 0)) {
                    
                    // If a piece is located between the King and Rook
                    // or if any squares inbetween the King and Rook are threatened by the opposing (white) team
                    for (int i = 1; i <= 3; i++){
                        if ((board.getSquare(0, i).occPiece != null) || threatenedSquares.contains(board.getSquare(0, i))) {
                            cantPass = true;
                        }
                    }

                    // If castling is a valid move, castle
                    if (!cantPass) {
                        board.getSquare(0, 3).occPiece = board.getSquare(0, 0).occPiece; 
                        Piece.kill(board.getSquare(0, 0)); 
                        board.getSquare(0, 3).occPiece.row = 0;
                        board.getSquare(0, 3).occPiece.col = 3;

                        board.getSquare(0, 2).occPiece = board.getSquare(0, 4).occPiece; 
                        Piece.kill(board.getSquare(0, 4)); 
                        board.getSquare(0, 2).occPiece.row = 0;
                        board.getSquare(0, 2).occPiece.col = 2;

                        return board.white;
                    }
                }
            }
		}

		// Castling on King's side
		if (side == 1) {

			// If the King and Rook have not yet been moved
			if ((board.getSquare(0, 7).occPiece != null) && (board.getSquare(0, 4).occPiece != null)) {
                if ((board.getSquare(0, 7).occPiece.numMoves == 0) && (board.getSquare(0, 4).occPiece.numMoves == 0)) {
                    
                    // If a piece is located between the King and Rook
                    // or if any squares inbetween the King and Rook are threatened by the opposing (white) team
                    for (int i = 1; i < 3; i++){
                        if ((board.getSquare(0, (4 + i)).occPiece != null) || threatenedSquares.contains(board.getSquare(0, (4 + i)))) {
                            cantPass = true;
                        }
                    }

                    // If castling is a valid move, castle
                    if (!cantPass) {
                        board.getSquare(0, 5).occPiece = board.getSquare(0, 7).occPiece; 
                        Piece.kill(board.getSquare(0, 7)); 
                        board.getSquare(0, 5).occPiece.row = 0;
                        board.getSquare(0, 5).occPiece.col = 5;

                        board.getSquare(0, 6).occPiece = board.getSquare(0, 4).occPiece; 
                        Piece.kill(board.getSquare(0, 4));
                        board.getSquare(0, 6).occPiece.row = 0;
                        board.getSquare(0, 6).occPiece.col = 6;

                        return board.white;
                    }
                }
            }
		}
		return board.black;

	} // castleBlack ()
	
//========================================================================================
} // class Team
//========================================================================================

