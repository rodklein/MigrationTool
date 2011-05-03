package com.jmigration;

import static java.sql.Types.DATE;
import static com.jmigration.Migration.*;

import static java.sql.Types.NUMERIC;
import static java.sql.Types.VARCHAR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jmigration.core.ForeignKey;

public class MigrationTest {
	
	@Test
	public void testCreateTableMigration() {
		Migration m = createTable("Pessoa")
		.add(column("Nome").as(VARCHAR).size(30))
		.add(column("DataNasc").as(DATE))
		.add(column("Id").as(NUMERIC).size(6).notNull())
		.add(column("Peso").as(NUMERIC).size(5, 2));
		StringBuilder sql = new StringBuilder();
		m.parse(sql);
		
		assertEquals("create table Pessoa (Nome VARCHAR(30), DataNasc DATE, Id NUMERIC(6) not null , Peso NUMERIC(5,2))", sql.toString());
	}
	
	@Test
	public void testAlterTableMigration() {
		Migration m = alterTable("Pessoa")
		.add(column("Altura").as(NUMERIC).size(3));
		StringBuilder sql = new StringBuilder();
		m.parse(sql);
		
		assertEquals("alter table Pessoa add Altura NUMERIC(3)", sql.toString());
	}
	
	@Test
	public void testAlterColumnMigration() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3));
		StringBuilder sql = new StringBuilder();
		m.parse(sql);
		
		assertEquals("alter table Pessoa alter column Altura NUMERIC(3)", sql.toString());
	}
	
	@Test
	public void testAddForeignKey() {
		StringBuilder sql = new StringBuilder();
		alterTable("Pessoa")
		.add(foreignKey("fk_cidade").references("Cidade", "id_cidade")).parse(sql);
		
		assertEquals("alter table Pessoa add constraint fk_cidade foreign key (id_cidade) references Cidade (id_cidade)", sql.toString());
	}



}
