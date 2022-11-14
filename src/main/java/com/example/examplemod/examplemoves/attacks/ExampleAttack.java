package com.example.examplemod.examplemoves.attacks;

import pokecube.api.data.moves.LoadedMove.PreProcessor;
import pokecube.api.data.moves.MoveProvider;
import pokecube.api.moves.utils.MoveApplication;
import pokecube.api.moves.utils.MoveApplication.Damage;
import pokecube.api.moves.utils.MoveApplication.PostMoveUse;

/**
 * Here we create a basic attack, which simple makes the user faint if the enemy
 * has more health. You can find more examples of these in the package:
 * pokecube.mobs.moves.attacks
 * 
 * For interfaces to implement here, see the ones used in
 * pokecube.api.data.moves.IMove
 * 
 * @author Thutmose
 *
 */
@MoveProvider(name = "example-attack")
public class ExampleAttack implements PostMoveUse, PreProcessor
{
    @Override
    public void preProcess(MoveApplication apply)
    {
        // During pre-process, lets just change something for the entry, say
        // setting PP to 999.
        apply.getMove().pp = 999;
    }

    @Override
    public void applyPostMove(Damage t)
    {
        // Set target health to 0.
        if (t.move().getTarget().getHealth() > t.move().getUser().getEntity().getHealth())
            t.move().getTarget().setHealth(0);
    }
}
