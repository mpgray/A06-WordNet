package wordnet;
public class SAP {

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G)

   // is the digraph a directed acyclic graph?
   public boolean isDAG()

   // is the digraph a rooted DAG?
   public boolean isRootedDAG()

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w)

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w)

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w)

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w)

   // do unit testing of this class
   public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
        int v = StdIn.readInt();
        int w = StdIn.readInt();
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}

   
}




package wordNet;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Stack;

public class Sap {
	private Digraph graph;
	
	 public Sap(Digraph G){
	    	if (G == null) throw new NullPointerException("Digraph G can't be null");
		    graph = new Digraph(G);
	    }
	 
	   public boolean isDAG(){
		    // A digraph has a topological order iff no directed cycle
		    return !new DirectedCycle(graph).hasCycle();
	    }
	   
	   public boolean isRootedDAG(){
		    // First, make sure the graph is a DAG (use method above)
		    if(!isDAG()) return false;
		    
		    DepthFirstOrder dfo = new DepthFirstOrder(this.graph);
		    Integer rootVertex = dfo.post().iterator().next();
		    
		    DepthFirstDirectedPaths dfdp = new DepthFirstDirectedPaths(graph.reverse(), rootVertex);
		    for(int i=0; i<graph.V(); i++){
			    // return false if one of the vertices does NOT have a path to the root
			    if(!dfdp.hasPathTo(i)) return false;
		    }
		    
		    return true;
	    }
	   
	   public int length(int v, int w){
	    	Stack<Integer> vStack = new Stack<>();
	    	vStack.push(v);
	    	Stack<Integer> wStack = new Stack<>();
	    	wStack.push(w);
		    return ancestorAndLength(vStack, wStack)[1];
	    }
	   
	   public int ancestor(int v, int w){
	    	Stack<Integer> vStack = new Stack<>();
	    	vStack.push(v);
	    	Stack<Integer> wStack = new Stack<>();
	    	wStack.push(w);
		    return ancestorAndLength(vStack, wStack)[0];
	    }
	   
	   public int length(Iterable<Integer> v, Iterable<Integer> w){
		    return ancestorAndLength(v, w)[1];
	    }
	   
	   private int[] ancestorAndLength(Iterable<Integer> v, Iterable<Integer> w){
		    // Data Structures to find the paths
		    BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(graph, v);
		    BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(graph, w);
	   
		    DepthFirstOrder DFO = new DepthFirstOrder(graph);
		    
		    int ancestor = -1;
		    int length = -1;
		    
		    for(int i: DFO.reversePost()){
			    if(vPaths.hasPathTo(i) && wPaths.hasPathTo(i)){
				    // Common ancestor found, calculate the current length
				    int currentLength = vPaths.distTo(i) + wPaths.distTo(i);
				   
				    // See if it's closer than the current
				    if(currentLength < length || ancestor == -1){
					    ancestor = i;
					    length = currentLength;
				    }else break;
			    }
		    }
		    
		    int[] ancestorAndLength = {ancestor, length};
		    return ancestorAndLength;
	    }  
}
