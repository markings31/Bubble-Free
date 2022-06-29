package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.ChatColor;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.ChatPaginator;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ShowCommand extends SimpleSubCommand {

	private int messagesLength = 1;

	protected ShowCommand() {
		super("show");

		setDescription("Display a preview of the given broadcast message.");
		setPermission(Permissions.Command.SHOW);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		val page = new ChatPaginator(maximumLength() + 6, ChatColor.BLUE);

		page.setHeader("&9" + Common.chatLineSmooth() + "&r", ChatUtil.center("&3&l&nInformation&r"), "&f");
		page.setPages(list()).send(getPlayer());
	}

	private int maximumLength() {
		val labels = Broadcasts.getAllBroadcastNames();

		for (int i = 0; i < labels.size(); i++) {
			val currentLabel = (String) labels.toArray()[i];
			val messageList = Broadcasts.getBroadcast(currentLabel).getMessage();
			if (messageList.size() > messagesLength)
				messagesLength = messageList.size();
		}

		return messagesLength + 3;
	}

	private List<SimpleComponent> list() {
		final List<SimpleComponent> messages = new ArrayList<>();
		val labels = Broadcasts.getAllBroadcastNames();

		for (int i = 0; i < labels.size(); i++) {
			val currentLabel = (String) labels.toArray()[i];
			val broadcast = Broadcasts.getBroadcast(currentLabel);
			val messageList = broadcast.getMessage();

			Stream.of(
					"&7- &b&lLabel: &f" + currentLabel,
					"&7- &b&lPermission: &f" + broadcast.getPermission(),
					"&7- &b&lCentered: &f" + (Settings.BroadcastSettings.CENTER_ALL || broadcast.getCentered()),
					"&f",
					"&b&lMessage:",
					"&f").map(SimpleComponent::of).forEach(messages::add);

			messageList.forEach(message -> messages.add(SimpleComponent.of(MessageUtil.format(message))));

			messages.addAll(Arrays.asList(
					SimpleComponent.of("&f"),
					SimpleComponent.of("&f"),
					SimpleComponent.of(ChatUtil.center("&7&oClick to Edit Message"))
							.onClickRunCmd("/bubble edit " + currentLabel)));
		}

		return messages;
	}
}
