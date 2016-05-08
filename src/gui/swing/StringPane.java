package gui.swing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class StringPane extends JPanel {

	private JTextField txt;

	public StringPane(String name, String text) {
		setLayout(new BorderLayout());
		add(new JLabel(name), BorderLayout.WEST);

		JPanel pane = new JPanel();
		pane.setLayout(new MigLayout("aligny center,fillx", "[]", "[]"));
		txt = new JTextField();
		pane.add(txt, "growx");
		add(pane, BorderLayout.CENTER);
		if (text != null) {
			setText(text);
		}
	}
	
	public void setEnabled(boolean enable){
		txt.setEnabled(enable);
	}

	public String getText() {
		return txt.getText();
	}

	public void setText(String text) {
		txt.setText(text);
	}
}
