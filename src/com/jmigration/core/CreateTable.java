package com.jmigration.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;


public class CreateTable extends Migration {
	
	private final String tableName;
	private List<Column<?>> columns = new ArrayList<Column<?>>();

	public CreateTable(String tableName) {
		this.tableName = tableName;
	}
	
	public CreateTable add(Column<?> column) {
		columns.add(column);
		return this;
	}
	
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append(session.getDialect().createTable(tableName));
		sql.append(" (");
		for (Iterator<Column<?>> iterator = columns.iterator(); iterator.hasNext();) {
			Column<?> column = iterator.next();
			column.parse(session, sql);
			if (iterator.hasNext()) sql.append(", "); 
		}
		sql.append(")");
		session.appendSQL(sql);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columns == null) ? 0 : columns.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateTable other = (CreateTable) obj;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if (!columns.equals(other.columns))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	
}
