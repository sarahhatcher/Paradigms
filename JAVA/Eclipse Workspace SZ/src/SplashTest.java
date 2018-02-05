import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SplashTest {

	private SplashParser Parser;
	private SplashSearcher Searcher;
	int[][][] pntArray = new int[8][8][2];
	private String line;

	@BeforeEach
	void setUp() throws Exception {
		line = null;
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
/////////////////////////////
/////////////////////////////
/////// ALL ERROR CASES//////	
/////////////////////////////
/////////////////////////////	
 
	/*
	 * Tests For No file Found whe Parseing
	 */
	@Test()
	public void testNoFile() throws IOException{
		Parser = new SplashParser("Null.txt");
		// maybe unneeded as we just exit the system but that doest tell anything to the user
		// perhaps print to output file
	}

	/*
	 * Tests too many Columns 
	 */
	@Test()
	public void test9Columns() throws IOException {
		Parser = new SplashParser("9Col.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests too many Rows
	 */
	@Test()
	public void test9CRows() throws IOException {
		Parser = new SplashParser("9Row.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests Assignment Error. Due to Multiple Forced Assignments
	 */
	@Test()
	public void testAssignmentError() throws IOException {
		Parser = new SplashParser("AssignmentError.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "partial assignment error");
	}
	
	
	/*
	 * Tests to see what happenes in the event of a Forced Task Being Forbidden
	 */
	@Test()
	public void testForcedForbid()  {
		Parser = new SplashParser("ForcedForbid.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests to see what happenes in the event of a Forced Tasks Being Forbidden 
	 * due to a Too Near Task Restriction
	 */
	@Test()
	public void testForcedNear()  {
		Parser = new SplashParser("ForcedNear.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests when An Error in the Categories of the input file are present 
	 */
	@Test()
	public void testInvalidCat()  {
		Parser = new SplashParser("InvalidCat.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	
	/*
	 * Test For an invalid argument 
	 */
	@Test()
	public void testInvalidInterval()  {
		Parser = new SplashParser("InvalidInterval.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "invalid machine/task");
	}
	
	/*
	 * Test for when the penalty is insufficient
	 */
	@Test()
	public void testMissingLine()  {
		Parser = new SplashParser("MissingLine.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Test for when the file is missing a category
	 */
	@Test()
	public void testMissingSection()  {
		Parser = new SplashParser("MissingSection.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}

	
	/*
	 * Test for when there are multiple empty lines between Sections
	 * *Should be fixed by johns new parser*
	 */
	@Test()
	public void testMultiLine()  {
		Parser = new SplashParser("MultiLine.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	@Test()
	public void testPenaltyError() throws IOException {
		Parser = new SplashParser("NegPen.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Machine Penalty Error");
	}
	
	
	
	/*
	 * Tests when NO Possible Task is Available for the AMchine to be Assigned
	 */
	@Test()
	public void testNoLine()  {
		Parser = new SplashParser("NoLine.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "No valid solution possible!");
	}
	
	/*
	 * Tests when NO Possible Task is Available for the AMchine to be Assigned 
	 * due to too near restrictions
	 */
	@Test()
	public void testNoSol()  {
		Parser = new SplashParser("NoSol.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "No valid solution possible!");
	}
	
	/*
	 * Tests Insufficient Penalty Arguments
	 */
	@Test()
	public void testInvalidPenalty() throws IOException {
		Parser = new SplashParser("NotEnough.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests Overlapping Too Near Penalties
	 * *Should be fixed in johns new parser*
	 */
	@Test()
	public void testOverlap() throws IOException {
		Parser = new SplashParser("Overlap.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests to see what happens when 2 names are given
	 */
	@Test()
	public void test2many() throws IOException {
		Parser = new SplashParser("Parse2Name.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}
	
	/*
	 * Tests when Arguments are not separated by a new line.
	 */
	@Test()
	public void testSameLine() throws IOException {
		Parser = new SplashParser("SameLine.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "Error while parsing input file");
	}


/////////////////////////////
/////////////////////////////
/////// SOLUTION CASES //////	
/////////////////////////////
/////////////////////////////
	
	/*
	 * Test Provided Example
	 */
	@Test()
	public void testGiven() throws IOException {
		Parser = new SplashParser("Minimalistic.txt");
		pntArray = Parser.masterGrid;
		Parser.systemStatePrinter();
		SplashSearcher searchInstance = new SplashSearcher(pntArray);
		searchInstance.treeConstruct();

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}
		
		assertEquals(line, "Solution A  B  C  D  E  F  G  H ; Quality: 8");

	}
	
	/*
	 * Test All Forced Assignments
	 */
	@Test()
	public void testForced() throws IOException {
		Parser = new SplashParser("Minimalistic.txt");
		pntArray = Parser.masterGrid;
		Parser.systemStatePrinter();
		SplashSearcher searchInstance = new SplashSearcher(pntArray);
		searchInstance.treeConstruct();

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}
		
		assertEquals(line, "Solution A  B  C  D  E  F  G  H ; Quality: 8");

	}
	
	/*
	 * Tests Richard's Basic File
	 */
	@Test()
	public void testBasic() throws IOException {
		Parser = new SplashParser("Basic.txt");
		pntArray = Parser.masterGrid;
		Parser.systemStatePrinter();
		SplashSearcher searchInstance = new SplashSearcher(pntArray);
		searchInstance.treeConstruct();

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}
		
		assertEquals(line, "Solution A  B  E  C  H  G  F  D ; Quality: 28");

	}
	
	
	
	/*
	 * Tests Richard's 3 sol File. has 3 possible solutions but we're checking for the
	 * best one
	 */
	@Test()
	public void test3Sol() throws IOException {
		Parser = new SplashParser("3Sol.txt");
		pntArray = Parser.masterGrid;
		Parser.systemStatePrinter();
		SplashSearcher searchInstance = new SplashSearcher(pntArray);
		searchInstance.treeConstruct();

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}
		
		//assertEquals(line, "Solution A  B  E  C  H  G  F  D ; Quality: 28");

	}
	
	

	//
	// @Test(expected = InvalidException.class)
	// public void testInvalid() {
	//
	// }

	// "IOexception" > "invalid machine/task" > "Error while parsing input file" >
	// "partial assignment error" > "No valid solution possible!" > "Solution"


}
