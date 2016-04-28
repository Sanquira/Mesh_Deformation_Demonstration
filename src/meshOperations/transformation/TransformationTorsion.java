package meshOperations.transformation;

import javax.swing.JFrame;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;
import source.MathToolbox;
/*
* Transformace krutu. Definuje dva body a hodnotu otoceni. Spojnice bodu je kolmice rovin jimi prochazejicich a urcujicich oblast transformace. Hodnota krutu je uhel o ktery se pootoci vertexy ve vzdalenosti hranicnich bodu a vetsi.
*/
public class TransformationTorsion extends AbstractTransformation {

	Vector3f plain1Point, plain2Point, normalVectorNormalized;
	float angle, d;

/*
* Konstruktor a nastaveni parametru transformace. (Je zatim v konstruktoru protoze neni implementovana metoda updateEditFrame.)
*/
	public TransformationTorsion(String transformationName, Vector3f plain1Point, Vector3f plain2Point, float angle) {
		super(transformationName);
		this.plain1Point = plain1Point;
		this.plain2Point = plain2Point;
		this.angle = angle;

		Vector3f centralPlain = (Vector3f) Vector3f.add(plain1Point, plain2Point, null).scale(0.5F); //bod ve stredu spojnice
		Vector3f normalVector = Vector3f.sub(plain2Point, centralPlain, null);

		normalVectorNormalized = normalVector.normalise(null);	//normalizovany vektor spojnice definicnich bodu
		d = -Vector3f.dot(centralPlain, normalVectorNormalized);	//posun roviny stredu transformace (v puli spojnice), spolu s normalou definuje rovinu tvořici střed transformace (vertexy na ni se nehybaji)
	}

	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		float weightX = Vector3f.dot(normalVectorNormalized, vertex) + d;	//vzdalenost vertexu od stredove roviny
		float weightY = MathToolbox.getWeightNumber(weightX);	//pomerove otoceni vertexu vuci stredove rovine <-1;1>

		float d = -Vector3f.dot(vertex, normalVectorNormalized);	// hledani roviny kolme na spojnici, prochazejici vertexem
		float ratio = (-d - Vector3f.dot(normalVectorNormalized, plain2Point)) / Vector3f.dot(normalVectorNormalized, normalVectorNormalized);	//hledani parametru pruseciku spojnice a roviny na ni kolme prochazejici vertexem
		Vector3f tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f p1 = Vector3f.add(plain2Point, tmp, null);	// prusecik

		Vector3f.sub(vertex, p1, tmp);	// vektor spojnice pruseciku a vertexu (stejna velikost a smer ale vede z nuly)
		Matrix3f.transform(vytvorRotacniMatici(weightY * angle * delta, normalVectorNormalized), tmp, tmp);	// otoceni spojnice pruseciku a vertexu podle normaloveho vektoru o uhel
		Vector3f.add(tmp, p1, tmp);	// presun transformovane spojnice pruseciku a vertexu (nyni vede z pruseciku a udava primo polohu transformavaneho vertexu)

		return tmp;

	}

/*
 * Metoda vytvarejici rotacni matici podle obecne osy.
*/
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

/*
* Metoda okna pro vytvareni/upravu transformace
*/
	@Override
	public void updateEditFrame(EditFrame frame) {
		
		
	}

}
