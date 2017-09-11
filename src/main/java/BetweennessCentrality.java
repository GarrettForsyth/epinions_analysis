package main.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/* Given a graph and a list of vertices in a SCC, this class will calculate
 * the betweenness centrality for the nodes in the SCC.
 *
 * The graph is needed to keep track of the correct vertex labels.
 *
 * It can either return the results as an array, or since the calculating
 * time may be a long time, it also can  write the results to a file. 
 *
 */

public class BetweennessCentrality {

    private static double[] betweennessCentrality;
	
	
	public static double[] computeBCtoArray(ArrayList<LinkedList<Integer>> graph,
			ArrayList<Integer> verticesInLargestSCC){

		computeBetweennessCentrality(graph, verticesInLargestSCC);

		return betweennessCentrality;
	}
	
	
	public static void computeBCtoFile(ArrayList<LinkedList<Integer>> graph,
			ArrayList<Integer> verticesInLargestSCC,String toFileName){

		computeBetweennessCentrality(graph, verticesInLargestSCC);
		writeResultsToFile(toFileName);

	}
	
	private static void computeBetweennessCentrality(ArrayList<LinkedList<Integer>> graph,
			ArrayList<Integer> verticesInLargestSCC){

		betweennessCentrality = new double[graph.size()];

		for( int startVertex : verticesInLargestSCC){
			Stack orderOfDiscovery = new Stack();
			// a list of vertices that reach startVertex through a shortest path
			HashMap<Integer, LinkedList<Integer>> predecessors = new HashMap<>();
			
			HashMap<Integer,Integer> distanceTo = new HashMap<>();
			distanceTo.put(startVertex,0);

			// tracks the number of shortest paths from startVertex to the rest
			HashMap<Integer,Integer> numberOfShortestPathsTo = new HashMap<>();
			numberOfShortestPathsTo.put(startVertex,1);

			PriorityQueue<Integer> unExploredVertices = new PriorityQueue<>();
            unExploredVertices.add(startVertex);

			BFS(unExploredVertices, orderOfDiscovery, distanceTo, numberOfShortestPathsTo,
					predecessors, graph);

			accumulate(orderOfDiscovery, predecessors, numberOfShortestPathsTo, startVertex);
		}

	}

	private static void BFS(PriorityQueue<Integer> unExploredVertices, Stack orderOfDiscovery,
		   	HashMap<Integer,Integer> distanceTo, HashMap<Integer,Integer> numberOfShortestPathsTo,
			HashMap<Integer,LinkedList<Integer>> predecessors, ArrayList<LinkedList<Integer>> graph){

		while(!unExploredVertices.isEmpty()){
			int unExploredVertex = unExploredVertices.poll();
			orderOfDiscovery.push(unExploredVertex);
																																					
			if(!distanceTo.containsKey(unExploredVertex)){
				distanceTo.put(unExploredVertex, -1);
			}
																																					
			for(int neighbour : graph.get(unExploredVertex)){
				if(!distanceTo.containsKey(neighbour)){
					distanceTo.put(neighbour, -1);
				}
				
				// is neighbour found for the first time?
				if (distanceTo.get(neighbour) < 0){
					unExploredVertices.add(neighbour);
					distanceTo.put(neighbour, distanceTo.get(unExploredVertex) +1);
				}	
				// is shortest path to neighbour  via the unExploredVertex ? 
				if (distanceTo.get(neighbour) == distanceTo.get(unExploredVertex) + 1){
					if(!numberOfShortestPathsTo.containsKey(neighbour)) numberOfShortestPathsTo.put(neighbour, 0);
					if(!numberOfShortestPathsTo.containsKey(unExploredVertex)) numberOfShortestPathsTo.put(unExploredVertex, 0);
																																					
					numberOfShortestPathsTo.put(neighbour, numberOfShortestPathsTo.get(neighbour) + numberOfShortestPathsTo.get(unExploredVertex));
					if(!predecessors.containsKey(neighbour)) predecessors.put(neighbour, new LinkedList<Integer>());
					predecessors.get(neighbour).add(unExploredVertex);
				}
			}
		}
	}

