package com.jmigration.core;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.jmigration.ClasspathScanner;
import com.jmigration.MigrationUnit;
import com.jmigration.database.DatabaseConfig;
import com.jmigration.dialect.BaseDialect;
import com.jmigration.dialect.MigrationDialect;

public class MigrationConfiguration {
	
	private static MigrationConfiguration instance;
	private MigrationDialect dialect = new BaseDialect();
	private List<MigrationUnit> migrations;
	private DatabaseConfig databaseConfig;

	public MigrationConfiguration(Properties props) {
		if (props.containsKey("migration.dialect")) {
			try {
				dialect = (MigrationDialect) Class.forName(props.getProperty("migration.dialect")).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (props.containsKey("migration.package")) {
			String prefix = props.getProperty("migration.package");
			migrations = ClasspathScanner.scan(prefix);
		}
		databaseConfig = new DatabaseConfig(props.getProperty("migration.jdbc.url"), props.getProperty("migration.jdbc.driver"), props.getProperty("migration.jdbc.username"), props.getProperty("migration.jdbc.password"));
	}
	
	public static MigrationConfiguration getInstance() {
		if (instance == null) {
			instance = new MigrationConfiguration(new Properties());
		}
		return instance;
	}

	public void setDialect(MigrationDialect dialect) {
		this.dialect = dialect;
	}

	public MigrationDialect getDialect() {
		return dialect;
	}

	public List<MigrationUnit> getMigrations() {
		return migrations;
	}

	public DataSource createDataSource() {
		try {
			return databaseConfig.createDataSource();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

}
