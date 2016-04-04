package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import source.Config;

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

	public void move() {
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		int dw = Mouse.getDWheel();
		if (cf.isSomeMouseDown()) {
			Mouse.setGrabbed(true);
		} else {
			Mouse.setGrabbed(false);
		}
		if (Mouse.isButtonDown(cf.KEY_ROTATER_CAMERA)) {
			azimuth += dx * cf.MOUSE_SENSITIVITY;
			zenith += -dy * cf.MOUSE_SENSITIVITY;
			zenith = ensureRange(zenith, -Math.PI / 2, Math.PI / 2);
		}

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

	private double ensureRange(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public Vector3f getCameraPosition() {
		return cameraPosition;
	}

	public Vector3f getCameraTarget() {
		return cameraTarget;
	}

}
