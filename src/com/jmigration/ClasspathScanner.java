package com.jmigration;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ClasspathScanner {
	
	@SuppressWarnings("unchecked")
	public static List<MigrationUnit> scan(String prefix) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
			.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(prefix)))
			.setUrls(ClasspathHelper.getUrlsForPackagePrefix(prefix))
			.setScanners(new SubTypesScanner()));
		Set<Class<? extends MigrationUnit>> units = reflections.getSubTypesOf(MigrationUnit.class);
		List<MigrationUnit> migrations = new ArrayList<MigrationUnit>();
		for (Class<? extends MigrationUnit> migrationUnitClass : units) {
			if (migrationUnitClass.isAnonymousClass()) continue;
			try {
				if (migrationUnitClass.isMemberClass()) {
					Object parent = migrationUnitClass.getEnclosingClass().newInstance();
					Constructor<? extends MigrationUnit> constructor = migrationUnitClass.getDeclaredConstructor(migrationUnitClass.getEnclosingClass());
					constructor.setAccessible(true);
					migrations.add(constructor.newInstance(parent));
				} else {
					Constructor<? extends MigrationUnit> constructor = (Constructor<? extends MigrationUnit>) migrationUnitClass.getConstructors()[0];
					constructor.setAccessible(true);
					migrations.add(constructor.newInstance());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return migrations;
	}

}
