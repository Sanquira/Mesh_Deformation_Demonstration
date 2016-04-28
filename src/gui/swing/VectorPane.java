package gui.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lwjgl.util.vector.Vector3f;
import net.miginfocom.swing.MigLayout;

public class VectorPane extends JPanel {

	public JNumberTextField x, y, z;

	public VectorPane(String text, Vector3f vec) {
		setLayout(new BorderLayout());
		add(new JLabel(text), BorderLayout.WEST);

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(1, 3, 5, 0));

		JPanel xPane = new JPanel();
		xPane.setLayout(new MigLayout("aligny center,fillx", "[]", "[]"));
		x = new JNumberTextField();
		x.setFormat(JNumberTextField.DECIMAL);
		x.setPrecision(10);
		x.setInt(0);
		xPane.add(x, "growx");

		JPanel yPane = new JPanel();
		yPane.setLayout(new MigLayout("aligny center,fillx", "[110px]", "[300px]"));
		y = new JNumberTextField();
		y.setFormat(JNumberTextField.DECIMAL);
		y.setPrecision(10);
		y.setInt(1);
		yPane.add(y, "growx");

		JPanel zPane = new JPanel();
		zPane.setLayout(new MigLayout("aligny center,fillx", "[110px]", "[300px]"));
		z = new JNumberTextField();
		z.setFormat(JNumberTextField.DECIMAL);
		z.setPrecision(10);
		z.setInt(2);
		zPane.add(z, "growx");

		fields.add(xPane);
		fields.add(yPane);
		fields.add(zPane);

		add(fields, BorderLayout.CENTER);
		if (vec != null) {
			setVector(vec);
		}
	}

	public Vector3f getVector() {
		return new Vector3f(x.getFloat(), y.getFloat(), z.getFloat());
	}

	public void setVector(Vector3f vec) {
		x.setFloat(vec.x);
		y.setFloat(vec.y);
		z.setFloat(vec.z);
	}
}
