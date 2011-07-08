package com.jmigration.core.parsable;

import com.jmigration.MigrationSession;
import com.jmigration.core.Column;
import com.jmigration.core.Parsable;
import com.jmigration.core.SQLCommand;

public class AddColumn implements Parsable {
	
	private final Column<?> column;

	public AddColumn(Column<?> column) {
		this.column = column;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(session.getDialect().addColumn());
		column.parse(session, sqlCommand);
	}

}
