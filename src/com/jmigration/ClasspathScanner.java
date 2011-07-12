package com.jmigration;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class ClasspathScanner {
	
	public static List<MigrationUnit> scan(String prefix) {
		return scan(prefix, ClasspathHelper.forPackage(prefix));
	}

	@SuppressWarnings("unchecked")
	public static List<MigrationUnit> scan(String prefix, Collection<URL> url) {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
		.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(prefix)))
		.setUrls(url)
		.setScanners(new SubTypesScanner()));
		URLClassLoader loader = URLClassLoader.newInstance(url.toArray(new URL[]{}), ClasspathScanner.class.getClassLoader());
		if (ClasspathHelper.defaultClassLoaders.length == 2) {
			ClassLoader[] copyOf = Arrays.copyOf(ClasspathHelper.defaultClassLoaders, 3);
			ClasspathHelper.defaultClassLoaders = copyOf;
		}
		ClasspathHelper.defaultClassLoaders[2] = loader;
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
