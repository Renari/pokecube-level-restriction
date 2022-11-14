package com.example.examplemod.examplemoves;

import pokecube.core.moves.implementations.MovesAdder;

/**
 * This class provides a root entry to this package for MovesAdder to scan for
 * added moves and world actions. moves define what the attack does, and world
 * actions define custom logic for the moves when they are used on blocks, item,
 * etc.
 * 
 * This scans for classes with the @MoveProvider annotation, and attempts to use
 * them to modify loaded moves. Moves must first be provided via a datapack
 * entry.
 * 
 * There are some additional classes which extend Move_Basic in
 * pokecube.core.moves.templates, which may be useful for your attack class to
 * extend.
 * 
 * @author Thutmose
 *
 */
public class MoveRegister
{
    public static void init()
    {
        MovesAdder.worldActionPackages.add(MoveRegister.class.getPackage());
        MovesAdder.moveRegistryPackages.add(MoveRegister.class.getPackage());
    }
}
