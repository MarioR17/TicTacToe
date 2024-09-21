import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Project of Jesus Rendon Quintanilla & Ethan Carmona
 * This program simulates a TicTacToe game with various different modes, including a single player mode,
 * two player mode, and a replay watching mode.
 */
public class TicTacToe {
    // Static variables for the TicTacToe class, effectively configuration options
    // Use these instead of hard-coding ' ', 'X', and 'O' as symbols for the game
    // In other words, changing one of these variables should change the program
    // to use that new symbol instead without breaking anything
    // Do NOT add additional static variables to the class!
    static char emptySpaceSymbol = ' ';
    static char playerOneSymbol = 'X';
    static char playerTwoSymbol = 'O';

    public static void main(String[] args) {
        // This is where the main menu system of the program will be written.
        // Hint: Since many of the game runner methods take in an array of player names,
        // you'll probably need to collect names here.

        Scanner in = new Scanner(System.in);
        // Initializing the choice that the user will input as a variable
        String modeChoice = "";
        // Initializing an ArrayList of 2d character arrays to store the game history whenever games are played
        ArrayList<char[][]> previousGame = new ArrayList<>();
        // Initializing the String array with the player names as an array with two elements that we will store later
        String[] playerNames = new String[2];

        // Quit this program if the user inputs the "Q" character
        while (!modeChoice.equals("Q")) {
            // Giving the user the menu options
            System.out.println();
            System.out.println("Welcome to game of Tic Tac Toe, choose one of the following options from below:");
            System.out.println();
            System.out.println("1. Single Player");
            System.out.println("2. Two Player");
            System.out.println("D. Display last match");
            System.out.println("Q. Quit");
            System.out.println("What do you want to do: ");

            // Take in their desired option as input
            modeChoice = in.next();

            // Initializing the player names before we assign them
            String player1Name;
            String player2Name;

            // Using a switch statement to do specific tasks that adhere to whatever mode the user selected
            switch (modeChoice) {
                // If the user selects mode 1 AKA the single player mode
                case "1" -> {
                    // Take in user input
                    System.out.println("Enter player 1 name: ");
                    player1Name = in.next();
                    // Assigning player 2 as computer because the computer will always be player 2 in single player
                    player2Name = "Computer";
                    // Assigning the name values
                    playerNames[0] = player1Name;
                    playerNames[1] = player2Name;
                    // Running the single player game and storing the game history in the previousGame ArrayList
                    previousGame = runOnePlayerGame(playerNames);
                }
                // If the user selects mode 2 AKA the two player mode
                case "2" -> {
                    // Take in user input
                    System.out.println("Enter player 1 name: ");
                    player1Name = in.next();
                    System.out.println("Enter player 2 name: ");
                    player2Name = in.next();
                    // Assign the name values
                    playerNames[0] = player1Name;
                    playerNames[1] = player2Name;
                    // Running the two player game and storing the game history in the previousGame ArrayList
                    previousGame = runTwoPlayerGame(playerNames);
                }
                // If the user selects mode D AKA replaying the last match played
                case "D" -> {
                    // If the previousGame ArrayList is open it means that there haven't been any games played yet
                    if (previousGame.isEmpty()) {
                        System.out.println("No match found.");
                    } else {
                        // Run the runGameHistory method if there has been a game played
                        runGameHistory(playerNames, previousGame);
                    }
                }
                // If the user selects "Q", then quit out of the program
                case "Q" -> System.out.println("Thanks for playing. Hope you had fun!");
                // If the user selects anything else besides the available options, tell them invalid and try again
                default -> System.out.println("Invalid input... Try again.");
            }
        }
    }

