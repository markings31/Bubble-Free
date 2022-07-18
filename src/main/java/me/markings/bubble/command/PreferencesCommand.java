package me.markings.bubble.command;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.MenuData;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

@AutoRegister
public final class PreferencesCommand extends SimpleCommand {

	public PreferencesCommand() {
		super("notifications|notifprefs|alerts");

		setDescription("Customize your notification preferences.");
		setPermission(Permissions.Command.PREFS);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		val menuName = MenuData.getMenuNames().toArray()[0].toString();
		val menu = MenuData.findMenu(menuName);

		menu.displayTo(getPlayer());
	}
}
