package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand extends SimpleSubCommand {

	protected RemoveCommand() {
		super("remove");

		setMinArguments(2);
		setDescription("Remove a broadcast section or message.");
		setUsage("<label> <line_number>");
		setPermission(Permissions.Command.REMOVE);
	}

	@Override
	protected void onCommand() {
		val label = args[0];
		val lineNumber = args[1];

		checkBoolean(Valid.isInteger(lineNumber), "Line number must be an integer!");

		Broadcasts.getBroadcast(label).removeLineFromBroadcast(Integer.parseInt(lineNumber));
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Broadcasts.getAllBroadcastNames());

		return new ArrayList<>();
	}
}
