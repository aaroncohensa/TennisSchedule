import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;


public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<DirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adj = (Bag<DirectedEdge>[]) new Bag[V]; //array size of number of tournaments
        for (int v = 0; v < V; v++)
            adj[v] = new Bag<DirectedEdge>(); // bags for each tournaments that contains edges
    }



    /**
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  tourns the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedDigraph(ArrayList<Tournament> tourns) {
        if (tourns == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = tourns.size();
            indegree = new int[V];
            adj = (Bag<DirectedEdge>[]) new Bag[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<DirectedEdge>();
            }

            for(int i = 0 ; i<tourns.size();i++){
                Tournament prevWeek = tourns.get(i);
                int counter = 0;
                if (prevWeek.weekNo == 35){ //end of calender, break out of loop
                    break;
                }
                for(int j = i ; j<i+6 ; j++ ){
                    Tournament currWeek = tourns.get(j);
                    if(prevWeek.weekNo == currWeek.weekNo){
                    }
                    else if(currWeek.weekNo == 35){ //last week in the calender
                        long pwMoney=prevWeek.tMoney;
                        long cwMoney = currWeek.tMoney;
                        long pwPoints = prevWeek.tPoints;
                        long cwPoints = currWeek.tPoints;
                        float weight = 1/((pwMoney*cwMoney)+(pwPoints+cwPoints));
                        DirectedEdge dEdge = new DirectedEdge(prevWeek.tournid , currWeek.tournid , weight);
                        addEdge(dEdge);
                        break;
                    }
                    else if(prevWeek.weekNo == (currWeek.weekNo-1)){ //checking if in next week
                        long cwMoney = currWeek.tMoney;
                        long cwPoints = currWeek.tPoints;
                        if(counter == 0){ //if first distance
                            long dtoT1 = prevWeek.tO1Dist;
                            float weight = 1/((3*cwMoney)+(2*cwPoints)-dtoT1);
                            DirectedEdge dEdge = new DirectedEdge(prevWeek.tournid , currWeek.tournid , weight);
                            addEdge(dEdge);
                        }
                        if(counter ==1){ //if second distance
                            long dtoT2 = prevWeek.tO2Dist;
                            float weight = 1/((3*cwMoney)+(2*cwPoints)-dtoT2);
                            DirectedEdge dEdge = new DirectedEdge(prevWeek.tournid , currWeek.tournid , weight);
                            addEdge(dEdge);
                        }
                        if (counter==2){ //if third distance
                            long dtoT3= prevWeek.tO3Dist;
                            float weight = 1/((3*cwMoney)+(2*cwPoints)-dtoT3);
                            DirectedEdge dEdge = new DirectedEdge(prevWeek.tournid , currWeek.tournid , weight);
                            addEdge(dEdge);
                        }

                        counter++;
                    }
                    else if (prevWeek.weekNo == (currWeek.weekNo-2)){ //if two weeks ahead go to next week
                        break;
                    }
                }
            }

        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e);
        }
    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of {@code G}.
     *
     * @param  G the edge-weighted digraph to copy
     */
    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++)
            this.indegree[v] = G.indegree(v);
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
            for (DirectedEdge e : G.adj[v]) {
                reverse.push(e);
            }
            for (DirectedEdge e : reverse) {
                adj[v].add(e);
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        indegree[w]++;
        E++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int v = 0; v < V; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

}