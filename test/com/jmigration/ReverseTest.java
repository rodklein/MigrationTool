package com.jmigration;

import static com.jmigration.Migration.*;
import static java.sql.Types.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jmigration.annotation.Reverse;
import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.MigrationItem;
import com.jmigration.core.SQLAppender;
import com.jmigration.database.DatabaseAccess;
import com.jmigration.dialect.BaseDialect;


public class ReverseTest {
	
	@Test
	public void testReverseMigration() {
		MigrationConfiguration config = mock(MigrationConfiguration.class);
		DatabaseAccess databaseAccess = mock(DatabaseAccess.class);
		when(config.getDialect()).thenReturn(new BaseDialect());
		when(config.createDatabaseAccess()).thenReturn(databaseAccess);
		when(databaseAccess.existsMigrationTable()).thenReturn(true);
		when(databaseAccess.wasExecuted(any(MigrationUnit.class), any(MigrationItem.class))).thenReturn(true);
		MigrationRunner runner = new MigrationRunner(config);
		Sample01 migrationUnit = new Sample01();
		runner.reverse(migrationUnit);
		verify(databaseAccess).rollbackMigration(eq(migrationUnit), any(MigrationItem.class));
	}
	
	@Test
	public void testReverseMigrationExecutingAReverse() {
		MigrationConfiguration config = mock(MigrationConfiguration.class);
		final Object[] steps = new Object[2];
		DatabaseAccess databaseAccess = new DatabaseAccess() {
			@Override
			public boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
				return true;
			}
			
			@Override
			public void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem) {
				if (steps[0] != null) fail();
				steps[0] = "X";
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
				assertEquals("drop table PERSON", sqlAppender.nextSql());
				if(steps[1] != null) fail();
				steps[1] = "X";
			}
		};
		when(config.getDialect()).thenReturn(new BaseDialect());
		when(config.createDatabaseAccess()).thenReturn(databaseAccess);
		MigrationRunner runner = new MigrationRunner(config);
		Sample02 migrationUnit = new Sample02();
		runner.reverse(migrationUnit);
		assertEquals("X", steps[0]);
		assertEquals("X", steps[1]);
	}

	@Test
	public void testReverseMigrationExecutingAReverseWithAnnotation() {
		MigrationConfiguration config = mock(MigrationConfiguration.class);
		final Object[] steps = new Object[2];
		DatabaseAccess databaseAccess = new DatabaseAccess() {
			@Override
			public boolean wasExecuted(MigrationUnit migrationUnit, MigrationItem migrationItem) {
				return true;
			}
			
			@Override
			public void rollbackMigration(MigrationUnit unit, MigrationItem migrationItem) {
				if (steps[0] != null) fail();
				steps[0] = "X";
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
				assertEquals("drop table PERSON", sqlAppender.nextSql());
				if(steps[1] != null) fail();
				steps[1] = "X";
			}
		};
		when(config.getDialect()).thenReturn(new BaseDialect());
		when(config.createDatabaseAccess()).thenReturn(databaseAccess);
		MigrationRunner runner = new MigrationRunner(config);
		Sample03 migrationUnit = new Sample03();
		runner.reverse(migrationUnit);
		assertEquals("X", steps[0]);
		assertEquals("X", steps[1]);
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
	
	class Sample02 implements MigrationUnit {

		@Override
		public String version() {
			return "002";
		}

		@Override
		public String name() {
			return "sample 02";
		}
		
		public Migration createTablePerson001() {
			return createTable("PERSON")
				.add(primaryKeyColumn("ID").as(NUMERIC).size(8))
				.add(column("NAME").as(VARCHAR).size(40));
		}
		
		public Migration reverseCreateTablePerson001() {
			return dropTable("PERSON");
		}
		
	}
	
	class Sample03 implements MigrationUnit {

		@Override
		public String version() {
			return "003";
		}

		@Override
		public String name() {
			return "sample 03";
		}
		
		public Migration createTablePerson001() {
			return createTable("PERSON")
				.add(primaryKeyColumn("ID").as(NUMERIC).size(8))
				.add(column("NAME").as(VARCHAR).size(40));
		}
		
		@Reverse("createTablePerson001")
		public Migration thisShouldReverse() {
			return dropTable("PERSON");
		}
		
	}
}
