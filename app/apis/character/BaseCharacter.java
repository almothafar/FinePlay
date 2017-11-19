package apis.character;

import java.lang.Character.UnicodeBlock;
import java.lang.Character.UnicodeScript;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

class BaseCharacter {

	private final int codePoint;
	private final String string;

	private final SortedMap<Integer, Variation> ivsCodePointToVariationMap = new TreeMap<>();

	BaseCharacter(final int codePoint) {

		this.codePoint = codePoint;
		this.string = new String(new int[] { codePoint }, 0, 1);
	}

	int getCodePoint() {

		return codePoint;
	}

	String getString() {

		return string;
	}

	UnicodeBlock getUnicodeBlock() {

		return UnicodeBlock.of(this.getCodePoint());
	}

	UnicodeScript getUnicodeScript() {

		return UnicodeScript.of(this.getCodePoint());
	}

	Variation addVariation(@Nonnull final Variation variation) {

		return ivsCodePointToVariationMap.put(variation.getCodePoint(), variation);
	}

	boolean containsKey(final int ivsCodePoint) {

		return ivsCodePointToVariationMap.containsKey(ivsCodePoint);
	}

	Variation get(final int ivsCodePoint) {

		return ivsCodePointToVariationMap.get(ivsCodePoint);
	}

	ObjectNode toObjectNode() {

		final ObjectMapper mapper = new ObjectMapper();
		final ObjectNode result = mapper.createObjectNode();

		result.put("codePoint", getCodePoint());
		result.put("hex", Integer.toHexString(getCodePoint()).toUpperCase());
		final String baseString = getString();
		result.put("character", baseString);
		result.put("block", getUnicodeBlock().toString());
		result.put("script", getUnicodeScript().toString());

		final ArrayNode variationsNode = result.putArray("variations");
		ivsCodePointToVariationMap.values().forEach(variation -> {

			final ObjectNode variationNode = variationsNode.addObject();

			variationNode.put("codePoint", variation.getCodePoint());
			variationNode.put("hex", Integer.toHexString(variation.getCodePoint()).toUpperCase());
			variationNode.put("character", variation.getString());
			variationNode.put("block", getUnicodeBlock().toString());
			variationNode.put("script", getUnicodeScript().toString());

			final ArrayNode collectionsNode = variationNode.putArray("collections");
			variation.getCollections().forEach(collection -> {

				final ObjectNode collectionNode = collectionsNode.addObject();
				collectionNode.put("name", collection.getType().getName());
				collectionNode.put("id", collection.getId());
			});
		});

		return result;
	}
}
