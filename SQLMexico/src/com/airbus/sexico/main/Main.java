package com.airbus.sexico.main;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseFactory;
import com.airbus.sexico.importation.MEXICOConfigImporter;

public class Main {

//	final static String MEXICO_CFG_FILE_NAME = "//filer011/CrossTools/SIGMA/TESTS/CONFS MEXICO/ATA7X/SSDBConfig_A320ID_IVP.xml";
	final static String MEXICO_CFG_FILE_NAME = "C:/Users/to28077/Desktop/big/SSDBConfig_A320ID_IVP.xml";
	
	public static void main(String[] args) throws Exception {
		long memStatAtStart = getFreeMemory();
		
		Database db = DatabaseFactory.getInstance().createH2Database("sexico3");
		File configFile = new File(MEXICO_CFG_FILE_NAME);
		MEXICOConfigImporter importer = new MEXICOConfigImporter(db);
		importer.setTimeStamped(true);
		importer.importFile(configFile);

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
