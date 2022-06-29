package me.markings.bubble.settings;

import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Broadcasts extends YamlConfig {

	private static final ConfigItems<Broadcasts> broadcasts = ConfigItems.fromFolder("broadcasts", Broadcasts.class);

	@Getter
	private final String broadcastName;

	private List<String> message;
	private String permission;
	private Boolean centered;
	private List<String> worlds;

	@SuppressWarnings("unused")
	private Broadcasts(final String broadcastName) {
		this(broadcastName, null, null, null, null);
	}

	private Broadcasts(final String broadcastName, @Nullable final List<String> message, @Nullable final String permission, @Nullable final Boolean centered, @Nullable final List<String> worlds) {
		this.broadcastName = broadcastName;
		this.message = message;
		this.permission = permission;
		this.centered = centered;
		this.worlds = worlds;

		this.setHeader(
				Common.configLine(),
				"Broadcast Settings",
				Common.configLine(),
				"\n"
		);

		this.loadConfiguration(NO_DEFAULT, "broadcasts/" + broadcastName + ".yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		// Only load if not created from a command.
		if (this.message != null && this.permission != null && this.centered != null && this.worlds != null) {
			this.save();

			return;
		}

		this.permission = getString("Permission");
		this.centered = getBoolean("Centered");
		this.message = getStringList("Message");
		this.worlds = getStringList("Worlds");
	}

	@Override
	protected void onSave() {
		super.onSave();
	}

	@Override
	public SerializedMap saveToMap() {
		final SerializedMap map = new SerializedMap();

		map.putIfExist("Permission", this.permission);
		map.putIfExist("Centered", this.centered);
		map.putIfExist("Message", this.message);
		map.putIfExist("Worlds", this.worlds);

		return map;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof Broadcasts && ((Broadcasts) obj).getBroadcastName().equals(this.getBroadcastName());
	}

	public List<String> getMessage() {
		return this.message;
	}

	public String getPermission() {
		return this.permission;
	}

	public List<String> getWorlds() {
		return this.worlds;
	}

	public Boolean getCentered() {
		return this.centered;
	}

	public void setMessage(final List<String> message) {
		this.message = message;

		this.save();
	}

	public void setCentered(final Boolean centered) {
		this.centered = centered;

		this.save();
	}

	public void setPermission(final String permission) {
		this.permission = permission;

		this.save();
	}

	public void setWorlds(final List<String> worlds) {
		this.worlds = worlds;

		this.save();
	}

	public void addLineToBroadcast(final String message) {
		val broadcast = broadcasts.findItem(broadcastName);
		if (broadcast == null) {
			createBroadcast(broadcastName, Collections.singletonList(message), "bubble.vip", false, Collections.singletonList("world"));
			return;
		}

		if (broadcast.getMessage() == null) {
			broadcast.setMessage(new ArrayList<>());
		}

		this.message.add(message);
		this.save();
	}

	public void removeLineFromBroadcast(final int index) {
		val broadcast = broadcasts.findItem(broadcastName);
		if (broadcast == null) {
			createBroadcast(broadcastName, Collections.singletonList(""), "bubble.vip", false, Collections.singletonList("world"));
			return;
		}

		if (broadcast.getMessage() == null) {
			broadcast.setMessage(new ArrayList<>());
		}

		broadcast.getMessage().remove(index - 1);
		this.save();
	}

	public void toggleCentered() {
		val broadcast = broadcasts.findItem(broadcastName);
		if (broadcast == null) {
			createBroadcast(broadcastName, Collections.singletonList(""), "bubble.vip", false, Collections.singletonList("world"));
			return;
		}

		if (broadcast.getCentered() == null) {
			broadcast.setCentered(false);
		}

		broadcast.setCentered(!broadcast.getCentered());
		this.save();
	}

	public static void loadBroadcasts() {
		broadcasts.loadItems();
	}

	// ---------------------------------------------------------------------------------------------------------------------
	// Static Methods
	// ---------------------------------------------------------------------------------------------------------------------

	public static void createBroadcast(@NonNull final String broadcastName, @NonNull final List<String> messages, @NonNull final String permissions, @NonNull final Boolean centered, @NonNull final List<String> worlds) {
		broadcasts.loadOrCreateItem(broadcastName, () -> new Broadcasts(broadcastName, messages, permissions, centered, worlds));
	}

	public static void toggleCenteredAll() {
		for (val broadcast : broadcasts.getItems()) {
			broadcast.setCentered(Boolean.FALSE.equals(broadcast.getCentered()));
		}
	}

	public static void removeBroadcast(@NonNull final Broadcasts broadcast) {
		broadcasts.removeItem(broadcast);
	}

	public static Collection<Broadcasts> getAllBroadcasts() {
		return broadcasts.getItems();
	}

	public static Collection<String> getAllBroadcastNames() {
		return broadcasts.getItemNames();
	}

	public static Broadcasts getBroadcast(@NonNull final String broadcastName) {
		return broadcasts.findItem(broadcastName);
	}

	public static List<List<String>> getAllMessages() {
		val allMessages = new ArrayList<List<String>>();
		broadcasts.getItems().forEach(broadcast -> allMessages.add(broadcast.getMessage()));

		return allMessages;
	}

	public static List<String> getAllWorlds() {
		val allWorlds = new ArrayList<String>();
		for (final Broadcasts broadcast : broadcasts.getItems()) {
			assert broadcast.getWorlds() != null;
			for (final String world : broadcast.getWorlds())
				if (!allWorlds.contains(world))
					allWorlds.add(world);
		}

		return allWorlds;
	}


	public static List<String> getAllPermissions() {
		val allPermissions = new ArrayList<String>();
		broadcasts.getItems().forEach(broadcast -> allPermissions.add(broadcast.getPermission()));

		return allPermissions;
	}

	public static List<Boolean> getAllCentered() {
		val allCentered = new ArrayList<Boolean>();
		broadcasts.getItems().forEach(broadcast -> allCentered.add(broadcast.getCentered()));

		return allCentered;
	}

	public static String getPermissionFromMessage(@NonNull final List<String> messages) {
		for (final Broadcasts broadcast : getAllBroadcasts()) {
			assert broadcast.getMessage() != null;
			if (broadcast.getMessage().equals(messages))
				return broadcast.getPermission();
		}

		return null;
	}

	public static Boolean getCenteredFromMessage(@NonNull final List<String> messages) {
		for (final Broadcasts broadcast : getAllBroadcasts()) {
			assert broadcast.getMessage() != null;
			if (broadcast.getMessage().equals(messages))
				return broadcast.getCentered();
		}

		return null;
	}
}

