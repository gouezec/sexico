package com.airbus.sexico.validation;

import java.io.File;

import com.airbus.sexico.db.Port;
import com.airbus.sexico.importation.ImportException;
import com.airbus.sexico.validation.rules.NoSpaceInUnitRule;
import com.airbus.sexico.validation.rules.RuleInterface;

public class RuleCheckerThread extends Thread {

	public RuleCheckerThread(Port []  ports, int first, int last) {
		super();
		this.ports = ports;
		this.first = first;
		this.last = last;
	}


	@Override
	public void run() {
		super.run();

		RuleInterface rule = new NoSpaceInUnitRule();
		for(int i=first; i<=last; i++) {
			rule.check(ports[i]);
		}
	}

	private Port[] ports;
	private int first;
	private int last;
}
