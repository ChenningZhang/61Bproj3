/* WUGraph.java */

package graph;

import list.*;
import dict.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {
	
  protected DList vertexLst;
  protected HashTableChained vertices;
  protected HashTableChained edges;

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
   
  public WUGraph() {
    vertexLst = new DList();
    vertices = new HashTableChained(10);
    edges = new HashTableChained(10);
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount(){
    return vertexLst.size();
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
    return edges.size();
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
    DListNode curr = (DListNode) vertexLst.front();
    Object[] v = new Object[this.vertexCount()];
    int i = 0;
    while (curr.isValidNode()) {
      try {
        VertexNode currVertex = (VertexNode) curr.item();
        v[i] = currVertex.realVertex;
        curr = (DListNode) curr.next();
        i++;
      } catch(InvalidNodeException e) {
        System.out.println("That shouldn't happen.");
      } 
    }
    return v;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
    if (isVertex(vertex)) {
      return;
    }
    VertexNode v = new VertexNode(this, vertex);
    vertices.insert(vertex, v);
    vertexLst.insertFront(v);
    v.node = (DListNode) vertexLst.front();
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    if (!(isVertex(vertex))) {
      return;
    }
    VertexNode v = getVertex(vertex);
    if (v.getDegree() > 0) {
      DListNode current = (DListNode) v.adjacentEdges.front();
      try {
        while (current.isValidNode()) {
          Edge e = (Edge) current.item();
          current = (DListNode) current.next();
          removeEdge(e.getV1(), e.getV2());
        }
      } catch(InvalidNodeException e) {
        System.out.println("That shouldn't happen.");
      } 
    }
    vertices.remove(vertex);
    try {
      v.node.remove();
    } catch (InvalidNodeException e) {
      System.out.println("That shouldn't happen.");
    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    if (getVertex(vertex) != null) {
      return true;
    }
    return false;
  }

  /**
  * Private helper functions that return internal representations of 
  * vertices and edges, via the hash tables.
  * Returns null in both cases if the vertex or edge does not exist in this graph.
  */

  private VertexNode getVertex(Object vertex) {
    Entry e = vertices.find(vertex);
    if ((e == null)) {
      return null;
    }
    VertexNode v = (VertexNode) e.value();
    return v;
  }

  private Edge getEdge(Object u, Object v) {
    if (isVertex(u) && isVertex(v)) {
      VertexPair vp = new VertexPair(u,v);
      Entry e = edges.find(vp);
      if (e != null) {
        return (Edge) e.value();
      }
    }
    return null;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
    VertexNode v = getVertex(vertex);
    if (v == null) {
      return 0;
    }
    return v.getDegree();
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
    if (!(isVertex(vertex))) {
      return null;
    }
    Neighbors result = new Neighbors();
    int d = degree(vertex);
    if (d == 0) {
      return null;
    }
    DList edges = getVertex(vertex).adjacentEdges;
    result.neighborList = new Object[d];
    result.weightList = new int[d];
    DListNode current = (DListNode) edges.front();
    for (int i = 0; i < d; i++) {
      try {
        Edge currE = (Edge) current.item();
        result.weightList[i] = currE.weight;
        result.neighborList[i] = currE.getV2();
        current = (DListNode) current.next();
      } catch(InvalidNodeException e) {
        System.out.println("That shouldn't happen.");
      } 
    }
    return result;
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
    if (isEdge(u,v)) {
      Edge e = getEdge(u,v);
      e.setWeight(weight);
      return;
    }
    VertexNode v1 = getVertex(u);
    VertexNode v2 = getVertex(v);
    Edge e = new Edge(this, weight, v1, v2);
    e.setPartner();
    v1.addEdge(e);
    if (e.partner != null) {
      v2.addEdge(e.partner);
    }
    VertexPair vp = e.getVertexPair();
    edges.insert(vp, e); //Only 1 of the 2 "half-edges" is inserted.
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    if (isEdge(u,v)) {
      Edge e = getEdge(u,v);
      VertexNode v1 = e.vertex1;
      VertexNode v2 = e.vertex2;
      v1.removeEdge(e);
      if (e.partner != null) {
        v2.removeEdge(e.partner);
      }
      VertexPair vp = e.getVertexPair();
      edges.remove(vp);
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
    if (getEdge(u,v) != null) {
      return true;
    }
    return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
    if (!(isEdge(u,v))) {
      return 0;
    }
    Edge e = getEdge(u,v);
    return e.weight;
  }

}
