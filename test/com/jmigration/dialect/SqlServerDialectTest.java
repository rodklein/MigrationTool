package com.jmigration.dialect;

import static com.jmigration.Migration.alterTable;
import static com.jmigration.Migration.column;
import static com.jmigration.Migration.createTable;
import static com.jmigration.Migration.dropTable;
import static com.jmigration.Migration.foreignKey;
import static com.jmigration.Migration.primaryKey;
import static com.jmigration.Migration.primaryKeyColumn;
import static java.sql.Types.TIMESTAMP;
import static java.sql.Types.NUMERIC;
import static java.sql.Types.VARCHAR;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jmigration.Migration;
import com.jmigration.MigrationSession;

public class SqlServerDialectTest {
	
	@Test
	public void testCreateTableMigration() {
		Migration m = createTable("Pessoa")
		.add(column("Nome").as(VARCHAR).size(30))
		.add(column("DataNasc").as(TIMESTAMP))
		.add(column("Id").as(NUMERIC).size(6).notNull())
		.add(column("Peso").as(NUMERIC).size(5, 2));
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		m.parse(session);
		
		assertEquals("create table Pessoa (Nome VARCHAR(30), DataNasc DATETIME, Id NUMERIC(6) not null, Peso NUMERIC(5,2))", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterTableMigration() {
		Migration m = alterTable("Pessoa")
		.add(column("Altura").as(NUMERIC).size(3));
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa add Altura NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnMigration() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").as(NUMERIC).size(3));
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura NUMERIC(3)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAlterColumnToNull() {
		Migration m = alterTable("Pessoa")
		.alter(column("Altura").nullable());
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		m.parse(session);
		
		assertEquals("alter table Pessoa alter column Altura null ", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropColumn() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.drop(column("nome"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop column nome", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddForeignKey() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.add(foreignKey("fk_cidade").references("Cidade", "id_cidade")).parse(session);
		
		assertEquals("alter table Pessoa add constraint fk_cidade foreign key (id_cidade) references Cidade (id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddForeignKeyMultipleColumns() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.add(foreignKey("fk_cidade").references("Cidade", "id_cidade", "id_estado")).parse(session);
		
		assertEquals("alter table Pessoa add constraint fk_cidade foreign key (id_cidade, id_estado) references Cidade (id_cidade, id_estado)", session.getAppender().nextSql());
	}

	
	@Test
	public void testAddForeignKeyWithoutName() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.add(foreignKey().references("Cidade", "id_cidade")).parse(session);
		
		assertEquals("alter table Pessoa add foreign key (id_cidade) references Cidade (id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKey() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.add(primaryKey("pk_pessoa").column("id_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add constraint pk_pessoa primary key (id_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKeyMultipleColumns() {
		MigrationSession session = new MigrationSession(new PostgreSQLDialect());
		alterTable("PessoaEmpresaCidade")
		.add(primaryKey("pk_pessoa").column("id_pessoa").column("id_empresa").column("id_cidade")).parse(session);
		
		assertEquals("alter table PessoaEmpresaCidade add constraint pk_pessoa primary key (id_pessoa, id_empresa, id_cidade)", session.getAppender().nextSql());
	}
	
	@Test
	public void testAddPrimaryKeyWithoutName() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.add(primaryKey().column("id_pessoa")).parse(session);
		
		assertEquals("alter table Pessoa add primary key (id_pessoa)", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropTable() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		dropTable("Pessoa").parse(session);
		
		assertEquals("drop table Pessoa", session.getAppender().nextSql());
	}
	
	@Test
	public void testDropConstraint() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.drop(foreignKey("fk_cidade"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop constraint fk_cidade", session.getAppender().nextSql());
		
		session = new MigrationSession(new SQLServerDialect());
		alterTable("Pessoa")
		.drop(primaryKey("pk_pessoa"))
		.parse(session);
		
		assertEquals("alter table Pessoa drop constraint pk_pessoa", session.getAppender().nextSql());
		
	}

	@Test
	public void testCreateAutoIncrementPrimaryKey() {
		MigrationSession session = new MigrationSession(new SQLServerDialect());
		createTable("Pessoa").add(primaryKeyColumn("cd_pessoa").as(NUMERIC).size(10).notNull().autoIncrement("seq_pessoa"))
		.parse(session);
		
		assertEquals("create table Pessoa (cd_pessoa NUMERIC(10) not null primary key identity)", session.getAppender().nextSql());
	}

}
