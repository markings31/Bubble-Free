package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.Localization;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionPrompt extends SimplePrompt {

	@Getter
	private static final PermissionPrompt instance = new PermissionPrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle(getPlayer(context), "&9Set Permission", "Please type your message in the chat.");
		return Localization.PromptMessages.PERMISSION_PROMPT_MESSAGE;
	}

	@Override
	protected String getCustomPrefix() {
		return "";
	}

	@Override
	protected boolean isInputValid(final ConversationContext context, final String input) {
		return !Valid.isNumber(input);
	}

	@Override
	protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
		return Messenger.getErrorPrefix() + "Please write the permission as a String text (Example: bubble.vip).";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		Broadcasts.getBroadcast(EditCommand.getInput()).setPermission(input);
		Messenger.success(getPlayer(context), "Successfully set permission to " + input + "!");

		return Prompt.END_OF_CONVERSATION;
	}
}
