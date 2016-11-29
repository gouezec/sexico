package com.airbus.sexico.validation.mem;

import com.airbus.sexico.db.Port;

public class RuleNoSpaceInUnit implements RuleInterface {

	@Override
	public boolean check(Port port) {
		for(long i=0; i<50000;i++) Math.sqrt(i^3);
		return port.getUnit().contains(" ");
	}

	@Override
	public String formatViolationMessage(Port port) {
		return "Error: port "+port.getPortName()+" unit is incorrect";
	}

}
