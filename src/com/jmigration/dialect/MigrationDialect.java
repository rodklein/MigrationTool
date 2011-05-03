package com.jmigration.dialect;


public interface MigrationDialect {

	public abstract String createTable(String tableName);
	public abstract String alterTable(String tableName);
	public abstract String addColumn();
	public abstract String getType(int type);
	public abstract String alterColumn();
	public abstract String dropColumn();
	public abstract String addConstraint();
	public abstract String dropConstraint();

}
