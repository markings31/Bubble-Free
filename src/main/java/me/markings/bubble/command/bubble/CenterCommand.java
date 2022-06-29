package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class CenterCommand extends SimpleSubCommand {

	protected CenterCommand() {
		super("center");

		setMinArguments(1);
		setDescription("Toggle automatic centering for broadcast messages.");
		setUsage("<label/all>");
		setPermission(Permissions.Command.CENTER);
	}

	@Override
	protected void onCommand() {
		val label = args[0];

		if (args[0].equalsIgnoreCase("all")) {
			Broadcasts.toggleCenteredAll();
		} else {
			Broadcasts.getBroadcast(label).toggleCentered();
		}

		tellSuccess("&aSuccessfully toggled centering for broadcast " + label + "!");
	}

	@Override
	protected String[] getMultilineUsageMessage() {
		val commandLabel = "&f/bu " + getSublabel();
		return new String[]{
				commandLabel + " <label>&7 - Centers the messages contained in the 'label' section.",
				commandLabel + " all&7 - Centers all messages across different sections.",
				"&f",
				"&c&lNOTE:&c 'label' refers to the name of the broadcast section."
		};
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Broadcasts.getAllBroadcastNames(), "all");

		return new ArrayList<>();
	}
}
