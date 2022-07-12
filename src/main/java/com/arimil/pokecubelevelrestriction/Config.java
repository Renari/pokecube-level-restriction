package com.arimil.pokecubelevelrestriction;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static class Server
    {
        private static final int defaultCatchRateLossPerLevel = 10;

        public final ForgeConfigSpec.ConfigValue<Integer> catchRateLossPerLevel;
        public Server(ForgeConfigSpec.Builder builder)
        {
            this.catchRateLossPerLevel = builder.comment("For every level higher than your max level how much harder does it become to catch? " +
                            "The default makes it 10% less likely to catch per level, so a pokemob 10 levels higher is impossible to catch.")
                    .defineInRange("Catch Rate Loss Per Level", defaultCatchRateLossPerLevel, 1, 100);
        }
    }

    private static final ForgeConfigSpec SERVER_SPEC;
    public  static final Server SERVER;
    static {
        {
            final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }
    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }
}