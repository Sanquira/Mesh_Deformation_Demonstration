package Demos;
import org.lwjgl.util.vector.Vector3f;

import gui.swing.EditFrame;
import meshOperations.transformation.TransformationBend;
import meshOperations.transformation.TransformationDrawn;

public class CreateEditTest {

	public static void main(String[] args) {
		new EditFrame(new TransformationDrawn("",new Vector3f(0,0,0),new Vector3f(0,0,0), 10),false).setVisible(true);
		
	}

}
