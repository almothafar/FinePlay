package common.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

class MessageKeysCreator {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {

		final SortedMap<String, String> constMap = readMessages();

		writeJavaFile(constMap);

		writeJavaScriptFile(constMap);
	}

	private static SortedMap<String, String> readMessages() throws IOException {

		final Path messagesPath = Paths.get(".", "conf", "messages");
		if (!Files.exists(messagesPath)) {

			throw new RuntimeException("Not exists messages. : " + messagesPath);
		}

		if (!nonOverlap(messagesPath)) {

			throw new RuntimeException("Overlaped messages key. : " + messagesPath);
		}

		final Properties messagesProperties = new Properties();
		messagesProperties.load(Files.newInputStream(messagesPath));

		final SortedMap<String, String> constMap = new TreeMap<>();
		for (final Entry<Object, Object> entry : messagesProperties.entrySet()) {

			final String constKey = entry.getKey().toString().replace("_", "__").replace(".", "_").toUpperCase();
			final String constValue = entry.getKey().toString();
			constMap.put(constKey, constValue);
		}

		return Collections.unmodifiableSortedMap(constMap);
	}

	private static boolean nonOverlap(final Path messagesPath) throws IOException {

		final SortedMap<String, List<Entry<String, String>>> keyToEntries = Files.lines(messagesPath, StandardCharsets.UTF_8).filter(line -> line.contains("=")).sorted().map(line -> {

			final String[] keyAndValue = line.split("=");
			final String key = keyAndValue[0].trim();
			final String value = 2 <= keyAndValue.length ? keyAndValue[1].trim() : "";

			final Entry<String, String> entry = new SimpleImmutableEntry<>(key, value);
			return entry;
		}).collect(Collectors.groupingBy(entry -> entry.getKey(), TreeMap::new, Collectors.toList()));

		final long overlapCount = keyToEntries.entrySet().stream().filter(keyToEntry -> {

			final int entryCount = keyToEntry.getValue().size();
			final boolean isOverlap = 2 <= entryCount;

			System.out.println((isOverlap ? "! " : "  ") + keyToEntry);

			return 2 <= entryCount;
		}).collect(Collectors.counting());

		return 0 == overlapCount;
	}

	private static void writeJavaFile(final SortedMap<String, String> constMap) throws IOException {

		final Builder builder = TypeSpec.classBuilder("MessageKeys")//
				.addJavadoc("Generated by JavaPoet.\n") //
				.addJavadoc("@see <a href=\"https://github.com/square/javapoet\">https://github.com/square/javapoet</a>\n") //
				.addModifiers(Modifier.PUBLIC);

		final MethodSpec constructorSpec = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
		builder.addMethod(constructorSpec);

		for (final Entry<String, String> entry : constMap.entrySet()) {

			final FieldSpec fieldSpec = FieldSpec.builder(String.class, entry.getKey(), Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)//
					.initializer("$S", entry.getValue())//
					.build();
			builder.addField(fieldSpec);
		}

		final TypeSpec typeSpec = builder.build();

		final JavaFile javaFile = JavaFile.builder("common.system", typeSpec).build();

		final Path constantsPath = Paths.get(".", "app");
		javaFile.writeTo(constantsPath);
	}

	private static void writeJavaScriptFile(final SortedMap<String, String> constMap) throws IOException {

		final Path path = Paths.get(".", "public", "javascripts", "messages.js");

		final List<String> lines = new ArrayList<>();
		lines.add("'use strict';");
		lines.add("");
		lines.add("var MessageKeys = {");
		lines.addAll(constMap.entrySet().stream().map(entry -> "\t" + entry.getKey() + ": \"" + entry.getValue() + "\",").collect(Collectors.toList()));
		lines.add("}");

		Files.write(path, lines, StandardCharsets.UTF_8);
	}
}
