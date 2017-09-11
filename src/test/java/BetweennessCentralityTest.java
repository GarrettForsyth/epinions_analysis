package test.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import main.java.BetweennessCentrality;
import main.java.EpinionsGraph;
import main.java.Kosaraju;
import main.java.Main;

public class BetweennessCentralityTest {

    private static ArrayList<LinkedList<Integer>> graph;
	private static HashMap<Integer,Integer> SCCs;
	private static HashMap<Integer,Integer> expectedSCCs;
	private static ArrayList<Integer> largetstSCC;
	private static ArrayList<Integer> expectedLargestSCC;
	private static int[] expectedOutput;
	private static String testFileName;
	
	@BeforeClass
	public static void setup(){
		testFileName= "build/test/testFiles/bcTestGraph.txt";
		graph = EpinionsGraph.createUnSignedGraphFrom(testFileName);
	}

	@Test
	public void testExpectedBetweennessCentrality(){
		double[] bc = BetweennessCentrality.computeBCtoArray(graph,
				Kosaraju.getVerticesInLargestSCC(graph));
		System.out.println(Kosaraju.getVerticesInLargestSCC(graph));
		System.out.println(Arrays.toString(bc));
	}


}
