package com.jmigration.dialect;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class BaseDialect implements MigrationDialect {
	
	protected Map<Integer, String> types = new HashMap<Integer, String>();
	
	public BaseDialect() {
		types.put(Types.VARCHAR, "VARCHAR");
		types.put(Types.NUMERIC, "NUMERIC");
		types.put(Types.DATE, "DATE");
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
		return " drop colum ";
	}

	@Override
	public String addConstraint() {
		return " add constraint ";
	}

	@Override
	public String dropConstraint() {
		return " drop constraint ";
	}


}
