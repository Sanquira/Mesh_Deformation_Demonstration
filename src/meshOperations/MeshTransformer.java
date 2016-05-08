package meshOperations;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import meshOperations.transformation.AbstractTransformation;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

/*
 * Trida fronty mesh transformaci.
 */
public class MeshTransformer {

	// Fronta transformaci
	CopyOnWriteArrayList<AbstractTransformation> transformationList = new CopyOnWriteArrayList<>();

	/*
	 * Metoda provadejici transformaci mesh mrizky entity.
	 * Vraci transformovanou entitu.
	 * Aplikuje postupne vsechny transformace ve fronte a kazdy vertex.
	 * Parametr delta umoznuje provest jen cast transformace.
	 */
	public Entity transformtEntity(Entity entity, float delta) {
		if (transformationList.isEmpty()) {
			return entity;
		}

		int[] indicies = entity.getIndexes();
		Vector3f[] vertices = entity.getVerticies();
		Vector3f[] newVertices = new Vector3f[vertices.length];

		for (int i = 0; i < newVertices.length; i++) {
			newVertices[i] = new Vector3f(vertices[i].x, vertices[i].y, vertices[i].z);
		}

		for (AbstractTransformation at : transformationList) { // cyklus transformaci

			for (int i = 0; i < vertices.length; i++) { // cyklus vertexu
				newVertices[i] = at.transformVertex(newVertices[i], delta);

			}

		}

		return new Entity(newVertices, indicies);
	}

	public void addTransformation(AbstractTransformation td) {
		transformationList.add(td);
	}

	public void addTransformationAll(ArrayList<AbstractTransformation> list) {
		transformationList.addAll(list);
	}

	public void removeTransformation(AbstractTransformation tr) {
		transformationList.remove(tr);
	}

	public CopyOnWriteArrayList<AbstractTransformation> getTransformationAll() {
		return transformationList;
	}

	public void clearAll() {
		transformationList.clear();
	}

}
