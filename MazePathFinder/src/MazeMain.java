import java.io.IOException;

/**
 * MazeMain class starts the entire program
 *
 * @date 07/11/2022
 * @author Andrew Photinakis
 */
public class MazeMain {

    public static void main( String[] args ) throws IOException, Exception {
        if ( args.length != 1 ) {
            System.out.println( "Usage: java PotOfGold maze-file" );
            System.exit( 1 );
        }
        UserControl userCtrl = null;
        try {
            userCtrl = new UserControl(args[0]);
        }
        catch( IOException e ) {
            System.out.println( "File " + args[0] + " could not be read." );
            System.exit( 1 );
        }

        userCtrl.doUserInput();
    }
}
