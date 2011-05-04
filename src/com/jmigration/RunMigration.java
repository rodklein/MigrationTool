package com.jmigration;

import java.io.IOException;
import java.util.Properties;

import com.jmigration.core.MigrationConfiguration;

public class RunMigration {
	
	public static void main(String[] args) throws IOException {
		Properties p = new Properties();
		p.load(RunMigration.class.getResourceAsStream("/migration.properties"));
		MigrationConfiguration config = new MigrationConfiguration(p);
		for (MigrationUnit migrationUnit : config.getMigrations()) {
			System.out.println(migrationUnit.version());
		}
	}

}
