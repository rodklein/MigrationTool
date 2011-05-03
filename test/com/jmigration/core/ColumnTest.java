package com.jmigration.core;

import static org.junit.Assert.*;

import java.sql.Types;

import org.junit.Test;

import com.jmigration.core.Column;


public class ColumnTest {
	
	@Test
	public void testColumnDefinition() {
		Column c = new Column("nm_pessoa").as(Types.VARCHAR).notNull().size(30);
		assertEquals("nm_pessoa", c.columnName);
		assertEquals(Types.VARCHAR, c.type);
		assertTrue(c.notNull);
		assertEquals(30, c.lenght);
	}
	
	@Test
	public void testColumnDefinition2() {
		Column c = new Column("id_pessoa").as(Types.DECIMAL).notNull().size(10, 5);
		assertEquals("id_pessoa", c.columnName);
		assertEquals(Types.DECIMAL, c.type);
		assertTrue(c.notNull);
		assertEquals(10, c.lenght);
		assertEquals(5, c.precision);
	}

}
