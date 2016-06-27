package Bridges;

/**
 * Created by Максим on 24.06.2016.
 */
public class Main_class {
    public static void main(String[] args) {
        graph G = new graph(5);
        
         System.out.println("V = " + G.V + ", E = " + G.E);
        G.initialisation(5,graph.density.rare);
        
        
        G.show();
        
        Algorithm T = new Algorithm();
        T.DFS_bridge(G);

    }
}
