package com.example.examplemod.examplemoves.attacks;

import pokecube.api.entity.pokemob.moves.MovePacket;
import pokecube.core.moves.templates.Move_Basic;

/**
 * Here we create a basic attack, which simple makes the user faint if the enemy
 * has more health.
 * 
 * @author Thutmose
 *
 */
public class ExampleAttack extends Move_Basic
{

    public ExampleAttack()
    {
        // The string here is the name of the attack. It is generally advised to
        // also create a datapack entry for this movename, and use that to fill
        // in most of the required values. Otherwise, we can adjust the values
        // ourselves.
        super("example_attack");
        // Example of adjusting a value.
        this.move.pp = 999;
    }

    @Override
    public void onAttack(MovePacket packet)
    {
        packet.attacker.setHealth(0);
    }
}
