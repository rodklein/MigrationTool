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
import com.jmigration.core.SQLAppender;
import com.jmigration.core.SQLCommand;

public class DatabaseAccess {
	
	private JdbcTemplate template;

	public DatabaseAccess(MigrationConfiguration config) {
		template = new JdbcTemplate(config.getDataSource());
	}
	
	public boolean wasExecuted(MigrationUnit migrationUnit) {
		int count = template.queryForInt("select count(*) from MIGRATIONS_VERSION where MIGRATION_NAME = ?", migrationUnit.name());
		return count != 0;
	}
	
	public void exec(SQLAppender sqlAppender) {
		for (SQLCommand sql : sqlAppender) {
			template.execute(sql.toString());
		}
	}
	
	public void markAsExecuted(MigrationUnit migrationUnit) {
		template.update("insert into MIGRATIONS_VERSION (ID, MIGRATION_NAME) values(? , ?)", getID(), migrationUnit.name());
	}

	private String getID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public boolean existsMigrationTable() {
		return template.execute(new ConnectionCallback<Boolean>() {
			@Override
			public Boolean doInConnection(Connection conn) throws SQLException, DataAccessException {
				ResultSet resultSet = conn.getMetaData().getTables(null, null, "MIGRATIONS_VERSION", null);
				return resultSet.next();
			}
		});
	}

}