	private static void accumulate(Stack orderOfDiscovery, HashMap<Integer,LinkedList<Integer>> predecessors,
		   	HashMap<Integer,Integer> numberOfShortestPathsTo, int startVertex){

		HashMap<Integer, Double> dependency = new HashMap<>();
																																													
		// S returns vertices in order of non-increasing distance from s
		while (!orderOfDiscovery.isEmpty()){
			int discoveredVertex = (Integer) orderOfDiscovery.pop();
			if(predecessors.containsKey(discoveredVertex)){
				for (int v : predecessors.get(discoveredVertex)){
					if(!dependency.containsKey(v)) dependency.put(v, 0.0);	
					if(!dependency.containsKey(discoveredVertex)) dependency.put(discoveredVertex, 0.0);	
																																													
					dependency.put( v , dependency.get(v) + (numberOfShortestPathsTo.get(v)/numberOfShortestPathsTo.get(discoveredVertex))*(1.0+dependency.get(discoveredVertex)));
					if(discoveredVertex != startVertex){
						betweennessCentrality[discoveredVertex]+= dependency.get(discoveredVertex);
					}                                                                                                                                                              		
				}
			}
		}	
	}

	private static void writeResultsToFile(String toFileName){

    	BufferedWriter bw = null;
    	FileWriter fw = null;

    	try{
    		String content = formatDataAsString(); 

    		fw = new FileWriter(toFileName);
    		bw = new BufferedWriter(fw);
    		bw.write(content);

    		System.out.println("done");

    	}catch(IOException e){
    		e.printStackTrace();
    	}finally{

    		try{
    			if (bw != null) bw.close();
    			if (fw != null) fw.close();
    		
        	}catch(IOException ex){

    		ex.printStackTrace();
         	}
    	}

    }

	private static String formatDataAsString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < betweennessCentrality.length; i++){
			sb.append(i + "\t" + betweennessCentrality[i] + "\n");
		}
		return sb.toString();
	}

	private static String formatSortedDataAsString(Integer[] sortedIndices, ArrayList<Double> data){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sortedIndices.length; i++){
			sb.append(sortedIndices[i] + "\t" +  data.get(sortedIndices[i]) + "\n");
		}
		return sb.toString();
	}
	
	public static void readBCFromFileAndSortAndWrite(String fileName){

		ArrayList<Double> data = readFile(fileName);
		Integer[] vertexIds = null;
		sortData(data, vertexIds);
        writeSortedDataToFile(data, "../sortedBCValues.txt", vertexIds);

	}

	private static ArrayList<Double> readFile(String fileName){

		BufferedReader br = null;
		FileReader fr = null;
		ArrayList<Double> data = new ArrayList<>();

		try{

			String[] splitLine;
			String currLine;
			br = new BufferedReader(new FileReader(fileName));

			while((currLine = br.readLine()) != null){
				splitLine = currLine.split("[^\\.A-Za-z0-9_]");
				double val = Double.valueOf(splitLine[1]).longValue();
				if (val != 0.0)	data.add(val);
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(br != null) br.close();
				if(fr != null) fr.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}

		return data;
	}

	private static  void sortData(ArrayList<Double> data, Integer[] vertexIds){

		BetweennessCentralityComparator cmp = new BetweennessCentralityComparator(data);
        vertexIds = cmp.createIndexArray();
        Arrays.sort(vertexIds, cmp);

	}

	private static void writeSortedDataToFile(ArrayList<Double> data, String toFileName, Integer[] vertexIds){

		BufferedWriter bw = null;                                                  	
        FileWriter fw = null;

        try{
        	String content = formatSortedDataAsString(vertexIds,data); 

        	fw = new FileWriter("../sortedBCValues.txt");
        	bw = new BufferedWriter(fw);
        	bw.write(content);

        	System.out.println("done");

        }catch(IOException e){
        	e.printStackTrace();
        }finally{

        	try{
        		if (bw != null) bw.close();
        		if (fw != null) fw.close();
        	
        	}catch(IOException ex){

        		ex.printStackTrace();
        	}
        }
	}
}
