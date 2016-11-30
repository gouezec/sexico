package com.airbus.sexico.validation.db;

import com.airbus.sexico.db.Database;
import com.airbus.sexico.db.Port;

public class SQLRuleNoSpaceInUnit {


	public void check(Database db) {
		for(long i=0; i<50000;i++) Math.sqrt(i^3);
	}

	public String formatViolationMessage(Port port) {
		return "Error: port "+port.getPortName()+" unit is incorrect";
	}

}
