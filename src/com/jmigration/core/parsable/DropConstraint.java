package com.jmigration.core.parsable;

import com.jmigration.core.Constraint;
import com.jmigration.core.Parsable;
import com.jmigration.dialect.MigrationDialect;

public class DropConstraint implements Parsable {
	
	private final Constraint constraint;

	public DropConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public String parse(MigrationDialect dialect) {
		StringBuilder sql = new StringBuilder();
		sql.append(dialect.dropConstraint());
		sql.append(constraint.parse(dialect));
		return sql.toString();
	}

}
