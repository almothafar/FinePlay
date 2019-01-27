package batchs.manage.hello.batchlet;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class Batchlet extends AbstractBatchlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private JobContext jobCtx;

	@Inject
	@BatchProperty(name = "int")
	int anInt;

	@Inject
	@BatchProperty(name = "int")
	long aLong;

	@Inject
	@BatchProperty(name = "list")
	int[] ints;

	@Inject
	@BatchProperty(name = "list")
	long[] longs;

	@Inject
	@BatchProperty(name = "list")
	char[] chars;

	@Inject
	@BatchProperty(name = "booleans")
	boolean[] booleans;

	@Inject
	@BatchProperty(name = "list")
	String[] listStringArray;

	@SuppressWarnings("rawtypes")
	@Inject
	@BatchProperty(name = "class")
	Class cls;

	@SuppressWarnings("rawtypes")
	@Inject
	@BatchProperty(name = "class")
	Class[] clss;

	@Inject
	@BatchProperty(name = "inet4.address")
	Inet4Address inet4Address;

	@Inject
	@BatchProperty(name = "inet6.address")
	Inet6Address inet6Address;

	@Inject
	@BatchProperty(name = "map")
	Map<String, String> map;

	@Inject
	@BatchProperty(name = "set")
	Set<String> set;

	@Inject
	@BatchProperty(name = "logger")
	java.util.logging.Logger logger;

	@Inject
	@BatchProperty(name = "pattern")
	Pattern pattern;

	@Inject
	@BatchProperty(name = "object.name")
	ObjectName objectName;

	@Inject
	@BatchProperty(name = "list")
	private List<String> list;

	@Inject
	@BatchProperty(name = "big.integer")
	private BigInteger bigInteger;

	@Inject
	@BatchProperty(name = "big.decimal")
	private BigDecimal bigDecimal;

	@Inject
	@BatchProperty(name = "url")
	private URL url;

	@Inject
	@BatchProperty(name = "uri")
	private URI uri;

	@Inject
	@BatchProperty(name = "file")
	private File file;

	@Inject
	@BatchProperty(name = "jar.files")
	JarFile[] jarFiles;

	@Override
	public String process() throws Exception {

		final String message = jobCtx.getProperties().getProperty("message");
		LOGGER.info(message);

		final Field[] declaredFields = this.getClass().getDeclaredFields();
		for (final Field field : declaredFields) {

			if (field.getAnnotation(BatchProperty.class) != null) {

				final Class<?> fieldType = field.getType();
				final Object fieldValue = field.get(this);
				System.out.printf("Field injection: %s %s = %s;%n", fieldType, field.getName(), fieldValue);
			}
		}

		return null;
	}
}
