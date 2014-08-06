package com.jmigration.dialect;

import java.sql.Types;

import com.jmigration.MigrationSession;
import com.jmigration.core.Column;
import com.jmigration.core.Constraint;
import com.jmigration.core.SQLCommand;
import com.jmigration.core.UniqueKey;

public class OracleDialect extends BaseDialect {
	
	public OracleDialect() {
		types.put(Types.VARCHAR, "VARCHAR2");
		types.put(Types.NUMERIC, "NUMBER");
		types.put(Types.DATE, "DATE");
		types.put(Column.VARCHAR99, "VARCHAR2");
		types.put(Types.BOOLEAN, "CHAR");
		types.put(Types.LONGVARCHAR, "CLOB");
	}

	@Override
	public String alterColumn() {
		return " modify ";
	}

	@Override
	public String addPrimaryKeyClause(MigrationSession session, String sequenceName) {
		if (sequenceName != null) {
			SQLCommand sqlCommand = new SQLCommand();
			sqlCommand.append("create sequence ").append(sequenceName).append(" increment by 1 start with 1 minvalue 1 maxvalue 999999999999999999999999999 noorder nocycle nocache");
			session.appendSQL(sqlCommand);
		}
		return super.addPrimaryKeyClause(session, sequenceName);
	}
	
	@Override
	public String dropConstraint(MigrationSession session, Constraint constraint) {
		if (constraint instanceof UniqueKey) {
			SQLCommand sqlCommand = new SQLCommand();
			sqlCommand.append("drop index ");
			sqlCommand.append(constraint.getName());
			session.appendSQL(sqlCommand);
		}
		return super.dropConstraint(session, constraint);
	}

}
