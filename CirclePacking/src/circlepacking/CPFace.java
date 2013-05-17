package circlepacking;

import java.util.ArrayList;
import java.util.Collection;
import toxi.geom.Vec2D;

/**
 * This class represents a triangular face of the complex K
 * serving as the input for circle packing. It stores data 
 * about the vertices the face contains and provides methods 
 * among others for computing angles.
 */
public class CPFace {
	private int idNumber;
	private ArrayList<CPVertex> vertexList;
	
    /**
     * Instantiates a new CPFace.
     *
     * @param collection the collection of vertices the face contains.
     * @param idNumber a unique id number
     */
    public CPFace(Collection<CPVertex> collection, int idNumber) {  
        if (collection.size() != 3) {
            throw new IllegalArgumentException(
                    "Input list must have 3 vertices");
        }
        this.idNumber = idNumber;
        this.vertexList = new ArrayList<CPVertex>(collection);
    }


	/**
	 * Gets the list of vertices of the face.
	 *
	 * @return the vertex list
	 */
	public ArrayList<CPVertex> getVertexList() {
		return vertexList;
	}


	/**
	 * Gets the unique id number.
	 *
	 * @return the id number
	 */
	public int getIdNumber() {
		return idNumber;
	}

	
	/**
	 * Tests if this face contains the input vertex.
	 *
	 * @param otherVertex the vertex we test for containment
	 * @return true, if the vertex is contained, false otherwise
	 */
	public boolean containsVertex(CPVertex otherVertex) {
		return this.vertexList.contains(otherVertex);
	}
	
	
	/**
	 * Returns the vertex that this face has *NOT* in common 
	 * with the input face.
	 *
	 * @param otherFace the other face in which we look the 
	 * vertex *NOT* in common.
	 * @return the CPvertex the two faces have in common
	 * If there is no unshared vertex, null is returned
	 * If more than two vertex are unshared the first found
	 * is returned
	 */
	public CPVertex vertexNotShared(CPFace otherFace) {
		CPVertex toReturn = null;
		for (CPVertex thisVertex: this.vertexList){
			if (!otherFace.containsVertex(thisVertex)) {
				toReturn = thisVertex;
				break;
			}
		}
		return toReturn;
	}
	
	
	/**
	 * Computes the angle formed by the other two vertices
	 * and the input vertex of the face.
	 *
	 * @param center the hinge vertex of the angle
	 * @return the angle
	 */
	public double getAngle(CPVertex center) {
		ArrayList<Double> temp = new ArrayList<Double>(2);
		for (CPVertex vertex: this.vertexList) {
			if (vertex!=center)
				temp.add(vertex.getRadius());				
		}

		double radiusA = temp.get(0);
		double radiusB = temp.get(1);

		double num = Math.pow(center.getRadius() + radiusA, 2) 
				+ Math.pow(center.getRadius()+ radiusB, 2) 
				- Math.pow(radiusA + radiusB, 2);
		double den = 2*(center.getRadius() + radiusA)*(center.getRadius() + radiusB);
		
		return Math.acos(num/den);
	}

	
	/**
	 * Gets the average point of the vertices of the face.
	 *
	 * @param position a boolean indicating if we take into account
	 * the triangulation position of the face or the final position
	 * after the circles are layout.
	 * @return the average vertex, a Vec2D representing the position 
	 * of the average point.
	 */
	public Vec2D getAvgVertex(boolean position)  {
		float x = 0; float y = 0;
		for (CPVertex thisVtx : vertexList) {
			if (position) {
				x+=thisVtx.getPosition().x;
			    y+=thisVtx.getPosition().y;
			} else {
				x+=thisVtx.getX();
			    y+=thisVtx.getY();
			}
		}
		return new Vec2D(x/3, y/3);
	}
}

