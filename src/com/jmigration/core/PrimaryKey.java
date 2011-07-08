package com.jmigration.core;

import com.jmigration.MigrationSession;


public class PrimaryKey implements Constraint {
	
	private String primaryKeyName;
	private String columnName;

	public PrimaryKey(String pkName) {
		this.primaryKeyName = pkName;
	}
	
	public PrimaryKey() { }
	
	public PrimaryKey column(String columnName) {
		this.columnName = columnName;
		return this;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(session.getDialect().primaryKey(primaryKeyName)).append("primary key (").append(columnName).append(")");
	}

	@Override
	public String getName() {
		return primaryKeyName;
	}
}
