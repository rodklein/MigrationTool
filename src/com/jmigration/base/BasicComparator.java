package com.jmigration.base;

import java.util.Comparator;

import com.jmigration.MigrationUnit;

public class BasicComparator implements Comparator<MigrationUnit>{

	@Override
	public int compare(MigrationUnit unit1, MigrationUnit unit2) {
		return unit1.version().compareTo(unit2.version());
	}

}
