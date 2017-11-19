package common.tools;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import play.test.WSTestClient;

class LibraryUpdateChecker {

	private static WSClient ws = WSTestClient.newClient(-1);

	private static final Pattern PATTERN_SBT_LIB = Pattern.compile(".*?\"(.*?)?\".*?\"(.*?)?\".*?\"(.*?)?\".*");

	public static void main(String[] args) throws Exception {

		final List<String> lines = Files.readAllLines(Paths.get("build.sbt"), StandardCharsets.UTF_8);

		@SuppressWarnings("null")
		final List<Artifact> artifacts = toArtifacts(lines);

		getRepositoryArtifacts(artifacts);

		artifacts.stream().sorted().forEach(artifact -> {

			System.out.print(artifact.isAvailable() ? "* " : "  ");
			System.out.print(StringUtils.rightPad(artifact.toString(), 100));
			System.out.print(" -> ");

			artifact.getRepositoryArtifacts().forEach(repositoryArtifact -> {

				System.out.print(repositoryArtifact.isAvailable() ? "  " : "! ");
				System.out.println(repositoryArtifact.toString());
			});
		});

		System.exit(0);
	}

	// @SuppressWarnings("null")
	@SuppressWarnings("null")
	private static @Nonnull List<Artifact> toArtifacts(@Nonnull final List<String> lines) {

		final List<Artifact> artifacts = new ArrayList<>();
		boolean isStart = false;
		for (final String line : lines) {

			if (line.startsWith("libraryDependencies ++= Seq(")) {

				isStart = true;
				continue;
			} else if (line.startsWith(")")) {

				break;
			}

			if (!isStart)
				continue;

			if (!line.contains("%"))
				continue;

			if (line.contains("%%"))
				continue;

			final Matcher matcher = PATTERN_SBT_LIB.matcher(line);
			if (!matcher.matches()) {

				throw new IllegalStateException("");
			}
			final String groupId = matcher.group(1);
			final String artifactId = matcher.group(2);
			final String version = matcher.group(3);
			final Artifact artifact = new Artifact(groupId, artifactId, version);

			boolean isAvailable = !line.startsWith("//");
			artifact.setAvailable(isAvailable);

			artifacts.add(artifact);
		}

		return Collections.unmodifiableList(artifacts);
	}

	private static void getRepositoryArtifacts(@Nonnull final List<Artifact> artifacts) {

		artifacts.forEach(artifact -> {

			@SuppressWarnings("null")
			final List<Artifact> repositoryArtifacts = getRepositoryArtifacts(artifact);
			artifact.setRepositoryArtifacts(repositoryArtifacts);
		});
	}

	@SuppressWarnings("null")
	private static @Nonnull List<Artifact> getRepositoryArtifacts(@Nonnull final Artifact artifact) {

		System.out.println("Connect... " + artifact.toString());

		final Duration timeout = Duration.ofSeconds(10);

		final WSRequest wsRequest = ws.url("http://search.maven.org/solrsearch/select");
		wsRequest.addQueryParameter("q", "g:\"" + artifact.getGroupId() + "\" AND a:\"" + artifact.getArtifactId() + "\"");
		wsRequest.addQueryParameter("rows", "20");
		wsRequest.addQueryParameter("wt", "json");

		final CompletionStage<WSResponse> responsePromise = wsRequest.setRequestTimeout(timeout).get();

		final CompletionStage<List<Artifact>> recoverPromise = responsePromise.handle((response, throwable) -> {

			final List<Artifact> repositoryArtifacts = new ArrayList<>();
			if (throwable == null) {

				if (Http.Status.OK == response.getStatus()) {

					final JsonNode responseJson = response.asJson();
					final JsonNode docsJson = responseJson.get("response").get("docs");
					for (int i = 0; i < docsJson.size(); i++) {

						final JsonNode doc = docsJson.get(i);
						final String groupId = doc.get("g").textValue();
						final String artifactId = doc.get("a").textValue();
						final String version = doc.get("latestVersion").textValue();
						final Artifact repositoryArtifact = new Artifact(groupId, artifactId, version);
						repositoryArtifacts.add(repositoryArtifact);
					}
				} else {

					throw new RuntimeException("HTTP Status is not OK. :" + response.getStatus());
				}
			} else {

				throw new RuntimeException(throwable.getLocalizedMessage());
			}

			return Collections.unmodifiableList(repositoryArtifacts);
		});

		final List<Artifact> repositoryArtifacts;
		try {

			repositoryArtifacts = recoverPromise.toCompletableFuture().get();
		} catch (InterruptedException | ExecutionException e) {

			throw new RuntimeException(e);
		}

		return repositoryArtifacts;
	}

	private static class Artifact implements Comparable<Artifact> {

		private final String groupId;
		private final String artifactId;
		private final String version;

		private boolean available;

		private List<Artifact> repositoryArtifacts;

		public Artifact(@Nonnull final String groupId, @Nonnull final String artifactId, @Nonnull final String version) {

			this.groupId = groupId;
			this.artifactId = artifactId;
			this.version = version;
		}

		public String getGroupId() {
			return groupId;
		}

		public String getArtifactId() {

			return artifactId;
		}

		public String getVersion() {

			return version;
		}

		public boolean isAvailable() {

			return available;
		}

		public void setAvailable(boolean available) {

			this.available = available;
		}

		public List<Artifact> getRepositoryArtifacts() {

			return repositoryArtifacts;
		}

		public void setRepositoryArtifacts(List<Artifact> repositoryArtifacts) {

			repositoryArtifacts.parallelStream().forEach(repositoryArtifact -> {

				if (repositoryArtifact.equals(this)) {

					repositoryArtifact.setAvailable(this.isAvailable());
				}
			});

			this.repositoryArtifacts = repositoryArtifacts;
		}

		@Override
		public int compareTo(Artifact a) {

			final int groupIdResult = getGroupId().compareTo(a.getGroupId());
			if (0 != groupIdResult) {

				return groupIdResult;
			} else {

				final int artifactIdResult = getArtifactId().compareTo(a.getArtifactId());
				if (0 != artifactIdResult) {

					return artifactIdResult;
				} else {

					final int versionResult = getVersion().compareTo(a.getVersion());
					return versionResult;
				}
			}
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;
			result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
			result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
			result = prime * result + ((version == null) ? 0 : version.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Artifact other = (Artifact) obj;
			if (artifactId == null) {
				if (other.artifactId != null)
					return false;
			} else if (!artifactId.equals(other.artifactId))
				return false;
			if (groupId == null) {
				if (other.groupId != null)
					return false;
			} else if (!groupId.equals(other.groupId))
				return false;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}

		@Override
		public String toString() {

			return "  \"" + getGroupId() + "\" % \"" + getArtifactId() + "\" % \"" + getVersion() + "\",";
		}
	}
}
