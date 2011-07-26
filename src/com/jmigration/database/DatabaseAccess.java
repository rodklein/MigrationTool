package com.jmigration.database;

import com.jmigration.MigrationUnit;
import com.jmigration.core.MigrationItem;
import com.jmigration.core.SQLAppender;

public interface DatabaseAccess {

	public abstract boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem);
	public abstract void exec(SQLAppender sqlAppender);
	public abstract void markAsExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem);
	public abstract boolean existsMigrationTable();
	public abstract void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem);

}