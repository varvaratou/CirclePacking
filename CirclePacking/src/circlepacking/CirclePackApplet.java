package circlepacking;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import controlP5.*;
import convexhull.*;
import processing.core.*;
import toxi.geom.Vec2D;
import delaunay.*;


public class CirclePackApplet extends PApplet {
	private static final long serialVersionUID = 1L;
	boolean debug = true;
	private int sizeX;
	private int sizeY;
	private ControlP5 cp5;
	private static int initialSize = 100000;
	private double error = 0.0001;
	private String cpStatus = "";
	
	private String stepBtnName = "step circle packing";
	private String runBtnName = "run circle packing";
	private String resetBtnName = "reset graph";
	private String finalBtnName = "finalize graph";
		
	boolean isFinalized = false;	
	boolean canAddPts = true;
	boolean setUp = true;
	boolean drawRadii = false;

	private DelaunayTriangle superTriangle;
	private DelaunayTriangulation delaunayTri;
	private HashSet<DelaunayVertex> pointSet;
	private Complex k;
	private CirclePacker cp;

	
	public CirclePackApplet(int x, int y, Complex k) {
		this.sizeX = x;
		this.sizeY = y;
		this.k = k;
	}
	
	
	public void setup() {
		size(sizeX, sizeY);
		background(255);
		frameRate(30);

		cp5 = new ControlP5(this);
		cp5.addButton(resetBtnName)
			.setValue(0).setPosition(20, 30).setSize(100, 15);
		cp5.addButton(finalBtnName)
			.setValue(0).setPosition(20, 50).setSize(100, 15);
		cp5.addButton(stepBtnName)
			.setValue(0).setPosition(20, 70).setSize(100, 15);
		cp5.addButton(runBtnName)
			.setValue(0).setPosition(20, 90).setSize(100, 15);

		setLock(cp5.getController(stepBtnName), true);
		setLock(cp5.getController(runBtnName), true);
		
		
		superTriangle = new DelaunayTriangle(
				new DelaunayVertex(-initialSize, -initialSize),
				new DelaunayVertex(initialSize, -initialSize), 
				new DelaunayVertex(0, initialSize));
		delaunayTri = new DelaunayTriangulation(superTriangle);
		pointSet = new HashSet<DelaunayVertex>();
	}

	
	public void draw() {
		if (setUp) setUp = false;		
		background(255);
		ellipseMode(RADIUS);
		
		if (pointSet.size()>0) {
			drawPoints();
			
			if (drawRadii) {
				drawRadii();
				
				fill(0, 40);
				stroke(0, 40);
				textSize(12);
				text(cpStatus, 20, 170);
			}	
			
			if (pointSet.size()>2) {
				drawTriangles();
			}
		}
	}
	
		
	public void controlEvent(ControlEvent theEvent) {
		if (!setUp) {
			String name = theEvent.getController().getName();
			if (name.equals(resetBtnName)) {
				delaunayTri = new DelaunayTriangulation(superTriangle);
				pointSet = new HashSet<DelaunayVertex>();
				cpStatus = "";
				k.reset();
				canAddPts = true;
				isFinalized = false;
				drawRadii = false;
				LayoutApplet.doLayout = false;
				
				setLock(cp5.getController(stepBtnName), true);
				setLock(cp5.getController(runBtnName), true);
				setLock(cp5.getController(finalBtnName), false);
			}

			if (name.equals(finalBtnName) && !isFinalized) {
				//build complex K required for the circle-packer
				buildDataStructure();	
				//instantiate a circle-packer and assign the initial radii
				cp = new CirclePacker(k, error);
				cp.initialize(40, 20, 40);
				
				canAddPts = false;
				isFinalized = true;
				drawRadii = true;
				
				setLock(cp5.getController(stepBtnName), false);
				setLock(cp5.getController(runBtnName), false);		
				setLock(cp5.getController(finalBtnName), true);
			}			
			
			if (name.equals(stepBtnName)) {
				cp.computeRadii();
				if (cp.isPacked()) 
					cpStatus = "CIRCLE PACKED!";
				else 
					cpStatus = "NOT YET PACKED!";
			}
			if (name.equals(runBtnName)) {			
				while(!cp.isPacked()) {
					cp.computeRadii();
					cpStatus = "NOT YET PACKED!";
				}
				cpStatus = "CIRCLE PACKED!";
			}
		}
	}
	
	
	public void mousePressed() {
		if (canAddPts && mouseX>150 && mouseY > 30) {
			DelaunayVertex dVertex = new DelaunayVertex(mouseX, mouseY);
			delaunayTri.delaunayPlace(dVertex);
			pointSet.add(dVertex);
			if (debug)
				System.out.println(
						">POINT ADDED @: " + mouseX + ", " + mouseY);
		}
	}
	
	
	public void drawRadii() {
		stroke(0, 40);
		fill(255, 228, 225);
		for (CPVertex v : k.getIntVertices()) {				
			ellipse(v.getPosition().x, v.getPosition().y, 
					(float) v.getRadius(), (float) v.getRadius());
		}
		noFill();
		for (CPVertex v : k.getExtVertices()) {
			ellipse(v.getPosition().x, v.getPosition().y, 
					(float) v.getRadius(), (float) v.getRadius());
		}
	}

	
	public void drawTriangles() {
		noFill();
		stroke(0, 40);
		beginShape(TRIANGLES);
		if (isFinalized) {
			for (CPFace face : k.getFaces()) {
				for (CPVertex v: face.getVertexList()) {
					vertex(v.getPosition().x, v.getPosition().y);				
				}
				Vec2D textPos = face.getAvgVertex(true);
				textSize(9);
				text(face.getIdNumber(), textPos.x, textPos.y);				
			}
		} else {
			for (DelaunayTriangle triangle : delaunayTri) {
				Iterator<DelaunayVertex> itr = triangle.iterator();
				if (!isOfSuperTriangle(triangle)) {
					while (itr.hasNext()) {
						DelaunayVertex point = itr.next();
						vertex((float) point.coord(0), (float) point.coord(1));
					}
				}
			}
		}
		endShape();
	}
	
	
	public void drawPoints() {
		noStroke();
		fill(255, 0, 0);
		for (DelaunayVertex p: pointSet) {
			ellipse((float) p.coord(0), (float) p.coord(1), 2.5f, 2.5f);
		}
		fill(50, 0, 0);
		if(k.getExtVertices().size()>0) {
			for (CPVertex v : k.getExtVertices()) {
				ellipse(v.getPosition().x, v.getPosition().y, 3f, 3f);
			}
		}
	}

	
	@SuppressWarnings("rawtypes")
	void setLock(Controller theController, boolean theValue) {
		theController.setLock(theValue);
		if (theValue) {
			theController.setColorBackground(color(100, 100));
		} else {
			theController.setColorBackground(color(0, 0, 0));
		}
	}
	
	
	private boolean isOfSuperTriangle(DelaunayTriangle otherTriangle) {
		for (DelaunayVertex thisVtx: superTriangle) {
			for (DelaunayVertex otherVtx: otherTriangle) {
				if (thisVtx == otherVtx)
					return true;
			}
		}
		return false;
	}
	
	
	public Complex buildDataStructure() {
		HashMap<DelaunayVertex,CPVertex> delVtxToCpVtx = 
				new HashMap<DelaunayVertex,CPVertex>();
		HashMap<DelaunayTriangle,CPFace> delTriToCpFace = 
				new HashMap<DelaunayTriangle,CPFace>();
		HashMap<DelaunayVertex, ArrayList<DelaunayTriangle>> vertexToTri = 
				new HashMap<DelaunayVertex,ArrayList<DelaunayTriangle>>();
		ArrayList<CPVertex> allVertices = new ArrayList<CPVertex>();
		
		for (DelaunayVertex delVertex: pointSet) {
			CPVertex cpVertex = new CPVertex(delVertex.toVec2D());
			delVtxToCpVtx.put(delVertex, cpVertex);
			allVertices.add(cpVertex);
		}
		
		for (DelaunayTriangle triangle: delaunayTri) {
			if (!isOfSuperTriangle(triangle)) {				
				ArrayList<CPVertex> cpVertices = new ArrayList<CPVertex>();
				for (DelaunayVertex v: triangle) {
					cpVertices.add(delVtxToCpVtx.get(v));
					
					if(!vertexToTri.containsKey(v))
						vertexToTri.put(v, new ArrayList<DelaunayTriangle>());
					vertexToTri.get(v).add(triangle);
				}
				CPFace face = new CPFace(cpVertices, triangle.getIdNumber());
				k.getFaces().add(face);
				delTriToCpFace.put(triangle, face);					
			}
		}
		
		for (DelaunayVertex p: pointSet) {
			List<DelaunayTriangle> neighbors = 
					delaunayTri.surroundingTriangles(p, vertexToTri.get(p).get(0));
			ArrayList<CPFace> faceList = new ArrayList<CPFace>();
			for (DelaunayTriangle t: neighbors) {
				if (!isOfSuperTriangle(t))
					faceList.add(delTriToCpFace.get(t));
			}	
			delVtxToCpVtx.get(p).setFaces(faceList);
		}	
		
		FastConvexHull ch = new FastConvexHull();
		ArrayList<CPVertex> convexChain = ch.execute(allVertices);
		k.setExtVertices(convexChain);
		
		for(CPVertex v: delVtxToCpVtx.values()) {
			if(!k.getExtVertices().contains(v))
				k.getIntVertices().add(v);
		}				
		k.setUpdated(true);
		return k;		
	}

}