package api;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import org.w3c.dom.Node;


public class DWGraph_Algo implements dw_graph_algorithms{
	private  directed_weighted_graph DirectedWeightedGgraph ;
	static int Time = 0;

	@Override
	public void init(directed_weighted_graph g) {
		this.DirectedWeightedGgraph=g;

	}

	@Override
	public directed_weighted_graph getGraph() {
		return this.DirectedWeightedGgraph;
	}

	@Override
	public directed_weighted_graph copy() {
		directed_weighted_graph NewGraph = new DWGraph_DS();
		for (node_data nodeToCopy : this.DirectedWeightedGgraph.getV()) {
			int NodeKey = nodeToCopy.getKey();
			node_data AllNewNode = this.DirectedWeightedGgraph.getNode(NodeKey);
			NewGraph.addNode(AllNewNode);
			String nodeInfo = nodeToCopy.getInfo();
			NewGraph.getNode(NodeKey).setInfo(nodeInfo);
		}
		for (node_data nodeToCopy : this.DirectedWeightedGgraph.getV()) {
			int NodeKey = nodeToCopy.getKey();
			for (edge_data edgeToCopy : this.DirectedWeightedGgraph.getE(NodeKey)) {
				NewGraph.connect(NodeKey, edgeToCopy.getDest(), edgeToCopy.getWeight());
			}
		}
		return NewGraph;
	}

	@Override
	public boolean isConnected() {
		if (this.DirectedWeightedGgraph.nodeSize() == 0 || this.DirectedWeightedGgraph.nodeSize() == 1) {//if the g is null or onr he isConnected
			return true;
		}
		else
		{
			connected c=new connected(DirectedWeightedGgraph.getV());
			resetDiscLowSm(c);
			LinkedList<LinkedList<Integer>> l=SCC(c);

			if(l.size()==1)
				return true;
			else
				return false;

		}

	}
	protected void resetDiscLowSm(connected c) {
		directed_weighted_graph g = DirectedWeightedGgraph;

		for (connected.ver nd : c.getHM().values()) {
			nd.setDisc(-1);
			nd.setDisc( -1);
			nd.setLow (-1);
			nd.setSm( false);
		}
	}
	void SCCUtil(int u, Stack<Integer> st, LinkedList<LinkedList<Integer>>com,connected c)
	{
		directed_weighted_graph g=DirectedWeightedGgraph;
		//DWGraph_DS.NodeData nd=(DWGraph_DS.NodeData) g.getNode(u);
		connected.ver uFields=c.getNode(u);
		uFields.setDisc(Time);
		uFields.setLow(Time);
		Time += 1;
		uFields.setSm(true);
		st.push(u);
		connected.ver niFields;
		for(edge_data edge: g.getE(u))
		{

			niFields=c.getNode(edge.getDest());
			if (niFields.getDisc() == -1)
			{
				SCCUtil(niFields.getKey(), st,com,c);

				uFields.setLow(Math.min(uFields.getLow(),niFields.getLow()));
			}
			else if (niFields.isSm() == true)
			{


				uFields.setLow(Math.min(uFields.getLow(), niFields.getDisc()));
			}
		}

		int w = -1;
		LinkedList<Integer>list=new LinkedList<>();
		if (uFields.getLow() == uFields.getDisc())
		{
			while (w != u)
			{

				w = (int)st.pop();
				//DWGraph_DS.NodeData wn=(DWGraph_DS.NodeData) g.getNode(w);
				connected.ver wn=c.getNode(w);
				list.add(w);
				//System.out.print(w + " ");
				wn.setSm(false);

			}
			com.add(list);
		}
	}


	LinkedList<LinkedList<Integer>> SCC(connected c)
	{

		directed_weighted_graph g=DirectedWeightedGgraph;
		LinkedList<LinkedList<Integer>>com=new LinkedList<>();
		Stack<Integer> st = new Stack<Integer>();

		for(node_data nodes: g.getV())
		{
			int key=nodes.getKey();
			if( c.getNode(key).getDisc() == -1)
				SCCUtil(key, st,com,c);
		}
		return com;
	}




