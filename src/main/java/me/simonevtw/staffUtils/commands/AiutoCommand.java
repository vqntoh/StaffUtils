package me.simonevtw.staffUtils.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AiutoCommand {

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    private static final long COOLDOWN_TIME = 180000;

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("aiuto")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            var source = context.getSource();

                            if (!(source.getSender() instanceof Player player)) {
                                source.getSender().sendMessage("Solo i giocatori possono usare questo comando!");
                                return 0;
                            }

                            UUID uuid = player.getUniqueId();
                            long currentMillis = System.currentTimeMillis();

                            if (cooldowns.containsKey(uuid) && !player.hasPermission("horizons.aiuto.bypass")) {
                                long lastUsed = cooldowns.get(uuid);
                                long timeLeft = (lastUsed + COOLDOWN_TIME) - currentMillis;

                                if (timeLeft > 0) {
                                    long totalSeconds = timeLeft / 1000;
                                    long minutes = totalSeconds / 60;
                                    long seconds = totalSeconds % 60;

                                    player.sendRichMessage("<red>Devi aspettare ancora <dark_red>" + minutes + "m " + seconds + "s<red> prima di inviare un'altra richiesta di aiuto!");
                                    return 0;
                                }
                            }

                            String message = StringArgumentType.getString(context, "message");
                            player.sendRichMessage("<green>Richiesta di aiuto inviata con successo.\nMessaggio: <white>" + message);

                            Component tp = MiniMessage.miniMessage().deserialize("<green>[Clicca qui per teletrasportarti]")
                                    .clickEvent(ClickEvent.runCommand("/tp " + player.getName()))
                                    .hoverEvent(HoverEvent.showText(MiniMessage.miniMessage().deserialize("<green>Clicca per teletrasportarti dal giocatore")));

                            Component staffMessage = MiniMessage.miniMessage().deserialize("<dark_red>" + player.getName() + " <red>ha inviato una richiesta di aiuto:\n<white>" + message + "\n")
                                    .append(tp);

                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (onlinePlayer.hasPermission("horizons.aiuto.ricevi")) {
                                    onlinePlayer.sendMessage(staffMessage);
                                }
                            }

                            cooldowns.put(uuid, currentMillis);

                            return 1;
                        }))
                .build();
    }
}