package com.jmigration.core.parsable;

import com.jmigration.MigrationSession;
import com.jmigration.core.Column;
import com.jmigration.core.Parsable;
import com.jmigration.core.SQLCommand;

public class AlterColumn implements Parsable {
	
	private final Column<?> column;

	public AlterColumn(Column<?> column) {
		this.column = column;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sql) {
		sql.append(session.getDialect().alterColumn());
		column.parse(session, sql);
	}

}
