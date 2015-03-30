package com.jmigration.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jmigration.MigrationUnit;
import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.MigrationItem;
import com.jmigration.core.SQLAppender;
import com.jmigration.core.SQLCommand;

public class DatabaseAccessImpl implements DatabaseAccess {
	
	private JdbcTemplate template;

	public DatabaseAccessImpl(MigrationConfiguration config) {
		template = new JdbcTemplate(config.getDataSource());
	}
	
	@Override
	public boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
		int count = template.queryForObject("select count(*) from MIGRATIONS_VERSION where MIGRATION_NAME = ? and (MIGRATION_ITEM = ? or MIGRATION_ITEM is null)", Integer.class, migrationUnit.name(), migrationItem.getName());
		return count != 0;
	}
	
	@Override
	public void exec(SQLAppender sqlAppender) {
		for (SQLCommand sql : sqlAppender) {
			template.execute(sql.toString());
		}
	}
	
	@Override
	public void markAsExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
		template.update("insert into MIGRATIONS_VERSION (ID, MIGRATION_NAME, MIGRATION_ITEM) values(? , ?, ?)", getID(), migrationUnit.name(), migrationItem.getName());
	}

	private String getID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	@Override
	public boolean existsMigrationTable() {
		return template.execute(new ConnectionCallback<Boolean>() {
			@Override
			public Boolean doInConnection(Connection conn) throws SQLException, DataAccessException {
				ResultSet resultSet = conn.getMetaData().getTables(null, null, "migrations_version", null);
				boolean found = resultSet.next();
				if (!found) {
					resultSet = conn.getMetaData().getTables(null, null, "MIGRATIONS_VERSION", null);
					found = resultSet.next();
				}
				System.out.println("Found table migrations_version: " + found);
				return found;
			}
		});
	}

	@Override
	public void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem) {
		template.update("delete from MIGRATIONS_VERSION where MIGRATION_NAME = ? and MIGRATION_ITEM = ?", unit.name(), migrationItem.getName());
	}

}
