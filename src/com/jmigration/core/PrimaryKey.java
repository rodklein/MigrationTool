package com.jmigration.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmigration.MigrationSession;


public class PrimaryKey implements Constraint {
	
	private String primaryKeyName;
	private List<String> columns = new ArrayList<String>();

	public PrimaryKey(String pkName) {
		this.primaryKeyName = pkName;
	}
	
	public PrimaryKey() { }
	
	public PrimaryKey column(String columnName) {
		this.columns.add(columnName);
		return this;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(session.getDialect().primaryKey(primaryKeyName)).append("primary key (");
		for (Iterator<String> it = columns.iterator(); it.hasNext();) {
			sqlCommand.append(it.next());
			if (it.hasNext()) sqlCommand.append(", ");
		}
		sqlCommand.append(")");
	}

	@Override
	public String getName() {
		return primaryKeyName;
	}
}
