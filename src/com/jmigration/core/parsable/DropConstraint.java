package com.jmigration.core.parsable;

import com.jmigration.MigrationSession;
import com.jmigration.core.Constraint;
import com.jmigration.core.Parsable;
import com.jmigration.core.SQLCommand;

public class DropConstraint implements Parsable {
	
	private final Constraint constraint;

	public DropConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sql) {
		sql.append(session.getDialect().dropConstraint(constraint));
	}

}
