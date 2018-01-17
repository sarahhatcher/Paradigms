import java.io.*;
import java.util.*;

public class SplashParser {

    private BufferedReader br;
    private FileReader fr;

    private int[][] operationGrid; // Logic Matrix, this will be where operation will be performed.
    private int[][] taskGrid; // TNT-TNP data grid
    private int fpaPenalty; // Total cumulative penalty from forced partial assignment.

    private boolean setName = false;
    private boolean setFPA = false;
    private boolean setFM = false;
    private boolean setTNT = false;
    private boolean setMP = false;
    private boolean setTNP = false;
    private int execCycle = 0;

    public static final String INPUTNAME = "problem1.txt";
    public static final byte ASCII_INT_CHAR_FIX = -48;
    public static final byte ASCII_CAP_CHAR_FIX = -65;

    public static final byte FORBIDDEN = -1;
    public static final byte SIZEMAX = 8;
    public static final byte ASSIGNED = -2;
    public static final byte PLACEHOLDER = 0;
    public static final byte TASK = 0;
    public static final byte MACH = 1;

    public static final byte BRACKET = 40;
    public static final byte[] ASCII_INT_RANGE = {47,58};
    public static final byte[] ASCII_UPPER_RANGE = {64,91};
    public static final byte[] ASCII_LOWER_RANGE = {96,123};

    public SplashParser() {
        try {
            // Program will immediately try to find the input file and read them.
            fr = new FileReader(INPUTNAME);
            br = new BufferedReader(fr);
            
            int lineCounter = 0;
            String tempStr = br.readLine();
            byte verify;

            operationGrid = new int[SIZEMAX][SIZEMAX];
            taskGrid = new int[SIZEMAX][SIZEMAX];
            fpaPenalty = 0;

            while (tempStr != null) {
                // Verifies if the line read is not the empty line.
                tempStr = tempStr.trim();
                
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
                        // If a line starts with number, it is presumed that it is the line for Machine Penalty.
                        int[] tempValue = lineProcessor(tempStr);
                        for (byte n = 0; n < SIZEMAX; n++)
                        {
                            // If FPA is detected, process the FPA here, if not, simply assign MP.
                            if (operationGrid[n][lineCounter] == ASSIGNED)
                            {
                                fpaPenalty = fpaPenalty + tempValue[n];
                                operationGrid[n][lineCounter] = 0;
                            }
                            else if (operationGrid[n][lineCounter] == PLACEHOLDER)
                            {
                                operationGrid[n][lineCounter] = tempValue[n];
                            }
                        }
                        // Line counter is used to check how many line of number has been proceed.
                        lineCounter = lineCounter + 1;
                    } else if (setName == true) {
                        // Checks for integer starting name.
                        setName = false;
                    } else {
                        parseError("intCrash");
                    }
                }
                else if (((verify > ASCII_LOWER_RANGE[0]) && (verify < ASCII_LOWER_RANGE[1])) || ((verify > ASCII_UPPER_RANGE[0]) && (verify < ASCII_UPPER_RANGE[1]))) // If the line starts with english letter
                {
                    flagProcessor(tempStr);
                }
                else if (verify != PLACEHOLDER) {
                    // If no criteria is met, but has some ASCII characters, fault.
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

            if (lineCounter != SIZEMAX) {
                // Correct MP input is always 8 lines.
                parseError("intCrash");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No file named " + INPUTNAME + " found, aborting.");
            e.printStackTrace();
        } catch (IOException e) {
            parseError("0");
        }
    }

    // Returns Operation/Task matrix as requested.
    public int[][] fetchMatrix(boolean request) {
        if (request == true) {
            return operationGrid;
        } else {
            return taskGrid;
        }
    }

    // Returns precalculated Forced Assignment Penalty total. 
    public int fetchFPATotal() {
        return fpaPenalty;
    }

    // For debug purpose, prints initialized state of operation or task matrix.
    public void printMe(String context) {
        System.out.println("Initiating Debug Output for " + context);
        int[][] matrix;

        if (context.contentEquals("task")) {
            matrix = taskGrid;
        } else {
            matrix = operationGrid;
        }

        for (byte i=0; i < SIZEMAX; i++) {
            for (byte j=0; j < SIZEMAX; j++) {
                System.out.print("[" + matrix[i][j] + "]");
            }
            System.out.println("");
        }
    }

    // This function is used to assign pair wise input (FPA, FM, TNT, TNP) into appropriate data structure.
    private void assignOperator(int[] newPairs) {
        
        if (setTNP == true) {
            // If target location already forbidden, rewrite is not required.
            if (taskGrid[newPairs[TASK]][newPairs[MACH]] != FORBIDDEN)  {
                taskGrid[newPairs[TASK]][newPairs[MACH]] = newPairs[2];
            }
        }
        else if (setTNT == true) {
            // TNT shouldn't have any error during assignment.
            taskGrid[newPairs[TASK]][newPairs[MACH]] = FORBIDDEN;
        }
        else if (setFM == true) {
            // If FM is mapped to FPA, there will be no solution, which will be handled here.
            if (operationGrid[newPairs[TASK]][newPairs[MACH]] != ASSIGNED)  {
                operationGrid[newPairs[TASK]][newPairs[MACH]] = FORBIDDEN;
            } else {
                parseError("collision");
            }
        }
        else if (setFPA == true) {
            // Assigns FPA, also catches illegal FPA mapping.
            if (operationGrid[newPairs[TASK]][newPairs[MACH]] != FORBIDDEN) {
                for (byte n = 0; n < SIZEMAX; n++) {
                    operationGrid[newPairs[TASK]][n] = FORBIDDEN;
                    operationGrid[n][newPairs[MACH]] = FORBIDDEN;
                }
                operationGrid[newPairs[TASK]][newPairs[MACH]] = ASSIGNED;
            } else {
                parseError("fpa");
            }
        }
    }

    // This function handles flags for data assignment and error checking. It will also check if invalid strings are in the input.
    private void flagProcessor(String flagText) {

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

    // This function processes string in form of "(machine,task)", "(task,task)", or "(machine,task,penalty)" and process into array for data structure.
    private int[] pairProcessor(String tempStr) {
        String[] retStr = tempStr.trim().split(",");
        int[] retVal = new int[retStr.length];

        try {
            // Try to parse the machine (int) input.
            retVal[0] = Integer.parseInt(retStr[0]) - 1;
        } catch (NumberFormatException e) {
            // If it fails, it must be task or invalid, process it as though it is task.
            retVal[0] = (int) retStr[0].charAt(0);
            retVal[0] = retVal[0] + ASCII_CAP_CHAR_FIX;
        }
        
        // Second input is always task.
        retVal[1] = (int) retStr[1].charAt(0);
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
        String[] retStr = tempStr.trim().split(" ");
        int[] retVal = new int[SIZEMAX];

        // If there are more or less than 8 entry, crash.
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
            System.out.println("invalid machine/task");
        }
        else if (erCode.contentEquals("nullP"))
        {
            System.out.println("invalid penalty error");
        }
        else if (erCode.contentEquals("inFault"))
        {
            // It is presumed that Name will not start with bracket.
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

    private void systemStatePrinter() {
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
        System.out.println("Penalty: " + fpaPenalty);
        System.out.println("Cycle: " + execCycle);
        printMe("logic");
        printMe("task");
    }
}