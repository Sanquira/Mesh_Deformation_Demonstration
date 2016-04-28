package meshOperations;

import java.awt.Color;
import java.util.ArrayList;

import meshOperations.transformation.AbstractTransformation;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

/*
 * Trida fronty mesh transformaci.
 */
public class MeshTransformer {

	// Fronta transformaci
	ArrayList<AbstractTransformation> tr = new ArrayList<>();

	/*
	 * Metoda provadejici transformaci mesh mrizky entity.
	 * Vraci transformovanou entitu.
	 * Aplikuje postupne vsechny transformace ve fronte a kazdy vertex.
	 * Parametr delta umoznuje provest jen cast transformace.
	 */
	public Entity transformtEntity(Entity entity, float delta) {
		Color[] colors = entity.getColor();
		int[] indicies = entity.getIndexes();
		Vector3f[] vertices = entity.getVerticies();
		Vector3f[] newVertices = new Vector3f[vertices.length];

		for (int i = 0; i < newVertices.length; i++) {
			newVertices[i] = new Vector3f(vertices[i].x, vertices[i].y, vertices[i].z);
		}

		Color[] newColors = new Color[colors.length];
		for (AbstractTransformation at : tr) { // cyklus transformaci

			for (int i = 0; i < vertices.length; i++) { // cyklus vertexu
				newVertices[i] = at.transformVertex(newVertices[i], delta);
				newColors[i] = colors[i];
			}

		}

		return new Entity(newVertices, newColors, indicies);
	}

	/*
	 * Prida transformaci do fronty
	 */
	public void addTransformation(AbstractTransformation td) {
		tr.add(td);
	}

}
