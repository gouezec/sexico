package com.airbus.sexico.db;

public class Port {

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}



	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getTypeName() {
		return typeName;
	}

	public final void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public final boolean isMicdConsistency() {
		return this.micdConsistency;
	}

	public final void setMicdConsistency(boolean micdConsistency) {
		this.micdConsistency = micdConsistency;
	}

	private String unit;
	
	public final String getUnit() {
		return unit;
	}

	public final void setUnit(String unit) {
		this.unit = unit;
	}

	private String modelName;

	private String portName;
	
	private String description;

	private String typeName;
	
	public final Direction getDirection() {
		return direction;
	}

	public final void setDirection(Direction direction) {
		this.direction = direction;
	}


	private Direction direction;

	private boolean micdConsistency;

	public Port() {
	}

	public Port(String modelName, String portName, String description, String typeName, String unit, Direction direction, boolean micdConsistency) {
		super();
		this.modelName = modelName;
		this.portName = portName;
		this.description = description;
		this.typeName = typeName;
		this.direction = direction;
		this.unit = unit;
		this.micdConsistency = micdConsistency;
	}

}
