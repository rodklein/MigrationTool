package com.jmigration;

import static com.jmigration.Migration.column;
import static com.jmigration.Migration.createTable;
import static com.jmigration.Migration.primaryKeyColumn;
import static java.sql.Types.NUMERIC;
import static java.sql.Types.VARCHAR;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.MigrationItem;
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
