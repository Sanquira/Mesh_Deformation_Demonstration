package meshOperations.transformation;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;

/*
 * Abstraktni trida transformace.
 */
public abstract class AbstractTransformation {

	private String name;// jmeno transformace

	public AbstractTransformation(String transformationName) {
		name = transformationName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Implementace transformace jednoho vertexu o deltu.
	 */
	abstract public Vector3f transformVertex(Vector3f vertex, float delta);

	/*
	 * Implementace okna nastaveni parametru transformace.
	 */
	abstract public void updateEditFrame(EditFrame frame);

}
