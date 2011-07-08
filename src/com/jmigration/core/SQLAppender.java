package com.jmigration.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SQLAppender implements Iterable<SQLCommand>{
	
	private Queue<SQLCommand> sqls = new LinkedList<SQLCommand>();
	
	public void appendSQL(SQLCommand command) {
		sqls.offer(command);
	}
	
	public String nextSql() {
		if (sqls.isEmpty()) return "";
		return sqls.poll().toString();
	}

	@Override
	public Iterator<SQLCommand> iterator() {
		return sqls.iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sqls == null) ? 0 : sqls.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SQLAppender other = (SQLAppender) obj;
		if (sqls == null) {
			if (other.sqls != null)
				return false;
		} else if (!sqls.equals(other.sqls))
			return false;
		return true;
	}
	
	
}
