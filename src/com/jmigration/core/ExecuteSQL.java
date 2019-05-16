package com.jmigration.core;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;

public class ExecuteSQL extends Migration{
	
	private final String sqlCommand;
	
	public ExecuteSQL(String sqlCommand) {
		super();
		this.sqlCommand = sqlCommand;
	}

	@Override
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append(sqlCommand);
		session.appendSQL(sql);
	}

}
