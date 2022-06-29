package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.Localization;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectLabelPrompt extends SimplePrompt {

	@Getter
	private static final SelectLabelPrompt instance = new SelectLabelPrompt();

	@Override
	protected String getPrompt(final ConversationContext conversationContext) {
		Remain.sendTitle((Player) conversationContext.getForWhom(), "&9Edit Message", "Please type your message in the chat.");
		return Localization.PromptMessages.LABEL_PROMPT_MESSAGE;
	}

	@Override
	protected String getCustomPrefix() {
		return "";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {

		if (Broadcasts.getBroadcast(s) == null)
			Messenger.error((CommandSender) conversationContext.getForWhom(), "&cCould not find message group " + s + "!");
		else
			Common.dispatchCommandAsPlayer((Player) conversationContext.getForWhom(), "bu edit " + s);

		return Prompt.END_OF_CONVERSATION;
	}
}
