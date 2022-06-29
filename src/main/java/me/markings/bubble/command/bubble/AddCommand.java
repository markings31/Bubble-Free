package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends SimpleSubCommand {

	public AddCommand() {
		super("add");

		setMinArguments(2);
		setDescription("Add a line the given broadcast label.");
		setUsage("<label> [message]");
		setPermission(Permissions.Command.ADD);
	}

	@Override
	protected void onCommand() {
		val label = args[0];
		val message = !args[1].isEmpty() ? joinArgs(1) : "";

		checkBoolean(Broadcasts.getBroadcast(label) != null, "&cNo such section " + args[0] + " found!");

		val broadcast = Broadcasts.getBroadcast(label);
		broadcast.addLineToBroadcast(message);

		tellSuccess("&aSuccessfully added line to broadcast " + label + "!");
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Broadcasts.getAllBroadcastNames());

		return new ArrayList<>();
	}
}
