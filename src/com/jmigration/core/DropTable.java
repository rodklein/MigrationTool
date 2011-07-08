package com.jmigration.core;

import com.jmigration.Migration;


public class DropTable extends Migration {
	
	private final String tableName;

	public DropTable(String tableName, MigrationConfiguration configuration) {
		this.tableName = tableName;
	}
	
	public void parse(StringBuilder sql) {
		sql.append("drop table ");
		sql.append(tableName);
	}
	
}
