package com.jmigration.core.parsable;

import com.jmigration.core.Constraint;
import com.jmigration.core.Parsable;
import com.jmigration.dialect.MigrationDialect;

public class AddConstraint implements Parsable {
	
	private final Constraint constraint;

	public AddConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public String parse(MigrationDialect dialect) {
		StringBuilder sql = new StringBuilder();
		sql.append(dialect.addConstraint());
		sql.append(constraint.parse(dialect));
		return sql.toString();
	}

}
