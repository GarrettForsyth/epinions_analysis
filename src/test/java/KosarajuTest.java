package test.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import main.java.EpinionsGraph;
import main.java.Kosaraju;

public class KosarajuTest {

    private static ArrayList<LinkedList<Integer>> graph;
	private static int[] expectedOutput;
	private static String testFileName;

	private static HashMap<Integer,Integer> SCCs;
	private static HashMap<Integer,Integer> expectedSCCs;
	private static ArrayList<Integer> largetstSCC;
	private static ArrayList<Integer> expectedLargestSCC;
	
	@BeforeClass
	public static void setup(){
		testFileName= "build/test/testFiles/testGraph.txt";
		graph = EpinionsGraph.createUnSignedGraphFrom(testFileName);
		expectedOutput = new int[]{0,1,2,3,6,6,6,7,8,9};

		expectedSCCs = new HashMap<>();
		expectedSCCs.put(0,1);
		expectedSCCs.put(1,1);
		expectedSCCs.put(2,1);
		expectedSCCs.put(3,1);
		expectedSCCs.put(6,3);
		expectedSCCs.put(7,1);
		expectedSCCs.put(8,1);
		expectedSCCs.put(9,1);
																	
		expectedLargestSCC = new ArrayList<>(Arrays.asList(4,5,6));
	}

	@Test
	public void testExpectedOutput(){
	    assertArrayEquals(expectedOutput, Kosaraju.findSCC(graph));	
	}

	@Test
	public void testExpectedSCC(){
		assertEquals(expectedSCCs,Kosaraju.getSCCsAsHashMap(graph));
		assertEquals(expectedLargestSCC, Kosaraju.getVerticesInLargestSCC(graph)); 
	}

}
