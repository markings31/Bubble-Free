package me.markings.bubble.command.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.menu.EditMenu;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class EditCommand extends SimpleSubCommand {

	@Getter
	public static String input;

	protected EditCommand() {
		super("edit");

		setMinArguments(1);
		setDescription("Edit the contents of each broadcast message.");
		setUsage("<label>");
		setPermission(Permissions.Command.EDIT);
	}

	@Override
	protected void onCommand() {
		val label = args[0];

		checkBoolean(Broadcasts.getBroadcast(label) != null, "&cNo such section " + args[0] + " found!");

		input = label;

		new EditMenu().displayTo(getPlayer());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Broadcasts.getAllBroadcastNames());

		return new ArrayList<>();
	}
}
