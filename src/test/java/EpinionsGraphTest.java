package test.java;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import main.java.EpinionsGraph;

public class EpinionsGraphTest {

    private static ArrayList<LinkedList<Integer>>  graph;
    private static ArrayList<LinkedList<Integer>>  expectedGraph;
	private static int expectedSize;
	private static String testFileName; 
	
	@BeforeClass
	public static void setup() { 
		testFileName= "build/test/testFiles/testGraph.txt";
		expectedSize = 10;
		graph = EpinionsGraph.createUnSignedGraphFrom(testFileName);
		expectedGraph= new ArrayList<>();
	    expectedGraph.add(new LinkedList<>());
	    expectedGraph.add(new LinkedList<>(Arrays.asList(2,3)));
	    expectedGraph.add(new LinkedList<>(Arrays.asList(3,4)));
	    expectedGraph.add(new LinkedList<>(Arrays.asList()));
	    expectedGraph.add(new LinkedList<>(Arrays.asList(5)));
	    expectedGraph.add(new LinkedList<>(Arrays.asList(6)));
	    expectedGraph.add(new LinkedList<>(Arrays.asList(4,7)));
	    expectedGraph.add(new LinkedList<>(Arrays.asList()));
	    expectedGraph.add(new LinkedList<>(Arrays.asList(7,9)));
	    expectedGraph.add(new LinkedList<>(Arrays.asList(7)));
	}

	@Test
	public void testGraphCorrectSize(){
		assertEquals(expectedSize, graph.size());
	} 

	@Test
	public void testExpectedOutput(){
		assertEquals(expectedGraph, graph);
	}

	@Test
	public void testCreateEmptyGraph(){
		assertEquals(new ArrayList<LinkedList<Integer>>(), EpinionsGraph.createUnSignedGraphFrom("build/test/testFiles/testEmptyGraph.txt"));
		
	}
}
