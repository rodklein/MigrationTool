package com.jmigration.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.jmigration.database.DatabaseConfig;
import com.jmigration.dialect.BaseDialect;
import com.jmigration.dialect.MigrationDialect;

public class MigrationConfiguration {
	
	private MigrationDialect dialect = new BaseDialect();
	private List<MigrationUnit> migrations;
	private DatabaseConfig databaseConfig;
	private Comparator<MigrationUnit> versionComparator;
	private DataSource dataSource;
	private String dialectClass = "com.jmigration.dialect.BaseDialect";
	private String versionComparatorClass = "com.jmigration.base.BasicComparator";
	private String packagePrefix = null;
	private String migrationURL;

	public MigrationConfiguration() {}
			
	public MigrationConfiguration(Properties props) {
		if (props.containsKey("migration.dialect")) {
			setDialectClass(props.getProperty("migration.dialect"));
		}
		if (props.containsKey("migration.package")) {
			setPackagePrefix(props.getProperty("migration.package"));
		}
		if (props.contains("migration.comparator")) {
			setVersionComparatorClass(props.getProperty("migration.comparator"));
		}
		if (props.containsKey("migration.url")) {
			setMigrationURL(props.getProperty("migration.url"));
		}
		databaseConfig = new DatabaseConfig(props.getProperty("migration.jdbc.url"), props.getProperty("migration.jdbc.driver"), props.getProperty("migration.jdbc.username"), props.getProperty("migration.jdbc.password"));
		setDataSource(createDataSource());
		init();
	}

	public MigrationDialect getDialect() {
		return dialect;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public DataSource getDataSource() {
		return dataSource;
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

	private DataSource createDataSource() {
		try {
			return databaseConfig.createDataSource();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MigrationRunner createRunner() {
		return new MigrationRunner(this);
	}

	@SuppressWarnings("unchecked")
	public void init() {
		try {
			dialect = (MigrationDialect) Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (migrationURL != null) {
			try {
				migrations = ClasspathScanner.scan(packagePrefix, Arrays.asList(new URL(migrationURL)));
			} catch (MalformedURLException e) {
				migrations = ClasspathScanner.scan(packagePrefix);
			}
		} else if (packagePrefix != null){
			migrations = ClasspathScanner.scan(packagePrefix);
		}
		try {
			versionComparator = (Comparator<MigrationUnit>) Class.forName(versionComparatorClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		Collections.sort(migrations, versionComparator);
	}

	public void setMigrationURL(String migrationURL) {
		this.migrationURL = migrationURL;
	}

	public void setVersionComparatorClass(String versionComparatorClass) {
		this.versionComparatorClass = versionComparatorClass;
	}

	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}

	public void setDialectClass(String dialectClass) {
		this.dialectClass = dialectClass;
	}

}
