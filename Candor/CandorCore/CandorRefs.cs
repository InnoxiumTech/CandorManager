using System.IO;
using System.Runtime.ConstrainedExecution;
using log4net;
using log4net.Config;

[assembly: log4net.Config.XmlConfigurator(ConfigFileExtension="log4net", Watch=true)]
[assembly: log4net.Config.AliasRepository("Candor")]

namespace CandorCore
{
    public class CandorRefs
    {
        public static readonly ILog LOGGER = LogManager.GetLogger(typeof(CandorRefs));

        public static void Init()
        {
            LOGGER.Info("LOGGING");
        }
    }
}