package examen_1Parc;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Principal {
	private static String  DIR_BASE;
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		DIR_BASE = new File(".").getCanonicalPath();
		SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        
        EstanciasDB db;
        
		try {
			db = new EstanciasDB();
			Manejador manejador = new Manejador(db);
			saxParser.parse(DIR_BASE+"/recursosExternos/estancias.xml", manejador);
			System.out.println("Actualizacion finalizada");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

	}
	public static String getDirectorioBase() {
		return DIR_BASE;
	}
}
