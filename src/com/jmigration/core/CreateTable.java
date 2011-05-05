package com.jmigration.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jmigration.Migration;
import com.jmigration.dialect.MigrationDialect;


public class CreateTable extends Migration {
	
	private final String tableName;
	private List<Column> columns = new ArrayList<Column>();
	private final MigrationConfiguration configuration;

	public CreateTable(String tableName, MigrationConfiguration configuration) {
		this.tableName = tableName;
		this.configuration = configuration;
	}
	
	public CreateTable add(Column column) {
		columns.add(column);
		return this;
	}
	
	public void parse(StringBuilder sql) {
		sql.append(getDialect().createTable(tableName));
		sql.append(" (");
		for (Iterator<Column> iterator = columns.iterator(); iterator.hasNext();) {
			Column column = iterator.next();
			sql.append(column.parse(getDialect()));
			if (iterator.hasNext()) sql.append(", "); 
		}
		sql.append(")");
	}
	
	private MigrationDialect getDialect() {
		return configuration.getDialect();
	}
	
}
