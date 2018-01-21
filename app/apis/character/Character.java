package apis.character;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import apis.character.Variation.Collection;
import common.system.MessageKeys;
import models.system.System.PermissionsAllowed;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

@PermissionsAllowed
public class Character extends Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	private MessagesApi messages;

	private static class LazyHolder {

		private static Map<Integer, BaseCharacter> CODE_POINT_TO_CHARACTER_MAP = initCodePointToCharacterMap();
	}

	public static Map<Integer, BaseCharacter> getCodePointToCharacterMap() {

		return LazyHolder.CODE_POINT_TO_CHARACTER_MAP;
	}

	private static final Map<Integer, ObjectNode> CODE_POINT_TO_JSON_OBJECT_MAP = new HashMap<>();

	private static Map<Integer, BaseCharacter> initCodePointToCharacterMap() {

		final Path IVD_SequencesPath = Paths.get(".", "conf", "resources", "character", "IVD_Sequences.txt");
		if (!Files.exists(IVD_SequencesPath)) {

			throw new RuntimeException("Not exists IVD_Sequences. : " + IVD_SequencesPath);
		}

		List<String> lines;
		try {

			lines = Files.readAllLines(IVD_SequencesPath, StandardCharsets.UTF_8);
		} catch (IOException e) {

			throw new RuntimeException(" : " + IVD_SequencesPath, e);
		}

		final Map<Integer, BaseCharacter> codePointToCharacterMap = new HashMap<>();
		lines.stream().filter(line -> !line.startsWith("#")).forEach(line -> {

			final String[] strings = line.split("\\s|;\\s");
			final int codePoint = Integer.parseInt(strings[0], 16);
			final int ivsCodePoint = Integer.parseInt(strings[1], 16);
			final String ivdCollectionName = strings[2].trim();
			final String ivdCollectionCharacterId = strings[3].trim();

			final String s = new String(new int[] { codePoint }, 0, 1);
			final String ivd = new String(new int[] { codePoint, ivsCodePoint }, 0, 2);
			LOGGER.info(s + " / " + ivd + " / " + codePoint + " / " + ivsCodePoint + " / " + ivdCollectionName + " / " + ivdCollectionCharacterId);

			if (!codePointToCharacterMap.containsKey(codePoint)) {

				codePointToCharacterMap.put(codePoint, new BaseCharacter(codePoint));
			}
			final BaseCharacter information = codePointToCharacterMap.get(codePoint);

			if (!information.containsKey(ivsCodePoint)) {

				information.addVariation(new Variation(ivsCodePoint));
			}
			final Variation variation = information.get(ivsCodePoint);
			variation.addCollection(new Collection(Collection.Type.collectionOf(ivdCollectionName), ivdCollectionCharacterId));
		});

		return codePointToCharacterMap;
	}

	public enum Type {
		CHARACTER, CODEPOINT, HEX;

		public String getMessageKey() {

			switch (this) {
			case CHARACTER:

				return MessageKeys.CHARACTER;
			case CODEPOINT:

				return MessageKeys.CODEPOINT;
			case HEX:

				return MessageKeys.HEX;
			default:

				throw new IllegalStateException(this.name());
			}
		}
	}

	@Authenticated(common.core.Authenticator.class)
	@RequireCSRFCheck
	public Result character(@Nonnull final String typeString, @Nonnull final String searchText) {

		final Type type = Type.valueOf(typeString);
		switch (type) {
		case CHARACTER:

			return character(searchText);
		case CODEPOINT:

			final int codePoint;
			try {

				codePoint = Integer.valueOf(searchText);
			} catch (NumberFormatException e) {

				return badRequest(createErrorResult(messages.get(lang(), MessageKeys.ERROR_NUMBER) + " :" + searchText));
			}

			return codePoint(codePoint);
		case HEX:

			return hex(searchText);
		default:

			throw new RuntimeException();
		}
	}

	private Result character(@Nonnull final String character) {

		if (1 != character.codePointCount(0, character.length())) {

			return badRequest(createErrorResult("Character is not 1 code point. :" + character));
		}

		final int codePoint = character.codePointAt(0);
		return codePoint(codePoint);
	}

	private Result codePoint(final int codePoint) {

		if (!java.lang.Character.isValidCodePoint(codePoint)) {

			return badRequest(createErrorResult("CodePoint is not valid. :" + codePoint));
		}

		final Map<Integer, BaseCharacter> codePointToCharacterMap = getCodePointToCharacterMap();

		if (!CODE_POINT_TO_JSON_OBJECT_MAP.containsKey(codePoint)) {

			if (!codePointToCharacterMap.containsKey(codePoint)) {

				codePointToCharacterMap.put(codePoint, new BaseCharacter(codePoint));
			}

			final BaseCharacter character = codePointToCharacterMap.get(codePoint);
			final ObjectNode characterObject = character.toObjectNode();
			CODE_POINT_TO_JSON_OBJECT_MAP.put(codePoint, characterObject);
		}

		return ok(CODE_POINT_TO_JSON_OBJECT_MAP.get(codePoint));
	}

	private Result hex(@Nonnull final String hex) {

		final int codePoint;
		try {

			codePoint = Integer.valueOf(hex, 16);
		} catch (NumberFormatException e) {

			return badRequest(createErrorResult(e.getLocalizedMessage() + " :" + hex));
		}

		return codePoint(codePoint);
	}

	private JsonNode createErrorResult(@Nonnull final String errorMessage) {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("error", errorMessage);

		return result;
	}
}
