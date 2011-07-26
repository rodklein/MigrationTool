package com.jmigration.base;

import static com.jmigration.Migration.alterTable;
import static com.jmigration.Migration.uniqueKey;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;

public class AlterMigrationTableScript implements MigrationUnit {

	@Override
	public String version() {
		return "0";
	}

	@Override
	public String name() {
		return getClass().getName();
	}
	
	public Migration alterMigrationTable002() {
		return alterTable("MIGRATIONS_VERSION")
			.add(uniqueKey("MIVE_UK")
					.column("MIGRATION_NAME")
					.column("MIGRATION_ITEM"));
	}

}
