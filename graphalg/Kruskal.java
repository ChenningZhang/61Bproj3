/* Kruskal.java */

package graphalg;

import graph.*;
import set.*;
import dict.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
    public static WUGraph minSpanTree(WUGraph g) {
        //[1] create a new graph result w/ same vertices as g, no edges
        WUGraph result = new WUGraph();
        Object[] allVertices = g.getVertices();
        for (int i=0; i < allVertices.length; i++) {
            Object currVertex = allVertices[i];
            result.addVertex(currVertex);
        }

        //[2] make an array for all edges
        EdgeK[] allEdges = new EdgeK[g.edgeCount()];
        //make hashtable b/c the hashtable fields are protected in WUGraph
        HashTableChained newEdges = new HashTableChained(g.edgeCount()*2+1);
        int x = 0;
        while (x < allEdges.length) {
            for (int i=0; i < allVertices.length; i++) {
                // fill in the array for all edges
                Object currVertex = allVertices[i];
                Neighbors currNeighbors = g.getNeighbors(currVertex);
                Object[] currNeighborLst = currNeighbors.neighborList;
                int[] currWeightLst = currNeighbors.weightList;
                for (int j=0; j < currNeighborLst.length;j++) {
                    //for every neighbor, make a edge, add to the list if not found
                    Object currNeighbor = currNeighborLst[j];
                    int currWeight = currWeightLst[j];
                    VertexPairK newEdgeKey = new VertexPairK(currVertex, currNeighbor);
                    if (newEdges.find(newEdgeKey) == null) {
                        EdgeK newEdgeValue = new EdgeK(currVertex, currNeighbor, currWeight);
                        newEdges.insert(newEdgeKey, newEdgeValue);
                        allEdges[x] = newEdgeValue;
                        x++;
                    }
                }
            }
        }

        //[3] sort the edges
        EdgeSort.quicksort(allEdges);

        //[4] DisjointSets
        HashTableChained VerticesInt = new HashTableChained(g.vertexCount());
        DisjointSets VerticesSets = new DisjointSets(g.vertexCount());
        Object[] resultVertices = result.getVertices();
        for (int i=0; i < resultVertices.length; i++) {
            VerticesInt.insert(resultVertices[i], i);
        }

        for (int i=0; i < allEdges.length; i++) {
            Object Vertex1 = allEdges[i].object1;
            Object Vertex2 = allEdges[i].object2;
            int w = allEdges[i].weight;
            int int1 = (Integer)VerticesInt.find(Vertex1).value();
            int int2 = (Integer)VerticesInt.find(Vertex2).value();
            if (VerticesSets.find(int1) != VerticesSets.find(int2)) {
                result.addEdge(Vertex1,Vertex2,w);
                VerticesSets.union(VerticesSets.find(int1), VerticesSets.find(int2));
            }
        }
        return result;
    }
}
