package com.jmigration.core;

import com.jmigration.dialect.MigrationDialect;

public class Column {
	
	final String columnName;
	int type = -1;
	int lenght = -1;
	int precision = -1;
	boolean notNull;

	public Column(String name) {
		this.columnName = name;
		
	}
	
	public Column as(int type) {
		this.type = type;
		return this;
	}

	public Column notNull() {
		notNull = true;
		return this;
	}

	public Column size(int lenght) {
		this.lenght = lenght;
		return this;
	}

	public Column size(int lenght, int precision) {
		this.lenght = lenght;
		this.precision = precision;
		return this;
	}

	public String parse(MigrationDialect dialect) {
		String columnDefinition = columnName;
		if (type > -1) {
			columnDefinition += " " + dialect.getType(type);
		}
		if (lenght > -1) {
			columnDefinition += "(" + lenght;
			if (precision > 0) {
				columnDefinition += "," + precision;
			}
			columnDefinition += ")";
		}
		if (notNull) {
			columnDefinition += " not null ";
		}
		return columnDefinition;
	}

}
