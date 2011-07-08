package com.jmigration;

import com.jmigration.core.SQLAppender;
import com.jmigration.core.SQLCommand;
import com.jmigration.dialect.BaseDialect;
import com.jmigration.dialect.MigrationDialect;


public class MigrationSession {
	
	private SQLAppender appender = new SQLAppender();
	private final MigrationDialect dialect;
	
	public MigrationSession(MigrationDialect dialect) {
		this.dialect = dialect;
	}
	
	public MigrationSession() {
		this.dialect = new BaseDialect();
	}

	public void appendSQL(SQLCommand sql) {
		appender.appendSQL(sql);
	}

	public SQLAppender getAppender() {
		return appender;
	}

	public MigrationDialect getDialect() {
		return dialect;
	}


}
