package meshOperations;

import java.awt.Color;
import java.util.ArrayList;

import meshOperations.transformation.AbstractTransformation;
import meshOperations.transformation.TransformationDrawn;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

public class MeshTransformer {

	ArrayList<AbstractTransformation> tr = new ArrayList<>();

	public Entity transformtEntity(Entity entity, float delta) {
		Color[] colors = entity.getColor();
		int[] indicies = entity.getIndexes();
		Vector3f[] vertices = entity.getVerticies();
		Vector3f[] newVertices = new Vector3f[vertices.length];

		for (int i = 0; i < newVertices.length; i++) {
			newVertices[i] = new Vector3f(vertices[i].x, vertices[i].y, vertices[i].z);
		}

		Color[] newColors = new Color[colors.length];
		for (AbstractTransformation at : tr) {

			for (int i = 0; i < vertices.length; i++) {
				newVertices[i] = at.transformVertex(newVertices[i], delta);
				newColors[i] = colors[i];
			}

		}

		return new Entity(newVertices, newColors, indicies);
	}

	public void addTransformation(AbstractTransformation td) {
		tr.add(td);
	}

}
