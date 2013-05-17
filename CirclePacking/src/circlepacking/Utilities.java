package circlepacking;
import java.awt.Point;

import toxi.geom.Vec2D;

public class Utilities {

	
	public static double determinant(double m[][], int dim) {
		double subMatrix[][];
		double result;

		if (dim == 1)
			result = m[0][0];
		else if (dim == 2)
			result = m[0][0] * m[1][1] - m[1][0] * m[0][1];

		else {
			result = 0;
			for (int p = 0; p < dim; p++) {
				subMatrix = new double[dim - 1][];
				for (int r = 0; r < (dim - 1); r++)
					subMatrix[r] = new double[dim - 1];
				for (int i = 1; i < dim; i++) {
					int count = 0;
					for (int j = 0; j < dim; j++) {
						if (j == p)
							continue;
						subMatrix[i - 1][count] = m[i][j];
						count++;
					}
				}
				result += Math.pow(-1.0, 1.0 + p + 1.0) * m[0][p]
						* determinant(subMatrix, dim - 1);
			}
		}
		return result;
	}

	
	
	public static int orient(Point p, Point q, Point r) {
		double[][] matrix = new double[][] { 
				{ 1.0, p.x, p.y },
				{ 1.0, q.x, q.y }, 
				{ 1.0, r.x, r.y } };
		if (determinant(matrix, 3)>0) 
			return 1;
		else if (determinant(matrix, 3)<0) 
			return -1;
		else 
			return 0;
	}
	
	public static int orient(CPVertex p, CPVertex q, CPVertex r) {
		double[][] matrix = new double[][] { 
				{ 1.0, p.getX(), p.getY() },
				{ 1.0, q.getX(), q.getY() }, 
				{ 1.0, r.getX(), r.getY() } };
		if (determinant(matrix, 3)>0) 
			return 1;
		else if (determinant(matrix, 3)<0) 
			return -1;
		else 
			return 0;
	}
	
	public static int orient(Vec2D p, Vec2D q, Vec2D r) {
		double[][] matrix = new double[][] { 
				{ 1.0, p.x, p.y },
				{ 1.0, q.x, q.y }, 
				{ 1.0, r.x, r.y } };
		if (determinant(matrix, 3)>0) 
			return 1;
		else if (determinant(matrix, 3)<0) 
			return -1;
		else 
			return 0;
	}
	
	
	
	public static void main (String[] args) {
		//testing a simple example
		Point pnt0 = new Point(0,0);
		Point pnt1 = new Point(5,5);
		Point toOrient1 = new Point(2,2);
		Point toOrient2 = new Point(1,2);
		Point toOrient3 = new Point(2,1);
		System.out.println(orient(pnt0, pnt1, toOrient1));
		System.out.println(orient(pnt0, pnt1, toOrient2));
		System.out.println(orient(pnt0, pnt1, toOrient3));
	}
	

}
