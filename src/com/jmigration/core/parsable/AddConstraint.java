package com.jmigration.core.parsable;

import com.jmigration.MigrationSession;
import com.jmigration.core.Constraint;
import com.jmigration.core.Parsable;
import com.jmigration.core.SQLCommand;

public class AddConstraint implements Parsable {
	
	private final Constraint constraint;

	public AddConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sql) {
		sql.append(session.getDialect().addConstraint());
		constraint.parse(session, sql);
	}

}
