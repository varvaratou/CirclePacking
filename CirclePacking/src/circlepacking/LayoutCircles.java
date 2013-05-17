package circlepacking;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import toxi.geom.Circle;
import toxi.geom.Vec2D;

/**
 * This class contains the methods to layout the circles once the
 * radii have been computed. 
 */
public class LayoutCircles {

	private boolean debug = true;
	private Complex k;
	private ArrayList<CPFace> updatedFaces;
	private LinkedList<CPFace> chain;
	private boolean isCompleted;
	
	public LayoutCircles(Complex k) {
		this.k = k;
		this.chain = new LinkedList<CPFace>();
		this.updatedFaces = new ArrayList<CPFace>();
		this.isCompleted = false;
	}
	
	
	/**
	 * Assigns the x and y coordinates for the first triple of 
	 * tangent circles. The first vertex is positioned at the origin
	 * and the edge between the first and second vertex along the
	 * horizontal axis. This is a helper method called by the method 
	 * that assigns the coordinates of the first petal.
	 * NOTE: the first triple of circles in the layout Applet 
	 * is filled with yellow color
	 * 
	 * @param firstFace an arbitrary face to be placed first
	 */
	private void placeFirstTriple(CPFace firstFace) {
		CPVertex a = firstFace.getVertexList().get(0);
		CPVertex b = firstFace.getVertexList().get(1);
		CPVertex c = firstFace.getVertexList().get(2);

		double ab = a.getRadius() + b.getRadius();
		double ca = b.getRadius() + c.getRadius();
		double bc = c.getRadius() + a.getRadius();

		double x = (ab * ab + bc * bc - ca * ca) / (2 * ab);
		a.setX(0);
		a.setY(0);
		a.setUpdated(true);
		b.setX(ab);
		b.setY(0);
		b.setUpdated(true);
		c.setX(x);
		c.setY(Math.sqrt(bc * bc - x * x));
		c.setUpdated(true);	
	}


	/**
	 * Assigns the x and y coordinates for the first petal of 
	 * tangent circles.
	 */
	public void placeFirstPetal() {
		//1.GET AN ARBITRARY INTERIOR VERTEX FROM THE COMPLEX
		CPVertex base = (CPVertex) k.getIntVertices().toArray()[0];
		//2.GET ITS SURROUNDING TRIANGULAR FACES
		List<CPFace> petals = base.getFaces();
		//3.ADD THEM TO THE LINKED LIST
		for (CPFace f: petals) {
			chain.add(f);
			if (debug)				
				System.out.print(f.getIdNumber() + " -> ");
		}
		//4.ITERATE OVER SURROUNDING TRIANGULAR FACES AND ASSIGN 
		//  THE FINAL COORDINATES
		for (int i=0; i<petals.size(); i++) {
			if (i==0) 
				placeFirstTriple(petals.get(i));
			else if (i!=petals.size()-1) 
				placeTriple(petals.get(i), petals.get(i-1));
			
			updatedFaces.add(petals.get(i));
		}		
	}
	
	
	/**
	 * Assigns the x and y coordinates for a triple of 
	 * tangent circles.
	 *
	 * @param currFace the triangular face under consideration
	 * @param prevFace the triangular face that have been previously
	 * assigned its final x and y coordinates 
	 * (following either CW or CCW order)
	 */
	public void placeTriple(CPFace currFace, CPFace prevFace) {
		
		ArrayList<CPVertex> updated = new ArrayList<CPVertex>();
		CPVertex notUpdated = null;
		//1.REFERENCE THE VERTEX OF THE CURRENT FACE THAT HAS NOT YET BEEN UPDATED
		//(two consecutive faces always share two vertices - these two vertices
		//should have been already updated when the previous face was processed)
		for (CPVertex v: currFace.getVertexList()) {
			if (v.isUpdated())
				updated.add(v);
			else 
				notUpdated = v;
		}

		//2.COMPUTE THE POSITION OF THE VERTEX NOT YET UPDATED
		//  a.compute the length of the sides
		double thisSide = updated.get(0).getRadius() + notUpdated.getRadius();
		double otherSide = updated.get(1).getRadius() + notUpdated.getRadius();
		//  b.get the circles with radii the computed lengths
		Circle circleOfbPrev = new Circle(new Vec2D(
				(float)updated.get(0).getX(), 
				(float)updated.get(0).getY()), (float)thisSide);

		Circle circleOfcPrev = new Circle(new Vec2D(
				(float)updated.get(1).getX(), 
				(float)updated.get(1).getY()), (float)otherSide);

		//  c.the circle intersections gives us 2 possible vertices
		//    we need to test orientation to determine which is the right one
		//    the orientation of the vertex should be the opposite of the
		//	  orientation of unshared vertex of the previously processed face
		//    with regard to the same two vertices of the updated list
		Vec2D[] intersection = circleOfbPrev.intersectsCircle(circleOfcPrev);
		int prevOrientation = orientPrevious(updated, prevFace);
		
		Vec2D thirdCorner = null;
		if (intersection!=null) {
			for (int i=0; i<intersection.length; i++) {
				int orientation = Utilities.orient(
						new Vec2D((float)updated.get(0).getX(), (float)updated.get(0).getY()), 
						new Vec2D((float)updated.get(1).getX(), (float)updated.get(1).getY()), 
						intersection[i]);
				if (orientation == prevOrientation*(-1)) {
					thirdCorner = intersection[i];
					break;
				}
			}
			notUpdated.setX(thirdCorner.x); 
			notUpdated.setY(thirdCorner.y);
			notUpdated.setUpdated(true);
		}
	}
	
	
	
