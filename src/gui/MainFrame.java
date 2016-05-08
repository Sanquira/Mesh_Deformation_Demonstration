package gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.bind.JAXBException;

import loader.EntityLoader;
import loader.Loader;
import meshOperations.MeshTransformer;
import meshOperations.transformation.AbstractTransformation;
import meshOperations.transformation.TransformationBend;
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
import gui.swing.EditFrame;
import gui.swing.SelectPane;

/*
 * Hlavni okno aplikace.
 */
public class MainFrame extends JFrame {

	private boolean run; // bezi program?
	private Canvas canvas; // canvas pro OpenGL

	private Entity entityToDraw;
	private MeshTransformer mt; // objekt transformaci
	private SelectPane sp;

	private AbstractTransformation tmpTrans = null;

	private static final int FPS_CAP = 60; // maximalni FPS OpenGL

	public MainFrame(int displayWidth, int displayHeight) {

		// vzhledem k pouziti OpenGL kontextu v cavasu, nemuze program natvrdo skoncit pro zavreni.
		// Proto jeho zavreni zakazeme a pri pokusu zavrit transformaci nastavime run na false a tim po dokonceni dalsiho vykreslovaciho cyklu uzavreme OpenGL kontext a
		// ukoncime program.
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

	/*
	 * Vytvoreni non-OpenGL gui
	 */
	private void init(int displayWidth, int displayHeight) {
		mt = new MeshTransformer();
		sp = new SelectPane(mt.getTransformationAll());

		run = true;

		setLayout(new BorderLayout());

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(displayWidth, displayHeight));
		canvas.setIgnoreRepaint(true);

		add(canvas, BorderLayout.CENTER);

		JPanel rightPanel = new JPanel(new GridLayout(2, 1));
		rightPanel.setPreferredSize(new Dimension(200, 0));

		JPanel rightTop = new JPanel(new GridLayout(6, 1));

		// tlacitko nacti objekt
		JButton loadObj = new JButton("Načti objekt");
		loadObj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				entityToDraw = EntityLoader.showDialog();
			}
		});
		rightTop.add(loadObj);

		// tlacitka nacitani a ukladani jedne trasformace
		JPanel loadSaveOne = new JPanel(new GridLayout(1, 2));
		loadSaveOne.setBorder(BorderFactory.createTitledBorder("Jedna transformace:"));

		JButton loadOne = new JButton("Načti");
		loadOne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<AbstractTransformation> tmp = Loader.loadDialog();
					if (tmp != null) {
						mt.addTransformation(tmp.get(0));
						sp.updateList();
					}
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton saveOne = new JButton("Ulož");
		saveOne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CopyOnWriteArrayList<AbstractTransformation> tmpArray = new CopyOnWriteArrayList<AbstractTransformation>();
					tmpArray.add(mt.getTransformationAll().get(sp.selected));
					Loader.saveDialog(tmpArray);
				} catch (FileNotFoundException | JAXBException e1) {
					e1.printStackTrace();
				}
			}

		});
		loadSaveOne.add(loadOne);
		loadSaveOne.add(saveOne);
		rightTop.add(loadSaveOne);

		// tlacitka nacitani a ukladani vsech trasformaci
		JPanel loadSaveAll = new JPanel(new GridLayout(1, 2));
		loadSaveAll.setBorder(BorderFactory.createTitledBorder("Všechny transformace:"));

		JButton loadAll = new JButton("Načti");
		loadAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ArrayList<AbstractTransformation> tmp = Loader.loadDialog();
					if (tmp != null) {
						mt.addTransformationAll(tmp);
						sp.updateList();
					}
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}

		});

		JButton saveAll = new JButton("Ulož");
		saveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Loader.saveDialog(mt.getTransformationAll());
				} catch (FileNotFoundException | JAXBException e1) {
					e1.printStackTrace();
				}
			}

		});
		loadSaveAll.add(loadAll);
		loadSaveAll.add(saveAll);
		rightTop.add(loadSaveAll);

		ActionListener createListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mt.addTransformation(tmpTrans);
				sp.updateList();
			}
		};

		JButton addBend = new JButton("Přidej ohyb");
		addBend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tmpTrans = new TransformationBend("Ohyb", new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), 0.5F, 1);
				new EditFrame(tmpTrans, false).setCreateListener(createListener).setVisible(true);
			}
		});

		JButton addTorsion = new JButton("Přidej krut");
		addTorsion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tmpTrans = new TransformationTorsion("Krut", new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0), 0.5f);
				new EditFrame(tmpTrans, false).setCreateListener(createListener).setVisible(true);
			}
		});

		JButton addDrawn = new JButton("Přidej tažení");
		addDrawn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tmpTrans = new TransformationDrawn("Tažení", new Vector3f(0, 0, -1), new Vector3f(0, 0, 1), 1);
				new EditFrame(tmpTrans, false).setCreateListener(createListener).setVisible(true);
			}
		});

		rightTop.add(addBend);
		rightTop.add(addTorsion);
		rightTop.add(addDrawn);

		rightPanel.add(rightTop);

		rightPanel.add(sp);
		add(rightPanel, BorderLayout.EAST);

	}

	/*
	 * OpenGL kontext a vykreslovaci cyklus.
	 */
	private void initGL(int displayWidth, int displayHeight) {
		try {
			// inicializace OpenGL
			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.create();
			GL11.glViewport(0, 0, displayWidth, displayHeight);

			// kamera
			Camera camera = new Camera(1.1207963368531804, 5, 0.4907963621106486, new Vector3f(0, 0, 0));

			// rendrovaci objekt
			MasterRenderer masterRenderer = new MasterRenderer(displayWidth, displayHeight);

			// vykreslovaci cyklus
			float delta = 0;
			float smer = 0.01F;
			while (run) {
				// resize okna
				if (Display.wasResized()) {
					GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
					masterRenderer.createProjectionMatrix(canvas.getWidth(), canvas.getHeight());
				}

				// logika pohybu kamery
				camera.move();

				// docasny pohyb animace
				delta += smer;
				if (delta >= 1 || delta < 0) {
					smer *= -1;
				}

				if (entityToDraw != null) {
					// transformace entity, deformace mesh mrizky, vysledkem je nova entita s deformovanou mzirkou
					Entity bendedEntity = mt.transformtEntity(entityToDraw, delta);

					// predame entitu rendrovacimu enginu
					masterRenderer.processEntity(bendedEntity);
				}

				// vykreslime objekty ve vykreslovacim enginu
				masterRenderer.render(camera);

				// vykresleni kontextu na obrazovku a synchronizace FPS
				Display.sync(FPS_CAP);
				Display.update();
			}

			// ukonceni OpenGL, zavreni okna aplikace a ukonceni programu
			Display.destroy();
			dispose();
			System.exit(0);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
