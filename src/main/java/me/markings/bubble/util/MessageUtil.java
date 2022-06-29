package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.remain.CompChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

	@Getter
	private static final String commandPlaceholder = "<command>";

	@Getter
	private static final String messageArg = "message";

	@Getter
	private static final String titleArg = "title";

	@Getter
	private static final String bossbarArg = "bossbar";

	@Getter
	private static final String actionbarArg = "actionbar";

	@Getter
	private static final String toastArg = "toast";

	private static final String titlePlaceholder = "<title>";
	private static final String actionbarPlaceholder = "<actionbar>";
	private static final String bossbarPlaceholder = "<bossbar>";
	private static final String toastPlaceholder = "<toast>";
	private static final String animatePlaceholder = "{animate:";
	private static final String scrollPlaceholder = "{scroll:";
	private static final String flashPlaceholder = "{flash:";
	//private static final String scrollingGradientPlaceholder = "{g:";

	private static final String gradientPattern = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3}):([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

	public static String format(final String message) {
		val fancyLinePlaceholder = "{fancy_line}";
		if (message.contains(fancyLinePlaceholder))
			return message.replace(fancyLinePlaceholder, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

		return message;
	}

	public static List<String> getTitleFrames(final String message) {
		final char[] msgArray = message.toCharArray();
		final ArrayList<String> frames;
		final ArrayList<Integer> indicies;
		val hasPeriod = getPeriod(message) != -1;
		indicies = IntStream
				.range(0, msgArray.length).filter(i -> msgArray[i] == ':' && msgArray[i - 1] != 't')
				.boxed()
				.collect(Collectors.toCollection(ArrayList::new));

		frames = IntStream
				.range(0, (hasPeriod ? indicies.size() - 1 : indicies.size())).
				mapToObj(i -> message.substring(indicies.get(i) + 1, i == indicies.size() - 1 ? message.indexOf('}') : indicies.get(i + 1)))
				.collect(Collectors.toCollection(ArrayList::new));

		return frames;
	}

	/*public static List<String> getGradientFrames(final String message, final CompChatColor firstColor, final CompChatColor lastColor) {
		final char[] msgArray = ChatUtil.generateGradient(message, firstColor, lastColor).toCharArray();
		final ArrayList<String> frames = new ArrayList<>();
		for (int i = 0; i < msgArray.length; i++)
			frames.add(String.valueOf(msgArray[i]));

		return frames;
	}*/

	public static int getPeriod(final String message) {
		val msgArray = message.toCharArray();
		for (int i = message.length() - 1; i > 0; i--)
			if (msgArray[i] == ':') {
				val period = message.substring(i + 1, message.indexOf('}'));
				return Valid.isInteger(period) ? Integer.parseInt(period) : -1;
			}

		return 10;
	}

	public static List<CompChatColor> getColors(final String message) {
		final ArrayList<CompChatColor> colors = new ArrayList<>();
		val colonIndex = message.indexOf(':');

		colors.add(CompChatColor.of(message.substring(colonIndex - 7, colonIndex - 1)));
		colors.add(CompChatColor.of(message.substring(colonIndex + 1, colonIndex + 7)));

		return colors;
	}

	public static String getLastMessage(final String message) {
		val msgArray = message.toCharArray();
		final List<Integer> indicies = new ArrayList<>();
		for (int i = message.length() - 1; i > 0; i--)
			if (msgArray[i] == ':')
				indicies.add(i);

		return message.substring(indicies.get(1) + 1, indicies.get(0));
	}

	public static String translateGradient(final String message) {
		val newMessage = stripPlaceholders(Common.revertColorizing(message));

		if (containsGradient(newMessage)) {
			val firstColor = "#" + newMessage.substring(newMessage.indexOf(":") - 6, newMessage.indexOf(":"));
			val secondColor = "#" + newMessage.substring(newMessage.indexOf(":") + 1, newMessage.indexOf(":") + 7);
			val previousMessage = newMessage.substring(0, newMessage.indexOf(":") - 6);
			val followingMessage = newMessage.substring(newMessage.indexOf(":") + 7);

			return getPlaceholder(message) + previousMessage + ChatUtil.generateGradient(followingMessage,
					CompChatColor.of(firstColor), CompChatColor.of(secondColor)) + "&r";
		}

		return message;
	}

	public static void executePlaceholders(final String message, final Player player) {
		List<CompChatColor> colors = new ArrayList<>();
		int period = 0;
		CompChatColor[] colorArr = new CompChatColor[3];

		if (message.startsWith(animatePlaceholder) || message.startsWith(scrollPlaceholder) || message.startsWith(flashPlaceholder)) {
			colors = getColors(message);
			period = getPeriod(message) != -1 ? getPeriod(message) : 10;
			colorArr = colors.toArray(colorArr);
		}

		if (message.startsWith(animatePlaceholder))
			AnimationUtil.animateTitle(player, getTitleFrames(message), null, period);

		if (message.startsWith(scrollPlaceholder))
			AnimationUtil.animateTitle(player, AnimationUtil.leftToRightFull(getLastMessage(message), colors.get(0), colors.get(1), colors.get(2)), null, period);

		if (message.startsWith(flashPlaceholder))
			AnimationUtil.animateTitle(player, AnimationUtil.flicker(getLastMessage(message), period, 2, colorArr), null, period);

		/*if (message.startsWith(scrollingGradientPlaceholder))
			AnimationUtil.animateTitle(player, getGradientFrames(getLastMessage(message),
							CompChatColor.of(String.valueOf(colors.get(0)).replace("§", "&")),
							CompChatColor.of(String.valueOf(colors.get(1)).replace("§", "&"))),
					null, period);*/

		if (message.startsWith(commandPlaceholder))
			Common.dispatchCommand(player, message.replace(commandPlaceholder, ""));

		if (message.startsWith(titlePlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " title " + message.replace(titlePlaceholder, ""));

		if (message.startsWith(actionbarPlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " actionbar " + message.replace(actionbarPlaceholder, ""));

		if (message.startsWith(bossbarPlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " bossbar " + message.replace(bossbarPlaceholder, ""));

		if (message.startsWith(toastPlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " toast " + message.replace(toastPlaceholder, ""));
	}

	public static boolean isExecutable(final String message) {
		return message.startsWith(titlePlaceholder)
				|| message.startsWith(actionbarPlaceholder)
				|| message.startsWith(bossbarPlaceholder)
				|| message.startsWith(toastPlaceholder)
				|| message.startsWith(commandPlaceholder)
				|| message.startsWith(animatePlaceholder)
				|| message.startsWith(scrollPlaceholder)
				|| message.startsWith(flashPlaceholder);
	}

	public static String getPlaceholder(final String message) {
		if (message.contains(titlePlaceholder))
			return titlePlaceholder;
		if (message.contains(actionbarPlaceholder))
			return actionbarPlaceholder;
		if (message.contains(bossbarPlaceholder))
			return bossbarPlaceholder;
		if (message.contains(toastPlaceholder))
			return toastPlaceholder;

		return "";
	}

	public static String stripPlaceholders(final String message) {
		if (message.contains(titlePlaceholder))
			return message.replace(titlePlaceholder, "");
		if (message.contains(actionbarPlaceholder))
			return message.replace(actionbarPlaceholder, "");
		if (message.contains(bossbarPlaceholder))
			return message.replace(bossbarPlaceholder, "");
		if (message.contains(toastPlaceholder))
			return message.replace(toastPlaceholder, "");

		return message;
	}

	public static boolean containsGradient(final String message) {
		final String gradientPrefix;
		if (message.contains(":")) {
			gradientPrefix = message.substring(message.indexOf(":") - 6, message.indexOf(":") + 7);

			return gradientPrefix.matches(gradientPattern);
		}

		return false;
	}
}
