package com.jmigration;

import com.jmigration.core.AlterTable;
import com.jmigration.core.Column;
import com.jmigration.core.CreateTable;
import com.jmigration.core.DropTable;
import com.jmigration.core.ForeignKey;
import com.jmigration.core.MigrationConfiguration;
import com.jmigration.core.PrimaryKey;

public abstract class Migration {
	
	protected StringBuilder sqlBuilder = new StringBuilder();
	private static MigrationConfiguration dialect = MigrationConfiguration.getInstance();
	
	public static CreateTable createTable(String tableName) {
		return new CreateTable(tableName, dialect);
	}
	
	public static DropTable dropTable(String tableName) {
		return new DropTable(tableName, dialect);
	}

	public abstract void parse(StringBuilder sql);

	public static AlterTable alterTable(String tableToAlter) {
		return new AlterTable(tableToAlter, dialect);
	}
	
	public static Column column(String columnName) {
		return new Column(columnName);
	}
	
	public static ForeignKey foreignKey(String fkName) {
		return new ForeignKey(fkName);
	}
	
	public static ForeignKey foreignKey() {
		return new ForeignKey();
	}
	
	public static PrimaryKey primaryKey(String pkName) {
		return new PrimaryKey(pkName);
	}

	public static PrimaryKey primaryKey() {
		return new PrimaryKey();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj instanceof Migration) {
			StringBuilder sql1 = new StringBuilder();
			StringBuilder sql2 = new StringBuilder();
			parse(sql1);
			((Migration) obj).parse(sql2);
			return sql1.toString().equals(sql2.toString());
		}
		return false;
	}
	
}
