package models.system;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class System {

	private static final Logger LOGGER = LoggerFactory.getLogger(System.class);

	public enum Permission {
		READ, WRITE, MANAGE
	}

	@Documented
	@Retention(RUNTIME)
	@Target({TYPE, METHOD})
	public @interface PermissionsAllowed {

		Permission[] value() default {Permission.READ, Permission.WRITE, Permission.MANAGE};
	}

	public static boolean isPermissionAllowed(@Nonnull final Set<Permission> permissions, @Nonnull final Set<Permission> userPermissions) {

		Objects.requireNonNull(permissions);
		Objects.requireNonNull(userPermissions);

		final Map<Permission, PermissionMatch> permissionMatchings = new LinkedHashMap<>();

		permissions.forEach(permission -> {

			if (!permissionMatchings.containsKey(permission)) {

				permissionMatchings.put(permission, new PermissionMatch());
			}
			final PermissionMatch match = permissionMatchings.get(permission);
			match.setWorkPermission(permission);
		});

		userPermissions.forEach(userPermission -> {

			if (!permissionMatchings.containsKey(userPermission)) {

				permissionMatchings.put(userPermission, new PermissionMatch());
			}
			final PermissionMatch match = permissionMatchings.get(userPermission);
			match.setUserPermission(userPermission);
		});

		LOGGER.info(PermissionMatch.toHeadString());
		LOGGER.info("|" + StringUtils.leftPad("-", 10, '-') + "|" + StringUtils.leftPad("-", 10, '-') + "|-----|");

		permissionMatchings.values().forEach(mathing -> LOGGER.info(mathing.toLineString()));

		LOGGER.info("|" + StringUtils.leftPad("-", 10, '-') + "|" + StringUtils.leftPad("-", 10, '-') + "|-----|");
		final boolean isPermissionAllowed = permissionMatchings.values().stream().anyMatch(matching -> matching.isAllowed());
		LOGGER.info(PermissionMatch.toFootString(isPermissionAllowed));
		//
		// final boolean isPermissionAllowed =
		// userPermissions.stream().anyMatch(userPermission ->
		// permissions.contains(userPermission));
		// LOGGER.info("Controller Permissions : " + permissions + " / User
		// Permissions : " + userPermissions + " " + isPermissionAllowed);

		return isPermissionAllowed;
	}

	private static class PermissionMatch {

		private Permission workPermission;
		private Permission userPermission;

		void setWorkPermission(@Nonnull final Permission workPermission) {

			this.workPermission = workPermission;
		}

		void setUserPermission(@Nonnull final Permission userPermission) {

			this.userPermission = userPermission;
		}

		boolean isAllowed() {

			return Objects.nonNull(workPermission) && Objects.nonNull(userPermission);
		}

		static String toHeadString() {

			final String head = "|" + StringUtils.rightPad("Work", 10, ' ') + "|" + StringUtils.rightPad("User", 10, ' ') + "|Allow|";
			return head;
		}

		String toLineString() {

			final String work = Objects.toString(workPermission, "");
			final String user = Objects.toString(userPermission, "");
			final String allowed = isAllowed() ? "O" : "X";

			final String line = "|" + StringUtils.rightPad(work, 10, ' ') + "|" + StringUtils.rightPad(user, 10, ' ') + "|  " + allowed + "  |";
			return line;
		}

		static String toFootString(final boolean anyAllowed) {

			final String allowed = anyAllowed ? "O" : "X";

			final String foot = "|" + StringUtils.rightPad("*", 10, ' ') + "|" + StringUtils.rightPad("*", 10, ' ') + "|  " + allowed + "  |";
			return foot;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((userPermission == null) ? 0 : userPermission.hashCode());
			result = prime * result + ((workPermission == null) ? 0 : workPermission.hashCode());
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
			PermissionMatch other = (PermissionMatch) obj;
			if (userPermission != other.userPermission)
				return false;
			if (workPermission != other.workPermission)
				return false;
			return true;
		}
	}
}
