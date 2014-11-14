/* Edge.java */

package graph;

import list.*;

/**
* The Edge class is the internal representation of an Edge in a WUGraph
* This internal representation uses the "half-edge" representation.
*/

class Edge {
  /**
  * myGraph is the graph this edge is in.
  * weight is the weight of this edge.
  * vertex1 is the vertex such that VertexNode.adjacencyList contains this Edge.
  * vertex2 is the other vertex.
  * partner is the Edge for which partner.vertex1 = vertex2 and vice-versa.
  * -Set to null if it is a self-edge by setPartner().
  * node is the DListNode in the adjacency list of vertex1 that this edge is
  *  stored in.
  */

  protected WUGraph myGraph;
  protected int weight;
  protected VertexNode vertex1;
  protected VertexNode vertex2;
  protected Edge partner;
  protected DListNode node;

  /**
  * Makes a new Edge object given its graph, weight, and 2 vertices.
  */

  protected Edge(WUGraph graph, int weight, VertexNode v1, VertexNode v2) {
    myGraph = graph;
    this.weight = weight;
    vertex1 = v1;
    vertex2 = v2;
  }

  /**
  * Various getter methods.
  * getVertexPair returns the VertexPair used to hash this edge in myGraph.edges.
  * getV1 and getV2 return the application representation of the vertices, instead
  *   of the internal representation.
  */

  protected VertexPair getVertexPair() {
    return new VertexPair(getV1(),getV2());
  }

  protected Object getV1() {
    return vertex1.realVertex;
  }

  protected Object getV2() {
    return vertex2.realVertex;
  }

  /**
  * Setters methods for changing the weight of the edge and setting its partner.
  */

  protected void setWeight(int newWeight) {
    weight = newWeight;
  }

  protected void setPartner() {
    if (vertex1 == vertex2) {
      partner = null; //If this edge is a self-edge, partner is set to null.
      return;
    }
    partner = new Edge(myGraph, weight, vertex2, vertex1);
    partner.partner = this;
  }
}