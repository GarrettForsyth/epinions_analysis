package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class EpinionsGraph{

    private static ArrayList<LinkedList<Integer>> eGraph;
	private static int graphType;
	private static final int UNSIGNED = 0;
	private static final int SIGNED = 1;
	private static final int FRIENDS =2;
	private static final int ENEMIES =3;

	public static ArrayList<LinkedList<Integer>> createUnSignedGraphFrom(String dataFileName){

        eGraph = new ArrayList<>();
		graphType = UNSIGNED;

		try{
		    populateGraphFrom(dataFileName);	
		} catch(IOException e){
			e.printStackTrace();
		}

		return eGraph;
	}

	public static ArrayList<LinkedList<Integer>> createSignedGraphFrom(String dataFileName){
																							   
		eGraph = new ArrayList<>();
		graphType = SIGNED;
																							   
		try{
			populateGraphFrom(dataFileName);	
		} catch(IOException e){
			e.printStackTrace();
		}
																							   
		return eGraph;
	}

	public static ArrayList<LinkedList<Integer>> createFriendGraphFrom(String dataFileName){
    																						   
    	eGraph = new ArrayList<>();
    	graphType = FRIENDS;
    																						   
    	try{
    		populateGraphFrom(dataFileName);	
    	} catch(IOException e){
    		e.printStackTrace();
    	}
    																						   
    	return eGraph;
    }

	public static ArrayList<LinkedList<Integer>> createEnemyGraphFrom(String dataFileName){
    																						   
    	eGraph = new ArrayList<>();
    	graphType = ENEMIES;
    																						   
    	try{
    		populateGraphFrom(dataFileName);	
    	} catch(IOException e){
    		e.printStackTrace();
    	}
    																						   
		return eGraph;    
	}

	private static void populateGraphFrom(String dataFileName) throws IOException{
   
		BufferedReader br = null;

		try{
			br = createBufferedReader(dataFileName);
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}

		populateGraph(br);
	}

	private static BufferedReader createBufferedReader(String dataFileName) throws FileNotFoundException{
		return new BufferedReader(new FileReader(dataFileName));
	}

	private static void populateGraph(BufferedReader br) throws IOException{

		String currentLine;
		String[] lineContents;

		while((currentLine = br.readLine()) != null){
			lineContents = currentLine.split("\\W+");	
			int headVertexId = Integer.parseInt(lineContents[1]);
			int tailVertexId = Integer.parseInt(lineContents[0]);
		    int relationshipType = Integer.parseInt(lineContents[2]);	
			// make sure vertex isn't skipped because it has no outgoing edges
			while (headVertexId >= eGraph.size() || tailVertexId >= eGraph.size()){
				eGraph.add(new LinkedList<>());
			}
            
			switch (graphType){

				case UNSIGNED: eGraph.get(headVertexId).add(tailVertexId);
							   break;
				case SIGNED:   eGraph.get(headVertexId).add(tailVertexId*relationshipType);
							    break;
				case FRIENDS:  if(relationshipType > 0) eGraph.get(headVertexId).add(tailVertexId*relationshipType);
								break;
				case ENEMIES:  if(relationshipType < 0) eGraph.get(headVertexId).add(tailVertexId*relationshipType);
								break;
				default:        break;
			}
		}
	}
}
