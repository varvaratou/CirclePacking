package circlepacking;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * The Class Complex maintains the data necessary for the
 * circle packing algorithms.
 */
public class Complex {
	
	private boolean isUpdated;
	private HashSet<CPVertex> intVertices;
	private ArrayList<CPVertex> extVertices;
	private HashSet<CPFace> faces;
	

	public Complex() {
		this.faces = new HashSet<CPFace>();
		this.intVertices = new HashSet<CPVertex>();
		this.extVertices = new ArrayList<CPVertex>();
		this.isUpdated = false;
	}
	
	/**
	 * Gets the faces of the complex.
	 * 
	 * @return A set of the faces of the complex.
	 */
	public HashSet<CPFace> getFaces() {
		return faces;
	}


	/**
	 * Sets the faces of the complex.
	 *
	 * @param The set of the new faces of the complex.
	 */
	public void setFaces(HashSet<CPFace> faces) {
		this.faces = faces;
	}

	
	/**
	 * Gets the vertices corresponding to the interior 
	 * circles of the circle packing.
	 *
	 * @return A set of the internal vertices.
	 */
	public HashSet<CPVertex> getIntVertices() {
		return intVertices;
	}
	
	
	/**
	 * Sets the vertices corresponding to the interior 
	 * circles of the circle packing.
	 * 
	 * @param  The set of the internal vertices.
	 */
	public void setIntVertices(HashSet<CPVertex> intVertices) {
		this.intVertices = intVertices;
	}
	
	
	/**
	 * Gets the vertices corresponding to the boundary 
	 * circles of the circle packing.
	 *
	 * @return A set of the boundary vertices.
	 */
	public ArrayList<CPVertex> getExtVertices() {
		return extVertices;
	}
	
	
	/**
	 * Sets the vertices corresponding to the boundary 
	 * circles of the circle packing.
	 *
	 * @param The set of the boundary vertices.
	 */
	public void setExtVertices(ArrayList<CPVertex> extVertices) {
		this.extVertices = extVertices;
	}

	
	/**
	 * Checks if the data structure has been updated to its 
	 * final state - this method is called by the layout
	 * Applet to ensure that the layout process can be initialized.
	 *
	 * @return true, if the data structure has been updated
	 */
	public boolean isUpdated() {
		return isUpdated;
	}


	/**
	 * Sets the status of the data structure state - this method
	 * is used by the Circle Packing Applet when the data from the
	 * triangulation has been converted to the data structure 
	 * required by the circle packing algorithms.
	 *
	 * @param isUpdated the new updated
	 */
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	
	/**
	 * Resets the data structure.
	 */
	public void reset() {
		this.faces = new HashSet<CPFace>();
		this.intVertices = new HashSet<CPVertex>();
		this.extVertices = new ArrayList<CPVertex>();
		this.isUpdated = false;
	}
	
}
