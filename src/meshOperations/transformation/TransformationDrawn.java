package meshOperations.transformation;

import org.lwjgl.util.vector.Vector3f;

import source.MathToolbox;

public class TransformationDrawn extends AbstractTransformation {

	Vector3f plain1Point, plain2Point, normalVector, normalVectorNormalized, centralPlain;
	float drawn, d;

	public TransformationDrawn(String transformationName, Vector3f plain1Point, Vector3f plain2Point, float drawn) {
		super(transformationName);
		this.plain1Point = plain1Point;
		this.plain2Point = plain2Point;
		this.drawn = drawn;

		centralPlain = (Vector3f) Vector3f.add(plain1Point, plain2Point, null).scale(0.5F);
		normalVector = Vector3f.sub(plain2Point, centralPlain, null);
		d = -Vector3f.dot(centralPlain, normalVector);
		normalVectorNormalized = normalVector.normalise(null);

	}

	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		float weightX = Vector3f.dot(normalVector, vertex) + d;
		float weightY = MathToolbox.getWeightNumber(weightX);

		float weightYsqrt = MathToolbox.getWeightDerivationNumber(weightX);//(float) Math.sqrt(Math.abs(weightY));

		float ratio = weightY * drawn * delta;

		Vector3f tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f ret = Vector3f.add(vertex, tmp, null);

		float d = -Vector3f.dot(vertex, normalVectorNormalized);
		float t = (-d - Vector3f.dot(normalVectorNormalized, plain2Point)) / Vector3f.dot(normalVectorNormalized, normalVectorNormalized);
		ratio = t;
		tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f p1 = Vector3f.add(plain2Point, tmp, null);

		ratio = weightYsqrt * drawn * delta;
		Vector3f taper = (Vector3f) Vector3f.sub(p1, vertex, null).scale(ratio);

		ret = Vector3f.add(ret, taper, null);
		
		return ret;
	}
}
