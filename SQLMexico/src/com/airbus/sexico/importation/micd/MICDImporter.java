package com.airbus.sexico.importation.micd;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.importation.ImportException;
import com.airbus.sexico.importation.Importer;

public class MICDImporter extends Importer {

	final static String FUN_IN = "FUN_IN";
	final static String FUN_OUT = "FUN_OUT";

	final static String DIRECTION_IN = "IN";
	final static String DIRECTION_OUT = "OUT";
	
	
	protected String _modelName;

	public MICDImporter(Database db, String modelName) {
		super(db);
		_modelName = modelName;
	}

	@Override
	protected long rawImportFile(File file) throws ImportException {
		try {
			long nb = 0;
			Workbook wb;
			Sheet st;
			
			wb = WorkbookFactory.create(file);
			st = wb.getSheet(FUN_IN);
			nb = importSheet(st, DIRECTION_IN);
			st = wb.getSheet(FUN_OUT);
			nb += importSheet(st, DIRECTION_OUT);

			wb.close();
			return nb;
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw new ImportException(e);
		}
	}

	protected long importSheet(Sheet st, String direction)  {
		long nb = 0;
		Iterator<Row> it = st.rowIterator();
		// skip first line
		if (it.hasNext()) {
			it.next();
		}
		while (it.hasNext()) {
			String typeName;
			String description;
			String portName;
			Cell cell;

			Row row = it.next();
			cell = row.getCell(0);
			portName = (cell != null) ? cell.getStringCellValue() : "";
			if ((!portName.startsWith("_")) && (!portName.startsWith("#")) && (!portName.equals(""))){
				cell = row.getCell(1);
				typeName = (cell != null) ? cell.getStringCellValue() : "";
				cell = row.getCell(2);
				description = (cell != null) ? cell.getStringCellValue() : "";
				boolean consistency = true;
				try {
					_db.insertPort(_modelName, portName, typeName, description, direction, consistency);
					nb++;
				} catch (DatabaseException e) {
					if(e.getReason() == DatabaseException.REASON_UNIQUE_ID_VIOLATION) {
						System.err.println("Error: More than one port with same port and model names.");
					}
					else {
						System.err.println("Fatal error: invalid database.");
						e.printStackTrace();
						break;
					}
				}
			}
		}
		return nb;
	}

}
