package examen_1Parc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Manejador extends DefaultHandler {
	private boolean insertando, enNombre, enIni, enFin;
	private String hab, hotel, nombre, fIni, fFin;
	private EstanciasDB db;

	static {
		try {
			FileHandler fh = new FileHandler(Principal.getDirectorioBase() + "/erroresDB.log");
			fh.setFormatter(new SimpleFormatter());
			Logger.getGlobal().addHandler(fh);
		} catch (IOException e) {
			Logger.getGlobal().warning("Error asociando file handle al LOG.\n" + e.getMessage());
		}
	}

	public Manejador(EstanciasDB db) {
		this.db = db;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("inserciones")) {
			insertando = true;
		}
		if (insertando) {
			if (qName.equalsIgnoreCase("estancia")) {
				hab = attributes.getValue("hab");
				hotel = attributes.getValue("hotel");
			}
			if (qName.equalsIgnoreCase("nombre")) {
				enNombre = true;
			}
			if (qName.equalsIgnoreCase("fechaInicio")) {
				enIni = true;
			}
			if (qName.equalsIgnoreCase("fechaFin")) {
				enFin = true;
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch, start, length).trim();
		if (s.length() != 0) {
			if (enNombre) {
				nombre = s;
			}
			if (enIni) {
				fIni = s;
			}
			if (enFin) {
				fFin = s;
			}
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("inserciones")) {
			insertando = false;
		}

		if (insertando) {
			if (qName.equalsIgnoreCase("nombre")) {
				enNombre = false;
			}
			if (qName.equalsIgnoreCase("fechaInicio")) {
				enIni = false;
			}
			if (qName.equalsIgnoreCase("fechaFin")) {
				enFin = false;
			}
			if (qName.equalsIgnoreCase("estancia")) {
				Estancia estancia = new Estancia(nombre, fIni, fFin, hab, hotel);
				try {
					db.insert(estancia);
				} catch (SQLException e) {
					Logger.getGlobal().severe("Error insertando: " + estancia.toString());
				}
			}
		}

	}
}
