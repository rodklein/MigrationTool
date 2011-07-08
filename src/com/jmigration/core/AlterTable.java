package com.jmigration.core;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;
import com.jmigration.core.parsable.AddColumn;
import com.jmigration.core.parsable.AddConstraint;
import com.jmigration.core.parsable.AlterColumn;
import com.jmigration.core.parsable.DropColumn;
import com.jmigration.core.parsable.DropConstraint;

public class AlterTable extends Migration {
	
	private final String tableName;
	private Parsable parsable;

	public AlterTable(String tableName) {
		this.tableName = tableName;
	}

	public AlterTable add(Column<?> column) {
		parsable = new AddColumn(column);
		return this;
	}
	
	public AlterTable add(Constraint constraint) {
		parsable = new AddConstraint(constraint);
		return this;
	}
	
	@Override
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append(session.getDialect().alterTable(tableName));
		parsable.parse(session, sql);
		session.appendSQL(sql);
	}

	public AlterTable alter(Column<?> column) {
		parsable = new AlterColumn(column);
		return this;
	}
	
	public AlterTable drop(Column<?> column) {
		parsable = new DropColumn(column);
		return this;
	}
	
	public AlterTable drop(Constraint constraint) {
		parsable = new DropConstraint(constraint);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parsable == null) ? 0 : parsable.hashCode());
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
		AlterTable other = (AlterTable) obj;
		if (parsable == null) {
			if (other.parsable != null)
				return false;
		} else if (!parsable.equals(other.parsable))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	
	
}
