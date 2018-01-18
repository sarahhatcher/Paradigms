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
        v0.4: SplashParser updated, detail on on SplashParser
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
        SplashParser aParser = new SplashParser(args[0]);
        
        aParser.systemStatePrinter();
    }
}
