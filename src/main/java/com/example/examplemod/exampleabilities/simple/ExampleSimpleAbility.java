package com.example.examplemod.exampleabilities.simple;

import pokecube.api.PokecubeAPI;
import pokecube.api.data.abilities.Ability;
import pokecube.api.entity.pokemob.IPokemob;

public class ExampleSimpleAbility extends Ability
{
    @Override
    /**
     * Here we spam console on tick. See other functions available in
     * Ability.class for what you might want to do with the ability
     */
    public void onUpdate(IPokemob mob)
    {
        // Just spam some stuff so we know we are loaded.
        PokecubeAPI.LOGGER.info(this.toString() + " " + mob.getDisplayName().getString());
    }
}
