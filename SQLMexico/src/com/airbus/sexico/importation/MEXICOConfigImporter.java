package com.airbus.sexico.importation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;

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
			ImporterThread [] threads = new ImporterThread[nbThreads];
			int nbActors = actors.getLength() / nbThreads;

			for (int i=0; i<nbThreads-1; i++) {
				int first = i*nbActors;
 				int last = (i+1)*nbActors; 
 				threads[i] = new ImporterThread(_db, folder, actors, first, last, isTimeStamped());					
 				threads[i].start();
			}
			if (nbThreads > 0) {
				threads[nbThreads-1] = new ImporterThread(_db, folder, actors, (nbThreads-1)*nbActors ,actors.getLength()-1, isTimeStamped());
 				threads[nbThreads-1].start();
			}
			try {	
				for (int n=0; n<nbThreads; n++) {
					threads[n].join();
					nb += threads[n].getNb();
				}	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			_db.commitBase();
			return nb;
		} catch (ParserConfigurationException | SAXException | IOException | DatabaseException  e) {
			throw new ImportException(e);
		}
	}

}
