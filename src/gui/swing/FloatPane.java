package gui.swing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class FloatPane extends JPanel {

	private JNumberTextField txt;

	public FloatPane(String name, Float text) {
		setLayout(new BorderLayout());
		add(new JLabel(name), BorderLayout.WEST);

		JPanel pane = new JPanel();
		pane.setLayout(new MigLayout("aligny center,fillx", "[]", "[]"));
		txt = new JNumberTextField();
		txt.setFormat(JNumberTextField.DECIMAL);
		txt.setPrecision(10);
		txt.setInt(0);
		pane.add(txt, "growx");
		add(pane, BorderLayout.CENTER);
		if (text != null) {
			setFloat(text);
		}
	}

	public float getFloat() {
		return txt.getFloat();
	}

	public void setFloat(Float text) {
		txt.setFloat(text);
	}
}
