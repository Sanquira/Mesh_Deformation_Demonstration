package loader;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;

/*
* Trida parsujici .obj soubory do entity.
*/
public class EntityLoader {
	// zakladni barva kazdeho vertexu
	private static final Color DEFAULT_COLOR = Color.BLUE;
	
	/*
	* Zobrazi okno nacitani souboru a pri vyberu automaticky zavola nacitani souboru.
	*/
	public static Entity showDialog() {
		String desc = "Bender OBJ files";
		String ext = "obj";

		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(desc + " (*." + ext + ")", ext));
		int returnVal = fc.showOpenDialog(new JFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String name = "";
			if (!file.getAbsolutePath().contains("." + ext)) {
				name = file.getAbsolutePath() + "." + ext;
			}
			else {
				name = file.getAbsolutePath();
			}
			return loadNewEntity(name);
		}
		return null;
	}

/*
* Parsuje soubor .obj do entity.
*/
	public static Entity loadNewEntity(String fileName) {
		ArrayList<Vector3f> vertices = new ArrayList<>();
		ArrayList<Integer> indexes = new ArrayList<>();
		ArrayList<Color> colors = new ArrayList<>();

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = null;
			while ((line = br.readLine()) != null) {

				String[] arr = line.split(" ");
				if (arr[0].contains("#")) {	//komentare
					continue;
				}

				if (arr[0].contains("v")) {	//vertexy
					float x = Float.valueOf(arr[1]);
					float y = Float.valueOf(arr[2]);
					float z = Float.valueOf(arr[3]);
					Vector3f vertice = new Vector3f(x, y, z);
					vertices.add(vertice);
					colors.add(DEFAULT_COLOR);
				}

				if (arr[0].contains("f")) {	//indexy
					int x = Integer.valueOf(arr[1]) - 1;
					int y = Integer.valueOf(arr[2]) - 1;
					int z = Integer.valueOf(arr[3]) - 1;
					indexes.add(x);
					indexes.add(y);
					indexes.add(z);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//transformace v entitu
		Vector3f[] arrVertices = new Vector3f[vertices.size()];
		Color[] arrColors = new Color[colors.size()];
		int[] arrIndexes = new int[indexes.size()];

		for (int i = 0; i < arrVertices.length; i++) {
			arrVertices[i] = vertices.get(i);
			arrColors[i] = colors.get(i);
		}
		for (int i = 0; i < arrIndexes.length; i++) {
			arrIndexes[i] = indexes.get(i);
		}

		return new Entity(arrVertices, arrColors, arrIndexes);
	}
}
