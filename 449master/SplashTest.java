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

	@Test()
	public void testNoFile() throws IOException{
		Parser = new SplashParser("Null.txt");
		// maybe unneeded as we just exit the system but that doest tell anything to the user
		// perhaps print to output file
	}

	@Test()
	public void testInvalid()  {
		Parser = new SplashParser("InvalidDescrption.txt");

		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "invalid task description");
	}
	
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

	@Test()
	public void testParserError() throws IOException {
		Parser = new SplashParser("ParseError.txt");
		
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
		Parser = new SplashParser("PenaltyError.txt");
		
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
	
	@Test()
	public void testInvalidPenalty() throws IOException {
		Parser = new SplashParser("InvalidPenalty.txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
			line = reader.readLine();
			reader.close();
		} catch (IOException e) {
			System.out.println(e); 
			e.printStackTrace();
		}

		assertEquals(line, "invalid penalty");
	}

	@Test()
	public void testNoSol1() throws IOException {
		Parser = new SplashParser("NoSol1.txt");
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

		assertEquals(line, "No valid solution possible!");
		
	}
	
	@Test()
	public void testNoSol2() throws IOException {
		Parser = new SplashParser("NoSol2.txt");
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

		assertEquals(line, "No valid solution possible!");
		
	}

	@Test()
	public void testSol1() throws IOException {
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

	//
	// @Test(expected = InvalidException.class)
	// public void testInvalid() {
	//
	// }

	// "IOexception" > "invalid machine/task" > "Error while parsing input file" >
	// "partial assignment error" > "No valid solution possible!" > "Solution"


}
