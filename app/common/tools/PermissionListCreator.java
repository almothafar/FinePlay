package common.tools;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.lang3.StringUtils;

import common.tools.PermissionListCreator.ClassInfo.MethodInfo;
import models.system.System;
import models.system.System.Permission;
import play.api.mvc.Call;
import play.mvc.Controller;

class PermissionListCreator {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		final StringBuilder builder = new StringBuilder();
		builder.append(ClassInfo.toMdTableHeader());

		final List<ClassInfo> apiInfos = getClassInfos("apis");
		for (final ClassInfo apiInfo : apiInfos) {

			builder.append(apiInfo.toMdTable());
		}

		final List<ClassInfo> controllerInfos = getClassInfos("controllers");
		for (final ClassInfo controllerInfo : controllerInfos) {

			builder.append(controllerInfo.toMdTable());
		}

		final Path permission = Paths.get(".", "document", "permission.md");
		Files.write(permission, builder.toString().getBytes(StandardCharsets.UTF_8));
	}

	private static List<ClassInfo> getClassInfos(@Nonnull final String packageName) throws ClassNotFoundException, IOException {

		final List<ClassInfo> classInfos = new ArrayList<>();
		for (final Class<?> controllerClass : getClasses(packageName)) {

			final Set<Permission> classPermissions = getClassPermissions(controllerClass);
			final ClassInfo classInfo = new ClassInfo(packageName, controllerClass.getName(), classPermissions);

			for (final Method controllerMethod : getMethods(controllerClass)) {

				final Set<Permission> methodPermissions = getMethodPermissions(controllerMethod);
				final MethodInfo methodInfo = new MethodInfo(controllerMethod.getName(), methodPermissions);
				classInfo.addMethodInfo(methodInfo);
			}

			classInfos.add(classInfo);
		}

		return Collections.unmodifiableList(classInfos);
	}

	private static Set<Permission> getClassPermissions(@Nonnull final Class<?> clazz) {

		final System.PermissionsAllowed permissionsAllowed = clazz.getAnnotation(System.PermissionsAllowed.class);
		final Permission[] permissions = Objects.isNull(permissionsAllowed) ? new Permission[]{} : permissionsAllowed.value();
		return new HashSet<>(Arrays.asList(permissions));
	}
	private static Set<Permission> getMethodPermissions(@Nonnull final Method method) {

		final System.PermissionsAllowed permissionsAllowed = method.getAnnotation(System.PermissionsAllowed.class);
		final Permission[] permissions = Objects.isNull(permissionsAllowed) ? new Permission[]{} : permissionsAllowed.value();
		return new HashSet<>(Arrays.asList(permissions));
	}

	private static final Pattern PATTERN_PATH = Pattern.compile("^.*/fineplay/bin/(.*)\\.class");

	public static Set<Class<?>> getClasses(final String packageName) throws IOException {

		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final JavaFileManager fileManager = compiler.getStandardFileManager(new DiagnosticCollector<JavaFileObject>(), null, null);

		final ClassLoader loader = Thread.currentThread().getContextClassLoader();

		final Iterable<JavaFileObject> fileObjects = fileManager.list(StandardLocation.CLASS_PATH, packageName, EnumSet.of(Kind.CLASS), true);
		return StreamSupport.stream(fileObjects.spliterator(), true).map(fileObject -> fileObject.toUri()).filter(uri -> {

			if (!"file".equals(uri.getScheme()))
				return false;
			final String path = uri.getPath();
			if (!path.contains("/fineplay/bin/"))
				return false;
			if (path.contains("$"))
				return false;

			return true;
		}).map(uri -> {

			final String path = uri.getPath();
			final Matcher matcher = PATTERN_PATH.matcher(path);
			if (!matcher.matches()) {

				throw new IllegalStateException(":" + path);
			}

			final String fqcn = matcher.group(1).replaceAll("/", ".");
			return fqcn;
		}).filter(fqcn -> {

			final String fqrcn = convertReverseFQCN(fqcn);
			try {

				loader.loadClass(fqrcn);
				return true;
			} catch (ClassNotFoundException e) {

				return false;
			}
		}).map(fqcn -> {

			try {

				return loader.loadClass(fqcn);
			} catch (ClassNotFoundException e) {

				throw new IllegalStateException(":", e);
			}
		}).filter(clazz -> {

			if (Modifier.isAbstract(clazz.getModifiers()))
				return false;
			if (!Controller.class.isAssignableFrom(clazz))
				return false;

			return true;
		}).collect(Collectors.toCollection(() -> new TreeSet<Class<?>>((c0, c1) -> c0.getName().compareTo(c1.getName()))));
	}

	public static Set<Method> getMethods(final Class<?> clazz) throws ClassNotFoundException {

		final Set<Method> controllerMethods = Arrays.stream(clazz.getMethods()).filter(method -> {

			if (clazz != method.getDeclaringClass())
				return false;

			if (Modifier.isStatic(method.getModifiers()))
				return false;

			if (!Modifier.isPublic(method.getModifiers()))
				return false;

			return true;
		}).collect(Collectors.toSet());

		final String fqrcn = convertReverseFQCN(clazz.getName());
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final Class<?> reverseClazz = loader.loadClass(fqrcn);
		final Set<Method> reverseMethods = Arrays.stream(reverseClazz.getMethods()).filter(method -> {

			if (Call.class != method.getReturnType())
				return false;

			return true;
		}).collect(Collectors.toSet());

		return reverseMethods.stream().map(reverseMethod -> {

			final String reverseMethodName = reverseMethod.getName();
			final Class<?>[] reverseParameterTypes = reverseMethod.getParameterTypes();

			final List<Method> actionMethods = controllerMethods.stream().filter(controllerMethod -> {

				final String controllerMethodName = controllerMethod.getName();
				final Class<?>[] controllerParameterTypes = controllerMethod.getParameterTypes();

				return reverseMethodName.equals(controllerMethodName) && equalParameterTypes(reverseParameterTypes, controllerParameterTypes);
			}).collect(Collectors.toList());
			if (1 != actionMethods.size()) {

				throw new IllegalStateException();
			}

			return actionMethods.get(0);
		}).collect(Collectors.toCollection(() -> new TreeSet<Method>((m0, m1) -> m0.getName().compareTo(m1.getName()))));
	}

	private static String convertReverseFQCN(final String fqcn) {

		final int lastDot = fqcn.lastIndexOf(".");
		final String fqrcn = fqcn.substring(0, lastDot + 1) + "Reverse" + fqcn.substring(lastDot + 1, fqcn.length());
		return fqrcn;
	}

	private static boolean equalParameterTypes(final Class<?>[] reverseParameterTypes, final Class<?>[] controllerParameterTypes) {

		if (reverseParameterTypes.length != controllerParameterTypes.length)
			return false;

		return IntStream.range(0, reverseParameterTypes.length).allMatch(i -> {

			final Class<?> reverseParameterType = reverseParameterTypes[i];
			final String reverseParameterTypeName = reverseParameterType.getSimpleName();

			final Class<?> controllerParameterType = controllerParameterTypes[i];
			final String controllerParameterTypeName = controllerParameterType.getSimpleName();

			return reverseParameterTypeName.toLowerCase().substring(0, 3).equals(controllerParameterTypeName.toLowerCase().substring(0, 3));
		});
	}

	static class ClassInfo {

		private String packageName;
		private String name;
		private Set<Permission> permissions;

		private List<MethodInfo> methodInfos = new ArrayList<>();;

		public ClassInfo(@Nonnull final String packageName, @Nonnull String name, @Nonnull Set<Permission> permissions) {

			this.packageName = packageName;
			this.name = name;
			this.permissions = permissions;
		}

		void addMethodInfo(@Nonnull final MethodInfo methodInfo) {

			methodInfos.add(methodInfo);
		}

		static class MethodInfo {

			private String name;
			private Set<Permission> permissions;

			public MethodInfo(@Nonnull final String name, @Nonnull Set<Permission> permissions) {

				this.name = name;
				this.permissions = permissions;
			}

			public Set<Permission> getPermissions() {

				return permissions;
			}
		}

		static String toMdTableHeader() {

			final StringBuilder builder = new StringBuilder();

			final int maxPermissionNameLength = Arrays.stream(System.Permission.values()).map(permission -> permission.name().length()).max(Comparator.naturalOrder()).get();

			final List<String> headerColumns = new ArrayList<>();
			headerColumns.add(StringUtils.rightPad("PACKAGE", 20, ""));
			headerColumns.add(StringUtils.rightPad("CLASS", 70, ""));
			headerColumns.add(StringUtils.rightPad("METHOD", 30, ""));
			for (final Permission permission : System.Permission.values()) {

				headerColumns.add(StringUtils.leftPad(permission.name(), maxPermissionNameLength, ""));
			}
			builder.append(headerColumns.stream().collect(Collectors.joining("|", "|", "|")));
			builder.append("\n");

			final List<String> separateColumns = new ArrayList<>();
			separateColumns.add(StringUtils.rightPad("", 20, "-"));
			separateColumns.add(StringUtils.rightPad("", 70, "-"));
			separateColumns.add(StringUtils.rightPad("", 30, "-"));
			for (final Permission permission : System.Permission.values()) {

				separateColumns.add(StringUtils.leftPad("", maxPermissionNameLength, "-"));
			}
			builder.append(separateColumns.stream().collect(Collectors.joining("|", "|", "|")));
			builder.append("\n");

			return builder.toString().replaceFirst("$", "");
		}

		String toMdTable() {

			final StringBuilder builder = new StringBuilder();

			final int maxPermissionNameLength = Arrays.stream(System.Permission.values()).map(permission -> permission.name().length()).max(Comparator.naturalOrder()).get();

			final List<String> classColumns = new ArrayList<>();
			classColumns.add(StringUtils.rightPad(packageName, 20, ""));
			classColumns.add(StringUtils.rightPad(name, 70, ""));
			classColumns.add(StringUtils.rightPad("", 30, ""));
			for (final Permission permission : System.Permission.values()) {

				classColumns.add(StringUtils.leftPad(permissions.contains(permission) ? "O" : "", maxPermissionNameLength, ""));
			}
			builder.append(classColumns.stream().collect(Collectors.joining("|", "|", "|")));
			builder.append("\n");

			for (final MethodInfo methodInfo : methodInfos) {

				final List<String> methodColumns = new ArrayList<>();
				methodColumns.add(StringUtils.rightPad("", 20, ""));
				methodColumns.add(StringUtils.rightPad("", 70, ""));
				methodColumns.add(StringUtils.rightPad(methodInfo.name, 30, ""));
				for (final Permission permission : System.Permission.values()) {

					methodColumns.add(StringUtils.leftPad(methodInfo.permissions.contains(permission) ? "O" : "", maxPermissionNameLength, ""));
				}
				builder.append(methodColumns.stream().collect(Collectors.joining("|", "|", "|")));
				builder.append("\n");
			}

			return builder.toString().replaceFirst("$", "");
		}
	}
}
