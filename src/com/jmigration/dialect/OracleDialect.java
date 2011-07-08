package com.jmigration.dialect;

import java.sql.Types;

import com.jmigration.MigrationSession;
import com.jmigration.core.SQLCommand;

public class OracleDialect extends BaseDialect {
	
	public OracleDialect() {
		types.put(Types.VARCHAR, "VARCHAR2");
		types.put(Types.NUMERIC, "NUMBER");
		types.put(Types.DATE, "DATE");
		types.put(Types.TIMESTAMP, "DATE");
	}

	@Override
	public String alterColumn() {
		return " modify ";
	}

	@Override
	public String addPrimaryKeyClause(MigrationSession session, String sequenceName) {
		SQLCommand sqlCommand = new SQLCommand();
		sqlCommand.append("create sequence ").append(sequenceName).append(" increment by 1 start with 1 minvalue 1 maxvalue 999999999999999999999999999 noorder nocycle nocache");
		session.appendSQL(sqlCommand);
		return super.addPrimaryKeyClause(session, sequenceName);
	}

}
