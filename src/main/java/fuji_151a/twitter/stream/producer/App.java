/**
 * App.java
 *
 * @since 2015/11/01
 */
package fuji_151a.twitter.stream.producer;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
/**
 * Main Application.
 * @author fuji-151a
 */
public class App {
    @Option(name = "-c", aliases = "--config", required = true, metaVar = "<config>", usage = "configuration file")
    private String configFile;
    @Option(name = "-l", aliases = "--lang", metaVar = "<lang>", usage = "filter lang")
    private String lang;
    @Option(name="-h", aliases="--help", usage="print usage message and exit")
    private boolean usageFlag;

    public static void main(String[] args) {
        App app = new App();
        CmdLineParser parser = new CmdLineParser(app);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Got Exception: " + e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        if (app.usageFlag) {
            System.out.println("Usage:");
            System.out.println(" Shell [options]");
            System.out.println(" Shell [options] script [arguments]");
            System.out.println();
            System.out.println("Options:");
            parser.printUsage(System.out);
            return;
        }

        TwitterStreamProducer tsp = new TwitterStreamProducer(app.configFile);
        SimpleKafkaProducer producer = new SimpleKafkaProducer(app.configFile);
        tsp.setup(producer);
        tsp.run(app.lang);
    }
}
