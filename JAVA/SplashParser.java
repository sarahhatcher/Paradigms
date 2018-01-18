import java.io.*;
import java.util.*;

// v0.4: Output matrix is now masterGrid[][][].
//       Error handling has been altered: Collision will be no longer processed during parsing.
//       FPA is no longer pre-calculated. It will be assigned normally. ASSIGN constant is kept for the purpose of detecting collision.
// TODO: Error detection related to bracket is yet to be fixed.

public class SplashParser {

    private BufferedReader br;
    private FileReader fr;

    private int[][][] masterGrid; // General Matrix, at third place, 0 is used for Logic Matrix, and 1 is used for Task Matrix.
    private int fpaPenalty; // Total cumulative penalty from forced partial assignment.
    //Boolean for each step of parsing string 
    //Set flag as we parse 
    private boolean setName = false; //True when name is not in correct format -->name:: 
    private boolean setFPA = false; //Check if structure remains--> no error 
    private boolean setFM = false;
    private boolean setTNT = false;
    private boolean setMP = false;
    private boolean setTNP = false;
    private boolean setCollision = false;
    private int execCycle = 0; //Debugging variable.
    private int lineCounter = 0; //lineCounter tracks the number of lines that have been parsed for machine penalty. If it isn't exactly 8 after MP is processed, throw error.

    public static final byte ASCII_INT_CHAR_FIX = -48;
    public static final byte ASCII_CAP_CHAR_FIX = -65;

    public static final byte FORBIDDEN = -1;
    public static final byte SIZEMAX = 8;
    public static final byte ASSIGNED = -2;
    public static final byte PLACEHOLDER = 0;
    public static final byte LOGIC = 0;
    public static final byte TASK = 1;

    public static final byte BRACKET = 40;
    public static final byte[] ASCII_INT_RANGE = {47,58};
    public static final byte[] ASCII_UPPER_RANGE = {64,91};
    public static final byte[] ASCII_LOWER_RANGE = {96,123};

