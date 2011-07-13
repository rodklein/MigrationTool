package com.jmigration;

import java.util.List;

import com.jmigration.base.MigrationTableScript;
import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.MigrationsExtractor;
import com.jmigration.database.DatabaseAccess;


public class MigrationRunner {

	private final MigrationConfiguration configuration;
	private final DatabaseAccess databaseAccess;

	public MigrationRunner(MigrationConfiguration configuration) {
		this.configuration = configuration;
		databaseAccess = new DatabaseAccess(configuration);
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

	private void executeUnit(MigrationUnit unit) {
		if (!wasExecuted(unit)) {
			execute(unit);
			markAsExecuted(unit);
		}
	}

	private void checkMigrationTable() {
		if (!databaseAccess.existsMigrationTable()) {
			execute(new MigrationTableScript());
		}
	}

	public boolean wasExecuted(MigrationUnit unit) {
		return databaseAccess.wasExecuted(unit);
	}

	public void markAsExecuted(MigrationUnit unit) {
		databaseAccess.markAsExecuted(unit);
	}

	public void execute(MigrationUnit unit) {
		List<Migration> list = MigrationsExtractor.extractAll(unit);
		MigrationSession session = new MigrationSession(configuration.getDialect());
		for (Migration migration : list) {
			migration.parse(session);
		}
		databaseAccess.exec(session.getAppender());
	}

}
