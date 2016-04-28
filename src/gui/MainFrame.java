package gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import loader.EntityLoader;
import meshOperations.MeshTransformer;
import meshOperations.transformation.TransformationDrawn;
import meshOperations.transformation.TransformationTorsion;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import gui.opengl.MasterRenderer;

/*
* Hlavni okno aplikace.
*/
public class MainFrame extends JFrame {

	private boolean run;	//bezi program?
	private Canvas canvas;	//canvas pro OpenGL

	private static final int FPS_CAP = 60;	//maximalni FPS OpenGL

	public MainFrame(int displayWidth, int displayHeight) {

		//nem
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				run = false;
			}
		});

		init(displayWidth, displayHeight);

		pack();
		setVisible(run);

		initGL(displayWidth, displayHeight);

	}

	private void init(int displayWidth, int displayHeight) {
		run = true;
		setLayout(new BorderLayout());

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(displayWidth, displayHeight));
		canvas.setIgnoreRepaint(true);

		add(canvas, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.setPreferredSize(new Dimension(200,0));
		JButton button = new JButton("test");
		JScrollPane transformationList = new JScrollPane();

		rightPanel.add(button);
		rightPanel.add(transformationList);
		add(rightPanel, BorderLayout.EAST);

	}

	private void initGL(int displayWidth, int displayHeight) {
		try {
			Entity entity = EntityLoader.showDialog();
			if (entity == null) {
				run = false;
			}

			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.create();
			GL11.glViewport(0, 0, displayWidth, displayHeight);

			Camera camera = new Camera(1.1207963368531804, 5, 0.4907963621106486, new Vector3f(0, 0, 0));
			MasterRenderer masterRenderer = new MasterRenderer(displayWidth, displayHeight);

			MeshTransformer mt = new MeshTransformer();

			TransformationDrawn td = new TransformationDrawn("test", new Vector3f(-0.5F, 0, 0), new Vector3f(1F, 0, 0), 1);
			TransformationTorsion ts = new TransformationTorsion("test", new Vector3f(-1F, 0, 0), new Vector3f(1F, 0, 0), 1);

			mt.addTransformation(ts);
			mt.addTransformation(td);

			float delta = 0;
			float smer = 0.01F;
			while (run) {
				if (Display.wasResized()) {
					GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
					masterRenderer.createProjectionMatrix(canvas.getWidth(), canvas.getHeight());
				}

				camera.move();

				delta += smer;
				if (delta >= 1 || delta < 0) {
					smer *= -1;
				}

				Entity bendedEntity = mt.transformtEntity(entity, delta);

				masterRenderer.processEntity(bendedEntity);

				masterRenderer.render(camera);

				Display.sync(FPS_CAP);
				Display.update();
			}

			Display.destroy();
			dispose();
			System.exit(0);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
