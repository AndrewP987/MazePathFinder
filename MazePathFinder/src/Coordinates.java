/**
 * Record that is used to hold the coordinates from the maze of each spot in it.
 *
 * @date 07/11/2022
 * @author Andrew Photinakis
 */
public record Coordinates( int row, int col )
        implements Comparable< Coordinates > {

    public Coordinates( String rowStr, String colStr ) {
        this( Integer.parseInt( rowStr ), Integer.parseInt( colStr ) );
    }

    @Override
    public String toString() {
        return "(" + this.row + ',' + this.col + ')';
    }

    @Override
    public int compareTo( Coordinates o ) {
        int result = this.row - o.row;
        if ( result == 0 ) result = this.col - o.col;
        return result;
    }
}
