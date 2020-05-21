//========================================================================================
// class Square
//========================================================================================

// IMPORTS
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

//========================================================================================

public class Square {

	// MEMBER VARIABLES
	
	// Dimensions of square
	private static final int HEIGHT = 100;
	private static final int WIDTH = 100;

	// Piece currently occupying the square
	public Piece occPiece;

	// Location of square on grid
	private int row;
	private int column;
	
	//========================================================================================

	// CONSTRUCTOR
	// Each square keeps track of whether it is occupied by a piece
	// Each square has a row and column
	public Square(Piece initial, int sqRow, int sqCol) {
		
		// Occupying piece
		occPiece = initial;

		// Square location
		row = sqRow;
		column = sqCol;

	} // Square ()

	//========================================================================================

	// Draws individual squares
	public void drawSquare(Graphics g, boolean highLighted) throws IOException {
		
		int leftX = column * 100;
		int topY = row * 100;
		int size = 100;

		// Draws each square either pink or light gray (checkerboard)
		// When a square is selected, set color as red

		int check = row + column;
		if (check % 2 == 0) {
			g.setColor(Color.pink);
			if (highLighted) g.setColor(Color.red);

		} else {
			g.setColor(Color.LIGHT_GRAY);
			if (highLighted) g.setColor(Color.red);
		}

		g.fillRect(leftX,topY,size,size);

		//Read files to input piece icons
		if (occPiece != null) {
			BufferedImage myPicture = ImageIO.read(new File(occPiece.getImage()));
			g.drawImage(myPicture,column*100 + 12 ,row*100 + 15,70,70,null);
		}

	} // drawSquare ()

	//========================================================================================

	// Return the row of a square
	public int getRow() {
		
		return row;

	} //getRow ()

	//========================================================================================

	// Return the column of a square
	public int getColumn() { 
		
		return column;

	} //getColumn ()

	//========================================================================================

	// Determine if two squares are the same 
	public boolean equals(Square square){
		
		if ((this.row == square.getRow()) && (this.column == square.getColumn())) {
			return true;
		
		} else {
			return false;
		}

	} // equals ()

	//========================================================================================

	// Print coordinates of a square
	public String printSquare(){
		
		return (row + " " + column);

	} // printSquare ()

//========================================================================================
} // class Square
//========================================================================================

