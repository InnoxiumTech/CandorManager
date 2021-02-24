package uk.co.innoxium.candor.tool;

import uk.co.innoxium.candor.module.RunConfig;

import java.util.Objects;


public class Tool {

    public String name;
    public String icon;
    public RunConfig runConfig;

    @Override
    public int hashCode() {

        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {

        assert obj instanceof Tool;
        return name.equals(((Tool) obj).name);
    }
}