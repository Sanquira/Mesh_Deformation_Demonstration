package meshOperations.transformation;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;
import source.MathToolbox;

public class TransformationTorsion extends AbstractTransformation {

	Vector3f plain1Point, plain2Point, normalVector, normalVectorNormalized, centralPlain;
	float angle, d;

	public TransformationTorsion(String transformationName, Vector3f plain1Point, Vector3f plain2Point, float angle) {
		super(transformationName);
		this.plain1Point = plain1Point;
		this.plain2Point = plain2Point;
		this.angle = angle;

		centralPlain = (Vector3f) Vector3f.add(plain1Point, plain2Point, null).scale(0.5F);
		normalVector = Vector3f.sub(plain2Point, centralPlain, null);

		normalVectorNormalized = normalVector.normalise(null);
		d = -Vector3f.dot(centralPlain, normalVectorNormalized);
	}

	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		float weightX = Vector3f.dot(normalVectorNormalized, vertex) + d;
		float weightY = MathToolbox.getWeightNumber(weightX);

		float d = -Vector3f.dot(vertex, normalVectorNormalized);
		float ratio = (-d - Vector3f.dot(normalVectorNormalized, plain2Point)) / Vector3f.dot(normalVectorNormalized, normalVectorNormalized);
		Vector3f tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f p1 = Vector3f.add(plain2Point, tmp, null);

		Vector3f.sub(vertex, p1, tmp);
		Matrix3f.transform(vytvorRotacniMatici(weightY * angle * delta, normalVectorNormalized), tmp, tmp);
		Vector3f.add(tmp, p1, tmp);

		return tmp;

	}

	private Matrix3f vytvorRotacniMatici(float angle, Vector3f axis) {
		Vector3f naxis = new Vector3f(axis);
		naxis.normalise(naxis);
		double ux = naxis.x;
		double uy = naxis.y;
		double uz = naxis.z;
		double jmc = 1 - Math.cos(angle);
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		Matrix3f ret = new Matrix3f();
		ret.m00 = (float) (c + ux * ux * jmc);
		ret.m10 = (float) (ux * uy * jmc - uz * s);
		ret.m20 = (float) (ux * uz * jmc + uy * s);
		ret.m01 = (float) (ux * uy * jmc + uz * s);
		ret.m11 = (float) (c + uy * uy * jmc);
		ret.m21 = (float) (uy * uz * jmc - ux * s);
		ret.m02 = (float) (ux * uz * jmc - uy * s);
		ret.m12 = (float) (uy * uz * jmc + ux * s);
		ret.m22 = (float) (c + uz * uz * jmc);
		return ret;
	}

	@Override
	public void updateEditFrame(EditFrame frame) {
		
		
	}

}
