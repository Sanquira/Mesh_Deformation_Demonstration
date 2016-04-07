package meshOperations.transformation;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Vector3f;

public class TransformationBend extends AbstractTransformation {

	public TransformationBend(String transformationName) {
		super(transformationName);
	}

	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		return null;
	}

	@Override
	public JFrame createEditFrame() {
		return null;
	}

}
