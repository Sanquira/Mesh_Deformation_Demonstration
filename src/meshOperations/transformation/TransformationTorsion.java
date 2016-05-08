package meshOperations.transformation;

import javax.swing.JFrame;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;
import loader.MarshalVector;
import source.MathToolbox;

/*
 * Transformace krutu. Definuje dva body a hodnotu otoceni. 
 * Spojnice bodu je kolmice rovin jimi prochazejicich a urcujicich oblast transformace. 
 * Hodnota krutu je uhel o ktery se pootoci vertexy ve vzdalenosti hranicnich bodu a vetsi.
 */
@XmlRootElement
public class TransformationTorsion extends AbstractTransformation {
	
	@XmlTransient
	Vector3f plain1Point, plain2Point, normalVectorNormalized;
	@XmlElement
	float angle, d;
	
	public TransformationTorsion(){}
	/*
	 * Konstruktor a nastaveni parametru transformace.
	 * (Je zatim v konstruktoru protoze neni implementovana metoda updateEditFrame.)
	 */
	public TransformationTorsion(String transformationName, Vector3f plain1Point, Vector3f plain2Point, float angle) {
		super(transformationName);
		this.plain1Point = plain1Point;
		this.plain2Point = plain2Point;
		this.angle = angle;
		
		setup()
	}
	public setup(){
		Vector3f centralPlain = (Vector3f) Vector3f.add(plain1Point, plain2Point, null).scale(0.5F); // bod ve stredu spojnice
		Vector3f normalVector = Vector3f.sub(plain2Point, centralPlain, null);

		normalVectorNormalized = normalVector.normalise(null); // normalizovany vektor spojnice definicnich bodu
		d = -Vector3f.dot(centralPlain, normalVectorNormalized); // posun roviny stredu transformace (v puli spojnice), spolu s normalou definuje rovinu tvořici střed
																	// transformace (vertexy na ni se nehybaji)	
	}
	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		float weightX = Vector3f.dot(normalVectorNormalized, vertex) + d; // vzdalenost vertexu od stredove roviny
		float weightY = MathToolbox.getWeightNumber(weightX); // pomerove otoceni vertexu vuci stredove rovine <-1;1>

		float d = -Vector3f.dot(vertex, normalVectorNormalized); // hledani roviny kolme na spojnici, prochazejici vertexem
		// hledani parametru pruseciku spojnice a roviny na ni kolme prochazejici vertexem
		float ratio = (-d - Vector3f.dot(normalVectorNormalized, plain2Point)) / Vector3f.dot(normalVectorNormalized, normalVectorNormalized);
		Vector3f tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f p1 = Vector3f.add(plain2Point, tmp, null); // prusecik

		Vector3f.sub(vertex, p1, tmp); // vektor spojnice pruseciku a vertexu (stejna velikost a smer ale vede z nuly)
		Matrix3f.transform(vytvorRotacniMatici(weightY * angle * delta, normalVectorNormalized), tmp, tmp); // otoceni spojnice pruseciku a vertexu podle normaloveho
																											// vektoru o uhel
		Vector3f.add(tmp, p1, tmp); // presun transformovane spojnice pruseciku a vertexu (nyni vede z pruseciku a udava primo polohu transformavaneho vertexu)

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
		frame.setTitle("Krutu?");
		frame.setSize(600, 200);
		JPanel pane = frame.getPanel();
		pane.setLayout(new GridLayout(2, 2, 10, 10));

		VectorPane plain1 = new VectorPane("plain1point", plain1Point);
		pane.add(plain1);

		VectorPane plain2 = new VectorPane("plain2point", plain2Point);
		pane.add(plain2);

		StringPane name = new StringPane("Jméno", super.getName());
		pane.add(name);

		FloatPane flt = new FloatPane("angle", angle);
		pane.add(flt);

		TransformationDrawn drw = this;
		frame.setEditListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				plain1Point = plain1.getVector();
				plain2Point = plain2.getVector();
				angle = flt.getFloat();
				drw.setName(name.getText());
				setup();
			}

		});
	}
	@XmlElement
	public MarshalVector getPlain1Point() {
		return new MarshalVector(plain1Point);
	}

	public void setPlain1Point(MarshalVector plain1Point) {
		this.plain1Point = plain1Point.getVector();
	}
	@XmlElement
	public MarshalVector getPlain2Point() {
		return new MarshalVector(plain2Point);
	}

	public void setPlain2Point(MarshalVector plain2Point) {
		this.plain2Point = plain2Point.getVector();
	}
	@XmlElement
	public MarshalVector getNormalVectorNormalized() {
		return new MarshalVector(normalVectorNormalized);
	}

	public void setNormalVectorNormalized(MarshalVector normalVectorNormalized) {
		this.normalVectorNormalized = normalVectorNormalized.getVector();
	}
	

}
