/**
 * Fetcher.java
 *
 * @since 2015/12/31
 * <p/>
 * Copyright 2015 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.consumer;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Main App.
 * Store Data from Kafka.
 * @author fuji-151a
 */
public final class Fetcher {

    /**
     * Config file.
     */
    @Option(name = "-c", aliases = "--config",
            required = true, metaVar = "<config>", usage = "configuration file")
    private String configFile;

    /**
     * Store root path.
     */
    @Option(name = "-p", aliases = "--path",
            required = true, metaVar = "<path>", usage = "store path")
    private String rootPath;

    /**
     * usage flag.
     */
    @Option(name = "-h", aliases = "--help",
            usage = "print usage message and exit")
    private boolean usageFlag;


    /**
     * Not Create Instance.
     */
    private Fetcher() { }

    /**
     * Main application.
     * @param args option.
     */
    public static void main(final String[] args) {
        Fetcher fetcher = new Fetcher();
        CmdLineParser parser = new CmdLineParser(fetcher);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Got Exception: " + e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        if (fetcher.usageFlag) {
            System.out.println("Usage:");
            System.out.println(" Shell [options]");
            System.out.println(" Shell [options] script [arguments]");
            System.out.println();
            System.out.println("Options:");
            parser.printUsage(System.out);
            return;
        }
        SimpleKafkaConsumer consumer
                = new SimpleKafkaConsumer(fetcher.configFile);
        TwitterStreamFetcher tsf
                = new TwitterStreamFetcher(consumer, fetcher.rootPath);
        try {
            tsf.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
