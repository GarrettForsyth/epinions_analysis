# Scope and Definition 

## Overview: 

This project aims to analyze a signed social network graph from [Epinions.com](http://epinions.com/) using graph algorithms to extract information regarding the Epinions.com community. 

In particular, it aims at identifying users who hold positions of influence over the community. Such users would be of interest to marketers and advertisers who would like good reviews of their products.

## Data:

### Epinions.com

Epinions is a general consumer review site. The vertices in this graph model the accounts in the Epinions community, and the edges represent relationships between accounts. If an account trusts another account’s reviews, it is indicated with a positively weighted edge. If an account mistrust another’s reviews, it is indicated with a negatively weighted edge. 
Graph Type: directed signed social network

Data retrieved from [https://snap.stanford.edu/data/soc-sign-epinions.html](https://snap.stanford.edu/data/soc-sign-epinions.html);

- Vertices : 131828
- Edges: 841372

The relationship between the number of vertices and number of edges is roughly linear so it would be considered a sparse graph and best represented as an adjacency list. 

## Questions:

1) Are there tightly-knit groups within the community?
How many of these subgroups are there? How big are they? 

2) How does information/trust flow through the network? Where are the heaviest areas of flow?

3) Do some people have more power/influence than others? If so, how much?

4) Is the graph in structural balance or weakly balanced?  (A structurally balanced social network is a social community that splits into two antagonistic factions (typical example being a two-party political system) See [https://courses.cit.cornell.edu/info204_2007sp/balance.pdf](https://courses.cit.cornell.edu/info204_2007sp/balance.pdf) for details.)

## Algorithms and Data Structures:

### 1) Are there tightly-knit groups within the community?
How many of these subgroups are there? How big are they? 

A broad sense of this can be found by calculating the clustering coefficient (the probability that two randomly selected friends of a node are friends with each other) of the average user. The clustering coefficient reflects the tendency of nodes to cluster together, so a high clustering coefficient would mean there are many tightly knit groups.

This can be done for each node by checking if the any two neighbour’s contain each other in their neighbour list entries.

More concretely, this can be seen by  performing a graph partitioning algorithm. The one I plan to implement is Kosaraju’s two pass algorithm which will find the strongly connected components (SCC’s) in the graph. 

### 2) How does information/trust flow through the network? Where are the heaviest areas of flow?

From wikipedia : max-flow min-cut theorem states that in a flow network, the maximum amount of flow passing from the source to the sink is equal to the total weight of the edges in the minimum cut,

The max flow can therefore be found by finding the min cuts of the graph. One way to find the min cuts is through a randomized contraction algorithm. Vertices will randomly contract/merge until only two vertex remain and the edges connecting them represent the minimum cuts. 

### 3) Do some people have more power/influence than others?

Are there any structural holes? (where a group of vertices has only a few vertices interfacing with other vertices) ? This would indicate a position of power for the person filling the structural hole as they are more likely to hear of ‘rare’ information and also play the role of ‘gatekeeper’ for this information to the rest of the vertices in their group.  These can be found by calculating the ‘embeddedness’ (number of common neighbours two endpoints have) of edges. 

To calculate the embeddedness, one can iterate through each pair of vertices and compare their neighbours and track how many they have in common. An edge that isn't very embedded will be  on the peripheral of their groups (or not in groups at all). An embeddedness of zero means the edge is a local bridge (an edge connecting two groups). 

Other ways for a node to have power are: 
  i) how many other nodes ‘depend’ on this node for information
  ii) a node’s ability to exclude other nodes from the rest of the network
  iii) ‘satiation’ or a nodes that has a variety of options for friends and can pick and choose to maintain only the relationships that yield an unequal share of power in favour of itself

### 4) Is the graph in structural balance or weakly balanced?

This can be found by identifying relationship triangles between accounts. And classifying whether they are in a balanced or imbalanced orientation. All triangles must be balanced for the network to be balanced.

