package com.jmigration.database;

import org.springframework.jdbc.core.JdbcTemplate;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;
import com.jmigration.base.MigrationTableScript;
import com.jmigration.core.MigrationConfiguration;

public class DatabaseAccess {
	
	private JdbcTemplate template;

	public DatabaseAccess(MigrationConfiguration config) {
		template = new JdbcTemplate(config.createDataSource());
	}
	
	public boolean wasExecuted(MigrationUnit migrationUnit) {
		int count = 0;
		try {
			count = template.queryForInt("select count(*) from MIGRATIONS_VERSION where MIGRATION_NAME = ?", migrationUnit.name());
		} catch(Exception e) {
			createMigrationTable();
			count = 0;
		}
		return count != 0;
	}
	
	private void createMigrationTable() {
		exec(new MigrationTableScript().createMigrationTable());
	}
	
	public void exec(Migration m) {
		StringBuilder sql = new StringBuilder();
		m.parse(sql);
		template.execute(sql.toString());
	}
	
	public void markAsExecuted(MigrationUnit migrationUnit) {
		template.update("insert into MIGRATIONS_VERSION (ID, MIGRATION_NAME) values(? , ?)", 1, migrationUnit.name());
	}

}
