package com.airbus.sexico.importation.coupling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.ZipInputStream;

import javax.activation.MimetypesFileTypeMap;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.importation.ImportException;
import com.airbus.sexico.importation.Importer;
import com.opencsv.CSVReader;

public class CouplingImporter extends Importer {

	protected String _modelName;

	public CouplingImporter(Database db, String modelName) {
		super(db);
		_modelName = modelName;
		this.mimeMap = new MimetypesFileTypeMap();
		this.mimeMap.addMimeTypes("application/zip zip");
	}
	
	private MimetypesFileTypeMap mimeMap;
	
	@Override
	protected long rawImportFile(File file) throws ImportException {	
		try {
			Reader freader;
			
			String mime = mimeMap.getContentType(file);
			if(mime.contentEquals("application/zip")) {
				ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
				zin.getNextEntry();
				freader = new InputStreamReader(zin);
			} else {
				freader = new FileReader(file);
			}
			CSVReader reader;
			reader = new CSVReader(freader, ';');

			
			String[] nextLine;
			// skip the first line
			reader.readNext();
			long nb = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine.length >= 2) {
					String portName = nextLine[0];
					String connectionName = nextLine[1];
					_db.insertConnection(_modelName, portName, connectionName);
					nb++;
				}
			}
			reader.close();
			return nb;
		} catch (IOException| DatabaseException e) {
			throw new ImportException(e);
		}
	}
}
