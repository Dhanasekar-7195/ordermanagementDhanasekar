package com.ordermanagement.SequenceGenerator;

import java.io.Serializable;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

public class StringPrefixedSequenceGenerator extends SequenceStyleGenerator {

	public static final String VALUE_PREFIX_PARAMETER = "valuePrefix";
	public static final String VALUE_PREFIX_DEFAULT = "";
	private String valuePrefix;

	public static final String NUMBER_FORMATE_PARAMETER = "numberFormate";
	public static final String NUMBER_FORMATE_DEFAULT = "%d";
	private String numberFormat;

	@Override
	public void configure(Type arg0, Properties params, ServiceRegistry arg2) throws MappingException {
		super.configure(LongType.INSTANCE, params, arg2);
		valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params, VALUE_PREFIX_DEFAULT);
		numberFormat = ConfigurationHelper.getString(NUMBER_FORMATE_PARAMETER, params, NUMBER_FORMATE_DEFAULT);
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

		return valuePrefix + String.format(numberFormat, super.generate(session, object));
	}

}
