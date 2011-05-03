package com.jmigration.core;

import com.jmigration.dialect.MigrationDialect;

public interface Parsable {
	
	String parse(MigrationDialect dialect);

}
