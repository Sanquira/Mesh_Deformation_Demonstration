import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.naming.spi.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.lwjgl.util.vector.Vector3f;

import loader.Loader;
import meshOperations.transformation.AbstractTransformation;
import meshOperations.transformation.TransformationDrawn;

public class MarshallTest {

	public static void main(String[] args) throws JAXBException, FileNotFoundException {
		Vector3f t = new Vector3f(1,1,1);
		TransformationDrawn trs = new TransformationDrawn("sexy",t,t,(float) 5);
		ArrayList<AbstractTransformation> list = new ArrayList<>();
		list.add(trs);
		Loader.saveDialog(list);
		ArrayList<AbstractTransformation> x = Loader.loadDialog();
		System.out.println(x.get(0));
//		JAXBContext jc = JAXBContext.newInstance(AbstractTransformation[].class);
//        JAXBElement<AbstractTransformation[]> root = new JAXBElement<AbstractTransformation[]>(new QName("testík"), AbstractTransformation[].class,AbstractTransformation.class,list.toArray(new AbstractTransformation[list.size()]));
//		Marshaller m = jc.createMarshaller();
//        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		Unmarshaller u = jc.createUnmarshaller();
//		
//        m.marshal(root, System.out);
////		m.marshal(root, new FileOutputStream(new File("/home/sprt/test.mas")));
//		JAXBElement<AbstractTransformation[]> o = u.unmarshal(new StreamSource("/home/sprt/test.mas"),AbstractTransformation[].class);
//		System.out.println(o.getName().getLocalPart());
	}

}
