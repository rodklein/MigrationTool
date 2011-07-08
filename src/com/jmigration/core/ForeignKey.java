package com.jmigration.core;

import com.jmigration.dialect.MigrationDialect;

public class ForeignKey implements Constraint {
	
	private String foreignKeyName;
	private String foreignTableName;
	private String referenceColumnName;
	private String foreignColumnName;

	public ForeignKey(String fkName) {
		this.foreignKeyName = fkName;
	}
	
	public ForeignKey() {
	}

	public ForeignKey column(String columnName) {
		this.referenceColumnName = columnName;
		return this;
		
	}
	
	public ForeignKey references(String tableName) {
		this.foreignTableName = tableName;
		return this;
	}
	
	public ForeignKey references(String tableName, String columnName) {
		this.foreignTableName = tableName;
		this.foreignColumnName = columnName;
		if (referenceColumnName == null) referenceColumnName = columnName;
		return this;
	}

	@Override
	public String parse(MigrationDialect dialect) {
		return dialect.foreignKey(foreignKeyName) + "foreign key (" + referenceColumnName+ ") references " + foreignTableName + " (" + foreignColumnName + ")"; 
	}

}
