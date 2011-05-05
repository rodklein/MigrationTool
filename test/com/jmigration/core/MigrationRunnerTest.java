package com.jmigration.core;

import static org.mockito.Mockito.*;
import migrations.Sample01;

import org.junit.Test;

import com.jmigration.database.DatabaseAccess;


public class MigrationRunnerTest {
	
	@Test
	public void testRunningANewMigration() {
		DatabaseAccess db = mock(DatabaseAccess.class);
		MigrationRunner runner = new MigrationRunner(db);
		Sample01 unit = new Sample01();
		when(db.wasExecuted(unit)).thenReturn(false);
		
		runner.run(unit);
		
		verify(db).exec(unit.createTablePessoa());
	}
	
	@Test
	public void testRunningAnOldMigration() {
		DatabaseAccess db = mock(DatabaseAccess.class);
		MigrationRunner runner = new MigrationRunner(db);
		Sample01 unit = new Sample01();
		when(db.wasExecuted(unit)).thenReturn(true);
		
		runner.run(unit);
		
		verify(db,never()).exec(unit.createTablePessoa());
	}

}
