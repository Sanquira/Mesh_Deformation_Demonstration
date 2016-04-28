package gui.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import meshOperations.transformation.AbstractTransformation;
import net.miginfocom.swing.MigLayout;

public class EditFrame extends JFrame {

	private AbstractTransformation transformation;
	private boolean editing;
	private JPanel customPane;
	private JButton actionButton;
	private ActionListener edit, create;

	public EditFrame(AbstractTransformation trans, boolean edit) {
		this.transformation = trans;
		this.editing = edit;
		constructFrame();
	}

	private void constructFrame() {
		super.setTitle((editing ? "Úprava " : "Tvorba "));
		setSize(800, 600);
		setLayout(new MigLayout("nogrid,fillx", "[]", "[grow 200,fill][]"));

		customPane = new JPanel();
		add(customPane, "wrap");

		JPanel buttonPane = new JPanel();
		actionButton = new JButton((editing ? "Uložit" : "Vytvořit"));
		actionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				edit.actionPerformed(e);
			}

		});
		buttonPane.add(actionButton);
		add(buttonPane);
		transformation.updateEditFrame(this);
	}

	public void setTitle(String title) {
		super.setTitle((editing ? "Úprava " : "Tvorba ") + title);
	}

	public JPanel getPanel() {
		return customPane;
	}

	public void setCreateListener(ActionListener a) {
		create = a;
	}

	public void setEditListener(ActionListener a) {
		edit = a;
	}
}
