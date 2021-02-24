package uk.co.innoxium.candor.implement;

import uk.co.innoxium.candor.mod.Mod;

import java.io.File;


public abstract class FileMerger {

    public abstract boolean mergeFile(Mod mod, File original, File toMerge);
}
