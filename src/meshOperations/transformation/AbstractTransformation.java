package meshOperations.transformation;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Vector3f;

public abstract class AbstractTransformation {

	private String name;

	public AbstractTransformation(String transformationName) {
		name = transformationName;
	}

	public String getName() {
		return name;
	}

	abstract public Vector3f transformVertex(Vector3f vertex, float delta);
	abstract public JFrame createEditFrame();

}
