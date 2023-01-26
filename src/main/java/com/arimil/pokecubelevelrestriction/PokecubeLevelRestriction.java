package com.arimil.pokecubelevelrestriction;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pokecube.api.PokecubeAPI;
import pokecube.api.events.StarterEvent;
import pokecube.api.events.pokemobs.CaptureEvent;
import pokecube.api.events.pokemobs.LevelUpEvent;
import pokecube.core.handlers.PokecubePlayerDataHandler;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pokecubelevelrestriction")
public class PokecubeLevelRestriction
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String HIGHEST_LEVEL_KEY = "_highest_pokemob_level";

    private static final Random RANDOM = new Random();

    public PokecubeLevelRestriction() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        PokecubeAPI.POKEMOB_BUS.register(this);
        // register our config
        Config.register();
    }

    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        // do something when the server starts
        PokecubeLevelRestriction.LOGGER.info("Pokecube Level Restriction Loaded");
    }

    @SubscribeEvent
    public void canCapture(CaptureEvent.Pre evt) {
        // event can't be canceled?
        if (!evt.isCancelable()) return;
        // snag ball returns null for getCaught for non-pokecube mobs
        if (evt.getCaught() == null) return;

        final Entity catcher = evt.pokecube.shootingEntity;
        if (catcher instanceof final Player player) {
            int maxLevelOwned = PokecubePlayerDataHandler.getCustomDataTag(player).getInt(HIGHEST_LEVEL_KEY);
            int levelDifference = evt.getCaught().getLevel() - maxLevelOwned;

            // it's lower level so we do nothing
            if (levelDifference <= 0) return;

            float failRate = (levelDifference * Config.SERVER.catchRateLossPerLevel.get()) / 100f;
            boolean failed = failRate > RANDOM.nextFloat();

            if (failed) {
                MutableComponent pokemobName = (MutableComponent) evt.mob.getDisplayName();
                MutableComponent pokecubeName = (MutableComponent) evt.pokecube.getItem().getDisplayName();

                player.sendSystemMessage(
                        Component.translatable(
                                "message.pokecubelevelrestriction.failure",
                                pokemobName.withStyle(ChatFormatting.RED),
                                pokecubeName.withStyle(ChatFormatting.AQUA)
                        )
                );
                evt.pokecube.kill();
                evt.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void captureEvent(CaptureEvent.Post evt) {
        // snag ball returns null for getCaught for non-pokecube mobs
        if (evt.getCaught() == null) return;

        final Entity catcher = evt.pokecube.shootingEntity;
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
