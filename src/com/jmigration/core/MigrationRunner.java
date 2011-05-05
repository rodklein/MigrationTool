package com.jmigration.core;

import java.util.List;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;
import com.jmigration.database.DatabaseAccess;

public class MigrationRunner {

	private final DatabaseAccess db;

	public MigrationRunner(DatabaseAccess db) {
		this.db = db;
	}
	
	public void run(MigrationUnit unit) {
		if (!db.wasExecuted(unit)) {
			List<Migration> list = MigrationsExtractor.extractAll(unit);
			for (Migration migration : list) {
				db.exec(migration);
			}
		}
	}

}
