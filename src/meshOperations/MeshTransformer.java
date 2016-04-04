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
		Color[] newColors = new Color[colors.length];
		for (AbstractTransformation at : tr) {

			for (int i = 0; i < vertices.length; i++) {
				newVertices[i] = at.transformVertex(vertices[i], delta);
				newColors[i] = colors[i];
			}

		}

		return new Entity(newVertices, newColors, indicies);
	}

	public void addTransformation(TransformationDrawn td) {
		tr.add(td);
	}

}
