package me.simonevtw.staffUtils.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class AssistenzaCommand {
    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("assistenza")
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> {
                            var source = context.getSource();

                            if (!(source.getSender() instanceof Player player)) {
                                source.getSender().sendMessage("Solo i giocatori possono usare questo comando!");
                                return 0;
                            }

                            PlayerSelectorArgumentResolver resolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                            var resolvedPlayers = resolver.resolve(source);
                            Player target = resolvedPlayers.iterator().next();

                            if (target.equals(player)) {
                                player.sendRichMessage("<red>Non puoi usare questo comando su tè stesso");
                                return 0;
                            }

                            final Component mainTitle = Component.text("ASSISTENZA", NamedTextColor.DARK_RED, TextDecoration.BOLD);
                            final Component subtitle = Component.text("Sei stato convocato in assistenza!", NamedTextColor.RED);

                            final Title title = Title.title(mainTitle, subtitle);

                            target.showTitle(title);
                            target.sendRichMessage("<dark_red><bold>ASSISTENZA</bold><dark_red>\n\n<red>Sei stato convocato in assistenza!\nServer Discord: ds.horizonsmc.it");
                            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);

                            return 1;
                        }))
                .build();
    }
}
