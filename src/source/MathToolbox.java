package source;

public class MathToolbox {

	// /**
	// * Return number between 0 and 1, which is from derivation of sigmoid function. 98% of function is between -1 and 1 on x axis.
	// * Mathematicaly it's this: 4*E^(-t*5)/(1 + E^(-t*5))^2
	// *
	// * @param position
	// * @return
	// */
	public static float getWeightNumber(float position) {
		// return (float) (4 * Math.exp(-position * 5) / ((1 + Math.exp(-position * 5)) * (1 + Math.exp(-position * 5))));
		return (float) (2 / (1 + Math.exp(-position * 5))) - 1;
	}

	public static float getWeightDerivationNumber(float position) {
		return (float) ( Math.exp(-position * 5) / ((1 + Math.exp(-position * 5)) * (1 + Math.exp(-position * 5))));
	}

}
