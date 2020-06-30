using CandorCore;

namespace GenericModule
{
    public class GenericModule : AbstractModule
    {
        public override string GetModuleName()
        {
            return "Generic Module";
        }

        public override string GetGameName()
        {
            throw new System.NotImplementedException();
        }

        public override string GetModsFolder()
        {
            throw new System.NotImplementedException();
        }

        public override string GetReadableGameName()
        {
            throw new System.NotImplementedException();
        }
    }
}