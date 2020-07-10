using System.IO;
using System.Runtime.ConstrainedExecution;
using log4net;
using log4net.Config;

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