/* VertexNode.java */

package graph;

import list.*;

/**
* The VertexNode class stores both the adjacency list for a vertex and the object
* used to make that vertex.
*/

class VertexNode {

  /**
  * myGraph is the graph this vertex is in.
  * adjacentEdges is the adjacency list of edges for this vertex
  * neighbors is the list of vertices connected to this VertexNode, including itself
  *  if there is a self-edge.
  * realVertex is the Object used to make this VertexNode.
  * node is the DListNode in which this vertex is stored, in the vertices list
  *  of its graph.
  */

  protected WUGraph myGraph;
  protected DList adjacentEdges;
  protected Object realVertex;
  protected DListNode node;

  /**
  * Makes a new VertexNode object given its graph and the initializing object.
  */

  protected VertexNode(WUGraph graph, Object realVertex) {
    adjacentEdges = new DList();
    this.realVertex = realVertex;
    myGraph = graph;
  }

  /**
  * Returns the degree of this vertex.
  */

  protected int getDegree() {
    return adjacentEdges.size();
  }

  /**
  * Methods for adding/removing an edge to/from an adjacency list.
  * Checks to make sure e.vertex1 == this.
  */

  protected void addEdge(Edge e) {
    if (e.vertex1 != this) {
      return;
    }
    adjacentEdges.insertBack(e);
    e.node = (DListNode) adjacentEdges.back();
  }

  /**
  * Only removes e from this vertex, not from e.vertex2.
  */

  protected void removeEdge(Edge e) {
    if (e.vertex1 != this) {
      return;
    }
    try {
      e.node.remove();
    } catch(InvalidNodeException e1) {
        System.out.println("That shouldn't happen.");
    }
  } 


}