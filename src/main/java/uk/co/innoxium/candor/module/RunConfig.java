package uk.co.innoxium.candor.module;

// TODO: Migrate to Record once it is a release feature
public abstract class RunConfig {

    public abstract String getStartCommand();

    public abstract String getProgramArgs();

    public abstract String getWorkingDir();
}
