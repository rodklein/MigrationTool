package com.jmigration.dialect;

import java.sql.Types;

import com.jmigration.MigrationSession;
import com.jmigration.core.Column;
import com.jmigration.core.SQLCommand;

public class PostgreSQLDialect extends BaseDialect {
	
	public PostgreSQLDialect(){
		types.put(Types.VARCHAR, "VARCHAR");
		types.put(Types.NUMERIC, "NUMERIC");
		types.put(Types.DATE, "DATE");
		types.put(Types.TIMESTAMP, "TIMESTAMP");
	}

	@Override
	public String addPrimaryKeyClause(MigrationSession session, String sequenceName) {
		if (sequenceName != null) {
			SQLCommand sqlCommand = new SQLCommand();
			sqlCommand.append("create sequence ").append(sequenceName).append(" increment by 1 start with 1 minvalue 1 maxvalue 999999999999999999");
			session.appendSQL(sqlCommand);
		}
		return super.addPrimaryKeyClause(session, sequenceName);
	}
	
	@Override
	public String alterType() {
		return "type ";
	}
	
	@Override
	public void alterDropNotNull(MigrationSession session, SQLCommand sql, Column<?> column) {
		String sqlFragment = sql.toString().substring(0, sql.toString().indexOf(column.getColumnName()));
		SQLCommand newSql = new SQLCommand();
		newSql.append(sqlFragment).append(column.getColumnName()).append(" drop not null");
		session.appendSQL(newSql);
	}
	
	@Override
	public void alterAddNotNull(MigrationSession session, SQLCommand sql, Column<?> column) {
		String sqlFragment = sql.toString().substring(0, sql.toString().indexOf(column.getColumnName()));
		if (column.getDefaultValue() != null) {
			SQLCommand newSql = new SQLCommand();
			newSql.append("update ").append(sqlFragment.replaceAll(".*table (.*) alter .*", "$1")).append(" set ").append(column.getColumnName()).append(" = ").append(column.getDefaultValue())
			.append(" where ").append(column.getColumnName()).append(" is null");
			session.appendSQL(newSql);	
		}
		SQLCommand newSql = new SQLCommand();
		newSql.append(sqlFragment).append(column.getColumnName()).append(" set not null");
		session.appendSQL(newSql);
	}
}
