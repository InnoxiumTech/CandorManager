
namespace CandorCore
{
    public abstract class AbstractModInstaller
    {
        private AbstractModule module;
        
        public AbstractModInstaller(AbstractModule module)
        {
            this.module = module;
        }

        public abstract bool Install(Mod mod);
    }
}