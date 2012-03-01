package com.jmigration.dialect;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.jmigration.MigrationSession;
import com.jmigration.core.Column;
import com.jmigration.core.Constraint;
import com.jmigration.core.SQLCommand;

public class BaseDialect implements MigrationDialect {
	
	protected Map<Integer, String> types = new HashMap<Integer, String>();
	
	public BaseDialect() {
		types.put(Types.VARCHAR, "VARCHAR");
		types.put(Types.NUMERIC, "NUMERIC");
		types.put(Types.DATE, "DATE");
		types.put(Types.TIMESTAMP, "TIMESTAMP");
		types.put(Types.BOOLEAN, "BOOLEAN");
		types.put(Column.VARCHAR99, "VARCHAR");
	}
	
	@Override
	public String createTable(String tableName) {
		return "create table "+ tableName;
	}

	@Override
	public String alterTable(String tableName) {
		return "alter table " + tableName;
	}

	@Override
	public String addColumn() {
		return " add ";
	}

	@Override
	public String getType(int type) {
		return types.get(type);
	}

	@Override
	public String alterColumn() {
		return " alter column ";
	}

	@Override
	public String dropColumn() {
		return " drop column ";
	}

	@Override
	public String addConstraint() {
		return " add ";
	}

	@Override
	public String dropConstraint(Constraint constraint) {
		return " drop constraint " + constraint.getName();
	}

	@Override
	public String foreignKey(String foreignKeyName) {
		return foreignKeyName == null ? "" : "constraint " + foreignKeyName + " ";
	}

	@Override
	public String primaryKey(String primaryKeyName) {
		return primaryKeyName == null ? "" : "constraint " + primaryKeyName + " ";
	}

	@Override
	public String addPrimaryKeyClause(MigrationSession session, String sequenceName) {
		return "primary key";
	}
	
	@Override
	public String alterType() {
		return "";
	}

	@Override
	public String uniqueKey(String uniqueKeyName) {
		return uniqueKeyName == null ? "" : "constraint " + uniqueKeyName + " ";
	}

	@Override
	public String createIndex(String indexName) {
		return "create index " + indexName;
	}

	@Override
	public String indexName(String table, String indexName) {
		return indexName;
	}

	@Override
	public void alterAddNotNull(MigrationSession session, SQLCommand sql, Column<?> column) {
		sql.append(" not null ");
	}

	@Override
	public void alterDropNotNull(MigrationSession session, SQLCommand sql, Column<?> column) {
		sql.append(" null ");
	}

}
