package com.jmigration.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;
import com.jmigration.annotation.Order;
import com.jmigration.annotation.Reverse;

public class MigrationsExtractor {
	
	public static List<MigrationItem> extractAll(MigrationUnit unit) {
		List<MigrationItem> result = new ArrayList<MigrationItem>();
		Map<String, MigrationItem> migrations = new TreeMap<String, MigrationItem>();
		SortedSet<SortedMethod> orderedMethods = new TreeSet<MigrationsExtractor.SortedMethod>();
		Method[] methods = unit.getClass().getMethods();
		for (Method method : methods) {
			try {
				if (Migration.class.isAssignableFrom(method.getReturnType()) && method.getParameterTypes().length == 0) {
					orderedMethods.add(new SortedMethod(method));
				} else if (Collection.class.isAssignableFrom(method.getReturnType()) && method.getGenericReturnType() instanceof ParameterizedType) {
					Type[] actualTypeArguments = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
					if (actualTypeArguments.length == 1 && Migration.class.isAssignableFrom((Class<?>) actualTypeArguments[0])) {
						orderedMethods.add(new SortedMethod(method));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (SortedMethod sortedMethod : orderedMethods) {
			if (isReverse(sortedMethod)) {
				String migrationToReverse = getMigrationToReverse(sortedMethod);
				if(!migrations.containsKey(migrationToReverse)) {
					MigrationItem migrationItem = new MigrationItem(unit.name(), migrationToReverse);
					migrations.put(migrationToReverse, migrationItem);
					result.add(migrationItem);
				}
				migrations.get(migrationToReverse).addReverse(sortedMethod.execute(unit));
			} else {
				if(!migrations.containsKey(sortedMethod.method.getName())) {
					MigrationItem migrationItem = new MigrationItem(unit.name(), sortedMethod.method.getName());
					migrations.put(sortedMethod.method.getName(), migrationItem);
					result.add(migrationItem);
				}
				migrations.get(sortedMethod.method.getName()).addMigrations(sortedMethod.execute(unit));
			}
		}
		return result;
	}
	
	private static String getMigrationToReverse(SortedMethod sortedMethod) {
		String reverseName = null;
		if (sortedMethod.method.getAnnotation(Reverse.class) != null) {
			reverseName = sortedMethod.method.getAnnotation(Reverse.class).value();
		} else {
			reverseName = sortedMethod.method.getName().replaceAll("reverse(.+)", "$1");
		}
		return reverseName.substring(0,1).toLowerCase() + reverseName.substring(1);
	}

	private static boolean isReverse(SortedMethod sortedMethod) {
		if (sortedMethod.method.getAnnotation(Reverse.class) != null || sortedMethod.method.getName().matches("reverse.+")) {
			return true;
		} else {
			return false;
		}
	}

	static class SortedMethod implements Comparable<SortedMethod>{
		
		private Method method;
		private Integer order = 0;
		
		public SortedMethod(Method method) {
			this.method = method;
			if (method.getAnnotation(Order.class) != null) {
				order = method.getAnnotation(Order.class).value();
			} else if (method.getName().matches(".+\\d+")) {
				String value = method.getName().replaceAll("[^\\d]+(\\d+)", "$1");
				order = Integer.valueOf(value);
			}
		}

		@Override
		public int compareTo(SortedMethod other) {
			int orderIndex = order.compareTo(other.order);
			return orderIndex == 0 ? -1 : orderIndex;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof SortedMethod) {
				return method.equals(((SortedMethod) obj).method);
			}
			return false;
		}
		
		@SuppressWarnings("unchecked")
		public Collection<Migration> execute(Object target) {
			try {
				method.setAccessible(true);
				Object result = method.invoke(target);
				if (result instanceof Collection) {
					return (Collection<Migration>) result;
				} else {
					return Collections.singleton((Migration) result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		
	}

}
