package gui.opengl;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import source.Config;
import entities.Camera;
import entities.Entity;

public class MasterRenderer {
	// parametry projection transformace
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 2000;

	// barva pozadi
	private static final float RED = 0.7f;
	private static final float GREEN = 0.7f;
	private static final float BLUE = 0.7f;

	private ArrayList<Entity> entities = new ArrayList<>();

	public MasterRenderer(int width, int height) {
		// enableCulling();
		createProjectionMatrix(width, height);
	}

	/*
	 * Culling setri vykon pocitace zakazanim renderovani zadni strany trojuhelniku.
	 * Objekt do ktereho pak vjede kamera se stava neviditelnym.
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	/*
	 * Hlavni renderovaci metoda volana kazdy frame. Neprve vytvori matici kamery a umisti ji do modelview zasobniku. Pak projizdi vsechny entity ve fronte. Z kazde
	 * vytahne jeji transformaci a prinasobi ji k matici kamery, kterou si predtim naduplikuje v zasobniku, aby byla nezmenena k dispozici pro dalsi entitu. Kdyz je
	 * modelview transformace pripravena, vykresli vsechny trojuhelniky entity. Nasledne zvetsi meritko modelu o 0.1% a v tomto meritku vykresli hrany trojuhelniku. Po
	 * vykresleni cas zahodi modelview matici a zacne vykreslovat dalsi entitu. Kdyz jsou vsechny entity ve fronte vykresleny, smaze frontu.
	 */
	public void render(Camera camera) {
		prepare();
		// matice kamery
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		Vector3f camPos = camera.getCameraPosition();
		Vector3f camTrg = camera.getCameraTarget();
		GLU.gluLookAt(camPos.x, camPos.y, camPos.z, camTrg.x, camTrg.y, camTrg.z, 0, 1, 0);

		for (Entity entity : entities) {
			Vector3f[] vertices = entity.getVerticies();
			int[] indexes = entity.getIndexes();
			// transformace entity
			Vector3f entityPos = entity.getPosition();
			Vector3f entityScale = entity.getScale();
			GL11.glPushMatrix();
			GL11.glRotatef(entity.getRotZ(), 0, 0, 1);
			GL11.glRotatef(entity.getRotY(), 0, 1, 0);
			GL11.glRotatef(entity.getRotX(), 1, 0, 0);
			GL11.glScalef(entityScale.x, entityScale.y, entityScale.z);
			GL11.glTranslatef(entityPos.x, entityPos.y, entityPos.z);
			// vykresleni trojuhelniku
			GL11.glBegin(GL11.GL_TRIANGLES);
			for (int i = 0; i < indexes.length; i++) {
				int idx = indexes[i];
				Vector3f vertex = vertices[idx];
				GL11.glColor3f(Config.DEFAULT_COLOR_TRIANGLES.x, Config.DEFAULT_COLOR_TRIANGLES.y, Config.DEFAULT_COLOR_TRIANGLES.z);
				GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
			}
			GL11.glEnd();
			// vykresleni car
			GL11.glLineWidth(2);
			GL11.glScalef(1.001F, 1.001F, 1.001F);
			GL11.glBegin(GL11.GL_LINES);
			for (int i = 0; i < indexes.length; i += 3) {
				int idx = indexes[i];
				int idx2 = indexes[i + 1];
				int idx3 = indexes[i + 2];
				Vector3f vertex = vertices[idx];
				Vector3f vertex2 = vertices[idx2];
				Vector3f vertex3 = vertices[idx3];
				GL11.glColor3f(Config.DEFAULT_COLOR_LINES.x, Config.DEFAULT_COLOR_LINES.y, Config.DEFAULT_COLOR_LINES.z);
				GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
				GL11.glVertex3f(vertex2.x, vertex2.y, vertex2.z);
				GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
				GL11.glVertex3f(vertex3.x, vertex3.y, vertex3.z);
				GL11.glVertex3f(vertex2.x, vertex2.y, vertex2.z);
				GL11.glVertex3f(vertex3.x, vertex3.y, vertex3.z);
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		}
		entities.clear();
	}

	/*
	 * Prida entitu do fronty k vykresleni
	 */
	public void processEntity(Entity entity) {
		entities.add(entity);
	}

	/*
	 * Nastavi OpenGL pred kazdym framem.
	 */
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); // zapne depth test
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // smaze predchozi depth a color buffer
		GL11.glClearColor(RED, GREEN, BLUE, 1); // nastavi barvu pozadi
		GL11.glEnable(GL11.GL_BLEND);// zapne pruhlednost (zbytecne)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);// nastavi funkci pruhlednosti (zbytecne)
	}

	/*
	 * Vytvari projekcni matici a nahraje ji do projection zasobniku
	 */
	public void createProjectionMatrix(int width, int height) {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float aspect = (float) (width) / (float) (height);
		float fH = (float) (Math.tan((FOV / 360.0f * 3.14159f)) * NEAR_PLANE);
		float fW = fH * aspect;
		GL11.glFrustum(-fW, fW, -fH, fH, NEAR_PLANE, FAR_PLANE);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}
}
