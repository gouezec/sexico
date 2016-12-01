package com.airbus.sexico.importation.micd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipInputStream;

import javax.activation.MimetypesFileTypeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.DatabaseContentHandler;
import com.airbus.sexico.db.DatabaseException;
import com.airbus.sexico.db.Direction;
import com.airbus.sexico.db.Port;
import com.airbus.sexico.importation.ImportException;
import com.airbus.sexico.importation.Importer;

public class MICDImporter extends Importer {

	final static String FUN_IN = "FUN_IN";
	final static String FUN_OUT = "FUN_OUT";

	final static String DIRECTION_IN = "IN";
	final static String DIRECTION_OUT = "OUT";


	protected String modelName;

	public MICDImporter(Database db, String modelName) {
		super(db);
		this.modelName = modelName;
		this.mimeMap = new MimetypesFileTypeMap();
		this.mimeMap.addMimeTypes("application/zip zip");
	}

	private MimetypesFileTypeMap mimeMap;

	@Override
	protected long rawImportFile(File file) throws ImportException {
		try {
			InputStream in;
			String mime = mimeMap.getContentType(file);
			if(mime.contentEquals("application/zip")) {
				ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
				zin.getNextEntry();
				in = zin;
			} else {
				in = new FileInputStream(file);
			}

			long nb = 0;
			Workbook wb;
			Sheet st;

			wb = WorkbookFactory.create(in);
			in.close();
			st = wb.getSheet(FUN_IN);
			nb = importSheet(st, Direction.IN);
			st = wb.getSheet(FUN_OUT);
			nb += importSheet(st, Direction.OUT);
			wb.close();
			return nb;
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			throw new ImportException(e);
		}
	}

	protected long importSheet(Sheet st, Direction direction)  {
		try {
			long nb = 0;
			Iterator<Row> it = st.rowIterator();
			DatabaseContentHandler dbHandler = _db.getContentHandler();

			// skip first line
			if (it.hasNext()) {
				it.next();
			}


			while (it.hasNext()) {
				String typeName;
				String description;
				String portName;
				String unit;
				Cell cell;

				Row row = it.next();
				cell = row.getCell(0);
				portName = (cell != null) ? cell.getStringCellValue() : "";
				Port port = new Port();
				if ((!portName.startsWith("_")) && (!portName.startsWith("#")) && (!portName.equals(""))){
					cell = row.getCell(1);
					typeName = (cell != null) ? cell.getStringCellValue() : "";
					cell = row.getCell(2);
					unit = (cell != null) ? cell.getStringCellValue() : "";
					cell = row.getCell(3);
					description = (cell != null) ? cell.getStringCellValue() : "";
					boolean consistency = true;
					try {
						port.setModelName(modelName);
						port.setPortName(portName);
						port.setDescription(description);
						port.setDirection(direction);
						port.setMicdConsistency(consistency);
						port.setTypeName(typeName);
						port.setUnit(unit);
						dbHandler.insertPort(port);
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
		} catch(DatabaseException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
