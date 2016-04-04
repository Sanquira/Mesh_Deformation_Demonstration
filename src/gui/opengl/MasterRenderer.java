package gui.opengl;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;

public class MasterRenderer {
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 2000;

	private static final float RED = 0.7f;
	private static final float GREEN = 0.7f;
	private static final float BLUE = 0.7f;

	private ArrayList<Entity> entities = new ArrayList<>();

	public MasterRenderer(int width, int height) {
		// enableCulling();
		createProjectionMatrix(width, height);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(Camera camera) {
		prepare();

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		Vector3f camPos = camera.getCameraPosition();
		Vector3f camTrg = camera.getCameraTarget();
		GLU.gluLookAt(camPos.x, camPos.y, camPos.z, camTrg.x, camTrg.y, camTrg.z, 0, 1, 0);

		for (Entity entity : entities) {
			Vector3f[] vertices = entity.getVerticies();
			Color[] colors = entity.getColor();
			int[] indexes = entity.getIndexes();

			Vector3f entityPos = entity.getPosition();
			Vector3f entityScale = entity.getScale();
			GL11.glPushMatrix();
			GL11.glRotatef(entity.getRotZ(), 0, 0, 1);
			GL11.glRotatef(entity.getRotY(), 0, 1, 0);
			GL11.glRotatef(entity.getRotX(), 1, 0, 0);
			GL11.glScalef(entityScale.x, entityScale.y, entityScale.z);
			GL11.glTranslatef(entityPos.x, entityPos.y, entityPos.z);

			GL11.glBegin(GL11.GL_TRIANGLES);
			for (int i = 0; i < indexes.length; i++) {
				int idx = indexes[i];
				Vector3f vertex = vertices[idx];
				Color clr = colors[idx];
				GL11.glColor3f(clr.getRed(), clr.getGreen(), clr.getBlue());
				GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
			}
			GL11.glEnd();

			GL11.glLineWidth(3);
			GL11.glPushMatrix();
			GL11.glScalef(1.001F, 1.001F, 1.001F);
			GL11.glBegin(GL11.GL_LINES);
			for (int i = 0; i < indexes.length; i += 3) {
				int idx = indexes[i];
				int idx2 = indexes[i + 1];
				int idx3 = indexes[i + 2];
				Vector3f vertex = vertices[idx];
				Vector3f vertex2 = vertices[idx2];
				Vector3f vertex3 = vertices[idx3];
				GL11.glColor3f(1, 1, 0);
				GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
				GL11.glVertex3f(vertex2.x, vertex2.y, vertex2.z);
				GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
				GL11.glVertex3f(vertex3.x, vertex3.y, vertex3.z);
				GL11.glVertex3f(vertex2.x, vertex2.y, vertex2.z);
				GL11.glVertex3f(vertex3.x, vertex3.y, vertex3.z);
			}
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
		entities.clear();
	}

	public void processEntity(Entity entity) {
		entities.add(entity);
	}

	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

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