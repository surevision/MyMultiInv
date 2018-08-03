package com.soulbaka.nmi;

import com.google.inject.Inject;
import com.soulbaka.nmi.command.CommandLoader;
import com.soulbaka.nmi.config.Config;
import com.soulbaka.nmi.config.InventoryConfig;
import com.soulbaka.nmi.listener.PlayerMoveListener;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.INFORMATIVE_VERSION, description = PluginInfo.DESCRIPTION )
public class NucleusMultiInv {

    @Inject
    private Logger logger;
    public Logger getLogger() {
        return logger;
    }

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    public Path getConfigDir()
    {
        return configDir;
    }

    protected NucleusMultiInv() {
    }

    private static NucleusMultiInv nuelcusMultiInv;
    public static NucleusMultiInv getNucleusMultiInv() {
       return nuelcusMultiInv;
    }


    @Listener
    public void onServerInit(GameInitializationEvent event)
    {
        // Register all commands.
        CommandLoader.registerCommands();
        getGame().getEventManager().registerListeners(this, new PlayerMoveListener());

        getLogger().info(String.format("%s %s is loaded!", PluginInfo.NAME, PluginInfo.VERSION));

    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event)
    {
        nuelcusMultiInv = this;

        // Create Config Directory for nucleusMultiInv
        if (!Files.exists(configDir))
        {
            if (Files.exists(configDir.resolveSibling("nucleusMultiInv")))
            {
                try
                {
                    Files.move(configDir.resolveSibling("nucleusMultiInv"), configDir);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    Files.createDirectories(configDir);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Create config.conf
        Config.getConfig().setup();
        // Create inventory.conf
        InventoryConfig.getConfig().setup();
    }

    public Game getGame() {
        return Sponge.getGame();
    }

}