	@Override
	public double shortestPathDist(int src, int dest) {
		directed_weighted_graph g= DirectedWeightedGgraph;
		HashMap<Integer, node_data> nodesMap = new HashMap<Integer, node_data>();
		for (node_data nodeData : g.getV()) {
			nodeData.setWeight(Integer.MAX_VALUE);
		}
		node_data start = g.getNode(src);
		start.setWeight(0);
		nodesMap.put(src, start);
		while (!nodesMap.isEmpty()) {
			node_data currentNode = this.findMinimumDistanceNode(nodesMap);
			if (currentNode.getKey() == dest) {
				break;
			}
			int currentNodeKey = currentNode.getKey();
			double currentNodeDistance = currentNode.getWeight();
			nodesMap.remove(currentNodeKey);

			for (edge_data e : g.getE(currentNodeKey)) {
				Integer neighbor=e.getDest();
				double distanceToNeighbor = g.getEdge(currentNodeKey, neighbor).getWeight() + currentNodeDistance;
				node_data nei=g.getNode(neighbor);

				if (distanceToNeighbor < nei.getWeight() ) {
					nei.setWeight(distanceToNeighbor);
					nodesMap.put(neighbor, nei);
				}
			}
		}

		double distance = g.getNode(dest).getWeight();

		return distance;
	}

	private node_data findMinimumDistanceNode(HashMap<Integer, node_data> nodesMap) {
		node_data minimumDistanceNode = null;
		double minimumDistance = Integer.MAX_VALUE;
		for (node_data node : nodesMap.values()) {
			if (node.getWeight() <= minimumDistance) {
				minimumDistanceNode = node;
				minimumDistance = node.getWeight();
			}
		}

		return minimumDistanceNode;
	}

/*	@Override
	public List<node_data> shortestPath(int src, int dest) {
		DWgraph_DS g=((DWgraph_DS)DirectedWeightedGgraph);
		if (g != null&&g.getV()!=null)//if there's g with vertices
		{
			DWgraph_DS.NodeData n=((DWgraph_DS.NodeData)g.getNode(src)); //n= the starting vertex
			DWgraph_DS.NodeData d=((DWgraph_DS.NodeData)g.getNode(dest));//d= the destination vertex

			if (n != null && d != null&&g.getV().contains(d)&&g.getV().contains(d)&&!g.getNi(src).isEmpty()) {//if the vertices exist and belong to the g
				//and if the destination vertex have any neighbors
				for (node_data nodeData : g.getV()) {
					nodeData.setWeight(Integer.MAX_VALUE);
					nodeData.setTag(-1);
				}
				if (dijkstra(n,d)) { //do dijkstraPath function, if a path is found, do:
					LinkedList<node_data> path = new LinkedList<>(); // create a list that illustrate the shortest path
					node_data crawl = d;//save the last vertex= the dest of the path
					path.addFirst(crawl); //add it to the list of path
					while (crawl.getTag() != -1) { //while isn't the start og the path
						path.addFirst(g.getNode((crawl).getTag()));// add to the list the previous vertex that we came from it to the current vertex
						crawl =g.getNode(crawl.getTag());// in the next iteration the tests will be on the previous vertex
					}

					return path;//return the list of the shortest path
				}
			}



			return null;// if there's no path or the vertices aren't exist, return null
		}
		return  null;

	}
*/@Override
public List<node_data> shortestPath(int src, int dest) {
	directed_weighted_graph g= DirectedWeightedGgraph;
	HashMap<Integer, node_data> nodesMap = new HashMap<Integer, node_data>();
	for (node_data nodeData : g.getV()) {
		nodeData.setWeight(Integer.MAX_VALUE);
	}
	HashMap<Integer, LinkedList<node_data>> shortestPathMap = new HashMap<>();
	for (node_data nodeData : g.getV()) {
		shortestPathMap.put(nodeData.getKey(), new LinkedList<>());
	}
	node_data start = g.getNode(src);
	start.setWeight(0);
	nodesMap.put(src, start);
	while (!nodesMap.isEmpty()) {
		node_data currentNode = this.findMinimumDistanceNode(nodesMap);
		if (currentNode.getKey() == dest) {
			break;
		}
		int currentNodeKey = currentNode.getKey();
		double currentNodeDistance = currentNode.getWeight();
		nodesMap.remove(currentNodeKey);
		LinkedList<node_data> currentNodePath;
		 currentNodePath= shortestPathMap.get(currentNodeKey);
		for (edge_data n : (g.getE(currentNodeKey))) {
			int neighbor=n.getDest();
			double distanceToNeighbor = g.getEdge(currentNodeKey, neighbor).getWeight() + currentNodeDistance;
			node_data nei = g.getNode(neighbor);
			if (distanceToNeighbor < nei.getWeight() ) {
				LinkedList<node_data> currentNeighborPath = new LinkedList<>(currentNodePath);
				currentNeighborPath.add(nei);
				shortestPathMap.put(nei.getKey(), currentNeighborPath);
				nei.setWeight(distanceToNeighbor);
				nodesMap.put(nei.getKey(), nei);
			}
		}
	}

	return shortestPathMap.get(dest);
}

