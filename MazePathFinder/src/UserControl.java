import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Objects;

/**
 * This class stores the HedgeMaze and prompts the user
 * to enter a starting and ending coordinate for the Breadth First Search
 * algorithm.
 *
 * @date 07/11/2022
 * @author Andrew Photinakis
 */
public class UserControl {

    /**
     * Holds the maze
     */
    private final HedgeMaze hMaze;

    /**
     * UserControl constructor that takes in a maze file,
     * creates the maze along with its adjacency list, and
     * prints out the maze for the user to see
     *
     * @param fileName the hedge maze description file
     * @throws IOException if there is a problem reading the file
     */
    public UserControl(String fileName) throws IOException {
        hMaze = new HedgeMaze(fileName);
        hMaze.printAdjacencyList();
        System.out.println();
        hMaze.printLayout();
    }

    /**
     * Method that prompts the user for start and end positions in the maze
     * and calls to execute Breadth First Search on the maze using the user prompts
     */
    public void doUserInput() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            System.out.print("Enter starting coordinates. (row, then column) ");
            line = in.readLine();
            if (Objects.equals(line, "quit")) {
                return;
            }
            Coordinates start;
            try {
                start = convertStringtoCord(line);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.print("Enter pot of gold coordinates. (row, then column) ");
            line = in.readLine();

            Coordinates finish;
            try {
                finish = convertStringtoCord(line);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println("Currently finding the shortest path...");
            Collection<Coordinates> pathFound = new BreadthFirstSearch(hMaze, start, finish, hMaze.getMap()).findPath();
            if (pathFound.isEmpty()) {
                System.out.println("No path was found.");
            } else {
                System.out.print("The path is ");
                for (Coordinates co : pathFound) {
                    System.out.print(co + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Takes in a coordinate to be converted and turns it into a Node
     *
     * @param cordToConvert coordinate to be converted
     * @return node version of the coordinate
     */
    private Node convertCordtoNode(Coordinates cordToConvert) {

        String rowStr = String.valueOf(cordToConvert.row());
        String colStr = String.valueOf(cordToConvert.col());
        StringBuilder result = new StringBuilder();
        result.append("(").append(rowStr).append(",").append(colStr).append(")");

        return new Node(result.toString());
    }

    /**
     * Takes in a string line and converts the string input into a coordinate
     *
     * @param line input from the user
     * @return new coordinate from the user input that was a string
     * @throws Exception input from the user is not valid
     */
    private Coordinates convertStringtoCord(String line) throws Exception {

        String[] lineArr = line.split("\\s+");
        int rowCord;
        int colCord;
        try {
            if (lineArr.length != 2) {
                throw new Exception("Not the right number of items");
            }
            rowCord = Integer.parseInt(lineArr[0]);
            colCord = Integer.parseInt(lineArr[1]);
            if (!hMaze.contains(rowCord, colCord)) {
                throw new Exception(rowCord + "," + colCord + " is not a valid cell location.");
            }
        } catch (NumberFormatException e) {
            throw new Exception("Input was of a non-Integer value");
        }
        return new Coordinates(rowCord, colCord);
    }


}