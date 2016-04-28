package meshOperations.transformation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;
import gui.swing.FloatPane;
import gui.swing.StringPane;
import gui.swing.VectorPane;
import source.MathToolbox;

/*
 * Transformace tazeni. Definuje dva body a hodnotu tazeni. 
 * Spojnice bodu je kolmice rovin jimi prochazejicich a urcujicich oblast transformace. 
 * Hodnota tazeni je cislo o ktere se oba body vzdali od stredu jejich spojnice.
 */
public class TransformationDrawn extends AbstractTransformation {

	Vector3f plain1Point, plain2Point, normalVectorNormalized;
	float drawn, d;

	public TransformationDrawn(String transformationName, Vector3f plain1Point, Vector3f plain2Point, float drawn) {
		super(transformationName);
		this.plain1Point = plain1Point;
		this.plain2Point = plain2Point;
		this.drawn = drawn;

		setup();
	}

	/*
	 * Metoda nastavujici parametry transformace.
	 */
	public void setup() {
		Vector3f centralPlain = (Vector3f) Vector3f.add(plain1Point, plain2Point, null).scale(0.5F); // bod ve stredu spojnice
		Vector3f normalVector = Vector3f.sub(plain2Point, centralPlain, null);

		normalVectorNormalized = normalVector.normalise(null); // normalizovany vektor spojnice definicnich bodu
		d = -Vector3f.dot(centralPlain, normalVectorNormalized); // posun roviny stredu transformace (v puli spojnice), spolu s normalou definuje rovinu tvořici střed
																	// transformace (vertexy na ni se nehybaji)
	}

	@Override
	public Vector3f transformVertex(Vector3f vertex, float delta) {
		float weightX = Vector3f.dot(normalVectorNormalized, vertex) + d; // vzdalenost vertexu od stredove roviny
		float weightY = MathToolbox.getWeightNumber(weightX); // pomerovy posun vertexu vuci stredove rovine <-1;1>

		float weightYsqrt = MathToolbox.getWeightDerivationNumber(weightX); // pomer zuzeni

		float ratio = weightY * drawn * delta; // finalni hodnota natazeni vertexu

		// vektor posunuti ve smeru kolmem na stredovou rovinu
		Vector3f tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f ret = Vector3f.add(vertex, tmp, null); // provedeni posunuti

		float d = -Vector3f.dot(vertex, normalVectorNormalized); // hledani roviny kolme na spojnici, prochazejici vertexem
		// hledani parametru pruseciku spojnice a roviny na ni kolme prochazejici vertexem
		float t = (-d - Vector3f.dot(normalVectorNormalized, plain2Point)) / Vector3f.dot(normalVectorNormalized, normalVectorNormalized);
		ratio = t;
		tmp = new Vector3f(ratio * normalVectorNormalized.x, ratio * normalVectorNormalized.y, ratio * normalVectorNormalized.z);
		Vector3f p1 = Vector3f.add(plain2Point, tmp, null); // prusecik

		ratio = weightYsqrt * drawn * delta; // finalni hodnota zuzeni
		Vector3f taper = (Vector3f) Vector3f.sub(p1, vertex, null).scale(ratio); // zuzeni jako posun ve smeru od vertexu ke spojnici

		ret = Vector3f.add(ret, taper, null); // spojeni natazeni a zuzeni

		return ret;
	}

	/*
	 * Metoda okna pro vytvareni/upravu transformace
	 */
	@Override
	public void updateEditFrame(EditFrame frame) {
		frame.setTitle("Nečeho?");
		frame.setSize(600, 200);
		JPanel pane = frame.getPanel();
		pane.setLayout(new GridLayout(2, 2, 10, 10));

		VectorPane plain1 = new VectorPane("plain1point", plain1Point);
		pane.add(plain1);

		VectorPane plain2 = new VectorPane("plain2point", plain2Point);
		pane.add(plain2);

		StringPane name = new StringPane("Jméno", super.getName());
		pane.add(name);

		FloatPane flt = new FloatPane("drawn", drawn);
		pane.add(flt);

		TransformationDrawn drw = this;
		frame.setEditListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				plain1Point = plain1.getVector();
				plain2Point = plain2.getVector();
				drawn = flt.getFloat();
				drw.setName(name.getText());
				setup();
			}

		});
	}
}
