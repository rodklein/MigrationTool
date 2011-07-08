package com.jmigration.core;

import com.jmigration.dialect.MigrationDialect;

public class PrimaryKey implements Constraint {
	
	private String primaryKeyName;
	private String columnName;

	public PrimaryKey(String pkName) {
		this.primaryKeyName = pkName;
	}
	
	public PrimaryKey() {
	}
	
	public PrimaryKey column(String columnName) {
		this.columnName = columnName;
		return this;
	}

	@Override
	public String parse(MigrationDialect dialect) {
		return dialect.primaryKey(primaryKeyName) + "primary key (" + columnName + ")";
	}

}
