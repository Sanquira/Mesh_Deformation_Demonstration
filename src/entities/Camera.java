package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import source.Config;

/*
 * Trida reprezentujici to-point kameru. Poloha kamery se odvozuje od bodu na ktery se diva. Resi logiku ovladani kamery.
 */
public class Camera {

	Config cf = Config.getInstance();

	private double azimuth, radius, zenith;

	private Vector3f cameraPosition, cameraDirection, cameraTarget;

	public Camera(double azimuth, double radius, double zenith, Vector3f cameraTarget) {
		this.azimuth = azimuth;
		this.radius = radius;
		this.zenith = zenith;
		this.cameraTarget = cameraTarget;
	}

	/*
	 * Metoda ovladani kamery. Aktualizuje parametry kamery v zavislosti na vstupu z myši. Volana pred kazdym vykreslenim.
	 */
	public void move() {
		// Vycteni HW vstupu
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		int dw = Mouse.getDWheel();
		// Vypne kurzor, pokud je stisknuto nejake tlacitko (brani ujizdeni kurzoru mimo okno)
		if (cf.isSomeMouseDown()) {
			Mouse.setGrabbed(true);
		} else {
			Mouse.setGrabbed(false);
		}
		// otoceni kamery
		if (Mouse.isButtonDown(cf.KEY_ROTATER_CAMERA)) {
			azimuth += dx * cf.MOUSE_SENSITIVITY;
			zenith += -dy * cf.MOUSE_SENSITIVITY;
			zenith = clampRange(zenith, -Math.PI / 2, Math.PI / 2);
		}
		// zoom
		radius += dw * cf.MOUSE_WHEEL_SENSITIVITY;
		if (radius <= 0) {
			radius = 0.01;
		}

		cameraDirection = new Vector3f((float) (Math.cos(azimuth) * Math.cos(zenith)), (float) (Math.sin(zenith)), (float) (Math.sin(azimuth) * Math.cos(zenith)));

		// TODO - posun kamery, nefunguje
		// if (Mouse.isButtonDown(cf.KEY_MOVE_CAMERA)) {
		// double tmpX = dx * cf.MOUSE_SENSITIVITY;
		// double tmpY = dy * cf.MOUSE_SENSITIVITY;
		// Vector3f rayD = new Vector3f(cameraDirection.x,0,cameraDirection.z).normalise(null);
		//
		// cameraPosition.x += tmpY * rayD.x - tmpX * rayD.z;
		// cameraPosition.z += tmpY * rayD.z + tmpX * rayD.x;
		// }

		cameraPosition = Vector3f.add(cameraTarget, new Vector3f(cameraDirection.x * (float) radius, cameraDirection.y * (float) radius, cameraDirection.z * (float) radius), null);
	}

	/*
	 * Omezi vstupni hodnotu na interval.
	 */
	private double clampRange(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public Vector3f getCameraPosition() {
		return cameraPosition;
	}

	public Vector3f getCameraTarget() {
		return cameraTarget;
	}

}
