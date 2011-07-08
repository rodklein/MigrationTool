package com.jmigration.core;

import com.jmigration.Migration;
import com.jmigration.core.parsable.AddColumn;
import com.jmigration.core.parsable.AddConstraint;
import com.jmigration.core.parsable.AlterColumn;
import com.jmigration.core.parsable.DropColumn;
import com.jmigration.core.parsable.DropConstraint;
import com.jmigration.dialect.MigrationDialect;

public class AlterTable extends Migration {
	
	private final String tableName;
	private Parsable parsable;
	private final MigrationConfiguration configuration;

	public AlterTable(String tableName, MigrationConfiguration dialect) {
		this.tableName = tableName;
		this.configuration = dialect;
	}

	public AlterTable add(Column column) {
		parsable = new AddColumn(column);
		return this;
	}
	
	public AlterTable add(Constraint constraint) {
		parsable = new AddConstraint(constraint);
		return this;
	}
	
	@Override
	public void parse(StringBuilder sql) {
		sql.append(getDialect().alterTable(tableName));
		sql.append(parsable.parse(getDialect()));
	}

	private MigrationDialect getDialect() {
		return configuration.getDialect();
	}

	public AlterTable alter(Column column) {
		parsable = new AlterColumn(column);
		return this;
	}
	
	public AlterTable drop(Column column) {
		parsable = new DropColumn(column);
		return this;
	}
	
	public AlterTable drop(Constraint constraint) {
		parsable = new DropConstraint(constraint);
		return this;
	}
}
