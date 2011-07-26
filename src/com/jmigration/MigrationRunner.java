package com.jmigration;

import java.util.Iterator;
import java.util.List;

import com.jmigration.base.AlterMigrationTableScript;
import com.jmigration.base.MigrationTableScript;
import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.MigrationItem;
import com.jmigration.core.MigrationsExtractor;
import com.jmigration.database.DatabaseAccess;


public class MigrationRunner {

	private final MigrationConfiguration configuration;
	private final DatabaseAccess databaseAccess;

	public MigrationRunner(MigrationConfiguration configuration) {
		this.configuration = configuration;
		databaseAccess = configuration.createDatabaseAccess();
	}
	
	public void run(String currentVersion) {
		checkMigrationTable();
		for (MigrationUnit unit : configuration.getMigrations(currentVersion)) {
			executeUnit(unit);
		}
	}
	
	public void run(MigrationUnit migrationUnit) {
		checkMigrationTable();
		executeUnit(migrationUnit);
	}
	
	public void reverse(MigrationUnit migrationUnit) {
		checkMigrationTable();
		reverseUnit(migrationUnit);
	}

	private void executeUnit(MigrationUnit unit) {
		List<MigrationItem> list = MigrationsExtractor.extractAll(unit);
		for (MigrationItem migrationItem : list) {
			if (!wasExecuted(unit, migrationItem)) {
				migrate(migrationItem);
				markAsExecuted(unit, migrationItem);
			}
		}
	}
	
	private void reverseUnit(MigrationUnit unit) {
		List<MigrationItem> list = MigrationsExtractor.extractAll(unit);
		for (MigrationItem migrationItem : list) {
			if (wasExecuted(unit, migrationItem)) {
				reverse(migrationItem);
				delete(unit, migrationItem);
			}
		}
	}

	private void checkMigrationTable() {
		if (!databaseAccess.existsMigrationTable()) {
			List<MigrationItem> list = MigrationsExtractor.extractAll(new MigrationTableScript());
			migrate(list.get(0));
		}
		runInternalScripts();
	}
	
	private void runInternalScripts() {
		executeUnit(new AlterMigrationTableScript());
	}

	public void migrate(MigrationItem item) {
		execute(item.migrationIterator());
	}

	public void reverse(MigrationItem item) {
		execute(item.reverseIterator());		
	}
	
	private boolean wasExecuted(MigrationUnit unit, MigrationItem item) {
		return databaseAccess.wasExecuted(unit, item);
	}
	
	private void markAsExecuted(MigrationUnit unit, MigrationItem item) {
		databaseAccess.markAsExecuted(unit, item);
	}
	
	private void delete(MigrationUnit unit, MigrationItem migrationItem) {
		databaseAccess.rollbackMigration(unit, migrationItem);
	}
	
	private void execute(Iterator<Migration> it) {
		MigrationSession session = new MigrationSession(configuration.getDialect());
		for (; it.hasNext(); ) {
			it.next().parse(session);
		}
		databaseAccess.exec(session.getAppender());
	}
	

}
