package com.jmigration.core;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;

public class DropIndex extends Migration {

	private final String indexName;
	private String table;

	public DropIndex(String indexName) {
		this.indexName = indexName;
	}
	
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append("drop index ");
		sql.append(session.getDialect().indexName(table, indexName));
		session.appendSQL(sql);
	}

	public Migration onTable(String table) {
		this.table = table;
		return this;
	}


}