	/**
	 * Returns the orientation of the unshared vertex of the previous face 
	 * with regard to the two input vertices.
	 *
	 * @param commonVertices the list of the common vertices
	 * @param prevFace the face previously processed
	 * @return -1,0,1 indicating the orientation 
	 */
	private int orientPrevious(ArrayList<CPVertex> commonVertices, CPFace prevFace) {
		CPVertex toOrient = null;
		for (CPVertex v: prevFace.getVertexList()) {
			if (!commonVertices.contains(v))
				toOrient = v;
		}
		int orientation = Utilities.orient(
				commonVertices.get(0), commonVertices.get(1), toOrient);		
		return orientation;
	}
	
	

	/**
	 * Performs one step for the layout visualization assigning vertices 
	 * coordinates for the next petal.
	 */
	public void layoutStep() {
		if (isCompleted)
			return;
		
		//1. GET THE NEXT VERTEX ON THE LINKED LIST THAT HAS 
		//   MORE NEIGHBOR CIRCLES TO BE PLACED
		CPVertex v = getNextVertex(chain.size());
		if (v==null) {
			isCompleted = true;
			return;
		}
		
		//2. GET ITS SURROUNDING CIRCLES
		List<CPFace> petals = v.getFaces();
		
		//3. GET A LIST WITH THE FACES TO BE VISITED IN THE RIGHT ORDER
		//   EXCLUDING THE FIRST AND LAST OF THE LINKED LIST
		CPFace first = chain.getFirst();
		CPFace second = chain.get(1);
		List<CPFace> subset = new ArrayList<CPFace>();
		if (first==petals.get(0) && second==petals.get(petals.size()-1)) {
			subset = petals.subList(1, petals.size()-1);
		} else if (second==petals.get(0) && first==petals.get(petals.size()-1)) {
			for (int i=petals.size()-2; i>0; i--) {
				subset.add(petals.get(i));
			}
		} else if (petals.indexOf(first) > petals.indexOf(second)) {
			for (int i=petals.indexOf(first)+1; i<petals.size()+2; i++) {
				subset.add(petals.get(i%petals.size()));
			}			
		} else if (petals.indexOf(first) < petals.indexOf(second)) {
			for (int i=0; i<petals.indexOf(first); i++) {
				subset.add(petals.get(i));
			}
			for (int i=petals.indexOf(second)+1; i<petals.size(); i++) {
				subset.add(petals.get(i));
			}
		}
		

		for (int i=0; i<subset.size(); i++) {
			if (i==0)
				placeTriple(petals.get(i), first);
			else if (i==subset.size()-1) 
				updatedFaces.add(subset.get(i));
			else 
				placeTriple(petals.get(i), petals.get(i-1));
		}
	}
	
		
	/**
	 * Gets the next vertex on the list with neighbor circles
	 * to be placed. The method will update the linked list
	 * accordingly.
	 *
	 * @param counter the counter
	 * @return the next vertex
	 */
	public CPVertex getNextVertex(int counter) {
		//stopping condition
		if (counter==0)
			return null;
		
		//1. GET THE FIRST AND LAST VERTEX OF THE LINKED LIST
		CPFace first = chain.getFirst();
		CPFace last = chain.getLast();
		
		//2. GET THE VERTEX OF THE HEAD OF THE LINKED LIST THAT
		//   IS NOT SHARED BY THE TAIL OF THE LINKED LIST
		//   (every pair of adjacent triangles has only two vertices in common)
		CPVertex notShared = first.vertexNotShared(last);

		//3. GET THE SURROUNDING TRIANGLES OF THE NOT-SHARED VERTEX 
		List<CPFace> petals = notShared.getFaces();
		
		//4. IF THE POINT HAS MORE THAN 2 SOUROUNDING TRIANGLES
		//   WE RETURN IT 
		if (petals.size()>2) 
			return notShared;

		//5. ELSE REMOVE THE HEAD FROM THE LIST, ADD IT TO THE END AND RECURSE
		CPFace removed = chain.removeFirst();
		chain.add(removed);	
		return getNextVertex(counter--);
	}
		
	
	public ArrayList<CPFace> getUpdatedFaces() {
		return updatedFaces;
	}

}
