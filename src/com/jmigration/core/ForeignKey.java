package com.jmigration.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmigration.MigrationSession;


public class ForeignKey implements Constraint {
	
	private String foreignKeyName;
	private String foreignTableName;
	private List<String> referenceColumnsNames = new ArrayList<String>();
	private List<String> foreignColumnsNames = new ArrayList<String>();
	private boolean columnMethodCalled = false;

	public ForeignKey(String fkName) {
		this.foreignKeyName = fkName;
	}
	
	public ForeignKey() {
	}

	public ForeignKey column(String columnName) {
		if (!columnMethodCalled) {
			this.referenceColumnsNames.clear();
			columnMethodCalled = true;
		}
		this.referenceColumnsNames.add(columnName);
		return this;
		
	}
	
	public ForeignKey references(String tableName) {
		this.foreignTableName = tableName;
		return this;
	}
	
	public ForeignKey references(String tableName, String... columnsNames) {
		boolean referenceColumnsIsEmpty = referenceColumnsNames.size() == 0;
		this.foreignTableName = tableName;
		for (String columnsName : columnsNames) {
			this.foreignColumnsNames.add(columnsName);
			if (referenceColumnsIsEmpty) referenceColumnsNames.add(columnsName);
		}
		return this;
	}

	@Override
	public void parse(MigrationSession session, SQLCommand sqlCommand) {
		sqlCommand.append(session.getDialect().foreignKey(foreignKeyName)).append("foreign key (");
		for (Iterator<String> it = referenceColumnsNames.iterator(); it.hasNext();) {
			sqlCommand.append(it.next());
			if (it.hasNext()) sqlCommand.append(", ");
		}
		
		sqlCommand.append(") references ").append(foreignTableName).append(" (");
		for (Iterator<String> it = foreignColumnsNames.iterator(); it.hasNext();) {
			sqlCommand.append(it.next());
			if (it.hasNext()) sqlCommand.append(", ");
		}
		
		sqlCommand.append(")"); 
	}

	@Override
	public String getName() {
		return foreignKeyName;
	}
}
