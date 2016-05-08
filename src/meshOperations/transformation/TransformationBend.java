package meshOperations.transformation;

import gui.swing.EditFrame;
import gui.swing.FloatPane;
import gui.swing.StringPane;
import gui.swing.VectorPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import loader.MarshalVector;

import org.lwjgl.util.vector.Vector3f;

import source.MathToolbox;

/*
 * Tranformace ohybu.
 */
public class TransformationBend extends AbstractTransformation {

	@XmlTransient
	Vector3f point, direction;

	@XmlElement
	float radius, bend;

	public TransformationBend() {
	}

	public TransformationBend(String transformationName, Vector3f point, Vector3f direction, float radius, float bend) {
		super(transformationName);
		this.point = point;
		this.radius = radius;
		this.bend = bend;
		this.direction = direction;
		setup();
	}

	private void setup() {
		if (direction.length() != 0) {
			this.direction = (Vector3f) this.direction.normalise();
		}
	}

	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		if (direction.length() == 0) {
			return vertex;
		}
		float k = Vector3f.dot(Vector3f.sub(point, vertex, null), direction) / Vector3f.dot(direction, direction);
		Vector3f tmp = new Vector3f(k * direction.x, k * direction.y, k * direction.z);
		Vector3f.add(point, tmp, tmp); // bod na primce nejbliz k vertexu

		float weightX = 2 * (Vector3f.add(tmp, vertex, null).length() - 1) / radius / 1; // vzdalenost vertexu od stredu transformace
		float weightY = 1 - MathToolbox.getWeightNumber(weightX); // pomerovy posun vertexu

		float ratio = weightY * bend * delta; // finalni hodnota posunuti

		tmp = new Vector3f(ratio * direction.x, ratio * direction.y, ratio * direction.z);

		return Vector3f.add(tmp, vertex, null);
	}

	@Override
	public void updateEditFrame(EditFrame frame) {
		frame.setTitle("Ohybu");
		frame.setSize(600, 200);
		JPanel pane = frame.getPanel();
		pane.setLayout(new GridLayout(3, 2, 10, 10));

		VectorPane plain1 = new VectorPane("střed", point);
		pane.add(plain1);

		VectorPane plain2 = new VectorPane("směr", direction);
		pane.add(plain2);

		StringPane name = new StringPane("Jméno", super.getName());
		if (frame.editing) {
			name.setEnabled(false);
		}
		pane.add(name);

		FloatPane flt = new FloatPane("poloměr", radius);
		pane.add(flt);

		FloatPane bendftl = new FloatPane("ohyb", bend);
		pane.add(bendftl);

		TransformationBend drw = this;
		frame.setEditListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				point = plain1.getVector();
				direction = plain2.getVector();
				radius = flt.getFloat();
				bend = bendftl.getFloat();
				drw.setName(name.getText());
				setup();
			}

		});

	}

	public MarshalVector getPoint() {
		return new MarshalVector(point);
	}

	public void setPoint(MarshalVector point) {
		this.point = point.getVector();
	}

	public MarshalVector getDirection() {
		return new MarshalVector(direction);
	}

	public void setDirection(MarshalVector direction) {
		this.direction = direction.getVector();
	}

}
