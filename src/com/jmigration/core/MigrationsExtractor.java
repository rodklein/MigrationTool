package com.jmigration.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jmigration.Migration;
import com.jmigration.MigrationUnit;
import com.jmigration.annotation.Order;

public class MigrationsExtractor {
	
	public static List<Migration> extractAll(MigrationUnit unit) {
		List<Migration> result = new ArrayList<Migration>();
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
			result.addAll(sortedMethod.execute(unit));
		}
		return result;
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
			return order.compareTo(other.order);
		}
		
		@SuppressWarnings("unchecked")
		public Collection<Migration> execute(Object target) {
			try {
				Object result = method.invoke(target);
				if (result instanceof Collection) {
					return (Collection<Migration>) result;
				} else {
					return Collections.singleton((Migration) result);
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		
	}

}
