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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.simonevtw.staffUtils.utils.ChatFormatter.*;

public class AssistenzaCommand {
    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("assistenza")
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            CommandSender sender = source.getSender();

                            if (!(sender instanceof Player player)) {
                                sender.sendMessage(serverMessage("Solo i giocatori possono usare questo comando!"));
                                return 0;
                            }

                            var resolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                            var resolvedPlayers = resolver.resolve(source);
                            if (resolvedPlayers.isEmpty()) {
                                player.sendMessage(serverMessage("<red>Devi inserire il nome del player!"));
                                return 0;
                            }
                            Player target = resolvedPlayers.getFirst();

                            if (target.equals(player)) {
                                player.sendMessage(serverMessage("<red>Non puoi usare questo comando su tè stesso"));
                                return 0;
                            }

                            Title title = getTitle();

                            target.showTitle(title);
                            target.sendMessage(serverMessage("<dark_red><bold>ASSISTENZA</bold><dark_red>\n\n<red>Sei stato convocato in assistenza!\nServer Discord: ds.horizonsmc.it"));
                            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);

                            return 1;
                        }))
                .build();
    }

    static private Title getTitle() {
        Component mainTitle = Component.text("ASSISTENZA", NamedTextColor.DARK_RED, TextDecoration.BOLD);
        Component subtitle = Component.text("Sei stato convocato in assistenza!", NamedTextColor.RED);

        return Title.title(mainTitle, subtitle);
    }

}