    /**
     * Given a state, return a String which is the textual representation of the tic-tac-toe board at that state.
     * @param state
     * @return board
     */
    private static String displayGameFromState(char[][] state) {
        // Hint: Make use of the newline character \n to get everything into one String
        // It would be best practice to do this with a loop, but since we hardcode the game to only use 3x3 boards
        // it's fine to do this without one.

        // Initializing a string with the board that we will add onto
        String board = "";

        // Iterate through the 2d character array to get all the symbols on the board
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                // Add each symbol in appearing order to the board
                board += " " + state[i][j] + " ";
                // Adds the vertical bars between each symbol in each row
                if (j < 2) {
                    board += "|";
                }
            }
            // Adds a newline character to board so that we can go to the next row of the board
            board += "\n";
            // Adds the horizontal bars between each of the rows onto the board
            if (i < 2) {
                board += "-----------\n";
            }
        }

        // Return the board string after it has been added with the proper characters to show the given board
        return board;
    }

    /**
     * Returns the state of a game that has just started.
     * This method is implemented for you. You can use it as an example of how to utilize the static class variables.
     * As you can see, you can use it just like any other variable, since it is instantiated and given a value already.
     * @return
     */
    private static char[][] getInitialGameState() {
        // Initializes an empty board with all empty space symbols because no moves have been made yet
        return new char[][]{{emptySpaceSymbol, emptySpaceSymbol, emptySpaceSymbol},
                            {emptySpaceSymbol, emptySpaceSymbol, emptySpaceSymbol},
                            {emptySpaceSymbol, emptySpaceSymbol, emptySpaceSymbol}};
    }

    /**
     * Given the player names, run the two-player game.
     * Return an ArrayList of game states of each turn -- in other words, the gameHistory
     * @param playerNames
     * @return gameHistory
     */
    private static ArrayList<char[][]> runTwoPlayerGame(String[] playerNames) {
        // Locally stores each of the players' names
        String playerOne = playerNames[0];
        String playerTwo = playerNames[1];
        // Initializing the current player as player one which will change if he is not the current player
        String currentPlayer = playerOne;
        // Initializing the current symbol as player one's symbol to match with the above variable. Will change.
        char currentSymbol = playerOneSymbol;
        // Setting the game history as an ArrayList of 2d character arrays, which we will return
        ArrayList<char[][]> gameHistory = new ArrayList<>();

        // Simulating a toin coss to determine which of the players will move first
        System.out.println("Tossing a coin to decide who goes first!!!");

        double coinToss = Math.random();

        if (coinToss < 0.5) {
            System.out.println(playerOne + " gets to go first.");
        // If player one does not go first, change the current player and symbol to player two because they are first
        } else {
            System.out.println(playerTwo + " gets to go first.");
            currentPlayer = playerTwo;
            currentSymbol = playerTwoSymbol;
        }

        // Make the initial bored and store it as gameState
        char[][] gameState = getInitialGameState();
        // Add the initial board to gameHistory
        gameHistory.add(gameState);

        // Print out the starting board
        System.out.println(displayGameFromState(gameState));

        // Variable that determines whether someone has won in the game or whether it is a draw, stopping turns
        boolean haveEnded = false;

        // While the game is not done
        while (!haveEnded) {
            // Store the player's move as a board
            char[][] currentMove = runPlayerMove(currentPlayer, currentSymbol, gameState);
            // gives gameState the reference of the new move
            gameState = currentMove;
            // Adds a copy of this board state to gameHistory so the boards are all different and not the same
            gameHistory.add(Arrays.copyOf(gameState, gameState.length));
            // Print out the board of the player's move
            System.out.println(displayGameFromState(currentMove));

            // If the move that was just played resulted in a win, print out who won and end the turn taking
            if (checkWin(currentMove)) {
                System.out.println(currentPlayer + " wins!");
                haveEnded = true;
            // If the move that was just played resulted in a draw, print out that it was a draw and end turn taking
            } else if (checkDraw(currentMove)) {
                System.out.println("It's a draw!");
                haveEnded = true;
            }

            // If the match did not finish with the last move
            if (!haveEnded) {
                // If the player that just played a turn was player one, switch to player two
                if (currentPlayer.equals(playerOne)) {
                    currentPlayer = playerTwo;
                    currentSymbol = playerTwoSymbol;
                // If the player that just played a turn was player two, switch to player one
                } else {
                    currentPlayer = playerOne;
                    currentSymbol = playerOneSymbol;
                }
            }
        }

        // Return the ArrayList with all the different boards of the moves that were played by the players
        return gameHistory;
    }

    /**
     * Given the player names (where player two is "Computer"),
     * Run the one-player game.
     * Return an ArrayList of game states of each turn -- in other words, the gameHistory
     * @param playerNames
     * @return
     */
    private static ArrayList<char[][]> runOnePlayerGame(String[] playerNames) {
        // Same initial process as the one from the two player game
        String playerOne = playerNames[0];
        String playerTwo = playerNames[1];
        String currentPlayer = playerOne;
        char currentSymbol = playerOneSymbol;
        ArrayList<char[][]> gameHistory = new ArrayList<>();

        System.out.println("Tossing a coin to decide who goes first!!!");

        double coinToss = Math.random();

        if (coinToss < 0.5) {
            System.out.println(playerOne + " gets to go first.");
        } else {
            System.out.println(playerTwo + " gets to go first.");
            currentPlayer = playerTwo;
            currentSymbol = playerTwoSymbol;
        }

        char[][] gameState = getInitialGameState();
        gameHistory.add(gameState);

        System.out.println(displayGameFromState(gameState));

        boolean haveEnded = false;

        while (!haveEnded) {
            // Don't assign the currentMove right away because the player and computer have different ways of moving
            char[][] currentMove;

            // If the current player is the human player
            if (currentPlayer.equals(playerOne)) {
                // Take in their move and assign the board to show that move
                currentMove = runPlayerMove(currentPlayer, currentSymbol, gameState);
            // If the current player is the computer
            } else {
                System.out.println(currentPlayer + "'s turn:");
                // Take in the CPU's move and set it as the board
                currentMove = getCPUMove(gameState);
            }

            // Add the board of the move to gameHistory and print out the move that was just played
            gameHistory.add(currentMove);
            System.out.println(displayGameFromState(currentMove));

            // If the move that was just played led to a win print who won and end the game
            if (checkWin(currentMove)) {
                System.out.println(currentPlayer + " wins!");
                haveEnded = true;
            // If the move that was just played led to a draw print that it's a draw and end the game
            } else if (checkDraw(currentMove)) {
                System.out.println("It's a draw!");
                haveEnded = true;
            }

            // If the move that was just played did not lead to a win or draw keep going
            if (!haveEnded) {
                // Switch players
                if (currentPlayer.equals(playerOne)) {
                    currentPlayer = playerTwo;
                    currentSymbol = playerTwoSymbol;
                } else {
                    currentPlayer = playerOne;
                    currentSymbol = playerOneSymbol;
                }
            }

            // Set the gameState to a copy of the move that was just played, so they're different in gameHistory
            gameState = Arrays.copyOf(currentMove, currentMove.length);

        }

        // Return the game's history
        return gameHistory;

    }

    /**
     * Repeatedly prompts player for move in current state, returning new state after their valid move is made
     * @param playerName
     * @param playerSymbol
     * @param currentState
     * @return
     */
    private static char[][] runPlayerMove(String playerName, char playerSymbol, char[][] currentState) {

        // Have a player make a move by asking them for a row and a column
        int[] playerMove = getInBoundsPlayerMove(playerName);
        // If their move is already taken tell them and ask for another move
        while (!checkValidMove(playerMove, currentState)) {
            System.out.println("That space is already taken. Try again");
            playerMove = getInBoundsPlayerMove(playerName);
        }

        // Return the new board using the makeMove method with the given move, symbol, and state
        return makeMove(playerMove, playerSymbol, currentState);
    }

    /**
     * Repeatedly prompts player for move. Returns [row, column] of their desired move such that row & column are on
     * the 3x3 board, but does not check for availability of that space.
     * @param playerName
     * @return
     */
    private static int[] getInBoundsPlayerMove(String playerName) {

        // Initialize a scanner to take in user input
        Scanner sc = new Scanner(System.in);

        // Initialize a variable that is done when given a valid row and column
        boolean done = false;

        // Initialize row and column variables that will be changed
        int row = 0;
        int column = 0;

        // See if the player's name starts with an s to properly show their turn
        boolean nameEndsWithS = playerName.endsWith("s");

        // While a valid move has not been entered
        while(!done) {
            // If their name ends with an s, properly show their turn
            if (nameEndsWithS) {
                System.out.println(playerName + "' turn:");
            } else {
                System.out.println(playerName + "'s turn:");
            }

            // Take in user input and assign the given values to the row and column variables
            System.out.println(playerName + " enter row: ");
            row = sc.nextInt();
            System.out.println(playerName + " enter column: ");
            column = sc.nextInt();

            // if the row AND columns are between 0 and 2 then they are valid
            if ((row >= 0 && row < 3) && (column >= 0 && column < 3)) {
                // exit out of the loop
                done = true;
            // If the row OR column variables are not valid, then tell them and ask for new values
            } else {
                System.out.println("That row or column is out of bounds. Try again.");
            }
        }
        // Return their valid, desired move
        return new int[]{row, column};
    }

    /**
     * Given a [row, col] move, return true if a space is unclaimed.
     * Doesn't need to check whether move is within bounds of the board.
     * @param move
     * @param state
     * @return
     */
    private static boolean checkValidMove(int[] move, char[][] state) {

        // Return if the desired move has an empty character or not
        return state[move[0]][move[1]] == emptySpaceSymbol;

    }

    /**
     * Given a [row, col] move, the symbol to add, and a game state,
     * Return a NEW array (do NOT modify the argument currentState) with the new game state
     * @param move
     * @param symbol
     * @param currentState
     * @return arrayCopy
     */
    private static char[][] makeMove(int[] move, char symbol, char[][] currentState) {
        // Hint: Make use of Arrays.copyOf() somehow to copy a 1D array easily
        // You may need to use it multiple times for a 1D array

        // Create a new copy of the given 2d character array, so we do not change the passed argument
        char[][] arrayCopy = new char[currentState.length][];
        for (int i = 0; i < currentState.length; i++) {
            arrayCopy[i] = Arrays.copyOf(currentState[i], currentState[i].length);
        }

        // Changes the move in the new array to the player's symbol
        arrayCopy[move[0]][move[1]] = symbol;

        // Return the new board
        return arrayCopy;
    }

    /**
     * Given a state, return true if some player has won in that state
     * @param state
     * @return
     */
    private static boolean checkWin(char[][] state) {
        // Hint: no need to check if player one has won and if player two has won in separate steps,
        // you can just check if the same symbol occurs three times in any row, col, or diagonal (except empty space symbol)
        // But either implementation is valid: do whatever makes most sense to you.

        // Horizontals
        // For each row in the 2d array
        for (char[] chars : state) {
            // If all the columns in each row array are the same, then it is a win
            if ((chars[0] != emptySpaceSymbol) && (chars[0] == chars[1]) && (chars[1] == chars[2])) {
                return true;
            }
        }

        // Verticals
        // For each column in the 2d array
        for (int j = 0; j < state[0].length; j++) {
            // If each value in each of the vertical columns are the same, then it is a win
            if ((state[0][j] != emptySpaceSymbol) && (state[0][j] == state[1][j]) && (state[1][j] == state[2][j])) {
                return true;
            }
        }

        // Diagonals
        // Return if either the top left to bottom right diagonal has the same values
        // Or if the top right to bottom left diagonal has the same values
        return (state[1][1] != emptySpaceSymbol) && (((state[0][0] == state[1][1]) && (state[2][2] == state[1][1]))
                || ((state[0][2] == state[1][1]) && (state[2][0] == state[1][1])));
    }

    /**
     * Given a state, simply checks whether all spaces are occupied. Does not care or check if a player has won.
     * @param state
     * @return
     */
    private static boolean checkDraw(char[][] state) {

        // See if the there are no unoccupied moves when we ask for them with the current state
        return getValidMoves(state).isEmpty();
    }

    /**
     * Given a game state, return a new game state with move from the AI
     * It follows the algorithm in the PDF to ensure a tie (or win if possible)
     * @param gameState
     * @return
     */
    private static char[][] getCPUMove(char[][] gameState) {
        // Hint: you can call makeMove() and not end up returning the result, in order to "test" a move
        // and see what would happen. This is one reason why makeMove() does not modify the state argument
        // Determine all available spaces

        // Initializes the available moves in the given state to an ArrayList
        ArrayList<int[]> availableMoves = getValidMoves(gameState);

        // If there is a winning move available, make that move
        // For each move in the available moves
        for (int[] move : availableMoves) {
            // Have the computer make that move and store it as a new board
            char[][] newState = makeMove(move, playerTwoSymbol, gameState);
            // See if that new board results in a win, if so return it
            if (checkWin(newState)) {
                return newState;
            }
        }

        // If not, check if opponent has a winning move, and if so, make a move there
        // For each move in available moves
        for (int[] move : availableMoves) {
            // Have the human player make that move and store it as a new board
            char[][] newState = makeMove(move, playerOneSymbol, gameState);
            // If the new board results in a win for the human player, have the computer block that
            if (checkWin(newState)) {
                return makeMove(move, playerTwoSymbol, gameState);
            }
        }


        // If not, move on center space if possible
        // Make the center of the board as a move
        int[] centerMove = {1, 1};
        // If the center is unoccupied, then have the computer move there
        if (checkValidMove(centerMove, gameState)) {
            return makeMove(centerMove, playerTwoSymbol, gameState);
        }

        // If not, move on corner spaces if possible
        // Make the corners into a 2d array of those moves
        int[][] cornerSpaces = {
                {0, 0},
                {0, 2},
                {2, 0},
                {2, 2}
        };

        // See if the corner moves are available moves, if so have the computer move to the first one available
        for (int[] moves : availableMoves) {
            for (int[] cornerMoves : cornerSpaces) {
                if (Arrays.equals(moves, cornerMoves)) {
                    return makeMove(cornerMoves, playerTwoSymbol, gameState);
                }
            }
        }

        // Otherwise, move in any available spot
        // Return the first available spot we can get
        return makeMove(availableMoves.get(0), playerTwoSymbol, gameState);
    }

    /**
     * Given a game state, return an ArrayList of [row, column] positions that are unclaimed on the board
     * @param gameState
     * @return validMoves
     */
    private static ArrayList<int[]> getValidMoves(char[][] gameState) {
        // Initialize the valid moves as an empty ArrayList that we will add to
        ArrayList<int[]> validMoves = new ArrayList<>();

        // For each symbol on the board, if it is empty add that move onto the validMoves ArrayList
        for (int i = 0; i < gameState.length; i++) {
            for (int j = 0; j < gameState[0].length; j++) {
                if (gameState[i][j] == emptySpaceSymbol) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }
        // Return the validMoves
        return validMoves;
    }

    /**
     * Given player names and the game history, display the past game as in the PDF sample code output
     * @param playerNames
     * @param gameHistory
     */
    private static void runGameHistory(String[] playerNames, ArrayList<char[][]> gameHistory) {
        // We have the names of the players in the format [playerOneName, playerTwoName]
        // Player one always gets 'X' while player two always gets 'O'
        // However, we do not know yet which player went first, but we'll need to know...
        // Hint for the above: which symbol appears after one turn is taken?

        // Hint: iterate over gameHistory using a loop

        // Store the player names locally
        String playerOne = playerNames[0];
        String playerTwo = playerNames[1];
        // Set the current player as player one for now
        String currentPlayer = playerOne;

        // Check which symbol had the first turn and set the current player as the one who has that symbol
        for (char[] rows: gameHistory.get(1)) {
            for (char value : rows) {
                if (value == playerTwoSymbol) {
                    currentPlayer = playerTwo;
                    break;
                }
            }
        }

        // Iterate through all the boards in the gameHistory
        for (int i = 0; i < gameHistory.size(); i++) {
            // In the first board, print out the match details
            if (i == 0) {
                System.out.println(playerOne + " (" + playerOneSymbol + ") vs " + playerTwo + " (" + playerTwoSymbol + ")");
            // After the first board, just print out whose turn we are seeing
            } else {
                System.out.println(currentPlayer + ":");
            }

            // Print out the board in the current instance
            System.out.println(displayGameFromState(gameHistory.get(i)));

            // If it is the last board, check to see if it was a win or a draw and print out the win or draw
            if (i == gameHistory.size() - 1) {
                if (checkWin(gameHistory.get(i))) {
                    System.out.println(currentPlayer + " wins!");
                } else {
                    System.out.println("It's a draw!");
                }
            }

            // If we are past the first, empty board, then switch players after their turns have been displayed
            if (i > 0) {
                if (currentPlayer.equals(playerOne)) {
                    currentPlayer = playerTwo;
                } else {
                    currentPlayer = playerOne;
                }
            }
        }
    }

}