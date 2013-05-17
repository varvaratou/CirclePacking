package circlepacking;


public class CirclePacker {

	private boolean debug = true;
	private double error;
	private Complex k;


	public CirclePacker(Complex k, double e) {
		this.k = k;
		this.error = e;
	}

	
	public void initialize(double boundVal, double min, double max) {
		//1.SET BOUNDARY RADII TO ASSIGNED VALUES
		for (CPVertex v : k.getExtVertices()) {
			v.setRadius(boundVal);
			v.setIsInterior(false);
		}
		//2.SET INTERIOR RADII TO ARBITRARY VALUES
		for (CPVertex v : k.getIntVertices()) {
			double random = Math.random() * (max - min) + min;
			v.setRadius(random);
			v.setIsInterior(true);
		}
	}

	
	/**
	 * Computes the radii of the internal circles.
	 */
	public void computeRadii() {
		//FOR EVERY *INTERIOR* VERTEX
		for (CPVertex v : k.getIntVertices()) {
			// 1.COMPUTE THE INTERIOR ANGLE SUM USING THE LAW OF COSINES
			double angleSum = v.getAngleSum();
			if (debug)
				System.out.println("Computed angleSum: " + angleSum);
			// 2.ADJUST THE RADIUS TO DECREASE THE DIFFERENCE
			if (2 * Math.PI != angleSum) {
				double prediction = predictNextRadius(v, angleSum);
				if (debug)
					System.out.println("Predicted radius: " + prediction);
				v.setRadius(prediction);
			}
		}
	}

	
	/**
	 * Checks if is packed.
	 *
	 * @return true, if is packed
	 */
	public boolean isPacked() {
		for (CPVertex v : k.getIntVertices()) {
			if (Math.abs(2 * Math.PI - v.getAngleSum()) > error) {
				if (debug)
					System.out.println("Not yet packed!");
				return false;
			}
		}
		if (debug)
			System.out.println("Circle Packed!");
		return true;
	}

	
	/**
	 * Predict next radius.
	 *
	 * @param v the v
	 * @param angleSum the angle sum
	 * @return the double
	 */
	private double predictNextRadius(CPVertex v, double angleSum) {
		//USE THE UNIFORM NEIGHBOR MODEL TO PREDICT NEXT RADIUS VALUE
		int numPetals = v.getFaces().size();
		double beta = Math.sin(angleSum / (2 * numPetals));
		double delta = Math.sin(Math.PI / numPetals);

		double newRadius = (beta / (1 - beta)) * v.getRadius()
				* ((1 - delta) / delta);
		return newRadius;
	}
}
