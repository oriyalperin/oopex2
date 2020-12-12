package api;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

public class myTest {
    public static void main(String[]args)
    {
        DWGraph_DS g=new DWGraph_DS();
        DWGraph_DS.NodeData n0=new DWGraph_DS.NodeData();
        DWGraph_DS.NodeData n1=new DWGraph_DS.NodeData();
        DWGraph_DS.NodeData n2=new DWGraph_DS.NodeData();
        DWGraph_DS.NodeData n3=new DWGraph_DS.NodeData();
        DWGraph_DS.NodeData n4=new DWGraph_DS.NodeData();
        DWGraph_DS.NodeData n5=new DWGraph_DS.NodeData();
        DWGraph_DS.NodeData n6=new DWGraph_DS.NodeData();
        PriorityQueue<node_data> pq=new PriorityQueue();

        //Map<Integer, Array> mp= new

                /*    HashMap<Integer,HashMap<Integer,edge_data>[]>hm=new HashMap<>();
        hm.put(0,new HashMap[2]);
        hm.get(0)[0]=new HashMap<>();
        hm.get(0)[1]=new HashMap<>();
        hm.get(0)[0].put(5, new DWgraph_DS.EdgeData(0,5,15));
        System.out.println(hm.get(0)[0].get(5).getWeight());*/


  /*      g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.connect(1,0,1);
        g.connect(0,2,1);
        g.connect(2,1,1);
        g.connect(0,3,1);
        g.connect(3,4,1);
        DWGraph_Algo ga=new DWGraph_Algo();
        ga.init(g);
        ga.SCC();
*/
        DWGraph_DS g3 = new DWGraph_DS();
        g3.addNode(n0);
        g3.addNode(n1);
        g3.addNode(n2);
        g3.addNode(n3);
        g3.addNode(n4);
        g3.addNode(n5);
        g3.addNode(n6);
        g3.connect(0, 1,1);
        g3.connect(0,4,5);
        g3.connect(1, 2,1);
        g3.connect(2, 0,3);
        g3.connect(1, 3,4);
        g3.connect(1, 4,11);
        g3.connect(1, 6,6);
        g3.connect(5,1,5);
        g3.connect(3, 5,15);
        g3.connect(4, 5,2);
        //       g3.connect(6, 4,1);
        System.out.println("\nSSC in third graph ");
        DWGraph_Algo ga3=new DWGraph_Algo();
        ga3.init(g3);

        DWGraph_Algo gAlgo = new DWGraph_Algo();

      System.out.println(ga3.shortestPath(1, 5));
        System.out.println(ga3.save("C:\\Users\\Oriya\\Desktop\\new\\JsonFile.json"));
       gAlgo.load("C:\\Users\\Oriya\\Desktop\\new\\JsonFile.json");
       if(gAlgo==null)
           System.out.println("sucks");
       System.out.println(gAlgo.shortestPathDist(1, 5));
        System.out.println(ga3.shortestPathDist(1, 5));

    }
}

