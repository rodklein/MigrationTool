package com.jmigration.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;


public class CreateIndex extends Migration {
	
	private final String indexName;
	private List<Column<?>> columns = new ArrayList<Column<?>>();
	private String tableName;

	public CreateIndex(String indexName) {
		this.indexName = indexName;
	}
	
	public CreateIndex add(Column<?> column) {
		columns.add(column);
		return this;
	}
	
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append(session.getDialect().createIndex(indexName));
		sql.append(" on ").append(tableName).append("(");
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
		result = prime * result + ((indexName == null) ? 0 : indexName.hashCode());
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
		CreateIndex other = (CreateIndex) obj;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if (!columns.equals(other.columns))
			return false;
		if (indexName == null) {
			if (other.indexName != null)
				return false;
		} else if (!indexName.equals(other.indexName))
			return false;
		return true;
	}

	public CreateIndex on(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
}
