package com.jmigration.database;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class DatabaseConfig {
	
	private String jdbcUrl;
	private String classDriver;
	private String username;
	private String password;
	
	public DatabaseConfig(String jdbcUrl, String classDriver, String username, String password) {
		this.jdbcUrl = jdbcUrl;
		this.classDriver = classDriver;
		this.username = username;
		this.password = password;
	}

	public DataSource createDataSource() throws Exception {
		Class.forName(classDriver);
		return new SingleConnectionDataSource(jdbcUrl, username, password, true);
	}

}
