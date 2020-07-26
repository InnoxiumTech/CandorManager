package me.shadowchild.candor.generic;

import me.shadowchild.candor.ConfigHandler;

import java.io.File;

public class GenericConfig implements ConfigHandler.IConfig {

    @Override
    public String getName() {

        return "Generic";
    }

    @Override
    public void loadConfig(File configFolder) {

    }
}
