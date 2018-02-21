package com.jmigration.database;

import com.jmigration.MigrationUnit;
import com.jmigration.core.MigrationItem;
import com.jmigration.core.SQLAppender;
import com.jmigration.core.SQLCommand;

public class DatabaseAccessLogAll implements DatabaseAccess {
	

	@Override
	public boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
		return false;
	}
	
	@Override
	public void exec(SQLAppender sqlAppender) {
		for (SQLCommand sql : sqlAppender) {
			System.out.println(sql.toString());
		}
	}
	
	@Override
	public void markAsExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {}


	@Override
	public boolean existsMigrationTable() {
		return true;
	}

	@Override
	public void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem) {}

}
