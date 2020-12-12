package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;

public class Ex2 implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static HashMap<Integer,LinkedList<node_data>>path=new HashMap<>();
	private static PriorityQueue<double[]>sp=new PriorityQueue<>(Comparator.comparingDouble(o -> (o[2])));
	private static boolean first=true;
	private static int time=0;

	public static void main(String[] a) {
		Thread client = new Thread(new Ex2());
		client.start();
	}
	
	@Override
	public void run() {
		int scenario_num = 23;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
	//	int id = 999;
	//	game.login(id);
		String g = game.getGraph();
		String pks = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);
		DWGraph_Algo ga=new DWGraph_Algo();
		ga.init(gg);
		//System.out.println(ga.isConnected());
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;
		
		while(game.isRunning()) {
			moveAgants(game, gg);
			time++;
			try {
				synchronized (sp) {
					if (ind % 1 == 0) {
						_win.repaint();
					}
					Thread.sleep(dt);
					ind++;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);

		System.exit(0);
	}
	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */

	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> age = Arena.getAgents(lg, gg);
		_ar.setAgents(age);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> poks = Arena.json2Pokemons(fs);
		_ar.setPokemons(poks);
		DWGraph_Algo ga=new DWGraph_Algo();
		ga.init(gg);

		if(sp.isEmpty()) // אם סיימנו לעשות התאמות בין סוכנים לפוקימונים
		{
			match(gg,ga,poks,age);
		}

		int count = 0;
		//LinkedList<CL_Agent>newAge=new LinkedList<>();
		while(!sp.isEmpty()&&count<age.size()) {//נעבור על התור

			//int nxtNode;
			double[] arr = sp.poll();
			int id = (int) arr[1];
			//int idPok = (int) arr[0];
			if (!path.containsKey(id))
				path.put(id, new LinkedList<>());
			if(!path.get(id).isEmpty() )
				count++;
			if (path.get(id).isEmpty()) {//אם לסוכן אין מסלול כרגע או שהוא לא שודך לפוקימון

				CL_Pokemon p = poks.get(((int) arr[0]));
				CL_Agent a = age.get(id);
				if (!a.isPok() && !p.isAgent()) {//אם הפוקמון לא שודך או (מיותר: הסוכן לא שודך, כבר וידאנו)
					p.setAgent(true);//נעדכן שידוך לפוקימון
					a.setPok(true);//נעדכן שידוך לסוכן
					a.setId_curr_pok((int) arr[0]);//נעדכן מי השידוך לסוכן
					int src = age.get(id).getSrcNode();// נקבל על איזה קודקוד יושב הסוכן
					int dest;
					node_data lastNode;
					//if(p.getType()<0) {
					dest = p.get_edge().getSrc();//נקבל את קודקוד שורש של הצלע עליה הפוקימון
					//	dest = p.get_edge().getDest();
					lastNode = gg.getNode(p.get_edge().getDest());//נקבל את הקודקוד של סוף הצלע של הפוקימון
					//}
					//else {
					//lastNode = gg.getNode(p.get_edge().getSrc());
					//}
					count++;

					if (src == dest)//אם אלו אותם קודקודים
						path.get(id).addLast(gg.getNode(p.get_edge().getDest()));//נעדכן רק את קודקוד היעד האחרון להיות סוף הצלע של הפוקימון
					else {
						LinkedList<node_data> track = (LinkedList<node_data>) ga.shortestPath(src, dest);//מסלול=המסלול הקצר ביותר
						track.addLast(lastNode);//נוסיף לו את הקודקוד האחרון כדי שיוכל לאכול את הפוקימון
						path.put(id, track);//נוסיף למסלול
					}

				}
			}
		}
		sp.clear();
		nextNode(game,poks,age);
		//System.out.println(time);
}


	private static void match(directed_weighted_graph gg, dw_graph_algorithms ga,List<CL_Pokemon> poks,List<CL_Agent> age)
	{
		//בכל פעם שנגיע לסוכן/פוקימון תפוסים אז נדלג עליהם. אם הם לא תפוסים נחשב מרחק בינהם ונוסיף לתור

		for (int i = 0; i < poks.size(); i++) {
			CL_Pokemon p = poks.get(i);
			if(p.isAgent() )
			{
				continue;
				}
			for (int j = 0; j < age.size(); j++) {

				CL_Agent a = age.get(j);
				if(a.isPok() &&!path.get(j).isEmpty()) {
					//System.out.println("good");
					continue;
				}
				_ar.updateEdge(p, gg);
				int dest;
				double[] pokAge = new double[3];
				pokAge[0] = i;
				pokAge[1] = a.getID();
				//if(p.getType()<0)
				dest = p.get_edge().getSrc();
				//else
				//dest=p.get_edge().getDest();
				if (a.getSrcNode() == dest)
					pokAge[2] = 0;
				else {
		 			Double dist = ga.shortestPathDist(a.getSrcNode(), dest);
					pokAge[2] = dist;
				}
				sp.add(pokAge);

			}
		}
	}



	private static void nextNode(game_service game,List<CL_Pokemon> poks,List<CL_Agent> age) {

		for (int j = 0; j < age.size(); j++) {//אם לסוכן קיים כבר מסלול לעבר פוקימון מסויים
			if(path.containsKey(j))
			{

				if(!path.get(j).isEmpty()) {
					CL_Agent a = age.get(j);
					int id=a.getID();
					int nxt = path.get(id).removeFirst().getKey();
					a.setNextNode(nxt);

					game.chooseNextEdge(id, nxt);
					System.out.println("Agent: "+id+", val: "+age.get(id).getValue()+"   turned to node: "+nxt+ " in speed: "+a.getSpeed());
					if(path.get(id).isEmpty())
					{
						a.setPok(false);
						poks.get(a.getId_curr_pok()).setAgent(false);
						a.setId_curr_pok(-1);

					}
				}

			}
		}
	}


	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				CL_Pokemon c = cl_fs.get(a);
				int sn = c.get_edge().getSrc();

				game.addAgent(sn);



			}


		}
		catch (JSONException e) {e.printStackTrace();}
	}
}
