package me.simonevtw.staffUtils;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.simonevtw.staffUtils.commands.AiutoCommand;
import me.simonevtw.staffUtils.commands.AssistenzaCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(AiutoCommand.create());
            event.registrar().register(AssistenzaCommand.create());
        });}

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
