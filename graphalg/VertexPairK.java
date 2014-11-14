/* VertexPairK.java */

package graphalg;

public class VertexPairK {
    protected Object object1;
    protected Object object2;

    protected VertexPairK(Object obj1, Object obj2) {
        object1 = obj1;
        object2 = obj2;
    }


    public int hashCode() {
        if (object1.equals(object2)) {
            return object1.hashCode() + 1;
        } else {
            return object1.hashCode() + object2.hashCode();
        }
    }

    public boolean equals(Object o) {
        return ((object1.equals(((VertexPairK)o).object1)) &&
                (object2.equals(((VertexPairK) o).object2))) ||
            ((object1.equals(((VertexPairK) o).object2)) &&
             (object2.equals(((VertexPairK) o).object1)));
    }
}
