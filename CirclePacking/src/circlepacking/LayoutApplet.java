package circlepacking;
import controlP5.*;
import processing.core.*;
import toxi.geom.Vec2D;

public class LayoutApplet extends PApplet {
	private static final long serialVersionUID = 1L;
	private int sizeX;
	private int sizeY;
	private ControlP5 cp5;
	private Complex k;
	private LayoutCircles lc;
	private String stepLayout = "start";
	private String runLayout = "layout step";
	static public boolean doLayout = false;
	
	public LayoutApplet(int x, int y, Complex k) {
		this.sizeX = x;
		this.sizeY = y;
		this.k = k;
		this.lc = new LayoutCircles(k);
	}
	
	
	public void setup() {
		size(sizeX, sizeY);
		background(255);
		frameRate(30);
		  
		cp5 = new ControlP5(this);
		cp5.addButton(stepLayout)
			.setValue(0).setPosition(20, 30).setSize(100, 15);
		cp5.addButton(runLayout)
		.setValue(0).setPosition(20, 50).setSize(100, 15);
	}

	
	public void draw() {
		if(!doLayout) {
			this.lc = new LayoutCircles(k);
		}
		background(255);
		if (doLayout && !lc.getUpdatedFaces().isEmpty()) {			
			pushMatrix();
			
			beginShape(TRIANGLES);
			translate(sizeX/2, sizeY/2);

			for (int i=0; i<lc.getUpdatedFaces().size();i++) {
				CPFace updated = lc.getUpdatedFaces().get(i);
				for (CPVertex v: updated.getVertexList()) {
					if (i==0) 
						fill(255, 255, 224);
					else 
						noFill();
					ellipseMode(RADIUS);
					ellipse((float) v.getX(), (float) v.getY(), (float) v.getRadius(),
							(float) v.getRadius());
					noFill();
					stroke(0, 40);
					vertex((float)v.getX(), (float)v.getY());	
				}
				fill(50);
				Vec2D textPos = updated.getAvgVertex(false);
				text(updated.getIdNumber(), textPos.x, textPos.y);
			}
			endShape();
			popMatrix();			
		}
	}
	
	
	public void controlEvent(ControlEvent theEvent) {
		String name = theEvent.getController().getName();
		if (name.equals(stepLayout) && k.isUpdated()) {
			lc.placeFirstPetal();
			doLayout = true;
		}	
		if (name.equals(runLayout) && k.isUpdated()) {
			//lc.layoutStep();
		}
	}
	
	
}
