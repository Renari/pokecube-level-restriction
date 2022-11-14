package com.example.examplemod.exampleabilities;

import pokecube.api.data.abilities.AbilityManager;

/**
 * This class provides a root entry to this package, which AbilityManager will
 * then scan for ability classes to add. We have 2 examples, in sub-packages for
 * what are considered "complex" and "simple" abilities. These differ as
 * follows:
 *
 * complex: These abilities store some value about the user, so each mob gets a
 * separate instance<br>
 * simple: These abilities are singletons.
 * 
 * The sub-packages here are not necessary, and are merely here for
 * organisation.
 * 
 * The @AbilityProvider annotation is then used to determine the name of the
 * ability to register.
 * 
 * @author Thutmose
 *
 */
public class AbilityRegister
{
    public static void init()
    {
        AbilityManager.registerAbilityPackage(AbilityRegister.class.getPackage());
    }
}
