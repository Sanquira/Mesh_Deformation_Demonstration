package source;

/*
 * Zakladni matematicke funkce.
 */
public class MathToolbox {

	/*
	 * Vraci cislo mezi 0 a 1 jako hodnotu sigmoid funkce.
	 */
	public static float getWeightNumber(float position) {
		return (float) (2 / (1 + Math.exp(-position * 5))) - 1;
	}

	/*
	 * Vraci cislo mezi 0 a 1 jako hodnotu derivace sigmoidy. 98% plochy funkce je v intervalu -1 a 1.
	 */
	public static float getWeightDerivationNumber(float position) {
		return (float) (Math.exp(-position * 5) / ((1 + Math.exp(-position * 5)) * (1 + Math.exp(-position * 5))));
	}

}
