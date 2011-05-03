package com.jmigration.core.parsable;

import com.jmigration.core.Column;
import com.jmigration.core.Parsable;
import com.jmigration.dialect.MigrationDialect;

public class AlterColumn implements Parsable {
	
	private final Column column;

	public AlterColumn(Column column) {
		this.column = column;
	}

	@Override
	public String parse(MigrationDialect dialect) {
		StringBuilder sql = new StringBuilder();
		sql.append(dialect.alterColumn());
		sql.append(column.parse(dialect));
		return sql.toString();
	}

}
