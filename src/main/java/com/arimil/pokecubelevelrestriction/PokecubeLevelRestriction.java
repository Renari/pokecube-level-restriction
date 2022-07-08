package com.arimil.pokecubelevelrestriction;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.apache.commons.lang3.tuple.Pair;
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

    public static class Config
    {
        private static final int defaultMaxLevelCatch = 10;
        private static final int defaultCatchRateLossPerLevel = 10;

        public final ForgeConfigSpec.ConfigValue<Integer> maxLevelCatch;
        public final ForgeConfigSpec.ConfigValue<Integer> catchRateLossPerLevel;


        public Config(ForgeConfigSpec.Builder builder)
        {
            builder.push("Pokecube Level Restriction");
            this.maxLevelCatch = builder.comment("How many levels higher before you will no longer be able to catch pokemob?")
                    .worldRestart()
                    .defineInRange("Max Catch Level", defaultMaxLevelCatch, 0, 99);
            this.catchRateLossPerLevel = builder.comment("For every level higher than your max level how much harder does it become to catch? " +
                            "The default makes it 10% less likely to catch per level, so a pokemob 10 levels higher is impossible to catch.")
                    .worldRestart()
                    .defineInRange("Short but readable name 2", defaultCatchRateLossPerLevel, 1, 100);
            builder.pop();
        }
    }



    public static final  Config CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String HIGHEST_LEVEL_KEY = "_highest_pokemob_level";

    static {
        // Add config
        Pair<Config, ForgeConfigSpec> configSpecPair = new ForgeConfigSpec.Builder().configure(Config::new);
        CONFIG = configSpecPair.getLeft();
        CONFIG_SPEC = configSpecPair.getRight();
    }

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
        // event can't be canceled?
        if (!evt.isCancelable()) return;

        final Entity catcher = ((EntityPokecube) evt.pokecube).shootingEntity;
        if (catcher instanceof final Player player) {
            int maxLevelOwned = PokecubePlayerDataHandler.getCustomDataTag(player).getInt(HIGHEST_LEVEL_KEY);
            int levelDifference = evt.getCaught().getLevel() - maxLevelOwned;

            // it's lower level so we do nothing
            if (levelDifference <= 0) return;

            double failRate = (levelDifference * CONFIG.catchRateLossPerLevel.get()) / 100d;
            boolean failed = levelDifference >= CONFIG.maxLevelCatch.get() || failRate >= 1d || failRate < Math.random();

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
