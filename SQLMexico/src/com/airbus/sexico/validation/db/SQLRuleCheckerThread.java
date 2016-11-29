package com.airbus.sexico.validation.db;

import com.airbus.sexico.db.Port;
import com.airbus.sexico.validation.mem.RuleInterface;
import com.airbus.sexico.validation.mem.RuleNoSpaceInUnit;

public class SQLRuleCheckerThread extends Thread {

	public SQLRuleCheckerThread(Port []  ports, int first, int last) {
		super();
		this.ports = ports;
		this.first = first;
		this.last = last;
	}


	@Override
	public void run() {
		super.run();

		RuleInterface rule = new RuleNoSpaceInUnit();
		for(int i=first; i<=last; i++) {
			rule.check(ports[i]);
		}
	}

	private Port[] ports;
	private int first;
	private int last;
}
