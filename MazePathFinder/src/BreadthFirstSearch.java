import java.util.*;

/**
 * Class that runs the Breadth First Search.
 * BFS is a pathfinding algorithm for unweighted graphs
 * that is guaranteed to find the shortest path
 * as long as one exists.
 *
 * @date 07/11/2022
 * @author Andrew Photinakis
 */
public class BreadthFirstSearch {

    //holds the hedgemaze instance
    private HedgeMaze hedgeMaze;
    //holds the start coordinate for the algo
    private Coordinates start;
    //holds the end coordinate for the algo
    private Coordinates finish;
    //treemap that holds the adjacent neighbors for all coordinates
    private TreeMap<Coordinates, Node> map;

    /**
     * Constructor for the Breadth First Search class
     *
     * @param hedgeMaze maze instance passed in
     * @param start start coordinate passed in
     * @param finish finish coordinate passed in
     * @param map treemap passed in
     */
    public BreadthFirstSearch(HedgeMaze hedgeMaze, Coordinates start, Coordinates finish, TreeMap<Coordinates, Node>
                                                                                                        map){
        this.hedgeMaze = hedgeMaze;
        this.start = start;
        this.finish = finish;
        this.map = map;
    }

    /**
     * Part 1 of breadth first search. This part creates the queue and the map.
     * Loops until either the finish node is found, or the queue is empty.
     * The method processes the next coordinate at the front of the queue
     * and loops through all the neighbors of that current coordinate.
     *
     * {@inheritDoc}
     * @return a call to the contructPath method which is passed in the
     *          map of predecessors along with the start and finish coordinates
     */
    public Collection<Coordinates> findPath(){

        List<Coordinates> queue = new LinkedList<>();
        queue.add(this.start);
        Map<Coordinates , Coordinates> predecessors = new HashMap<>();
        predecessors.put(this.start, this.finish);

        while(!queue.isEmpty()){
            Coordinates current = queue.remove(0);
            if(current.equals(finish)){
                break;
            }
            for(Node nbr : this.map.get(current).getNeighbors()){
                Coordinates c = hedgeMaze.convertNodeToCoordinate(nbr);
                if(!predecessors.containsKey(c)){
                    predecessors.put(c, current);
                    queue.add(c);
                }
            }
        }
        return constructPath(predecessors, start, finish);
    }

    /**
     * Part 2 of breadth first search. Takes in a list of predecessors, start
     * and finish coordinates. Constructs a path from the start coordinate to
     * the finish coordinate.
     *
     * @param predecessors map of the predecessors from findPath
     * @param start start coordinate passed into the method
     * @param finish end coordinate passed into the method
     * @return a List of coordinates path from the start to end coordinates
     */
    private List<Coordinates> constructPath(Map<Coordinates, Coordinates> predecessors,
                                            Coordinates start, Coordinates finish) {
        List<Coordinates> path = new LinkedList<>();
        if (predecessors.containsKey(finish)) {
            Coordinates currCord = finish;
            while (!currCord.equals(start)) {
                path.add(0, currCord);
                currCord = predecessors.get(currCord);
            }
            path.add(0, start);
        }
        return path;
    }

    /**
     * @return Getter method that returns the maze
     */
    public HedgeMaze getHedgeMaze(){
        return this.hedgeMaze;
    }

    /**
     * @return Getter method that returns the starting coordinate
     */
    public Coordinates getStart(){
        return this.start;
    }

    /**
     * @return Getter method that returns the finish coordinate
     */
    public Coordinates getFinish(){
        return this.finish;
    }

}
