package com.jmigration.core;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.jmigration.ClasspathScanner;
import com.jmigration.MigrationUnit;

public class MigrationConfigurationTest {
	
	@Test
	public void testMigrationConfigurationReadingClasspath() throws MalformedURLException, Exception {
		URL[] urls = new URL[] {new URL("file:///Users/rodrigoklein/Development/github/MigrationTool/lib/migrations.jar")};
		URLClassLoader loader = URLClassLoader.newInstance(urls, ClasspathScanner.class.getClassLoader());
		Class<?> loadClass = loader.loadClass("migrations.Sample02");
		System.out.println(loadClass);
		Properties props = new Properties();
		props.put("migration.dialect", "com.jmigration.dialect.BaseDialect");
		props.put("migration.package", "migrations");
		props.put("migration.url", "file:///Users/rodrigoklein/Development/github/MigrationTool/lib/migrations.jar");
		props.put("migration.jdbc.driver", "net.sourceforge.jtds.jdbc.Driver");
		props.put("migration.jdbc.url", "jdbc:jtds:sqlserver://10.1.1.21:1433/AntaraArlei");
		props.put("migration.jdbc.username", "sa");
		props.put("migration.jdbc.password", "syshaus");
		MigrationConfiguration conf = new MigrationConfiguration(props);
		List<MigrationUnit> migrations = conf.getMigrations(null);
		assertEquals(4, migrations.size());
	}

}
