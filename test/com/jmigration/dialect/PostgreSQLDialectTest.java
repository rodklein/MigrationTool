package com.jmigration.dialect;

import static com.jmigration.Migration.*;
import static java.sql.Types.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;

public class PostgreSQLDialectTest {
	
	@Test
	public void testCreateTableMigration() {
		Migration m = createTable("Pessoa")
		.add(column("Nome").as(VARCHAR).size(30))
		.add(column("DataNasc").as(TIMESTAMP))
		.add(column("Id").as(NUMERIC).size(6).notNull())
		.add(column("Peso").as(NUMERIC).size(5, 2));
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		m.parse(session);
		
		assertEquals("create table Pessoa (Nome VARCHAR(30), DataNasc TIMESTAMP, Id NUMERIC(6) not null, Peso NUMERIC(5,2))", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterTableMigration() {
		Migration m = alterTable("Pessoa")
		.add(column("Altura").as(NUMERIC).size(3));
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa add Altura NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnMigration() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3));
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura type NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnToNull() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3).nullable());
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura drop not null", session.getAppender().nextSql());
		assertEquals("alter table Pessoa alter column Altura type NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnToNotNull() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3).notNull());
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura set not null", session.getAppender().nextSql());
		assertEquals("alter table Pessoa alter column Altura type NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnToNotNullWithDefaultValue() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3).defaultValue("1").notNull());
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		m.parse(session);
		
		assertEquals("update Pessoa set Altura = 1 where Altura is null", session.getAppender().nextSql());
		assertEquals("alter table Pessoa alter column Altura set not null", session.getAppender().nextSql());
		assertEquals("alter table Pessoa alter column Altura type NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropColumn() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.drop(column("nome"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop column nome", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddForeignKey() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.add(foreignKey("fk_cidade").references("Cidade", "id_cidade")).parse(session);
		
		assertEquals("alter table Pessoa add constraint fk_cidade foreign key (id_cidade) references Cidade (id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddForeignKeyWithoutName() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.add(foreignKey().references("Cidade", "id_cidade")).parse(session);
		
		assertEquals("alter table Pessoa add foreign key (id_cidade) references Cidade (id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKey() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.add(primaryKey("pk_pessoa").column("id_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add constraint pk_pessoa primary key (id_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKeyWithoutName() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.add(primaryKey().column("id_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add primary key (id_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropTable() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		dropTable("Pessoa").parse(session);
		
		assertEquals("drop table Pessoa", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropConstraint() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.drop(foreignKey("fk_cidade"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop constraint fk_cidade", session.getAppender().nextSql());
		
		session = new MigrationSession(new PostgreSQLDialect());
		alterTable("Pessoa")
		.drop(primaryKey("pk_pessoa"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop constraint pk_pessoa", session.getAppender().nextSql());
		
	}

	@Test
	public void testCreateAutoIncrementPrimaryKey() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		createTable("Pessoa").add(primaryKeyColumn("cd_pessoa").as(NUMERIC).size(10).notNull().autoIncrement("seq_pessoa"))
		.parse(session);
		
		assertEquals("create sequence seq_pessoa increment by 1 start with 1 minvalue 1 maxvalue 999999999999999999", session.getAppender().nextSql());
		assertEquals("create table Pessoa (cd_pessoa NUMERIC(10) not null primary key)", session.getAppender().nextSql());
	}

}
