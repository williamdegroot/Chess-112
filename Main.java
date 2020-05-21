
//========================================================================================
// class Main
//========================================================================================

// IMPORTS
import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

//========================================================================================

public class Main extends JPanel implements KeyListener, MouseListener, WindowListener {
  
    // MEMBER VARIABLES

    // Window width
    public static final int WIDTH = 800;

    // Window height
    public static final int HEIGHT = 800;

    // Window
    public static JFrame frame;

    // Team colors
    public Team white;
    public Team black;

    // Game board - contains entire chess game
    public Board _board;

    // CPU  
    public static boolean aiGame = false; // Set to true if AI opponent chosen
    EasyComputer easyComputer;
    MediumComputer mediumComputer;
    HardComputer hardComputer;
    public static boolean aiEASYGame = false;
    public static boolean aiMEDIUMGame = false;
    public static boolean aiHARDGame = false;

    // Variables for clicking events
    public final Square offBoard = new Square(null, -1, -1);
    public Square clicked;
    public Square first;
    public int click;

    // Team currently moving a piece 
    public Team turn;

    // End of Game
    public static Team winner;
    public static boolean staleMate;

    // Three-fold repetition variables
    ArrayList<Board> positionHolder = new ArrayList<Board>();
    public static boolean threeFoldRepetition;
    public boolean firstBoard;
   
   //========================================================================================

    // CONSTRUCTOR
    public Main() {
        //creates white & black teams, creates board, & initilizes teams
        
        // Set window dimension
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        //Implement mouse and key listener
        addKeyListener(this);
        addMouseListener(this);

        //Create white and black teams
        white = new Team("WHITE");
        black = new Team("BLACK");

        //Create game board
        _board = new Board(white, black);

        //Initialize teams
        white.initializeTeam(_board); // Assigns white pieces to white team
        black.initializeTeam(_board); // Assigns black pieces to black team

        // Initialize AI games if AI opponent is chosen     
        if (aiEASYGame){
            easyComputer = new EasyComputer(black, _board);
        }
        if (aiMEDIUMGame){
            mediumComputer = new MediumComputer(black, _board);
        }
        if (aiHARDGame){
        hardComputer = new HardComputer(black, _board);
        }
        
        // White team moves first
        turn = white; 

        // Track number of clicks
        click = 1;

        // First square clicked during a player's turn
        first = new Square(null, -1, -1);

        // Track any square that is clicked
        clicked = new Square(null, -1, -1);

        // Trigger end of game when updated
        winner = null;
        staleMate = false;

        // Add first board to list of boards for three-fold repetition
        firstBoard = true;
     
    } // Main ()

    //========================================================================================

