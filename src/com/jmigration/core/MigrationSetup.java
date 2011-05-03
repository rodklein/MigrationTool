package com.jmigration.core;

import com.jmigration.dialect.BaseDialect;
import com.jmigration.dialect.MigrationDialect;

public class MigrationSetup {
	
	private static MigrationSetup instance;
	private MigrationDialect dialect = new BaseDialect();

	public static MigrationSetup getInstance() {
		if (instance == null) {
			instance = new MigrationSetup();
		}
		return instance;
	}

	public void setDialect(MigrationDialect dialect) {
		this.dialect = dialect;
	}

	public MigrationDialect getDialect() {
		return dialect;
	}

}
