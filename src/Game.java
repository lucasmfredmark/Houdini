import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by lucasmfredmark on 20/07/2016.
 */
public class Game {
    private String gameFile;
    private int boardSize;
    private int boardCenter;
    private int[][] mines;
    private int[][] moves;

    public void loadGameFile(String gameFile) {
        Path path = Paths.get(gameFile);
        List<String> fileLines = null;

        try {
            fileLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.gameFile = gameFile.substring(gameFile.lastIndexOf("/") + 1);
        this.boardSize = fileLines.get(0).length() - 2;
        this.boardCenter = boardSize / 2;
        this.mines = new int[boardSize][boardSize];
        this.moves = new int[boardSize][boardSize];

        for (int y = 1; y < (boardSize + 1); y++) {
            for (int x = 1; x < (boardSize + 1); x++) {
                char c = fileLines.get(y).charAt(x);

                if (c == 'x') {
                    mines[x - 1][y - 1] = 1;
                }
            }
        }
    }

    public void solve() {
        printBoard();
        System.out.println();

        if (findPath(boardCenter, boardCenter)) {
            //System.out.println("Solved game " + this.gameFile);

            System.out.println();
            printBoard();
        } else {
            System.out.println("Could not solve game " + this.gameFile);
        }
    }

    private boolean findPath(int x, int y) {
        System.out.println("Moved to (" + x + ", " + y + ")");

        if (isMine(x, y) || hasMoved(x, y)) {
            System.out.println("Mine found. Backtracking...");
            return false;
        }

        if (isAtEdge(x, y)) {
            moves[x][y] = 1;
            System.out.println("Reached the edge - a solution has been found.");
            return true;
        }

        moves[x][y] = 1;

        // find new directions
        if (x < boardCenter) {
            // if we are in the left side of the board
            if (y < boardCenter) {
                // if we are in the top-left side of the board
                if (x < y) {
                    // if the edge is closer to the left
                    if (findPath(x - 1, y + 1)) return true; // southwest
                    if (findPath(x - 1, y)) return true; // west
                    if (findPath(x - 1, y - 1)) return true; // northwest
                } else if (x > y) {
                    // if the edge is closer upwards
                    if (findPath(x, y - 1)) return true; // north
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x - 1, y - 1)) return true; // northwest
                } else {
                    // if x and y is the same, the distance to the edge is the same
                    if (findPath(x, y - 1)) return true; // north
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x - 1, y + 1)) return true; // southwest
                    if (findPath(x - 1, y)) return true; // west
                    if (findPath(x - 1, y - 1)) return true; // northwest
                }
            } else if (y > boardCenter) {
                // if we are in the bottom-left side of the board
                if (x < boardSize - y + 1) {
                    // if the edge is closer to the left
                    if (findPath(x - 1, y + 1)) return true; // southwest
                    if (findPath(x - 1, y)) return true; // west
                    if (findPath(x - 1, y - 1)) return true; // northwest
                } else if (x > boardSize - y + 1) {
                    // if the edge is closer downwards
                    if (findPath(x + 1, y + 1)) return true; // southeast
                    if (findPath(x, y + 1)) return true; // south
                    if (findPath(x - 1, y + 1)) return true; // southwest
                } else {
                    // x equals y, same distance
                    if (findPath(x + 1, y + 1)) return true; // southeast
                    if (findPath(x, y + 1)) return true; // south
                    if (findPath(x - 1, y + 1)) return true; // southwest
                    if (findPath(x - 1, y)) return true; // west
                    if (findPath(x - 1, y - 1)) return true; // northwest
                }
            } else {
                // if y is still the same, just go left
                if (findPath(x - 1, y + 1)) return true; // southwest
                if (findPath(x - 1, y)) return true; // west
                if (findPath(x - 1, y - 1)) return true; // northwest
            }
        } else if (x > boardCenter) {
            // if we are in the right side of the board
            if (y < boardCenter) {
                // if we are in the top-right side of the board
                if (y < boardCenter - x + 1) {
                    // the edge is closer upwards
                    if (findPath(x, y - 1)) return true; // north
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x - 1, y - 1)) return true; // northwest
                } else if (y > boardSize - x + 1) {
                    // the edge is closer to the right
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x + 1, y)) return true; // east
                    if (findPath(x + 1, y + 1)) return true; // southeast
                } else {
                    // the edge is equally close
                    if (findPath(x, y - 1)) return true; // north
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x + 1, y)) return true; // east
                    if (findPath(x + 1, y + 1)) return true; // southeast
                    if (findPath(x - 1, y - 1)) return true; // northwest
                }
            } else if (y > boardCenter) {
                // if we are in the bottom-right side of the board
                if (y < x) {
                    // the edge is closer to the right
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x + 1, y)) return true; // east
                    if (findPath(x + 1, y + 1)) return true; // southeast
                } else if (y > x) {
                    // the edge is closer downwards
                    if (findPath(x + 1, y + 1)) return true; // southeast
                    if (findPath(x, y + 1)) return true; // south
                    if (findPath(x - 1, y + 1)) return true; // southwest
                } else {
                    // the edge is equally close
                    if (findPath(x + 1, y - 1)) return true; // northeast
                    if (findPath(x + 1, y)) return true; // east
                    if (findPath(x + 1, y + 1)) return true; // southeast
                    if (findPath(x, y + 1)) return true; // south
                    if (findPath(x - 1, y + 1)) return true; // southwest
                }
            } else {
                // if y is still the same, just go right
                if (findPath(x + 1, y - 1)) return true; // northeast
                if (findPath(x + 1, y)) return true; // east
                if (findPath(x + 1, y + 1)) return true; // southeast
            }
        } else {
            // if x is still the same
            if (y < boardCenter) {
                // if we are in the top side of the board, just go up
                if (findPath(x, y - 1)) return true; // north
                if (findPath(x + 1, y - 1)) return true; // northeast
                if (findPath(x - 1, y - 1)) return true; // northwest
            } else if (y > boardCenter) {
                // if we are in the bottom side of the board, just go down
                if (findPath(x + 1, y + 1)) return true; // southeast
                if (findPath(x, y + 1)) return true; // south
                if (findPath(x - 1, y + 1)) return true; // southwest
            } else {
                // if we have not moved yet
                if (findPath(x, y - 1)) return true; // north
                if (findPath(x + 1, y - 1)) return true; // northeast
                if (findPath(x + 1, y)) return true; // east
                if (findPath(x + 1, y + 1)) return true; // southeast
                if (findPath(x, y + 1)) return true; // south
                if (findPath(x - 1, y + 1)) return true; // southwest
                if (findPath(x - 1, y)) return true; // west
                if (findPath(x - 1, y - 1)) return true; // northwest
            }
        }

        moves[x][y] = 0;

        return false;
    }

    private boolean isMine(int x, int y) {
        return (mines[x][y] == 1);
    }

    private boolean hasMoved(int x, int y) {
        return (moves[x][y] == 1);
    }

    private boolean isAtEdge(int x, int y) {
        return (x == 0 || x == (boardSize - 1) || y == 0 || y == (boardSize - 1));
    }

    private void printBoard() {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                if (isMine(x, y)) {
                    System.out.print("x");
                } else if ((x == boardCenter && y == boardCenter) || hasMoved(x, y)) {
                    System.out.print("@");
                } else {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }
    }
}
