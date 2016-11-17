package com.airbus.sexico.importation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.importation.coupling.CouplingImporter;
import com.airbus.sexico.importation.micd.MICDImporter;

public class MEXICOConfigImporter extends Importer {

	public MEXICOConfigImporter(Database db) {
		super(db);
	}

	@Override
	protected long rawImportFile(File file) throws ImportException {
		try {
			File folder = file.getParentFile();

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(file);

			NodeList actors = document.getElementsByTagName("Actor");

			long nb = 0;

			int nbThreads = 1;
			Thread [] threads = new Thread[nbThreads];
			int nbActors = actors.getLength() / nbThreads;
			boolean even = ((actors.getLength() & 1) == 0);
			for (int n=0; n<nbThreads; n++) {
				ImporterThread im = new ImporterThread();				
				im.setDb(_db);
				im.setFolder(folder);
				im.setActors(actors);
				im.setFirst(n*nbActors);
				if((n == (nbThreads - 1)) && (even) ) {
					im.setLast((n+1)*nbActors-1);
				} else {
					im.setLast((n+1)*nbActors);					
				}
				im.start();
				threads[n] = im;
			}

			try {	
				for (int n=0; n<nbThreads; n++) {
					threads[n].join();
				}	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			_db.commitBase();
			return nb;
		} catch (ParserConfigurationException | SAXException | IOException | DatabaseException  e) {
			throw new ImportException(e);
		}
	}

	protected void importActor(Element actor, File folder) {
		String actorName = actor.getAttribute("name");

		System.out.println("Importing actor " + actorName);

		NodeList micds = actor.getElementsByTagName("ICD");
		for (int i = 0; i < micds.getLength(); i++) {
			Element micd = (Element) micds.item(i);
			try {
				importMicd(micd, actorName, folder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void importMicd(Element micd, String actorName, File folder) throws Exception {
		String fileName = micd.getAttribute("fileName");

		Importer impoter = new MICDImporter(_db, actorName);
		impoter.setTimeStamped(this.isTimeStamped());
		impoter.importFile(new File(folder, fileName));

		NodeList couplings = micd.getElementsByTagName("Coupling");
		for (int i = 0; i < couplings.getLength(); i++) {
			Element coupling = (Element) couplings.item(i);
			try {
				importCoupling(coupling, actorName, folder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void importCoupling(Element coupling, String actorName, File folder) throws Exception {
		String fileName = coupling.getAttribute("fileName");
		Importer importer = new CouplingImporter(_db, actorName);
		importer.setTimeStamped(this.isTimeStamped());
		importer.importFile(new File(folder, fileName));
	}
}
