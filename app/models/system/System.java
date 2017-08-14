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

import play.Logger;
import play.Logger.ALogger;

public class System {

	public static final ALogger LOGGER = Logger.of(System.class);

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

	public static class ColorType {

		private final Color backgroundColor;
		private final Color borderColor;

		public ColorType(final Color backgroundColor, final Color borderColor) {

			this.backgroundColor = backgroundColor;
			this.borderColor = borderColor;
		}

		public Color getBackgroundColor() {

			return backgroundColor;
		}

		public Color getBorderColor() {
			return borderColor;
		}

		public Color getActiveBackgroundColor() {

			return getBackgroundColor().toActiveBackgroundColor();
		}

		public Color getActiveBorderColor() {

			return getBorderColor().toActiveBorderColor();
		}

		public Color getDisabledBackgroundColor() {

			return getBackgroundColor().toDisabledBackgroundColor();
		}

		public Color getDisabledBorderColor() {

			return getBorderColor().toDisabledBorderColor();
		}

		public Color toFocusShadowColor() {

			return getBackgroundColor().toFocusShadowColor();
		}

		public Color getColor() {

			return getBackgroundColor().toColor();
		}
	}

	public static class Color {

		private final javafx.scene.paint.Color colorImpl;

		public Color(final String value) {

			this.colorImpl = javafx.scene.paint.Color.web(value);
		}

		public Color toActiveBackgroundColor() {

			final javafx.scene.paint.Color newColor = colorImpl.deriveColor(0, 1.0, 0.85, 1.0);
			return new Color(newColor.toString());
		}

		public Color toActiveBorderColor() {

			final javafx.scene.paint.Color newColor = colorImpl.deriveColor(0, 1.0, 0.8, 1.0);
			return new Color(newColor.toString());
		}

		public Color toColor() {

			final int r = (int) Math.round(this.colorImpl.getRed() * 255.0);
			final int g = (int) Math.round(this.colorImpl.getGreen() * 255.0);
			final int b = (int) Math.round(this.colorImpl.getBlue() * 255.0);

			final int yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000;
			if (yiq >= 150) {

				return new Color("#111");
			} else {

				return new Color(javafx.scene.paint.Color.WHITE.toString());
			}
		}

		public Color toFocusShadowColor() {

			final javafx.scene.paint.Color newColor = colorImpl.deriveColor(0, 1.0, 1.0, 0.5);
			return new Color(newColor.toString());
		}

		public Color toDisabledBackgroundColor() {

			final javafx.scene.paint.Color newColor = colorImpl.deriveColor(0, 1.0, 1.0, 0.5);
			return new Color(newColor.toString());
		}

		public Color toDisabledBorderColor() {

			final javafx.scene.paint.Color newColor = colorImpl.deriveColor(0, 1.0, 1.0, 0.5);
			return new Color(newColor.toString());
		}

		public String toHex() {

			final int r = (int) Math.round(this.colorImpl.getRed() * 255.0);
			final int g = (int) Math.round(this.colorImpl.getGreen() * 255.0);
			final int b = (int) Math.round(this.colorImpl.getBlue() * 255.0);
			return String.format("#%02x%02x%02x", r, g, b);
		}

		public String toHex3() {

			final int r = (int) Math.round(this.colorImpl.getRed() * 15.0);
			final int g = (int) Math.round(this.colorImpl.getGreen() * 15.0);
			final int b = (int) Math.round(this.colorImpl.getBlue() * 15.0);
			return String.format("#%1x%1x%1x", r, g, b);
		}

		public String toRgba() {

			final int r = (int) Math.round(this.colorImpl.getRed() * 255.0);
			final int g = (int) Math.round(this.colorImpl.getGreen() * 255.0);
			final int b = (int) Math.round(this.colorImpl.getBlue() * 255.0);
			final double a = this.colorImpl.getOpacity();
			return String.format("rgba(%d, %d, %d, %.1f)", r, g, b, a);
		}
	}
}
