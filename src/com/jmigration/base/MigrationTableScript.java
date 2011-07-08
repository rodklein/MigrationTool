package com.jmigration.base;
import static com.jmigration.Migration.*;

import static java.sql.Types.*;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;

public class MigrationTableScript implements MigrationUnit {

	@Override
	public String version() {
		return "000";
	}
	
	public Migration createMigrationTable() {
		return createTable("MIGRATIONS_VERSION")
			.add(primaryKeyColumn("ID").as(VARCHAR).size(32))
			.add(column("MIGRATION_NAME").as(VARCHAR).size(300));
	}

	@Override
	public String name() {
		return "migration_table";
	}

}
