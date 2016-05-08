import gui.swing.EditFrame;
import gui.swing.SelectPane;

import java.awt.EventQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

import meshOperations.transformation.AbstractTransformation;

import org.lwjgl.util.vector.Vector3f;

public class GUITest {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUITest window = new GUITest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUITest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CopyOnWriteArrayList<AbstractTransformation> list = new CopyOnWriteArrayList<AbstractTransformation>();
		list.add(new AbstractTransformation("Karel"){
			
			@Override
			public Vector3f transformVertex(Vector3f vertex, float delta) {
				// TODO Auto-generated method stub
				return vertex;
			}

			@Override
			public void updateEditFrame(EditFrame frame) {
				// TODO Auto-generated method stub
				
			}
			
		});
		list.add(new AbstractTransformation("Testik"){
			
			@Override
			public Vector3f transformVertex(Vector3f vertex, float delta) {
				// TODO Auto-generated method stub
				return vertex;
			}

			@Override
			public void updateEditFrame(EditFrame frame) {
				// TODO Auto-generated method stub
				
			}
			
		});
		list.add(new AbstractTransformation("Nuda"){
			
			@Override
			public Vector3f transformVertex(Vector3f vertex, float delta) {
				// TODO Auto-generated method stub
				return vertex;
			}

			@Override
			public void updateEditFrame(EditFrame frame) {
				// TODO Auto-generated method stub
				
			}
			
		});
		frame.getContentPane().add(new SelectPane(list));
	}

}
