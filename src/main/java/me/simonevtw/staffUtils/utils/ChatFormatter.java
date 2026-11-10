package me.simonevtw.staffUtils.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class ChatFormatter {

    private static final MiniMessage sanitizer = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.decorations())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.reset())
                    .build())
            .build();

    public static Component getTpMessage(String name) {
        return deserialize("<hover:show_text:'<green>Clicca per teletrasportarti dal giocatore'><click:run_command:/tp " + name + "><green>[Clicca qui per teletrasportarti]</click></hover>");
    }

    public static Component serverMessage(String text, boolean unsafe) {
        return unsafe ? MiniMessage.miniMessage().deserialize("\uE90B " + text) : deserialize("\uE90B " + text);
    }

    public static Component serverMessage(String text) {
        return serverMessage(text, false);
    }

    public static Component deserialize(String str) {
        return sanitizer.deserialize(str)
                // buggato l'italic, non so perche' ma frega un cazzo perche' tanto e' brutto anyway
                .style(style -> style.decoration(TextDecoration.ITALIC, false));
    }

}
