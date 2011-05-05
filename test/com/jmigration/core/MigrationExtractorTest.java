package com.jmigration.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import migrations.Sample01;

import org.junit.Test;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;
import com.jmigration.annotation.Order;


public class MigrationExtractorTest {
	
	@Test
	public void testSimpleExtraction() {
		Migration m = MigrationsExtractor.extractAll(new Sample01()).get(0);
		assertEquals(new Sample01().createTablePessoa(), m);
	}
	
	@Test
	public void testMultiExtraction() {
		List<Migration> list = MigrationsExtractor.extractAll(new Sample02());
		assertEquals(new Sample02().m001(), list.get(0));
		assertEquals(new Sample02().m002(), list.get(1));
	}
	
	@Test
	public void testExtractionOfList() {
		List<Migration> list = MigrationsExtractor.extractAll(new Sample03());
		assertEquals(new Sample03().m022(), list);
	}
	
	@Test
	public void testExtractionSort() {
		List<Migration> list = MigrationsExtractor.extractAll(new Sample04());
		assertEquals(new Sample04().m001().get(0), list.get(0));
		assertEquals(new Sample04().mmm(), list.get(2));
		assertEquals(new Sample04().m003(), list.get(3));
	}

	class Sample02 implements MigrationUnit {

		@Override
		public String version() {
			return "001";
		}

		@Override
		public String name() {
			return "Sample02";
		}
		
		public Migration m001() {
			return Migration.createTable("AB");
		}

		public Migration m002() {
			return Migration.createTable("CD");
		}
	}
	
	class Sample03 implements MigrationUnit {

		@Override
		public String version() {
			return "001";
		}

		@Override
		public String name() {
			return "Sample03";
		}
		
		public List<Migration> m022() {
			List<Migration> migrations = new ArrayList<Migration>();
			migrations.add(Migration.createTable("AB"));
			migrations.add(Migration.createTable("CD"));
			return migrations;
		}

	}
	
	class Sample04 implements MigrationUnit {

		@Override
		public String version() {
			return "001";
		}

		@Override
		public String name() {
			return "Sample04";
		}
		
		public List<Migration> m001() {
			List<Migration> migrations = new ArrayList<Migration>();
			migrations.add(Migration.createTable("AB"));
			migrations.add(Migration.createTable("CD"));
			return migrations;
		}
		
		public Migration m003() {
			return Migration.createTable("CD");
		}

		@Order(2)
		public Migration mmm() {
			return Migration.createTable("EF");
		}
	}
}
