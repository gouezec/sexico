package com.airbus.sexico.importation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.derby.impl.sql.compile.DB2LengthOperatorNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;

public class MEXICOConfigImporter extends Importer {

	private int nbThreads;
	
	public MEXICOConfigImporter(Database db, int nbThreads) {
		super(db);
		this.nbThreads = nbThreads;
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

			long nbItems = 0;
			ImporterThread [] threads = new ImporterThread[nbThreads];
			int nbActors = actors.getLength() / nbThreads;
			for (int i=0; i<nbThreads-1; i++) {
				int first = i*nbActors;
 				int last = (i+1)*nbActors-1; 
 				threads[i] = new ImporterThread(_db, folder, actors, first, last, isTimeStamped());					
 				threads[i].start();
			}
			if (nbThreads > 0) {
				threads[nbThreads-1] = new ImporterThread(_db, folder, actors, (nbThreads-1)*nbActors ,actors.getLength()-1, isTimeStamped());
 				threads[nbThreads-1].start();
			}
			try {	
				for (int i=0; i<nbThreads; i++) {
					threads[i].join();
					nbItems += threads[i].getNbItems();
				}	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			_db.updateIndex();
			_db.commitBase();
			
			return nbItems;
		} catch (ParserConfigurationException | SAXException | IOException | DatabaseException  e) {
			throw new ImportException(e);
		}
	}

}
