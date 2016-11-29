package com.airbus.sexico.validation.mem;

import com.airbus.sexico.db.Port;

public interface RuleInterface {
	public boolean check(Port port);
	
	public String formatViolationMessage(Port port);
}
