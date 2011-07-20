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
		sql.append(column.getColumnName());
		if (column.hasType()) {
			sql.append(" ")
				.append(session.getDialect().alterType())
				.append(session.getDialect().getType(column.getType()));
			if (column.hasLength()) {
				sql.append("(").append(String.valueOf(column.getLenght()));
				if (column.hasPrecision()) {
					sql.append(",").append(String.valueOf(column.getPrecision()));
				}
				sql.append(")");
			}
		}
		if (column.isNotNull()) {
			sql.append(" not null ");
		} else if (column.isNullable()) {
			sql.append(" null ");
		}
	}

}
