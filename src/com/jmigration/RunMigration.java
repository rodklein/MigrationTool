package com.jmigration;

import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import com.jmigration.core.MigrationConfiguration;

public class RunMigration {
	
	public static void main(String[] args) throws Exception {
		String migrationClass = null;
		if (args.length > 0) {
			migrationClass = args[0];
		}
		Set<String> resources = new Reflections("", new ResourcesScanner()).getResources(Pattern.compile("migration.properties"));
		if (resources.isEmpty()) throw new RuntimeException("The file 'migration.properties' was not found in the classpath.");
		Properties p = new Properties();
		String next = resources.iterator().next();
		System.out.println(next);
		p.load(RunMigration.class.getResourceAsStream("/" + next));
		MigrationConfiguration config = new MigrationConfiguration(p);
		Class<?> migrationUnitClass = Class.forName(migrationClass);
		MigrationUnit migrationUnit = (MigrationUnit) migrationUnitClass.newInstance();
		config.createRunner().run(migrationUnit);
	}

}
