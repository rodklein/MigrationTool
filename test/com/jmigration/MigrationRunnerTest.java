package com.jmigration;

import static com.jmigration.Migration.*;
import static java.sql.Types.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.MigrationItem;
import com.jmigration.core.SQLAppender;
import com.jmigration.database.DatabaseAccess;
import com.jmigration.dialect.BaseDialect;


public class MigrationRunnerTest {
	
	@Test
	public void testRunMigrationCreatingMigrationTable() throws SQLException {
		MigrationConfiguration config = mock(MigrationConfiguration.class);
		final List<SQLAppender> sqls = new ArrayList<SQLAppender>();
		DatabaseAccess databaseAccess = new DatabaseAccess() {
			@Override
			public boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
				return !"sample 01".equals(migrationUnit.name());
			}
			
			@Override
			public void markAsExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
			}
			
			@Override
			public boolean existsMigrationTable() {
				return false;
			}
			
			@Override
			public void exec(SQLAppender sqlAppender) {
				sqls.add(sqlAppender);
			}
			
			@Override
			public void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem) {
			}
		};
		when(config.getDialect()).thenReturn(new BaseDialect());
		when(config.createDatabaseAccess()).thenReturn(databaseAccess);
		MigrationRunner runner = new MigrationRunner(config);
		runner.run(new Sample01());
		
		assertEquals("create table MIGRATIONS_VERSION (ID VARCHAR(32) primary key, MIGRATION_NAME VARCHAR(300))", sqls.get(0).nextSql());
		assertEquals("create table PERSON (ID NUMERIC(8) primary key, NAME VARCHAR(40))", sqls.get(1).nextSql());
	}
	
	@Test
	public void testRunMigrationWhithoutCreatingMigrationTable() throws SQLException {
		MigrationConfiguration config = mock(MigrationConfiguration.class);
		final List<SQLAppender> sqls = new ArrayList<SQLAppender>();
		DatabaseAccess databaseAccess = new DatabaseAccess() {
			@Override
			public boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
				return !"sample 01".equals(migrationUnit.name());
			}
			
			@Override
			public void markAsExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
			}
			
			@Override
			public boolean existsMigrationTable() {
				return true;
			}
			
			@Override
			public void exec(SQLAppender sqlAppender) {
				sqls.add(sqlAppender);
			}
			
			@Override
			public void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem) {
			}
		};
		when(config.getDialect()).thenReturn(new BaseDialect());
		when(config.createDatabaseAccess()).thenReturn(databaseAccess);
		MigrationRunner runner = new MigrationRunner(config);
		runner.run(new Sample01());
		
		assertEquals("create table PERSON (ID NUMERIC(8) primary key, NAME VARCHAR(40))", sqls.get(0).nextSql());
	}
		
	class Sample01 implements MigrationUnit {

		@Override
		public String version() {
			return "001";
		}

		@Override
		public String name() {
			return "sample 01";
		}
		
		public Migration createTablePerson001() {
			return createTable("PERSON")
				.add(primaryKeyColumn("ID").as(NUMERIC).size(8))
				.add(column("NAME").as(VARCHAR).size(40));
		}
		
	}

}
