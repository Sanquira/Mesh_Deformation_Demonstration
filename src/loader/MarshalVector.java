package loader;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.lwjgl.util.vector.Vector3f;

/*
 * Náhrada za Vector3f, kvůli ukládání. Vector3f není podporován JAXB (API pro ukládání souborů do XML)
 */
@XmlRootElement
public class MarshalVector {
	float x, y, z;

	public MarshalVector() {
	}

	public MarshalVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public MarshalVector(Vector3f vect) {
		this.x = vect.x;
		this.y = vect.y;
		this.z = vect.z;
	}

	public Vector3f getVector() {
		return new Vector3f(x, y, z);
	}
	@XmlAttribute
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	@XmlAttribute
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	@XmlAttribute
	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}

