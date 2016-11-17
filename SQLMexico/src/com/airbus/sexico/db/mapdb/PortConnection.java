package com.airbus.sexico.db.mapdb;

public class PortConnection {

	public final String getModelName() {
		return modelName;
	}

	public final void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public final String getPortName() {
		return portName;
	}

	public final void setPortName(String portName) {
		this.portName = portName;
	}

	public final String getConnectionName() {
		return connectionName;
	}

	public final void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public PortConnection(String modelName, String portName, String connectionName) {
		super();
		this.modelName = modelName;
		this.portName = portName;
		this.connectionName = connectionName;
	}

	private String modelName;
	
	private String portName;
	
	private String connectionName;
	
	public PortConnection() {
	}

}
