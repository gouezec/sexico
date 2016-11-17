package com.airbus.sexico.db;

public interface Database {

	void insertPort(String modelName, String portName, String typeName, String description, Direction direction, boolean micdConsistency)
			throws DatabaseException;

	void insertConnection(String modelName, String portName, String connectionName) throws DatabaseException;

	void commitBase() throws DatabaseException;

	void dropBase() throws DatabaseException;
}