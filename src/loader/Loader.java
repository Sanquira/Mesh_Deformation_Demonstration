package loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import meshOperations.transformation.AbstractTransformation;

public class Loader {
	
	private static String ext = "xml";
	private static JAXBContext context;
	private static Marshaller m;
	private static Unmarshaller u;
	/*
	 * Obecne okno na otevírání souborů
	 */
	private static String showDialog(Boolean save){
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Soubory XML (*." + ext + ")", ext));
		int returnVal = 0;
		if(save){
			returnVal=fc.showSaveDialog(new JFrame());
		}
		else{
			returnVal=fc.showOpenDialog(new JFrame());
		}
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String name = "";
			if (!file.getAbsolutePath().contains("." + ext)) {
				name = file.getAbsolutePath() + "." + ext;
			}
			else {
				name = file.getAbsolutePath();
			}
			return name;
		}
		return null;
	}
	/*
	 * Načítací dialog
	 */
	public static ArrayList<AbstractTransformation> loadDialog() throws JAXBException{
		String name = showDialog(false);
		if( name != null ){
			return load(name);
		}
		return null;
	}
	/*
	 * Ukládací dialog
	 */
	public static void saveDialog(ArrayList<AbstractTransformation> list) throws FileNotFoundException, JAXBException{
		String name = showDialog(true);
		if( name != null ){
			save(name, list);
		}
	}
	/*
	 * Samotné načítání
	 */
	private static ArrayList<AbstractTransformation> load(String name) throws JAXBException {
		setup();
		AbstractTransformation[] array = u.unmarshal(new StreamSource(name), AbstractTransformation[].class).getValue();
		return new ArrayList<AbstractTransformation>(Arrays.asList(array));
	}
	/*
	 * Samotné ukládání
	 */
	private static void save(String name, ArrayList<AbstractTransformation> list) throws FileNotFoundException, JAXBException{
		setup();
		JAXBElement<AbstractTransformation[]> root = new JAXBElement<AbstractTransformation[]>(new QName("transformace"), AbstractTransformation[].class,AbstractTransformation.class,list.toArray(new AbstractTransformation[list.size()]));
		m.marshal(root, new FileOutputStream(new File(name)));
	}
	/*
	 * Nastavení proměných
	 */
	private static void setup() throws JAXBException{
		if(context == null){
			context = JAXBContext.newInstance(AbstractTransformation[].class);
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			u = context.createUnmarshaller();
		}
	}
}
