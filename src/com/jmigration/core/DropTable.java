package com.jmigration.core;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;


public class DropTable extends Migration {
	
	private final String tableName;

	public DropTable(String tableName) {
		this.tableName = tableName;
	}
	
	public void parse(MigrationSession session) {
		SQLCommand sql = new SQLCommand();
		sql.append("drop table ");
		sql.append(tableName);
		session.appendSQL(sql);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		DropTable other = (DropTable) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	
}
