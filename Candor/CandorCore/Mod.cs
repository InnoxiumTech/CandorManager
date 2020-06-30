using System;
using System.IO;

namespace CandorCore
{
    public class Mod
    {
        public Mod(FileInfo modFile, string modName)
        {
            ModFile = modFile;
            ModName = modName;
        }

        public FileInfo ModFile { get; }
        public string ModName { get; }
    }
}