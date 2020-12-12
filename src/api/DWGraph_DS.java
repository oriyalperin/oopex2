package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;




public class DWGraph_DS implements directed_weighted_graph {
	private HashMap<Integer,node_data> Nodes= new HashMap();
	private HashMap<Integer,HashMap<Integer,edge_data>> Edges= new HashMap();
	private HashMap<Integer,ArrayList<Integer> > successors;
	private	int edgeCount;
	private int ModeCount;
	private static int keyCount=0;

	public void toj() {

		Gson objGson = new GsonBuilder().setPrettyPrinting().create();
		objGson.toJson(Nodes);

	}
	public DWGraph_DS()
	{
		Nodes=new HashMap<>();
		Edges=new HashMap<>();
		successors= new HashMap<>();


	}
	
	@Override
	public node_data getNode(int key) {
		return Nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		if(Edges!=null&&Edges.containsKey(src)&&Edges.get(src).containsKey(dest)) {
			return Edges.get(src).get(dest);
		}
		return null;
	}

	@Override
	public void addNode(node_data n) {
			if(n!=null) {
				this.Nodes.put(n.getKey(), n);
				HashMap<Integer,edge_data>ni=new HashMap<>();
				this.Edges.put(n.getKey(),ni);
				this.successors.put(n.getKey(),new ArrayList<>());
				this.ModeCount++;
			}
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(src==dest) { 
			return;
		}
		
		if(getEdge(src,dest)==null) {
			edge_data edgeSrcDest = new EdgeData(src,dest,w);
			this.Edges.get(src).put(dest, edgeSrcDest);
			this.successors.get(dest).add(src);
			this.ModeCount++;
			this.edgeCount++;

		}
		
		else{
			edge_data edgeSrcDest = new EdgeData(src,dest,w);
			this.Edges.get(src).replace(dest, edgeSrcDest);
			this.ModeCount++;
		}
	}

	@Override
	public Collection<node_data> getV() {
		return Nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		return Edges.get(node_id).values();

	}

	public HashMap<Integer,edge_data> getNi(int node_id) {
		return Edges.get(node_id);

	}
	@Override
	public node_data removeNode(int key) {
		if (!this.Nodes.containsKey(key)) { 
			return null;
		}
		node_data nodeR = this.Nodes.get(key);
		this.Nodes.remove(key);
		this.edgeCount-=Edges.get(key).size();
		for(Integer KeysOfNode: this.successors.get(key)) {
				Edges.get(KeysOfNode).remove(key);
				edgeCount--;
			}
		successors.remove(key);
		this.ModeCount++;
		return nodeR;
		
		
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(getEdge(src,dest)==null) {
			return null;
		}
		else {
			edge_data RemoveEdge = this.Edges.get(src).remove(dest);
			this.edgeCount--;
			this.ModeCount++;

			//edge_data RemoveEdge = null;
			return RemoveEdge;
		}
	}

	@Override
	public int nodeSize() {
		return this.Nodes.size();
	}

	@Override
	public int edgeSize() {
		return this.edgeCount;
	}

	@Override
	public int getMC() {
		return this.ModeCount;
	}



	
	public static class NodeData implements node_data, Serializable {
		private int id;
		private int pos;
		private int tag;
		private double weight=Double.MAX_VALUE;
		private String Info="";
		private int disc=-1;
		private int low=-1;
		private boolean sm=false; //stack member

		public NodeData(int id,int pos,int tag,double w,String i)
		{
			this.id=id;
			this.pos=pos;
			this.Info=i;
			this.tag=tag;
		}
		public NodeData()
		{
			id=keyCount++;

		}

		public int getDisc() {
			return this.disc;
		}

		public void setDisc(int disc) {
			this.disc=disc;
		}

		public boolean isSm() {
			return this.sm;
		}
		public void setSm(boolean sm) {
			this.sm=sm;
		}
		public int getLow() {
			return this.low;
		}

		public void setLow( int low) {
			this.low=low;
		}

		@Override
		public int getKey() {
			return this.id;
		}

		@Override
		public geo_location getLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setLocation(geo_location p) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public double getWeight() {
			return this.weight;
		}

		@Override
		public void setWeight(double w) {
			this.weight=w;
			
		}

		@Override
		public String getInfo() {
			return this.Info;
		}

		@Override
		public void setInfo(String s) {
			this.Info=s;
			
		}

		@Override
		public int getTag() {
			return this.tag;
		}

		@Override
		public void setTag(int t) {
			this.tag=t;
			
		}


		/*@Override
		public int compareTo(@NotNull node_data o) {
			int ans=0;
			if(this.weight-o.getWeight()>0)
				ans=1;
			else if (this.weight-o.getWeight()<0)
				ans=-1;
			return ans;
		}*/

		public String toString() {
			return "id: "+id;
		}
	}
	
		public static class EdgeData implements edge_data{
			private int src;
			private int dest;
			private String Info;
			private int tag=-1;
			private double w;
			
			public EdgeData(int src, int dest, double w) {
				this.src=src;
				this.dest=dest;
				this.w=w;
			}
			public EdgeData(int src, int dest, double w,String info) {
				this.src=src;
				this.dest=dest;
				this.w=w;
				this.Info=info;
			}
			
			public EdgeData(EdgeData other) {
				this.src=other.src;
				this.dest=other.dest;
				this.w=other.w;
			}
			@Override
			public int getSrc() {
				return this.src;
			}

			@Override
			public int getDest() {
				return this.dest;
			}

			@Override
			public double getWeight() {
				return this.w;
			}

			@Override
			public String getInfo() {
				return this.Info;
			}

			@Override
			public void setInfo(String s) {
				this.Info=s;
			}

			@Override
			public int getTag() {
				return this.tag;
			}

			@Override
			public void setTag(int t) {
				this.tag=t;
			}
			
		}
	
	
}
