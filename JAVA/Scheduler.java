/*
    Team: Splash Zone
    Members:
        -
        -
        -
        -
        -

	# Targeted for OpenJDK 1.8.0_151

	-------------------------------------------------------
	This program is designed to solve scheduling problem of pairing 8 system to 8 task.

		- Program will accept file to find the minimum solution to given problem.

	Known Issues:
		* TBA

	Known Limits:
		* TBA

	Revision History:
		Information on non-functioning version will be excluded.

        v0.1: Completed early implementation of SplashParser
        v0.2: SplashParser now can handle all ASCII cases except for the bracket.
        v0.3: SplashParser now correctly handles penalty error, debug function 'systemStatePrinter' added. Private.
        v0.4: SplashParser updated, detail on SplashParser
        v0.5: SplashParser updated, detail on SplashParser.
                Added implementation of SplashOutput
                Added implementation of SplashSearch
                Added implementation of Node
*/

public class Scheduler
{
	//
    public static void main (String[] args)
    {
        if (args.length < 2) {
            System.out.println("Please initiate program in following syntax.");
            System.out.println("java Scheduler (inputName) (outputName)");
            System.exit(0);
        }
        int[][][] pntArray = new int[8][8][2];
        try {
            SplashParser parseInstance = new SplashParser(args[0]);
            pntArray = parseInstance.fetchMatrix();
        } catch (Exception e) {
            System.out.println("Unhandled General Error");
            SplashOutput.printError(1);
        }

        

        splashSearch searchInstance = new splashSearch(pntArray);
        searchInstance.treeConstruct();
    }
}
