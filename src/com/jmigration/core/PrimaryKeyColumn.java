package com.jmigration.core;

import com.jmigration.MigrationSession;

public class PrimaryKeyColumn extends Column<PrimaryKeyColumn> {
	
	private String sequenceName;

	public PrimaryKeyColumn(String name) {
		super(name);
	}
	
	public PrimaryKeyColumn autoIncrement(String sequenceName) {
		this.sequenceName = sequenceName;
		return this;
	}
	
	public PrimaryKeyColumn autoIncrement() {
		sequenceName = "SEQ_" + columnName;
		return this;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		super.parse(session, sqlCommand);
		sqlCommand.append(session.getDialect().addPrimaryKeyClause(session, sequenceName));
	}
}
