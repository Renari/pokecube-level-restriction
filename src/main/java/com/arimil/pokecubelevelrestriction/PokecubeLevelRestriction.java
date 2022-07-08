package com.arimil.pokecubelevelrestriction;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pokecube.core.PokecubeCore;
import pokecube.core.events.StarterEvent;
import pokecube.core.events.pokemob.CaptureEvent;
import pokecube.core.events.pokemob.LevelUpEvent;
import pokecube.core.handlers.PokecubePlayerDataHandler;
import pokecube.core.items.pokecubes.EntityPokecube;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pokecubelevelrestriction")
public class PokecubeLevelRestriction
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String HIGHEST_LEVEL_KEY = "_highest_pokemob_level";

    public PokecubeLevelRestriction() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        PokecubeCore.POKEMOB_BUS.register(this);
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        // do something when the server starts
        PokecubeLevelRestriction.LOGGER.info("Pokecube Level Restriction Loaded");
    }

    @SubscribeEvent
    public void canCapture(CaptureEvent.Pre evt) {
        final Entity catcher = ((EntityPokecube) evt.pokecube).shootingEntity;
        if (catcher instanceof final Player player) {
            int maxLevelOwned = PokecubePlayerDataHandler.getCustomDataTag(player).getInt(HIGHEST_LEVEL_KEY);
            int levelDifference = evt.getCaught().getLevel() - maxLevelOwned;
            boolean failed = levelDifference > 0 && (levelDifference >= 10 || !((Math.random() * 10) > levelDifference));
            if (failed) {
                player.sendMessage(new TextComponent("They seem too strong to capture, they broke out!!!"), Util.NIL_UUID);
                evt.pokecube.kill();
                evt.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void captureEvent(CaptureEvent.Post evt) {
        final Entity catcher = ((EntityPokecube) evt.pokecube).shootingEntity;
        if (catcher instanceof final Player player) {
            final int level = evt.getCaught().getLevel();
            if (level > PokecubePlayerDataHandler.getCustomDataTag(player).getInt(HIGHEST_LEVEL_KEY)) {
                PokecubePlayerDataHandler.getCustomDataTag(player).putInt(HIGHEST_LEVEL_KEY, level);
                PokecubePlayerDataHandler.saveCustomData(player);
            }
        }
    }

    @SubscribeEvent
    public void levelUpEvent(LevelUpEvent evt) {
        if (evt.mob.getOwner() != null && evt.mob.getOwner() instanceof Player player) {
            if (evt.newLevel > PokecubePlayerDataHandler.getCustomDataTag(player).getInt(HIGHEST_LEVEL_KEY)) {
                PokecubePlayerDataHandler.getCustomDataTag(player).putInt(HIGHEST_LEVEL_KEY, evt.newLevel);
                PokecubePlayerDataHandler.saveCustomData(player);
            }
        }
    }

    @SubscribeEvent
    public void StarterEvent(StarterEvent evt) {
        // starters start at level 5
        if (5 > PokecubePlayerDataHandler.getCustomDataTag(evt.player).getInt(HIGHEST_LEVEL_KEY)) {
            PokecubePlayerDataHandler.getCustomDataTag(evt.player).putInt(HIGHEST_LEVEL_KEY, 5);
            PokecubePlayerDataHandler.saveCustomData(evt.player);
        }
    }
}