	@Override
	public boolean save(String file) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(DirectedWeightedGgraph);
		//System.out.println(json);
		try {

			PrintWriter pw = new PrintWriter(new File(file));
			pw.write(json);
			pw.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean load(String file) {
			try
			{
				GsonBuilder builder = new GsonBuilder();
				builder.registerTypeAdapter(DWGraph_DS.class, new json_to_graph());
				Gson gson = builder.create();
				//continue as usual..

				FileReader reader = new FileReader(file);
				DirectedWeightedGgraph= gson.fromJson(reader, DWGraph_DS.class);
				return true;
				//System.out.println(graph);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		return false;
	}
	public boolean dijkstra(DWGraph_DS.NodeData n, DWGraph_DS.NodeData d)
	{
		/*DWgraph_DS g=((DWgraph_DS)DirectedWeightedGgraph);

		n.setWeight(0); //the distance between vertex and itself is zero
		PriorityQueue<DWgraph_DS.NodeData>queue=new PriorityQueue<>();
		// create priority queue of vertices, the priority is about the tag of every vertex
		queue.add(n);// adding the starting vertex
		while(!queue.isEmpty())//while there are no vertices to check in order to find the shortest path
		{
			DWgraph_DS.NodeData current=queue.poll(); // get the minimum tag vertex , and remove it from the queue
			if(current.getKey()==d.getKey()) // if we through all the paths to the destination
				return true;//we have already scan all the paths and found the shortest
			for(int i: g.getNi(current.getKey()).keySet())//pass the neighbors of current vertex
			{
				DWgraph_DS.NodeData currentNi=((DWgraph_DS.NodeData)g.getNode(i)); //currentNi- the current neighbor of the current vertex

				if(!g.isSm(current.getKey()))//if the neighbor hasn't visited yet
				{
					boolean notExistInQueue=(currentNi.getWeight()== Double.POSITIVE_INFINITY);//replace queue.contains
					double t = current.getWeight() +g.getEdge(current.getKey(),i).getWeight() ;
					//t- the weight of the edge(current,currentNi) + the tag(total distance) of current
					if (t < currentNi.getWeight())//if we found another path to the neighbor with less total distance to it
					{
						currentNi.setWeight(t);
						currentNi.setTag(current.getKey());
					}//update the tag to be t-the total distance of the last path

					if(notExistInQueue)//if it's the first time to reach this neighbor
						queue.add(currentNi);//add this neighbor to the queue
						
				}


			}

			g.setSm(current.getKey(),true);//we have done passing all the neighbors of the current vertex, so we are marking it.

		}
		// if there's no path to the destination return the distance is infinity.*/
		return false;
	}
	}