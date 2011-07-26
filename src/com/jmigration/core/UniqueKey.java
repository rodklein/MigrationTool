package com.jmigration.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmigration.MigrationSession;


public class UniqueKey implements Constraint {
	
	private String uniqueKeyName;
	private List<String> columns = new ArrayList<String>();

	public UniqueKey(String ukName) {
		this.uniqueKeyName = ukName;
	}
	
	public UniqueKey() { }
	
	public UniqueKey column(String columnName) {
		this.columns.add(columnName);
		return this;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(session.getDialect().uniqueKey(uniqueKeyName)).append("unique (");
		for (Iterator<String> it = columns.iterator(); it.hasNext();) {
			sqlCommand.append(it.next());
			if (it.hasNext()) sqlCommand.append(", ");
		}
		sqlCommand.append(")");
	}

	@Override
	public String getName() {
		return uniqueKeyName;
	}
}
