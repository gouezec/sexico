package com.airbus.sexico.main;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseFactory;
import com.airbus.sexico.db.sqldb.SQLDatabase;
import com.airbus.sexico.db.sqldb.SQLDatabaseContentHandler;
import com.airbus.sexico.importation.MEXICOConfigImporter;

public class Main {

//	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "//filer011/CrossTools/SIGMA/TESTS/CONFS MEXICO/ATA7X/SSDBConfig_A320ID_IVP.xml";
//	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "C:/Users/to28077/Desktop/big/SSDBConfig_A320ID_IVP.xml";
//	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "C:/Users/to28077/Desktop/big_zip/SSDBConfig_A320ID_IVP.xml";
	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "./data/MediumConf/Medium_SDB.xml";
//	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "C:/Users/to28077/Desktop/TinyConf/Tiny_SDB.xml";	
//	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "H:/big/SSDBConfig_A320ID_IVP.xml";
//	final static String DEFAULT_MEXICO_CFG_FILE_NAME = "H:/big_zip/SSDBConfig_A320ID_IVP.xml";

	public static void main(String[] args) throws Exception {
		long memStatAtStart = getFreeMemory();
		String mexicoCfgFilePath;

		// Prendre comme chemin de la conf MEXICO l'argument en ligne de la commande
		// ou sinon prendre le chemin de la conf par défaut
		if(args.length>1) {
			mexicoCfgFilePath = args[1];
		} else {
			mexicoCfgFilePath = DEFAULT_MEXICO_CFG_FILE_NAME;
		}
		
		// Tester le chargement de la base avec les MICD et les couplages de la conf MEXICO
		// createNullDatabase, createMapDatabase, createH2Database, createDerbyDatabase
		Database db = DatabaseFactory.getInstance().createH2Database("sexico3");
		File configFile = new File(mexicoCfgFilePath);
		MEXICOConfigImporter importer = new MEXICOConfigImporter(db, 2);
		importer.setTimeStamped(true);
		importer.importFile(configFile);

		// Tester la parallelisation des regles de validation unitaire des MICD
//		Database db = DatabaseFactory.getInstance().connectH2Database("sexico3");
//		RuleEngine ruleEngine = new RuleEngine(db, 2);
//		ruleEngine.checkRules();
		
		SQLDatabase sqldb = (SQLDatabase) db;

		long startTime, endTime, elapsedTime;
		int nb;

		startTime = System.currentTimeMillis();
		nb = ((SQLDatabaseContentHandler)sqldb.getContentHandler()).queryHomonimy1();
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		System.out.println("HOMONIMY 1 done in " + elapsedTime + "ms, (" + nb + " connexions found).");
		
		startTime = System.currentTimeMillis();
		((SQLDatabaseContentHandler)sqldb.getContentHandler()).queryHomonimy2();
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		System.out.println("HOMONIMY 2 done in " + elapsedTime + "ms, (" + nb + " connexions found).");
		
		// Afficher l'emprise mémoire à l'issue des tests
		long memStatAtEnd = getFreeMemory();
		System.out.println("Memory usage: "+NumberFormat.getInstance(Locale.FRENCH).format(memStatAtStart-memStatAtEnd)+" kb");
}
	
	
	
	public final static long getFreeMemory() {
		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		return (freeMemory + (maxMemory - allocatedMemory)) / 1024;
	}

}