    public SplashParser(String inputName) {
        try {
            // Program will immediately try to find the input file and read them.
	    //May need to update version to include exception handling for non-ascii input (check with prof if need to catch exception for input not in ascii --> outside of parser scope)
	    
	    //Create new fileReader with INPUTNAME
	    ////Create new BufferedReader  
            fr = new FileReader(inputName + ".txt");
            br = new BufferedReader(fr);
            
	    //tempStr is string being processed
            String tempStr = br.readLine();
	    //Verify will try to pick up the first letter of given string in first while loop
            byte verify;
	    //Current data structures before matching to Richards output
	   
	    // Master Marrix initiallization, two 8x8 matrix, 0 for logic 1 for task.
            masterGrid = new int[SIZEMAX][SIZEMAX][2];

            while (tempStr != null) {
                // Verifies if the line read is not the empty line.
                tempStr = tempStr.trim();
		
                //If the processed string is not empty then read the first character
                if (!tempStr.contentEquals("")) {
                    verify = (byte) tempStr.charAt(0);
                } else {
                    verify = PLACEHOLDER; // If it is, assign null ASCII text.
                }

                if (verify == BRACKET) // If the line starts with bracket
                {
                    // FPA, FM, TNT, TNP takes in "(int, char)" as input. As such, it detects '('.
                    int[] processedString = pairProcessor(tempStr.substring(1, tempStr.length()-1));
                    assignOperator(processedString);
                }
                else if ((verify > ASCII_INT_RANGE[0]) && (verify < ASCII_INT_RANGE[1])) // If the line starts with number
                {
                    if (setMP == true) {
                        // If a line starts with number it is presumed that it is the line for Machine Penalty.
                        int[] tempValue = lineProcessor(tempStr);
                        for (byte n = 0; n < SIZEMAX; n++)
                        {
                            // Assign values to all valid possible positions in matrix.
                            if (masterGrid[n][lineCounter][LOGIC] != FORBIDDEN)
                            {
                                masterGrid[n][lineCounter][LOGIC] = tempValue[n];
                            }
                        }
                        // Line counter is used to check how many line of number has been proceed.
                        lineCounter = lineCounter + 1;
                    } else if (setName == true) {
                        // Checks for integer starting name.If name starts with number
                        setName = false;
                    } else {
                        parseError("intCrash");
                    }
                }
		// If the line starts with english letter
                else if (((verify > ASCII_LOWER_RANGE[0]) && (verify < ASCII_LOWER_RANGE[1])) || ((verify > ASCII_UPPER_RANGE[0]) && (verify < ASCII_UPPER_RANGE[1]))) {
                    flagProcessor(tempStr);
                }
                else if (verify != PLACEHOLDER) {
                    // If no criteria is met, but has some ASCII characters, fault
                    if (setName == true) {
                        setName = false;
                    }
                    else if (!tempStr.contentEquals("")) {
                        parseError("inFault");
                    }
                }
                // readLine here, so EOL can be terminated correctly.
                tempStr = br.readLine();
                execCycle++;
            }

            // Handler for collision caused no-solution matrix is moved outside of actual parsing process.
            if (setCollision = true) {
                parseError("Collision");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file named " + INPUTNAME + " found, aborting.");
            e.printStackTrace();
        } catch (IOException e) {
            parseError("0");
        }
    }

    // Returns Operation/Task matrix as requested.
    public int[][] fetchMatrix() {
        return masterGrid;
    }

    // For debug purpose, prints initialized state of operation or task matrix.
    public void printMe() {
        System.out.println("Initiating Debug Output.");

        for (byte i=0; i < 2; i++) {
            for (byte j=0; i < SIZEMAX; i++) {
                for (byte k=0; j < SIZEMAX; j++) {
                    System.out.print("[" + masterGrid[j][k][i] + "]");
                }
                System.out.println("");
            }
        }
    }

    // This function is used to assign pair wise input (FPA, FM, TNT, TNP) into appropriate data structure.
    private void assignOperator(int[] newPairs) {
        
        if (setTNP == true) {
            // If target location already forbidden, rewrite is not required.
            if (masterGrid[newPairs[0]][newPairs[1]][TASK] != FORBIDDEN)  {
                masterGrid[newPairs[0]][newPairs[1]][TASK] = newPairs[2];
            }
        }
        else if (setTNT == true) {
            // TNT shouldn't have any error during assignment.
            masterGrid[newPairs[0]][newPairs[1]][TASK] = FORBIDDEN;
        }
        else if (setFM == true) {
            // If FM is mapped to FPA, there will be no solution, which will be handled here.
            if (masterGrid[newPairs[0]][newPairs[1]][LOGIC] != ASSIGNED)  {
                masterGrid[newPairs[0]][newPairs[1]][LOGIC] = FORBIDDEN;
            } else {
                setCollision = true;
            }
        }
        else if (setFPA == true) {
            // Assigns FPA, also catches illegal FPA mapping.
            if (masterGrid[newPairs[0]][newPairs[1]][LOGIC] != FORBIDDEN) {
                for (byte n = 0; n < SIZEMAX; n++) {
                    masterGrid[newPairs[0]][n][LOGIC] = FORBIDDEN;
                    masterGrid[n][newPairs[1]][LOGIC] = FORBIDDEN;
                }
                masterGrid[newPairs[0]][newPairs[1]][LOGIC] = ASSIGNED;
            } else {
                parseError("fpa");
            }
        }
    }

    // This function handles flags for data assignment and error checking. It will also check if invalid strings are in the input.
    // Potential fringe case --> if line being parsed starts with a bracket it will be processed and trigger a different error instead of "error while parsing input file"
 
    private void flagProcessor(String flagText) {
	//Check format of initial label 
        if (flagText.equals("Name:"))
        {
            setName = true;
        }
        else if (flagText.equals("forced partial assignment:"))
        {
            // Missing Name input check
            if (setName == true) {
                parseError("inFault");
            }
            setFPA = true;
        }
        else if (flagText.equals("forbidden machine:"))
        {
            setFM = true;
            setFPA = false;
        }
        else if (flagText.equals("too-near tasks:"))
        {
            setTNT = true;
            setFM = false;
        }
        else if (flagText.equals("machine penalties:"))
        {
            setMP = true;
        }
        else if (flagText.equals("too-near penalities"))
        {
            if (lineCounter != SIZEMAX) {
                // Correct MP input is always 8 lines.
                parseError("intCrash");
            }
            setTNP = true;
            setTNT = false;
        }
        else
        {	
            // Checks for Name input for later ASCII index.
            if (setName == true && setFPA == false) {
                setName = false;
            } else {
                parseError("inFault");
            }
        }
    }

    // This function processes string in form of "(machine,task)", "(task,task)", or "(machine,task,penalty)" and processes into an array for data structure.
    private int[] pairProcessor(String tempStr) {
        String[] retStr = tempStr.split(",");
        int[] retVal = new int[retStr.length];

        try {
            // Try to parse the machine (int) input.
            retVal[0] = Integer.parseInt(retStr[0].trim()) - 1;
        } catch (NumberFormatException e) {
            // If it fails, it must be task or invalid, process it as though it is task.
            retVal[0] = (int) retStr[0].trim().charAt(0);
            retVal[0] = retVal[0] + ASCII_CAP_CHAR_FIX;
        }
        
        // Second input is always task.
        retVal[1] = (int) retStr[1].trim().charAt(0);
        retVal[1] = retVal[1] + ASCII_CAP_CHAR_FIX;
        if (retStr.length > 2) {
            // If there are more than three variable discovered, it is TNP, process penalty.
            retVal[2] = penaltyVerify(retStr[2]);
        }

        // Check to see if assignment is in expected range. If not, trigger invalid error.
        if ((retVal[1] > 7) || (retVal[1] < 0)) {
            parseError("nullMT");
        }
        else if ((retVal[0] > 7) || (retVal[0] < 0)) {
            parseError("nullMT");
        }

        return retVal;
    }

    // Handles line of integer for MP.
    private int[] lineProcessor(String tempStr) {
        String[] retStr = tempStr.split(" ");
        int[] retVal = new int[SIZEMAX];

        // If there are more or less than 8 entries, crash.
        if (retStr.length != SIZEMAX) {
            parseError("intCrash");
        }

        for (byte i=0; i < SIZEMAX; i++) {
            retVal[i] = penaltyVerify(retStr[i].trim());
        }

        return retVal;
    }

    // Analyzes if given panalty is valid or invalid. Checks for any non-zero, non-natural number.
    private int penaltyVerify(String target) {
        int output = PLACEHOLDER;

        try {
            output = Integer.parseInt(target);
        } catch (NumberFormatException e) {
            parseError("nullP");
        }
        if (output < 0) {
            parseError("nullP");
        }
        return output;
    }

    // Catch no name error here by ordering of errors and if statements checking setName status when later error triggers.
    private void parseError(String erCode) {

        if (erCode.contentEquals("fpa"))
        {
            System.out.println("partial assignment error");
        }
        if (erCode.contentEquals("intCrash"))
        {
            if (setMP == true && setTNT == true) {
                System.out.println("machine penalty error");
            } else {
                parseError("inFault");
            }
        }
        else if (erCode.contentEquals("collision"))
        { 
           System.out.println("No valid solution possible!");
        }
        else if (erCode.contentEquals("nullMT"))
        {
	        if (setTNP == false) {
		        System.out.println("invalid machine/task");
	        } else {
		        System.out.println("invalid task");
	        }
        }
        else if (erCode.contentEquals("nullP"))
        {
            System.out.println("invalid penalty error");
        }
        else if (erCode.contentEquals("inFault"))
        {
            // It is presumed that Name will not start with bracket.
	        // Patch for Name starting with bracket
            System.out.println("Error while parsing input file");
        }
        else
        {
            System.out.println("Unknown error has occured during file parsing. Aborting.");
        }

        // SSP should not be included while handing over.
        systemStatePrinter();
        System.exit(0);
    }

    public void systemStatePrinter() {
        if (setName == true) {
            System.out.println("Set Name: true");
        }
        if (setFPA == true) {
            System.out.println("Set FPA: true");
        }
        if (setFM == true) {
            System.out.println("Set FM: true");
        }
        if (setTNT == true) {
            System.out.println("Set TNT: true");
        }
        if (setMP == true) {
            System.out.println("Set MP: true");
        }
        if (setTNP == true) {
            System.out.println("Set TNP: true");
        }
        System.out.println("Cycle: " + execCycle);
        printMe();
    }
}
