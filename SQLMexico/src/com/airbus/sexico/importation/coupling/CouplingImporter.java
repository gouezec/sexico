package com.airbus.sexico.importation.coupling;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
	}

	@Override
	protected long rawImportFile(File file) throws ImportException {
		try {
			CSVReader reader;
			reader = new CSVReader(new FileReader(file), ';');

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
