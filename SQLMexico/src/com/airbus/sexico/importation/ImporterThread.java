package com.airbus.sexico.importation;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.importation.coupling.CouplingImporter;
import com.airbus.sexico.importation.micd.MICDImporter;

public class ImporterThread extends Thread {


	
	public ImporterThread(Database db, File folder, NodeList actors, int first, int last, boolean isTimeStamped) {
		super();
		this.db = db;
		this.folder = folder;
		this.actors = actors;
		this.first = first;
		this.last = last;
		this.isTimeStamped = isTimeStamped;
	}

	public final Database getDb() {
		return db;
	}

	public final void setDb(Database db) {
		this.db = db;
	}

	public final File getFolder() {
		return folder;
	}

	public final void setFolder(File folder) {
		this.folder = folder;
	}

	public final NodeList getActors() {
		return actors;
	}

	public final void setActors(NodeList actors) {
		this.actors = actors;
	}

	public final long getNbItems() {
		return nbItems;
	}

	private Database db;
	private File folder;
	private NodeList actors;
	private long nbItems;
	
	public final boolean isTimeStamped() {
		return isTimeStamped;
	}

	public final void setTimeStamped(boolean isTimeStamped) {
		this.isTimeStamped = isTimeStamped;
	}

	private boolean isTimeStamped;
	
	public final int getFirst() {
		return first;
	}

	public final void setFirst(int first) {
		this.first = first;
	}

	public final int getLast() {
		return last;
	}

	public final void setLast(int last) {
		this.last = last;
	}

	private int first;
	private int last;
	
	public ImporterThread() {

	}

	@Override
	public void run() {
		try {
			nbItems = 0;
			for (int i = first; i <= last; i++) {
				Element actor = (Element) actors.item(i);
				importActor(actor);
				nbItems++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void importActor(Element actor) {
		String actorName = actor.getAttribute("name");

		System.out.println("Importing actor " + actorName);

		NodeList micds = actor.getElementsByTagName("ICD");
		for (int i = 0; i < micds.getLength(); i++) {
			Element micd = (Element) micds.item(i);
			try {
				importMicd(micd, actorName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void importMicd(Element micd, String actorName) throws Exception {
		String fileName = micd.getAttribute("fileName");
		Importer importer = new MICDImporter(db, actorName);
		
		importer.setTimeStamped(isTimeStamped);
		importer.importFile(new File(folder, fileName));

		NodeList couplings = micd.getElementsByTagName("Coupling");
		for (int i = 0; i < couplings.getLength(); i++) {
			Element coupling = (Element) couplings.item(i);
			try {
				importCoupling(coupling, actorName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void importCoupling(Element coupling, String actorName) throws Exception {
		String fileName = coupling.getAttribute("fileName");
		Importer importer = new CouplingImporter(db, actorName);
		importer.setTimeStamped(isTimeStamped);
		importer.importFile(new File(folder, fileName));
	}
}
