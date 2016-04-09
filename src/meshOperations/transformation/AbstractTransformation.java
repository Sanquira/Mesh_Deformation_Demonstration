package meshOperations.transformation;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;

public abstract class AbstractTransformation {

	private String name;

	public AbstractTransformation(String transformationName) {
		name = transformationName;
	}

	public String getName() {
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	abstract public Vector3f transformVertex(Vector3f vertex, float delta);
	abstract public void updateEditFrame(EditFrame frame);

}
