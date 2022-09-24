package com.example.examplemod.examplemoves;

import pokecube.core.moves.implementations.MovesAdder;

/**
 * This class provides a root entry to this package for MovesAdder to scan for
 * added moves and world actions. moves define what the attack does, and world
 * actions define custom logic for the moves when they are used on blocks, item,
 * etc.
 * 
 * This scans for classes that extend pokecube.core.moves.templates.Move_Basic
 * to register as attacks, and classes that implement
 * pokecube.api.moves.IMoveAction to register as move actions.
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
        MovesAdder.packages.add(MoveRegister.class.getPackage());
    }
}
