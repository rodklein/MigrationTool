package com.jmigration.dialect;

import java.sql.Types;

import com.jmigration.MigrationSession;
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
		SQLCommand sqlCommand = new SQLCommand();
		sqlCommand.append("create sequence ").append(sequenceName).append(" increment by 1 start with 1 minvalue 1 maxvalue 999999999999999999999999999");
		session.appendSQL(sqlCommand);
		return super.addPrimaryKeyClause(session, sequenceName);
	}

}
