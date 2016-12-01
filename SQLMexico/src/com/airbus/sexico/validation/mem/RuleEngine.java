package com.airbus.sexico.validation.mem;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Port;

public class RuleEngine {

	private Database db;
	private int nbThreads;

	public RuleEngine(Database db, int nbThreads) {
		this.db = db;
		this.nbThreads = nbThreads;
	}

	public RuleEngine(Database db) {
		this(db, 1);
	}
	
	public void checkRules() {
		try {

			RuleCheckerThread [] threads = new RuleCheckerThread[nbThreads];
			DatabaseContentHandler dbHandler = db.getContentHandler();
			Port [] ports = dbHandler.getAllPorts();

			long statTime = System.currentTimeMillis();
			int nbActors = ports.length / nbThreads;
			for (int i=0; i<nbThreads-1; i++) {
				int first = i*nbActors;
				int last = (i+1)*nbActors; 
				threads[i] = new RuleCheckerThread(ports, first, last);					
				threads[i].start();
			}
			if (nbThreads > 0) {
				threads[nbThreads-1] = new RuleCheckerThread(ports, (nbThreads-1)*nbActors ,dbHandler.getPortLength()-1);
				threads[nbThreads-1].start();
			}
			try {	
				for (int i=0; i<nbThreads; i++) {
					threads[i].join();
				}	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - statTime;
			System.out.println(ports.length+" ports checked in " + elapsedTime + "ms");
		} catch (DatabaseException  e) {
			e.printStackTrace();
		}
	}
}

