package circlepacking;

import java.util.ArrayList;
import java.util.List;
import toxi.geom.Vec2D;


public class CPVertex {
	
	private double radius;
	private boolean isInterior;
	private List<CPFace> faces;
	//the final x and y coordinates obtained during
	//the layout process
	private double x;
	private double y;
	//the temporary position given by the triangulation
	private Vec2D position;
	//indicates whether the final x and y coordinates
	//have been assigned
	private boolean isUpdated = false;

	
	/**
	 * Instantiates a new cP vertex.
	 *
	 * @param position, the temporary position given by the
	 * triangulation.
	 */
	public CPVertex(Vec2D position) {
		this.position = position;
		this.faces = new ArrayList<CPFace>();
	}
	
	
	/**
	 * Gets the temporary position assigned during the stage
	 * of the input complex generation.
	 *
	 * @return A Vec2D representing the position.
	 */
	public Vec2D getPosition() {
		return position;
	}

	
	/**
	 * Gets the radius of the circle corresponding to the vertex.
	 *
	 * @return a double representing the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Sets the radius of the circle corresponding to the vertex.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Checks if the vertex corresponds to a boundary circle or
	 * to an internal circle.
	 *
	 * @return true, if it corresponds to an internal circle
	 * false if it corresponds to a boundary one.
	 */
	public boolean isInterior() {
		return isInterior;
	}

	
	/**
	 * Sets if the vertex corresponds to a boundary circle or
	 * to an internal circle.
	 *
	 * @param isInterior the boolean value indicating if the
	 * vertex corresponds to an interior circle or not.
	 */
	public void setIsInterior(boolean isInterior) {
		this.isInterior = isInterior;
	}

	
	/**
	 * Gets the faces surrounding a vertex in order (either CW or CCW)
	 *
	 * @return The list of faces
	 */
	public List<CPFace> getFaces() {
		return faces;
	}

	
	/**
	 * Sets the list of faces surrounding a vertex in order (either CW or CCW)
	 *
	 * @param faces the new list of faces
	 */
	public void setFaces(List<CPFace> faces) {
		this.faces = faces;
	}

	
	/**
	 * Gets the final x coordinate assigned after the layout process.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	
	public void setX(double x) {
		this.x = x;
		
	}

	
	/**
	 * Gets the final y coordinate assigned after the layout process.
	 *
	 * @return the x
	 */
	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Checks if the final x and y coordinates have been assigned.
	 *
	 * @return true, if is updated
	 */
	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	
	/**
	 * Gets the angle sum with regard to the vertex under consideration.
	 *
	 * @return the double value representing the angle sum
	 */
	public double getAngleSum() {
		double angleSum = 0;
		for (CPFace face: faces) {			
			angleSum += face.getAngle(this);
		}
		return angleSum;
	}
		
}
