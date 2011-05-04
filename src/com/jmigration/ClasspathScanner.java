package com.jmigration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class ClasspathScanner {
	
	public static List<MigrationUnit> scan(String prefix) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
		.setUrls(ClasspathHelper.getUrlsForPackagePrefix(prefix))
		.setScanners(new SubTypesScanner()));
		Set<Class<? extends MigrationUnit>> units = reflections.getSubTypesOf(MigrationUnit.class);
		List<MigrationUnit> migrations = new ArrayList<MigrationUnit>();
		for (Class<? extends MigrationUnit> migrationUnitClass : units) {
			try {
				migrations.add(migrationUnitClass.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return migrations;
	}

}
