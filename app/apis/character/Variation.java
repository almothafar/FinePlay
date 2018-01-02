package apis.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;

class Variation extends BaseCharacter {

	private final List<Collection> collections = new ArrayList<>();

	Variation(final int codePoint) {

		super(codePoint);
	}

	void addCollection(@Nonnull final Collection collection) {

		collections.add(collection);
	}

	List<Collection> getCollections() {

		return collections;
	}

	static class Collection {

		enum Type {
			ADOBE_JAPAN1("Adobe-Japan1"), //
			HANYO_DENSHI("Hanyo-Denshi"), //
			MOJI_JOHO("Moji_Joho"), //
			MSARG("MSARG"), //
			KRNAME("KRName");

			private final String name;

			private Type(@Nonnull final String name) {

				this.name = name;
			}

			String getName() {

				return this.name;
			}

			static Type collectionOf(@Nonnull final String name) {

				final Type collection;
				try {

					collection = Arrays.asList(values()).stream().filter(c -> name.equals(c.getName())).findFirst().get();
				} catch (NoSuchElementException e) {

					throw new IllegalArgumentException(" :" + name);
				}

				return collection;
			}
		}

		private final String id;
		private final Collection.Type type;

		Collection(@Nonnull final Collection.Type type, @Nonnull final String id) {

			this.type = type;
			this.id = id;
		}

		Collection.Type getType() {

			return type;
		}

		String getId() {

			return id;
		}
	}
}
