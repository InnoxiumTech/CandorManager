using System;
using System.IO;

namespace CandorCore
{
    public abstract class AbstractModule
    {
        // Gets the name of the module, can be anything, e.g "Kerbal Space Program Module" or "Module for KSP"
        public abstract String GetModuleName();

        // Gets the executable for the game.
        // This is what Candor will search for when launching the game
        public abstract String GetGameName();

        // Gets the folder in which mods should be installed
        public abstract String GetModsFolder();

        public abstract String GetReadableGameName();
    }
}