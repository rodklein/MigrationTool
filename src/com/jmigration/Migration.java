package com.jmigration;

import com.jmigration.core.AlterTable;
import com.jmigration.core.Column;
import com.jmigration.core.CreateTable;
import com.jmigration.core.ForeignKey;
import com.jmigration.core.MigrationSetup;

public abstract class Migration {
	
	protected StringBuilder sqlBuilder = new StringBuilder();
	private static MigrationSetup dialect = MigrationSetup.getInstance();
	
	public static CreateTable createTable(String tableName) {
		return new CreateTable(tableName, dialect);
	}

	public abstract void parse(StringBuilder sql);

	public static AlterTable alterTable(String tableToAlter) {
		return new AlterTable(tableToAlter, dialect);
	}
	
	public static Column column(String columnName) {
		return new Column(columnName);
	}
	
	public static ForeignKey foreignKey(String fkName) {
		return new ForeignKey(fkName);
	}
	
}
