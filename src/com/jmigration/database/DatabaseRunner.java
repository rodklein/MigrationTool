package com.jmigration.database;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;
import com.jmigration.base.MigrationTableScript;
import com.jmigration.core.MigrationConfiguration;

public class DatabaseRunner {
	
	private JdbcTemplate template;

	public DatabaseRunner(MigrationConfiguration config) {
		template = new JdbcTemplate(config.createDataSource());
	}
	
	public void run(MigrationUnit migrationUnit) {
		int count = 0;
		try {
			count = template.queryForInt("select count(*) from MIGRATIONS_VERSION where MIGRATION_NAME = ?", migrationUnit.name());
		} catch(Exception e) {
			createMigrationTable();
			count = 0;
		}
		if (count == 0) {
		}
	}
	
	private void createMigrationTable() {
		exec(new MigrationTableScript().createMigrationTable());
	}
	
	private void exec(Migration m) {
		StringBuilder sql = new StringBuilder();
		m.parse(sql);
		template.execute(sql.toString());
	}

}
