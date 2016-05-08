package gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import meshOperations.transformation.AbstractTransformation;
import net.miginfocom.swing.MigLayout;

public class EditFrame extends JFrame {

	private AbstractTransformation transformation;
	public boolean editing;
	private JPanel customPane;
	private JButton actionButton;
	private ActionListener edit, create;

	public EditFrame(AbstractTransformation trans, boolean edit) {
		this.transformation = trans;
		this.editing = edit;
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		constructFrame();
	}

	private void constructFrame() {
		super.setTitle((editing ? "Úprava " : "Tvorba "));
		setSize(800, 600);
		setLayout(new MigLayout("nogrid,fillx", "[]", "[grow 200,fill][]"));

		customPane = new JPanel();
		add(customPane, "wrap");

		JFrame frame = this;

		JPanel buttonPane = new JPanel();
		actionButton = new JButton((editing ? "Uložit" : "Vytvořit"));
		actionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				edit.actionPerformed(e);
				if (!editing) {
					create.actionPerformed(e);
					frame.dispose();
				}
			}

		});
		JButton closeButton = new JButton("Zavřít");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				if (!editing) {
					transformation = null;
				}
			}
		});
		buttonPane.add(actionButton);
		buttonPane.add(closeButton);
		add(buttonPane);
		transformation.updateEditFrame(this);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle((editing ? "Úprava " : "Tvorba ") + title);
	}

	public JPanel getPanel() {
		return customPane;
	}

	public void setEditListener(ActionListener a) {
		edit = a;
	}

	public EditFrame setCreateListener(ActionListener e) {
		create = e;
		return this;
	}
}
