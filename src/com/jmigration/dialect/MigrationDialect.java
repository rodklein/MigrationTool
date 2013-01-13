package com.jmigration.dialect;

import com.jmigration.MigrationSession;
import com.jmigration.core.Column;
import com.jmigration.core.Constraint;
import com.jmigration.core.SQLCommand;


public interface MigrationDialect {

	public abstract String createTable(String tableName);
	public abstract String alterTable(String tableName);
	public abstract String addColumn();
	public abstract String getType(int type);
	public abstract String alterColumn();
	public abstract String dropColumn();
	public abstract String addConstraint();
	public abstract String dropConstraint(MigrationSession session, Constraint constraint);
	public abstract String foreignKey(String foreignKeyName);
	public abstract String primaryKey(String primaryKeyName);
	public abstract String addPrimaryKeyClause(MigrationSession session, String sequenceName);
	public abstract String alterType();
	public abstract String uniqueKey(String uniqueKeyName);
	public abstract String createIndex(String indexName);
	public abstract String indexName(String table, String indexName);
	public abstract void alterAddNotNull(MigrationSession session, SQLCommand sql, Column<?> column);
	public abstract void alterDropNotNull(MigrationSession session, SQLCommand sql, Column<?> column);

}
