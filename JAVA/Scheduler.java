/*
    Team: Splash Zone
   
	# Targeted for OpenJDK 1.8.0_151

	-------------------------------------------------------
	This program is designed to solve scheduling problem of pairing 8 system to 8 task.

		- Program will accept file to find the minimum solution to given problem.

*/

public class Scheduler
{
    static String outputFile;
	//
    public static void main (String[] args)
    {
        if (args.length < 2) {
            System.out.println("Please initiate program in following syntax.");
            System.out.println("java Scheduler (inputName) (outputName)");
            System.exit(0);
        }
        outputFile = args[1];
        int[][][] pntArray = new int[8][8][2];
        try {
            SplashParser parseInstance = new SplashParser(args[0]);
            pntArray = parseInstance.fetchMatrix();
        } catch (Exception e) {
            System.out.println("Unhandled General Error");
            SplashOutput.printError(1);
        }
        
        

        SplashSearch searchInstance = new SplashSearch(pntArray);
        searchInstance.treeConstruct();
    }
}
