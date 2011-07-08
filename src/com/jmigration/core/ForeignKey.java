package com.jmigration.core;

import com.jmigration.MigrationSession;


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
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(session.getDialect().foreignKey(foreignKeyName)).append("foreign key (").append(referenceColumnName).append(") references ").append(foreignTableName).append(" (").append(foreignColumnName).append(")"); 
	}

	@Override
	public String getName() {
		return foreignKeyName;
	}
}
