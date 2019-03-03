package common.core;

import java.lang.invoke.MethodHandles;

import org.hibernate.EmptyInterceptor;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInterceptor extends EmptyInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public String onPrepareStatement(String sql) {

//		LOGGER.info(new BasicFormatterImpl().format(sql));
		return super.onPrepareStatement(sql);
	}
}
