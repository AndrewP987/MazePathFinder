import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A coordinate-based graph of nodes to represent a maze laid on a 2-D
 * grid of cells
 *
 * @date 07/11/2022
 * @author Andrew Photinakis
 */
public class HedgeMaze {

    /** Input file symbol for no barrier between adjacent horizontal cells */
    public final static String NO_WALL = ".";

    /** Treemap declaration to hold all the coordinates and node neighbors */
    private final TreeMap<Coordinates, Node> map;

    /** Integer declaration to later hold the number of rows in the maze */
    private int rows;

    /** Integer declaration to later hold the number of columns in the maze */
    private int columns;

    /** String declaration to hold the name of the Node */
    private String name;

    /**
     * Create a graph by reading a file. Details can be found in the lab
     * writeup.
     * @param fileName location of maze specification
     * @throws IOException if the file can't be read for any reason
     */
    public HedgeMaze( String fileName ) throws IOException {

        /* Creates the buffered reader to read in the file that is passed in */
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {

            /** instantiates the treemap structure */
            map = new TreeMap<>();

            /** gets the num of rows and columns for the maze */
            String l;
            String[] stuff;
            try {
                l = in.readLine();
                stuff = l.split("\\s+");
                this.rows = Integer.parseInt(stuff[0]);
                this.columns = Integer.parseInt(stuff[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /** prebuilds all the coordinates and nodes and puts them in the treemap */
            for(int i = 0; i < numRows(); i++){
                for(int j = 0; j < numCols(); j++){
                    Coordinates putIn = new Coordinates(i, j);
                    Node node = createNodeSimplified(putIn);
                    map.put(putIn, node);
                }
            }

            /**
             * The rest of the Hedgemaze constructor consists of this code segment.
             * In this segment the Maze is built by reading 2 lines at once
             * and comparing the barriers and assigning neighbors.
             * There are some self methods I made that are described at
             * the bottom of this file.
             */
            int rowIter = 0;
            int colIter = 0;
            String rowLine;
            String[] secondRow;
            while ((rowLine = in.readLine()) != null) {
                String[] firstRow = rowLine.split("\\s+");
                rowLine = in.readLine();
                int nextLineInd = 0;
                for(int i = 1; i < firstRow.length; i += 2){
                    if(firstRow[i].equals(NO_WALL)){
                        Coordinates cord1 = createCoordinateSimplified(rowIter, colIter);
                        Node cell1 = createNodeSimplified(cord1);
                        Coordinates cord2 = createCoordinateSimplified(rowIter, colIter + 1);
                        Node cell2 = createNodeSimplified(cord2);
                        repeatedNeighborAdd(cell1, cord1, cell2, cord2);

                    }
                    if(rowIter < rows - 1){
                        secondRow = rowLine.split("\\s+");
                        if(secondRow[nextLineInd].equals(NO_WALL)){
                            Coordinates cord1 = createCoordinateSimplified(rowIter, colIter);
                            Node cell1 = createNodeSimplified(cord1);
                            Coordinates cord2 = createCoordinateSimplified(rowIter + 1, colIter);
                            Node cell2 = createNodeSimplified(cord2);
                            repeatedNeighborAdd(cell1, cord1, cell2, cord2);
                        }
                    }
                    nextLineInd++;
                    colIter++;
                }
                if(rowIter < rows - 1){
                    secondRow = rowLine.split("\\s+");
                    if(secondRow[nextLineInd].equals(NO_WALL)){
                        Coordinates cord1 = createCoordinateSimplified(rowIter, colIter);
                        Node cell1 = map.get(cord1);
                        Coordinates cord2 = createCoordinateSimplified(rowIter + 1, colIter);
                        Node cell2 = map.get(cord2);
                        repeatedNeighborAdd(cell1, cord1, cell2, cord2);
                    }
                }
                rowIter += 1;
                colIter = 0;
            }
        }
    }

    /**
     *  Using the tree map and personal methods, the method takes
     *  in 4 ints representing 2 coordinates to be compared to each other.
     *  Their neighbors are compared and if they consist of each other,
     *  then they are therefore connected to each other.
     *
     * {@inheritDoc}
     * @rit.pre this.contains( r1, c1 ) and this.contains( r2, c2 )
     * @return true if both coordinates are neighbors of each other, false otherwise
     */
    public boolean connected( int r1, int c1, int r2, int c2 ) {

        Coordinates firstComp = new Coordinates(r1, c1);
        Coordinates secondComp = new Coordinates(r2, c2);
        Node first = createNodeSimplified(firstComp);
        Node second = createNodeSimplified(secondComp);
        int count = 0;

        for(Node f : map.get(firstComp).getNeighbors()){
            if(f.equals(second)){
                count++;
            }
        }
        for(Node f : map.get(secondComp).getNeighbors()){
            if(f.equals(first)){
                count++;
            }
        }
        return count == 2;
    }

    public boolean connects( Coordinates cell1, Coordinates cell2 ) {
        return connected( cell1.row(), cell1.col(), cell2.row(), cell2.col() );
    }

    /**
     * Prints out adjacency list of the graph using natural ordering
     */
    public void printAdjacencyList() {
        System.out.println( "Graph Details:" + System.lineSeparator() );
        for(Coordinates cord : map.keySet()){
            System.out.println("Node " + map.get(cord).toString());
        }
    }

    /**
     * Checks if the map contains the coordinate key that is passed into it
     *
     * @return true is the map contains the coordinate key, false otherwise
     */
    public boolean contains( int r, int c ) {
        return map.containsKey(new Coordinates(r, c));
    }

    /**
     * Method takes in a Node and converts it to a coordinate by
     * getting its name and using string manipulation.
     *
     * @param node that is passed in
     * @return a coordinate representation of the node string name
     */
    public Coordinates convertNodeToCoordinate(Node node){
        String s = node.getName();
        String n = s.replaceAll("[()]", "");
        String[] strArr = n.split("\\,");
        int rows = Integer.parseInt(strArr[0]);
        int cols = Integer.parseInt(strArr[1]);
        return new Coordinates(rows, cols);
    }

    /**
     * Method that does not take in anything but prints out the maze board.
     * Purely for user visuals
     */
    public void printLayout() {
        // Column coordinates
        System.out.print( "   " );
        for ( int c=0; c<this.numCols();++c ) {
            System.out.printf( " %2d ", c);
        }
        System.out.println();
        System.out.print( "   +" );
        for ( int c=0; c<this.numCols();++c) {
            System.out.print( "---+" );
        }
        System.out.println();
        for ( int r=0; r<this.numRows(); ++r) {
            System.out.printf( " %2d|", r );
            Coordinates right = new Coordinates( r, 0);
            for( int c=0; c<this.numCols(); ++c) {
                char barrier;
                if ( c == this.numCols() - 1) {
                    barrier = '|';
                }
                else {
                    Coordinates here = right;
                    right = new Coordinates( r, c + 1 );
                    barrier = this.connects( here, right ) ? ' ' : '|';
                }
                System.out.printf( "   " + barrier );
            }
            System.out.println();
            System.out.print( "   +" );

            if ( r < this.numRows() - 1 ) {
                for ( int c=0; c<this.numCols(); ++c  ) {
                    Coordinates here = new Coordinates( r, c );
                    Coordinates below = new Coordinates( r + 1, c );
                    String barrier =
                            this.connects( here, below ) ? "   " : "---";
                    System.out.print( barrier + '+' );
                }
            }
            else {
                for ( int c=0; c<this.numCols(); ++c  ) {
                    System.out.print( "---+" );
                }
            }
            System.out.println();
        }
    }

    /**
     * Takes in 2 nodes and 2 coordinates and gets their key and
     * adds the node neighbor to the coordinate.
     * Since the code in this function is repeated exactly,
     * this function is useful
     *
     * @param cell1 first node that is passed into method
     * @param cord1 first coordinate that is passed into method
     * @param cell2 second node that is passed into method
     * @param cord2 second coordinate that is passed into method
     */
    private void repeatedNeighborAdd(Node cell1, Coordinates cord1,
                                     Node cell2, Coordinates cord2){
        map.get(cord1).addNeighbor(cell2);
        map.get(cord2).addNeighbor(cell1);
    }

    /**
     * Takes in rowIter and colIter and creates a coordinate using
     * both integer values
     *
     * @param rowIter integer passed in at the time of function call
     * @param colIter integer passed in at the time of function call
     * @return new coordinate that's row is rowIter and col is colIter
     */
    private Coordinates createCoordinateSimplified(int rowIter, int colIter){
        return new Coordinates(rowIter, colIter);
    }

    /**
     * Takes in a coordinate and creates a node from it calling the coordinate
     * toString
     *
     * @param cordTopassToString coordinate passed in
     * @return new node that string value is the coordinate toString value
     */
    private Node createNodeSimplified(Coordinates cordTopassToString){
        return new Node(cordTopassToString.toString());
    }

    /**
     * Accessor method
     *
     * {@inheritDoc}
     * @return integer representing the number of rows in the maze
     */
    public int numRows() {
        return this.rows;
    }

    public TreeMap<Coordinates, Node> getMap(){
        return this.map;
    }

    /**
     * Accessor method
     *
     * {@inheritDoc}
     * @return integer representing the number of columns in the maze
     */
    public int numCols() {
        return this.columns;
    }

    /**
     * Overrides the gridgraph getName() and returns the name of the node
     *
     * @return String of the node name
     */
    public String getName() {
        return this.name;
    }
}
