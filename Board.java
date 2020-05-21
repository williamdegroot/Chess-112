//========================================================================================
// class Board
//========================================================================================

// IMPORTS
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

//========================================================================================

public class Board {

	// MEMBER VARIABLES

	// Grid of squares and any occupying pieces
	public Square grid[][];

	// Teams
	Team white;
	Team black;

	//========================================================================================

	// CONSTRUCTOR
	// Creates game board
	public Board(Team a, Team b) {
		
		// Initialize grid of squares
		grid = new Square[8][8]; 

		// Teams
		white = a;
		black = b;

		// Initialize Board
		this.initializeBoard();

		// Board for testing
		//this.testDifferentBoards();

	} // Board ()

	//========================================================================================

	public void initializeBoard() {
		
		// Create blank squares and add them to the grid
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				grid[row][column] = new Square(null, row, column);
			}
		}

		// Create black team pieces
		grid[0][0].occPiece = new Rook(black, 0,0);
		grid[0][1].occPiece = new Knight(black,0,1);
		grid[0][2].occPiece = new Bishop(black,0,2);
		grid[0][3].occPiece = new Queen(black,0,3);
		grid[0][4].occPiece = new King(black,0,4);
		grid[0][5].occPiece = new Bishop(black,0,5);
		grid[0][6].occPiece = new Knight(black,0,6);
		grid[0][7].occPiece = new Rook(black,0,7);

		for(int i = 0; i < 8; i++) {
			grid[1][i].occPiece = new Pawn(black,1,i);
		}

		// Create white team pieces
		for(int i = 0; i < 8; i++) {
			grid[6][i].occPiece = new Pawn(white,6,i);
		}

		grid[7][0].occPiece = new Rook(white,7,0);
		grid[7][1].occPiece = new Knight(white,7,1);
		grid[7][2].occPiece = new Bishop(white,7,2);
		grid[7][3].occPiece = new Queen(white,7,3);
		grid[7][4].occPiece = new King(white,7,4);
		grid[7][5].occPiece = new Bishop(white,7,5);
		grid[7][6].occPiece = new Knight(white,7,6);
		grid[7][7].occPiece = new Rook(white,7,7);

	} // initializeBoard()

	//========================================================================================

	// Draw board and highlight selected square
	public void drawBoard(Graphics g, Square highLighted) throws IOException {

		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				Square square = grid[row][column];
				if (square.equals(highLighted)) square.drawSquare(g, true);
				else square.drawSquare(g,false);

			}
		}	
	} // drawBoard ()

	//========================================================================================
	
	// Returns grid
	public Square[][] getGrid() { 

		return grid;

	} // getGrid ()

	//========================================================================================

	// Return square object at given row and column
	public Square getSquare(int row, int column) {

		return grid[row][column];

	} // getSquare ()
	
	//========================================================================================

	// Create board to test scenarios like checkmate
	public void testDifferentBoards() {

		// Create blank squares and add them to the grid
		for (int row = 0; row < 8; row++) {
			for (int column = 0; column < 8; column++) {
				grid[row][column] = new Square(null, row, column);
			}
		}

		// Create team pieces
		this.getSquare(1, 3).occPiece = new King(white, 1, 3);
		this.getSquare(2, 5).occPiece = new Bishop(white, 2, 5);

		this.getSquare(6, 6).occPiece = new King(black, 6, 6);
		//this.getSquare(6, 1).occPiece = new Pawn(black, 6, 1);
		//this.getSquare(6, 1).occPiece.numMoves = 1;
		this.getSquare(7, 2).occPiece = new Queen(white, 7, 2);
		this.getSquare(2, 1).occPiece = new Pawn(white, 2, 1);

	} // testDifferentBoards()

	//========================================================================================

	// Determine if a piece movement puts or leaves own King in check
	public boolean isYourKingInCheck(Team team) {

		ArrayList<Square> threatenedSquares = new ArrayList<Square>();
		Team other;
		
		if (team == white) {
			other = black;

		} else {
			other = white;
		}

		// Determine if team's King is in check by evaluating 
		// every square threatened by the opposinng team
		for (Piece p : other.getTeamPieces()) {
			threatenedSquares.addAll(p.getThreatenedSquares(this.getSquare(p.row, p.col), 
				this));
		}
		
		// Get square occupied by King
		Square kingSquare = this.getSquare(team.getKing().row, team.getKing().col);

		// Return true if the square occupied by team's king is threatened by other team
		for (Square s : threatenedSquares) {
			if (s.equals(kingSquare)) {
				return true;
			}
		}
		
		//otherwise return false
		return false;

	} // isYourKingInCheck ()

	//========================================================================================

	// Determine if a team has achieved checkmate - declare winner
	public boolean checkCheckMate(Team team) {

		// Determine if King is in check
		boolean nowInCheck = this.isYourKingInCheck(team);

		ArrayList<Square> possibleMoves = new ArrayList<Square>();
		Piece sample = new Piece(null, -1, -1);

		// Determine if team has any moves to save King
		// Return false if move is found
		for (Piece p : team.getTeamPieces()) {
			possibleMoves = p.getPossibleMoves(this.getSquare(p.row, p.col),this);
			if (possibleMoves.size() > 0) {
				return false;
			}
		}

		// If King is in check and no possible moves are found, return winning team
		return nowInCheck;

	} // checkCheckMate ()

	//========================================================================================
	
	// Determine if board is in stalemate
	public boolean checkStaleMate(Team team) {
	
		// Determine if King is in check
		boolean nowInCheck = this.isYourKingInCheck(team);

		ArrayList<Square> possibleMoves = new ArrayList<Square>();

		// Determine if team has any moves to save King
		// Return false if move is found 
		for (Piece p : team.getTeamPieces()) {
			possibleMoves = p.getPossibleMoves(this.getSquare(p.row, p.col),this);
			if (possibleMoves.size() > 0) {
				return false;
			}
		}

		// Return false if King is in check
		if (nowInCheck){
			return false;
		}
		
		// If team has no moves and King is *not* in check, declare stalemate
		return true;

	} // checkStaleMate ()

	//========================================================================================

	// Check if arraylist contains three repeated boards
	public Boolean threeRepetitions(ArrayList<Board> boards) {
		int counter = 0;

		// Compares previous boards to current board, if boards are the same, counter is added
		for (int index = 0; index<boards.size();index++){
			counter = 0;
			for (int k = 0;k<boards.size();k++){

				if (boards.get(index).equals(boards.get(k)) && (index != k)) counter ++;
			}
		}

		// Returns true if three repeated boards have occured
		return counter >= 2;

	} // threeRepetitions ()

	//========================================================================================

	// Determine if two boards are the same
	public Boolean equals(Board board) {

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col<8;col++) {

				if (board.getGrid()[row][col].occPiece != null) {
					if (grid[row][col].occPiece == null) {
						return false;
					}

					if (!grid[row][col].occPiece.getType().equals(board.getGrid()[row][col].occPiece.getType())) {
						return false;
					}

					if (grid[row][col].occPiece.team.color != grid[row][col].occPiece.team.color) {
						return false;
					}
				
				} else {
					if (grid[row][col].occPiece != null) {
						return false;
					}
				}
			}
		}
		return true;

	} // equals ()



	//========================================================================================

	// Copy board in order to simulate piece movements without disrupting game board
	public Board copyBoard() {

		// Initialize new teams and board
		Team copyWhite = new Team("WHITE");
		Team copyBlack = new Team("BLACK");
		Board copyBoard = new Board(copyWhite, copyBlack);
		Square[][] copyGrid = new Square[8][8];

		// Copy pieces and teams from game grid to a copied grid
		for (int row=0;row<8;row++) {
			for (int col=0;col<8;col++) {
				try {
					if (grid[row][col].occPiece == null) {
						copyGrid[row][col] = new Square(null,row,col);
					}

					else if (grid[row][col].occPiece.team.color == Color.WHITE) {
						copyGrid[row][col] = new Square(grid[row][col].occPiece.copyPiece(copyWhite), 
							row, col);
						copyWhite.addToTeam(copyGrid[row][col].occPiece);
					
					} else {
						copyGrid[row][col] = new Square(grid[row][col].occPiece.copyPiece(copyBlack), 
							row, col);
						copyBlack.addToTeam(copyGrid[row][col].occPiece);
					}
				}
				catch(Exception e){
				}
			}
		}

		// Transfer new copied grid to new copied board
		copyBoard.grid = copyGrid;
	
		return copyBoard;

	} // copyBoard ()

	//========================================================================================

	// Create an arraylist of boards for every possible move by one team
	public ArrayList<Board> successorBoards(Team input) {

		ArrayList<Board> successorBoardsList = new ArrayList<Board>();
		Board copyBoard = null;
		Board originalBoard = this.copyBoard();
		
		Team team;
		if (input.color == Color.WHITE) {
			team = originalBoard.white;
		}
		else {
			team = originalBoard.black;
		}

		// For each piece on the team
		for (Piece p : team.teamPieces) {
			
			// For each move possible for every piec eon the team
			for (Square s : p.getPossibleMoves(originalBoard.getSquare(p.row, p.col), 
				originalBoard)) {
				copyBoard = originalBoard.copyBoard();

				// Create a board with each move and add it to the arraylist
				p.move(copyBoard.getSquare(p.row, p.col), copyBoard.getSquare(s.getRow(),
					 s.getColumn()), copyBoard, true);
				successorBoardsList.add(copyBoard);
			}
		}

		return successorBoardsList;

	} // successorBoards ()

	//========================================================================================

	// Calculate score for both teams 
	// Based on number of pieces on the board, thier positions and possible moves
	public double evaluateBoard(String team) {

		Team turn;
		double blackScore = 0;
		double blackMoves = 0;
		double whiteScore = 0;
		double whiteMoves = 0;

		// Add points for each piece on the team
		for (Piece p : this.black.teamPieces) {
			if (p.getType().equals("QUEEN")) {
				blackScore = blackScore + 9;
			}

			if (p.getType().equals("ROOK")) {
				blackScore = blackScore + 5;
			}

			if (p.getType().equals("KNIGHT")) {
				blackScore = blackScore + 3;
			}

			if (p.getType().equals("BISHOP")) {
				blackScore = blackScore + 3;
			}

			if (p.getType().equals("PAWN")) {
				blackScore = blackScore + 1;
			}

			// Add a point for each possible move
			for (Square s : p.getPossibleMoves(this.getSquare(p.row, p.col), this)) {
				blackMoves = blackMoves + 1;
			}



			blackScore = blackScore + (blackMoves * 0.05);
		}

		// Add points for each piece on the team
		for (Piece p : this.white.teamPieces) {
			if (p.getType().equals("QUEEN")) {
				whiteScore = whiteScore + 9;
			}

			if (p.getType().equals("ROOK")) {
				whiteScore = whiteScore + 5;
			}

			if (p.getType().equals("KNIGHT")) {
				whiteScore = whiteScore + 3;
			}

			if (p.getType().equals("BISHOP")) {
				whiteScore = whiteScore + 3;
			}

			if (p.getType().equals("PAWN")) {
				whiteScore = whiteScore + 1;
			}

			// Add a point for each possible move
			for (Square s : p.getPossibleMoves(this.getSquare(p.row, p.col), this)) {
				whiteMoves = whiteMoves + 1;
			}

			whiteScore = whiteScore + (whiteMoves * 0.05);
		}

		// Add points if able to kill an opponent's piece
		if (team == "WHITE") turn = this.black;
		else turn = this.white;
		
		// For pieces on the opposing team
		for (Piece p : turn.teamPieces) { 
			for (Square s : p.getPossibleMoves(this.getSquare(p.row, p.col), this)) { 
			
			// For each square the opposing team can attack
				if (s.occPiece != null) {

					// Add points if square is occupied by opponent's piece
					if (s.occPiece.team != p.team) {
						if (s.occPiece.getType().equals("QUEEN")){
							if (turn.color == Color.white) whiteScore += 9;
							else blackScore += 9;
						}

						if (s.occPiece.getType().equals("ROOK")) {
							if (turn.color == Color.white) whiteScore += 5;
							else blackScore += 5;
						}

						if (s.occPiece.getType().equals("KNIGHT")) {
							if (turn.color == Color.white) whiteScore += 3;
							else blackScore += 3;
						}

						if (s.occPiece.getType().equals("BISHOP")) {
							if (turn.color == Color.white) whiteScore += 3;
							else blackScore += 3;
						}

						if (s.occPiece.getType().equals("PAWN")) {
							if (turn.color == Color.white) whiteScore += 1;
							else blackScore += 1;
						}
					}
				}
			}
		}

		double score = blackScore - whiteScore;
		return score;

	} // evaluateBoard ()

//========================================================================================
} // class Board
//========================================================================================

