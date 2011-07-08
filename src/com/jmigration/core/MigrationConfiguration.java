package com.jmigration.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jmigration.ClasspathScanner;
import com.jmigration.MigrationRunner;
import com.jmigration.MigrationUnit;
import com.jmigration.base.BasicComparator;
import com.jmigration.database.DatabaseConfig;
import com.jmigration.dialect.BaseDialect;
import com.jmigration.dialect.MigrationDialect;

public class MigrationConfiguration {
	
	private MigrationDialect dialect = new BaseDialect();
	private List<MigrationUnit> migrations;
	private DatabaseConfig databaseConfig;
	private Comparator<MigrationUnit> versionComparator;

	@SuppressWarnings("unchecked")
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
			versionComparator = new BasicComparator();
			if (props.contains("migration.comparator")) {
				try {
					versionComparator = (Comparator<MigrationUnit>) Class.forName(props.getProperty("migration.comparator")).newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			Collections.sort(migrations, versionComparator);
		}
		databaseConfig = new DatabaseConfig(props.getProperty("migration.jdbc.url"), props.getProperty("migration.jdbc.driver"), props.getProperty("migration.jdbc.username"), props.getProperty("migration.jdbc.password"));
	}

	public void setDialect(MigrationDialect dialect) {
		this.dialect = dialect;
	}

	public MigrationDialect getDialect() {
		return dialect;
	}

	public List<MigrationUnit> getMigrations(final String currentVersion) {
		if (currentVersion == null) {
			return migrations;
		}
		return (List<MigrationUnit>) new ArrayList<MigrationUnit>(Collections2.filter(migrations, new Predicate<MigrationUnit>() {
			@Override
			public boolean apply(MigrationUnit unit) {
				return versionComparator.compare(unit, new MigrationUnit() {
					
					@Override
					public String version() {
						return currentVersion;
					}
					
					@Override
					public String name() {
						return null;
					}
				}) <= 0;
			}
		}));
	}

	public DataSource createDataSource() {
		try {
			return databaseConfig.createDataSource();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MigrationRunner createRunner() {
		return new MigrationRunner(this);
	}
	

}
