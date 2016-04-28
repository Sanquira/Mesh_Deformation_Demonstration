package source;

import org.lwjgl.input.Mouse;

/*
 * Singleton uchovavajici dulezita nastaveni.
 */
public class Config {

	public int KEY_MOVE_CAMERA = 2; // tlacitko posunu kamery
	public int KEY_ROTATER_CAMERA = 1; // tlacitko rotace kamery
	public float MOUSE_SENSITIVITY = 0.03f; // citlivost posunu mysi
	public float MOUSE_WHEEL_SENSITIVITY = 0.01f;// citlivost kolecka mysi

	public boolean isSomeMouseDown() {
		for (int i = 0; i < Mouse.getButtonCount(); i++) {
			if (Mouse.isButtonDown(i)) {
				return true;
			}
		}
		return false;
	}

	private static Config instance = null;

	private Config() {
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

}
