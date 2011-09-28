package com.jmigration;

import static com.jmigration.Migration.*;
import static java.sql.Types.TIMESTAMP;
import static java.sql.Types.NUMERIC;
import static java.sql.Types.VARCHAR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MigrationTest {
	
	@Test
	public void testCreateTableMigration() {
		Migration m = createTable("Pessoa")
		.add(column("Nome").as(VARCHAR).size(30))
		.add(column("DataNasc").as(TIMESTAMP))
		.add(column("Id").as(NUMERIC).size(6).notNull())
		.add(column("Peso").as(NUMERIC).size(5, 2).defaultValue("78.5"));
		MigrationSession session = new MigrationSession();
		m.parse(session);
		
		assertEquals("create table Pessoa (Nome VARCHAR(30), DataNasc TIMESTAMP, Id NUMERIC(6) not null, Peso NUMERIC(5,2) default 78.5)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterTableMigration() {
		Migration m = alterTable("Pessoa")
		.add(column("Altura").as(NUMERIC).size(3));
		MigrationSession session = new MigrationSession();
		m.parse(session);
		
		assertEquals("alter table Pessoa add Altura NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterTableAddColumnWithDefault() {
		Migration m = alterTable("Pessoa")
		.add(column("sexo").as(VARCHAR).size(1).defaultValue("'M'"));
		MigrationSession session = new MigrationSession();
		m.parse(session);
		
		assertEquals("alter table Pessoa add sexo VARCHAR(1) default 'M'", session.getAppender().nextSql());
	}
	@Test
	public void testAlterColumnMigration() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3));
		MigrationSession session = new MigrationSession();
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnToNull() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").nullable());
		MigrationSession session = new MigrationSession();
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura null ", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropColumn() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.drop(column("nome"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop column nome", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddForeignKey() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.add(foreignKey("fk_cidade").references("Cidade", "id_cidade")).parse(session);
		
		assertEquals("alter table Pessoa add constraint fk_cidade foreign key (id_cidade) references Cidade (id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddForeignKeyWithoutName() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.add(foreignKey().references("Cidade", "id_cidade")).parse(session);
		
		assertEquals("alter table Pessoa add foreign key (id_cidade) references Cidade (id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKey() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.add(primaryKey("pk_pessoa").column("id_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add constraint pk_pessoa primary key (id_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKeyWithoutName() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.add(primaryKey().column("id_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add primary key (id_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropTable() {
		MigrationSession session = new MigrationSession();
		dropTable("Pessoa").parse(session);
		
		assertEquals("drop table Pessoa", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropConstraint() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.drop(foreignKey("fk_cidade"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop constraint fk_cidade", session.getAppender().nextSql());
		
		session = new MigrationSession();
		alterTable("Pessoa")
		.drop(primaryKey("pk_pessoa"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop constraint pk_pessoa", session.getAppender().nextSql());
		
	}

	@Test
	public void testCreateAutoIncrementPrimaryKey() {
		MigrationSession session = new MigrationSession();
		createTable("Pessoa").add(primaryKeyColumn("cd_pessoa").as(NUMERIC).size(10).notNull().autoIncrement("seq_pessoa"))
		.parse(session);
		
		assertEquals("create table Pessoa (cd_pessoa NUMERIC(10) not null primary key)", session.getAppender().nextSql());
	}

	@Test
	public void testAddUniqueKey() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.add(uniqueKey("pess_uk").column("nm_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add constraint pess_uk unique (nm_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddMultipleColumnUniqueKey() {
		MigrationSession session = new MigrationSession();
		alterTable("Pessoa")
		.add(uniqueKey("pess_uk").column("nm_pessoa").column("cpf_cnpj")).parse(session);
		
		assertEquals("alter table Pessoa add constraint pess_uk unique (nm_pessoa, cpf_cnpj)", session.getAppender().nextSql());
	}
	
	@Test
	public void testCreateIndex() {
		MigrationSession session = new MigrationSession();
		createIndex("Pessoa_idx")
			.on("Pessoa").add(column("nm_pessoa")).parse(session);
		
		assertEquals("create index Pessoa_idx on Pessoa(nm_pessoa)", session.getAppender().nextSql());
	}	
	
	@Test
	public void testDropIndex() {
		MigrationSession session = new MigrationSession();
		dropIndex("Pessoa_idx").onTable("Pessoas").parse(session);
		
		assertEquals("drop index Pessoa_idx", session.getAppender().nextSql());
	}	
}
