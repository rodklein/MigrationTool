package com.jmigration;

import java.io.IOException;
import java.util.Properties;

import com.jmigration.core.MigrationConfiguration;

public class RunMigration {
	
	public static void main(String[] args) throws IOException {
		String currentVersion = null;
		if (args.length > 0) {
			currentVersion = args[0];
		}
		Properties p = new Properties();
		p.load(RunMigration.class.getResourceAsStream("/migration.properties"));
		MigrationConfiguration config = new MigrationConfiguration(p);
		
		config.createRunner().run(currentVersion);
	}

}
