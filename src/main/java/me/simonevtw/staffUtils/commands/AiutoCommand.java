package me.simonevtw.staffUtils.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.simonevtw.staffUtils.utils.ChatFormatter.*;

public class AiutoCommand {

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    private static final long COOLDOWN_TIME = 180000;

    public static LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("aiuto")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            CommandSender sender = source.getSender();

                            if (!(sender instanceof Player player)) {
                                sender.sendMessage(serverMessage("Solo i giocatori possono usare questo comando!"));
                                return 0;
                            }


                            UUID uuid = player.getUniqueId();
                            long currentMillis = System.currentTimeMillis();

                            if (cooldowns.containsKey(uuid) && !player.hasPermission("horizons.aiuto.bypass")) {
                                return handleCooldown(player, currentMillis);
                            }


                            String message = StringArgumentType.getString(context, "message");
                            player.sendMessage(serverMessage("<green>Richiesta di aiuto inviata con successo.\nMessaggio: <white>" + message));

                            Component staffMessage = getStaffMessage(player.getName(), message);

                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                if (!onlinePlayer.hasPermission("horizons.aiuto.ricevi")) {
                                    continue;
                                }

                                onlinePlayer.sendMessage(staffMessage);
                            }


                            cooldowns.put(uuid, currentMillis);
                            return 1;
                        }))
                .build();
    }

    private static int handleCooldown(Player player, long currentMillis) {
        long lastUsed = cooldowns.get(player.getUniqueId());
        long timeLeft = (lastUsed + COOLDOWN_TIME) - currentMillis;

        if (timeLeft > 0) {
            long totalSeconds = timeLeft / 1000;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;

            player.sendMessage(serverMessage("<red>Devi aspettare ancora <dark_red>" + minutes + "m " + seconds + "s<red> prima di inviare un'altra richiesta di aiuto!"));
        }

        return 0;
    }

    private static Component getStaffMessage(String name, String message) {
        return deserialize("<dark_red>" + name + " <red>ha inviato una richiesta di aiuto:\n<white>" + message + "\n")
                .append(getTpMessage(name));
    }
}
