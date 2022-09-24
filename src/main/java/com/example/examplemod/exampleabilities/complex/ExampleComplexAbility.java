package com.example.examplemod.exampleabilities.complex;

import java.util.Arrays;

import pokecube.api.PokecubeAPI;
import pokecube.api.data.abilities.Ability;
import pokecube.api.entity.pokemob.IPokemob;

/**
 * This ability will be registered as "examplecomplex", note that this means it
 * will match any of the following:
 * 
 * "Example Complex Ability", "examplecomplexability",
 * "example_complex_ability", etc
 * 
 * @author Thutmose
 *
 */
public class ExampleComplexAbility extends Ability
{
    Object[] loaded_args;

    @Override
    /**
     * This init function usually gets handed the pokemob, but can also get
     * additional range information when used in the ability field amplifier.
     * See examples in pokecube.mobs.abilities.complex for how this is used.
     */
    public Ability init(Object... args)
    {
        this.loaded_args = args;
        return super.init(args);
    }

    @Override
    /**
     * Here we spam console on tick. See other functions available in
     * Ability.class for what you might want to do with the ability
     */
    public void onUpdate(IPokemob mob)
    {
        if (loaded_args != null) PokecubeAPI.LOGGER.info(Arrays.toString(loaded_args));
        else PokecubeAPI.LOGGER.info("No Loaded Args!");
    }
}
