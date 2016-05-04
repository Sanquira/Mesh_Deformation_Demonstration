package meshOperations.transformation;

import javax.swing.JFrame;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;

/*
 * Abstraktni trida transformace.
 * XMlRootElement - značí kořenový element XML
 * XMLAttribute - značí attribut XML
 * XMLElement - značí standardní element XML
 * XMLSeeAlso - odkazuje na potomky, abychom byly schopni uložit list s abstraktní classou
 */
@XmlSeeAlso({TransformationDrawn.class,TransformationTorsion.class,TransformationBend.class})
@XmlRootElement
public abstract class AbstractTransformation {

	private String name;// jmeno transformace
	
	/*
	 * Prázdný konstruktor kvůli ukládání, je potřeba ve všech potomcích
	 */
	public AbstractTransformation(){}
	public AbstractTransformation(String transformationName) {
		name = transformationName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Implementace transformace jednoho vertexu o deltu.
	 */
	abstract public Vector3f transformVertex(Vector3f vertex, float delta);

	/*
	 * Implementace okna nastaveni parametru transformace.
	 */
	abstract public void updateEditFrame(EditFrame frame);

}
