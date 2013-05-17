package convexhull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import circlepacking.CPVertex;

/*
 * This class for computing the convex hull was taken from 
 * http://code.google.com/p/convex-hull/source/browse/Convex+Hull/src/algorithms/FastConvexHull.java?r=4
 * and adapted to work with the CPVertex datastructure
 */
public class FastConvexHull {
	
	@SuppressWarnings("unchecked")
	public ArrayList<CPVertex> execute(ArrayList<CPVertex> points) {
		ArrayList<CPVertex> xSorted = (ArrayList<CPVertex>) points.clone();
		Collections.sort(xSorted, new XCompare());
		int n = xSorted.size();

		CPVertex[] lUpper = new CPVertex[n];

		lUpper[0] = xSorted.get(0);
		lUpper[1] = xSorted.get(1);

		int lUpperSize = 2;

		for (int i = 2; i < n; i++) {
			lUpper[lUpperSize] = xSorted.get(i);
			lUpperSize++;

			while (lUpperSize > 2
					&& !rightTurn(lUpper[lUpperSize - 3],
							lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
				// Remove the middle point of the three last
				lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
				lUpperSize--;
			}
		}

		CPVertex[] lLower = new CPVertex[n];

		lLower[0] = xSorted.get(n - 1);
		lLower[1] = xSorted.get(n - 2);

		int lLowerSize = 2;

		for (int i = n - 3; i >= 0; i--) {
			lLower[lLowerSize] = xSorted.get(i);
			lLowerSize++;

			while (lLowerSize > 2
					&& !rightTurn(lLower[lLowerSize - 3],
							lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
				// Remove the middle point of the three last
				lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
				lLowerSize--;
			}
		}

		ArrayList<CPVertex> result = new ArrayList<CPVertex>();

		for (int i = 0; i < lUpperSize; i++) {
			result.add(lUpper[i]);
		}

		for (int i = 1; i < lLowerSize - 1; i++) {
			result.add(lLower[i]);
		}

		return result;
	}

	private boolean rightTurn(CPVertex a, CPVertex b, CPVertex c) {
		return (b.getPosition().x - a.getPosition().x) * 
				(c.getPosition().y - a.getPosition().y) - 
				(b.getPosition().y - a.getPosition().y) * 
				(c.getPosition().x - a.getPosition().x) > 0;
	}

	private class XCompare implements Comparator<CPVertex> {
		@Override
		public int compare(CPVertex o1, CPVertex o2) {
			return (new Float(o1.getPosition().x))
					.compareTo(new Float(o2.getPosition().x));
		}
	}
}