package entities;

import org.lwjgl.util.vector.Vector3f;

/*
 * Objekt reprezentujici grafickou entitu. 
 * Obsahuje seznam vertexu a k nim prislusnych barev a pole indexu (navod k sestaveni). 
 * Obsahuje parametry transformace telesa a umoznuje jejich nastavovani.
 */
public class Entity {
	private Vector3f[] verticies;
	private int[] indexes;

	private Vector3f position = new Vector3f();
	private float rotX = 0, rotY = 0, rotZ = 0;
	private Vector3f scale = new Vector3f(1, 1, 1);

	public Entity(Vector3f[] verticies, int[] indexes) {
		this.verticies = verticies;
		this.indexes = indexes;
	}

	public void increasePosition(float dx, float dy, float dz) {
		position.x += dx;
		position.y += dy;
		position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		rotX = (rotX + dx) % 360;
		rotY = (rotY + dy) % 360;
		rotZ = (rotZ + dz) % 360;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Vector3f[] getVerticies() {
		return verticies;
	}

	public int[] getIndexes() {
		return indexes;
	}

}
