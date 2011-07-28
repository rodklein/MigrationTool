package com.jmigration.core;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;

public class DropIndex extends Migration {

	private final String indexName;

	public DropIndex(String indexName) {
		this.indexName = indexName;
	}
	
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append("drop index ");
		sql.append(indexName);
		session.appendSQL(sql);
	}


}