An algorithm for counting and listing triangles can be found here: [http://i11www.iti.kit.edu/extra/publications/sw-fclt-05_wea.pdf](http://i11www.iti.kit.edu/extra/publications/sw-fclt-05_wea.pdf) 

Alternatively, one could look at the macroscopic groups. A structurally balanced network will have only two groups, all of which are friends with all vertices inside their own group and enemies with the group. To be weakly balanced is similar, but it is allowed to have more than two groups. 

## Algorithm Analysis, Limitations and Risks:
 
### 1)  Firstly, finding the clustering coefficient:


for each vertex, v :
	get neighbours of v;
	for each neighbour of v, w:
		get neighbours of w;
		for each neighbour of w:
			increment total;
			if neighbours of v contains w
				increment counter;

return counter/total;

Here, we iterate through all vertices once, then for each vertex we iterate over its neighbours and for each neighbour we iterate over its neighbours. In a completely connected graph, this would be of complexity of theta(n^3), but since this is a sparse graph, we can expect something better, probably closer to O(n^2). 

To find the strongly connected components using Kosaraju’s algorithm: 

From wikipedia:

For each vertex u of the graph, mark u as unvisited. Let L be empty. 
1. For each vertex u of the graph do Visit(u), where Visit(u) is the recursive subroutine: 
  If u is unvisited then: 
   1. Mark u as visited. 
   2. For each out-neighbour v of u, do Visit(v). 
   3. Prepend u to L. 
  Otherwise do nothing. 

2. For each element u of L in order, do Assign(u,u) where Assign(u,root) is the recursive subroutine: 
  If u has not been assigned to a component then: 
  1. Assign u as belonging to the component whose root is root. 
  2. For each in-neighbour v of u, do Assign(v,root). 
  Otherwise do nothing. 

This algorithm works in O(V+E) or linearly (for an adjacency list)!


### 2) To find the min cuts using a randomized contraction algorithm:

From wikipedia:

1)  Initialize contracted graph CG as copy of original graph
2)  While there are more than 2 vertices.
      a) Pick a random edge (u, v) in the contracted graph.
      b) Merge (or contract) u and v into a single vertex (update 
         the contracted graph).
      c) Remove self-loops
3) Return cut represented by two vertices.


One run will take O(n^2). This is a random algorithm and its correctness is not guaranteed. To get a probabilistic guarantee, the algorithm should be run lg(n)\*n^2 times and take the fewest min cuts found. This brings the total running time up to O(n^4lg(n)). This is pretty high, and it might be best to run fewer runs and get an approximate result which should still be pretty good.

### 3) To calculate the embeddedness:
 One can iterate through each pair of vertices and compare their neighbours and track how many they have in common. An edge that isn’t very embedded will be  on the peripheral of their groups (or not in groups at all). An embeddedness of zero means the edge is a local bridge (an edge connected to two groups). 

For each edge e:
	counter = 0
	v1 = head vertex
	v2 = tail vertex
	for each of v1’s neighbours, w1:
		if  v2’s neighbours contain w1:
			increase counter
	set e’s embeddedness to counter

So it requires an iteration through all edges, and then for each an iteration through the neighbours of two vertices. In a completely connected graph, this would take O(m\*n\*n), but since this is a sparse graph, the complexity is probably more like O(m\*n) . 

4) See [http://i11www.iti.kit.edu/extra/publications/sw-fclt-05_wea.pdf](http://i11www.iti.kit.edu/extra/publications/sw-fclt-05_wea.pdf) for pseudo code to count and list the  number of triangles in the graph. The running time given in the paper is O(m^3/2).

## Additional Risks:

There is a high number of vertices in this graph (Vertices : 131828) and so the algorithms running in a complexity quadratic and above might prove to be too slow. 

Many of these questions assume there to be tightly knitted groups in the network (which is common for social networks), but if there are none, some of these questions would lose their significance.

The pseudo code here is kept very high level and might prove difficult to implement in practice. 