    // The program's entry point.
    // Create the game & pop-up frame.
    public static void main(String[] args) {

        frame = new JFrame("Chess");
        //SOURCE
        //https://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

        // Welcome window
        JOptionPane.showMessageDialog(frame, "WELCOME TO CHESS", "Chess", 
            JOptionPane.INFORMATION_MESSAGE);

        //Source:
        //https://stackoverflow.com/questions/15602170/printing-out-txt-file-with-joptionpane
        String input = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader("Instructions.txt"));
            String line = null;
            while ((line = reader.readLine()) != null){
                input += line + "\n";
            } 
            reader.close();
        } catch(IOException e){};
       
        Object[] begin = {"No, continue onto game", "Yes, view instructions"};
        int instructions = JOptionPane.showOptionDialog(frame, 
            "Would you like view the game instructions?", "Chess", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, begin, begin[1]);
         if (instructions == JOptionPane.NO_OPTION){
            JOptionPane.showMessageDialog(frame, input, "Instructions",
                JOptionPane.INFORMATION_MESSAGE);
        }
        if (instructions == JOptionPane.YES_OPTION){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
        // Choose opponent window
        int option = JOptionPane.showConfirmDialog(frame, 
            "Would you like to play against our resident AI?", "Pick Your Opponent", 
            JOptionPane.YES_NO_OPTION);
        
        // If opponent is human, start game
        if (option == JOptionPane.NO_OPTION) {
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }

        // If opponent is CPU, choose level of difficulty, then start game
        if (option == JOptionPane.YES_OPTION) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            aiGame = true;

            Object[] difficulty = {"Hard", "Medium", "Easy"};
            int aiDifficulty = JOptionPane.showOptionDialog(frame, 
                "Would you like to play against an easy, medium or hard CPU?", 
                "CPU Difficulty", JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE, null, difficulty, difficulty[2]);

            // If opponent is hard CPU
            if (aiDifficulty == JOptionPane.YES_OPTION) {
                aiHARDGame = true;
            }

            // If opponent is medium CPU
            if (aiDifficulty == JOptionPane.NO_OPTION) {
                aiMEDIUMGame = true;
            }

            // If opponent is easy CPU
            if (aiDifficulty == JOptionPane.CANCEL_OPTION) {
                aiEASYGame = true;
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
        // Open chess game window and begin game
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Main());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Exit game warning if user attempts to close window
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if ((winner == null) && (!staleMate)) {
                    int option = JOptionPane.showConfirmDialog(frame, "Game will not save." + 
                        "\nAre you sure you want to quit?", "Exit Game", 
                        JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }

                    if (option == JOptionPane.NO_OPTION) {
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }

                } else {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });

    } // main ()

    //========================================================================================

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        try {
            _board.drawBoard(g, clicked);
        } catch (IOException e) {
            e.printStackTrace();
        }

    } // paintComponent ()

    //========================================================================================

    // Check if a team is in checkmate
    // If yes, ends game
    public void checkMate(Team team) {

        // Display winning team
        if (team == white) {
            JOptionPane.showMessageDialog(frame, "Checkmate" + "\nWhite Wins!", "WINNER", 
                JOptionPane.INFORMATION_MESSAGE);
        }

        if (team == black) {
            JOptionPane.showMessageDialog(frame, "Checkmate" + "\nBlack Wins!", "WINNER", 
                JOptionPane.INFORMATION_MESSAGE);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } // checkMate ()

    //========================================================================================

    // Check if stalemate has occured
    public void staleMate() {

        JOptionPane.showMessageDialog(frame, "Stalemate", "End of Game", 
            JOptionPane.INFORMATION_MESSAGE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } // staleMate ()

    //========================================================================================

    // Pauses game if the same game board occurs three times
    public void setThreeFoldRepetition() {

        JOptionPane.showMessageDialog(frame, "Three Fold Repetition. Press OKAY if you choose to continue.", "Three Fold Repetition",
                JOptionPane.INFORMATION_MESSAGE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } // setThreeFoldRepetition ()

    //========================================================================================

    @Override
    public void mouseReleased(MouseEvent e) {
        
        // Set to null for beginning of player's move
        Square detected = new Square(null, -1, -1); 

        //Determine square where first click is located
        try{
            detected = _board.grid[e.getY()/100][e.getX()/100];
        } catch (ArrayIndexOutOfBoundsException a){};
        
        // Run after first click during player's turn
        if (click == 1) {

            // Add opening game board to list for three-fold repetition
            if (firstBoard){
                positionHolder.add(_board.copyBoard());
                firstBoard = false;
            }

            // Cancel promotions for un-updated pawns
            if (turn.color == Color.WHITE) {
                for (Pawn p : white.getPawn()) {
                    p.readyForPromotion = false;
                }

            } else {
                for (Pawn p : black.getPawn()) {
                    p.readyForPromotion = false;
                }
             }

            // Determine if first click is legal
            if ((detected.occPiece != null) && (detected.occPiece.team == turn)) {

                // Allow second click if first click is a piece on player's team
                first = detected;
                click = 2;
                
                // Highlight clicked square
                clicked = first;

            // Invalid first click, reset for player to try again
            } else {
                clicked = offBoard; 
            }

            repaint();

        // Run after second click during player's turn
        } else if (click == 2) {

            // If player clicks an empty square, move piece
            if (detected.occPiece == null) {
                if (first.occPiece.checkMove(first, detected, _board)) {
                    click = first.occPiece.move(first, detected, _board, false);

                    // Add updated board to list of boards to determine three-fold repetition
                    positionHolder.add(_board.copyBoard());
                    threeFoldRepetition = _board.threeRepetitions(positionHolder);

                   // In AI game, after player move, determnine AI move
                    if (aiGame) {

                        // Only play if opponent's pawn is not waiting to be promoted
                        if (!detected.occPiece.getType().equals("PAWN")) {
                            if (aiEASYGame) {
                                 turn = easyComputer.easyComputerMove();
                            }
                            if (aiMEDIUMGame) {
                                 turn = mediumComputer.mediumComputerMove(_board);
                            }
                            if (aiHARDGame) {
                                  turn = hardComputer.hardComputerMove(_board);
                            }

                            // Add updated board to list of boards to determine three-fold repetition
                            positionHolder.add(_board.copyBoard());
                            threeFoldRepetition = _board.threeRepetitions(positionHolder);

                        } else if (detected.occPiece.row != 7 && detected.occPiece.row != 0) {
                            if (aiEASYGame) {
                             turn = easyComputer.easyComputerMove();
                            }
                            if (aiMEDIUMGame) {
                                 turn = mediumComputer.mediumComputerMove(_board);
                            }
                             if (aiHARDGame) {
                                 turn = hardComputer.hardComputerMove(_board);
                            }

                            // Add updated board to list of boards to determine three-fold repetition
                            positionHolder.add(_board.copyBoard());
                            threeFoldRepetition = _board.threeRepetitions(positionHolder);

                        }
                    }

                // Invalid move, reset for player to try again
                } else {
                    click = 2;
                }

                // Turn off highlight
                clicked = offBoard;

            } else {

                // If player clicks a square occupied by opponent's piece, take piece and move to square
                if (detected.occPiece.team != first.occPiece.team) { 
                    if (first.occPiece.checkMove(first, detected, _board)) {
                        click = first.occPiece.move(first, detected, _board, false);

                        // Add updated board to list of boards to determine three-fold repetition
                        positionHolder.add(_board.copyBoard());
                        threeFoldRepetition = _board.threeRepetitions(positionHolder);

                        // In AI game, after player move, determnine AI move
                        if (aiGame) {

                        // Only play if opponent's pawn is not waiting to be promoted
                            if (!detected.occPiece.getType().equals("PAWN")) {
                                if (aiEASYGame) {
                                    turn = easyComputer.easyComputerMove();
                                }
                                if (aiMEDIUMGame) {

                                     turn = mediumComputer.mediumComputerMove(_board);
                                }
                                 if (aiHARDGame) {
                                      turn = hardComputer.hardComputerMove(_board);
                                 }

                                // Add updated board to list of boards to determine three-fold repetition
                                positionHolder.add(_board.copyBoard());
                                threeFoldRepetition = _board.threeRepetitions(positionHolder);


                            } else if (detected.occPiece.row != 7 && detected.occPiece.row != 0) {
                                if (aiEASYGame) {
                                    turn = easyComputer.easyComputerMove();
                                }
                                if (aiMEDIUMGame) {
                                    turn = mediumComputer.mediumComputerMove(_board);
                                }
                                if (aiHARDGame) {
                                     turn = hardComputer.hardComputerMove(_board);
                                }

                                // Add updated board to list of boards to determine three-fold repetition
                                positionHolder.add(_board.copyBoard());
                                threeFoldRepetition = _board.threeRepetitions(positionHolder);

                            }
                        }

                        // Update teams by removing any pieces that have been taken from the board
                        white.updateTeam();
                        black.updateTeam();

                    // Invalid move, reset for player to try again
                    } else {
                        click = 2;
                    }

                    // Turn off highlight
                    clicked = offBoard;
                
                //Invalid second click, reset for player to try again
                } else { 
                    first = detected;
                    clicked = detected;
                }
            }

            repaint();

           
            if (!aiGame) { 
                // Switch turns 
                if (click == 1) {
                    if (turn == white) turn = black;
                    else turn = white;
                // Declare checkmate
                } else if (click == 10) {
                    winner = turn;
                    checkMate(winner);
                //Declare stalemate
                } else if (click == -10) {
                    staleMate = true;
                    staleMate();
                }
            }
            if (aiGame){
                if (turn.color == Color.WHITE){
                    if (_board.checkCheckMate(_board.white)){
                        winner = _board.black;
                        checkMate(winner);
                    }
                    if (_board.checkCheckMate(_board.black)){
                        winner = _board.white;
                        checkMate(winner);
                    }
                    if (_board.checkStaleMate(_board.white)){
                        staleMate();
                    }
                    if (_board.checkStaleMate(_board.black)){
                        staleMate();
                    }
                }
            }
            // Check if three-fold repetition has occured
            if (threeFoldRepetition) setThreeFoldRepetition();
        }

    } //mouseReleased ()

    //========================================================================================

    public void addNotify() {

        super.addNotify();
        requestFocus();

    } // addNotify ()

    //========================================================================================

    @Override
    public void keyReleased(KeyEvent e) {

        char c = e.getKeyChar();
        Team current;
        current = turn;
        if (!aiGame){
            if (turn.color == Color.WHITE) current = black;
            else current = white;
        }

        // PROMOTE PAWN
        for (Pawn p : current.getPawn()) {
            // If pawn reaches opposite side of board
            if (p.readyForPromotion) {

                // Promote to Bishop
                if (c == 'b') {
                    Pawn.promote(p, _board, current, 'b');
                    p.readyForPromotion = false;

                // Promote to Knight
                } else if (c == 'k') {
                    Pawn.promote(p, _board, current, 'k');
                     p.readyForPromotion = false;

                // Promote to Rook
                } else if (c == 'r') {
                    Pawn.promote(p, _board, current, 'r');
                     p.readyForPromotion = false;

                // Promote to Queen
                } else {
                    Pawn.promote(p, _board, current, 'q');
                    p.readyForPromotion = false;
                }

                // Determine if promotion caused checkmate
                if (_board.checkCheckMate(current)) {
                    winner = current;
                    checkMate(winner);
                }

                // Determine if promotion caused stalemate
                if (_board.checkStaleMate(current)) {
                    staleMate = true;
                    staleMate();
                }

                // Continue game
                 if(aiGame) {
                    if (aiEASYGame) {
                         turn = easyComputer.easyComputerMove();
                    }
                    if (aiMEDIUMGame) {
                         turn = mediumComputer.mediumComputerMove(_board);
                    }
                    if (aiHARDGame) {
                         turn = hardComputer.hardComputerMove(_board);
                    }
                 }


            }

            repaint();
        }

        //CASTLING
        if (!_board.isYourKingInCheck(turn)) { //Cannot castle when King is in check
            if (turn.color == Color.WHITE) {

                // Castle Queen-side
                if (c == 'a') {                        
                    turn = turn.castleWhite(_board, 0);
                    if (aiGame){
                        if (turn == black){
                            if (aiEASYGame) {
                                turn = easyComputer.easyComputerMove();
                            }
                            if (aiMEDIUMGame) {
                                 turn = mediumComputer.mediumComputerMove(_board);
                            }
                            if (aiHARDGame) {
                                 turn = hardComputer.hardComputerMove(_board);
                             }
                        }
                    }
                }

                // Castle King-side
                if (c == 'd') {
                    if (aiGame){
                        turn = turn.castleWhite(_board, 1);
                        if (turn == black){
                            if (aiEASYGame) {
                                turn = easyComputer.easyComputerMove();
                            }
                            if (aiMEDIUMGame) {
                                 turn = mediumComputer.mediumComputerMove(_board);
                            }
                            if (aiHARDGame) {
                                 turn = hardComputer.hardComputerMove(_board);
                            }
                        }
                    }
                }

            } else {

                //Castle Queen-side
                if (c == 'a') {
                    turn = turn.castleBlack(_board, 0);
                }

                //Castle King-side
                if (c == 'd') {
                    turn = turn.castleBlack(_board, 1);
                }

            }

            repaint();
            click = 1;
        }
    } // keyReleased ()

    //========================================================================================

    public void windowDeactivated(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}


//========================================================================================
} // class Main
//========================================================================================

