package org.pablog.bookdb.utils;

import java.util.Date;

import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;

public class ExcludeDatePropertiesSelector implements PropertySelector {

	@SuppressWarnings("unchecked")
	@Override
	public boolean include(Object propertyValue, String propertyName, Type type) {
		
		return propertyValue != null
				&& (((propertyValue instanceof String) && !("").equals(propertyValue)) 
						|| ((propertyValue instanceof Number) && ((Number) propertyValue).longValue() != 0)
						|| !type.getReturnedClass().isAssignableFrom(Date.class));
		
	}

}
