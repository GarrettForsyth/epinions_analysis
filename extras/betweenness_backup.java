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

public class BetweennessCentrality {

    private static double[] betweennessCentrality;

	public static void computeBetweennessCentrality(ArrayList<LinkedList<Integer>> graph,
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

		writeResultsToFile();
		
	}

	private static void writeResultsToFile(){
                                                         
    	BufferedWriter bw = null;
    	FileWriter fw = null;
                                                         
    	try{
    		String content = formatDataAsString(); 
                                                         
    		fw = new FileWriter("../epinions_betweenness_centrality_reversed.txt");
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

		System.out.println(data.size());
		BetweennessCentralityComparator cmp = new BetweennessCentralityComparator(data);
		Integer[] vertexIds = cmp.createIndexArray();
		Arrays.sort(vertexIds, cmp);

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
