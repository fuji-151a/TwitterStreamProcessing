/**
 * TwitterLogCounterApp.java
 *
 * @since 2016/01/11
 * <p>
 * Copyright 2016 fuji-151a
 * All Rights Reserved.
 */
package fuji.twitter.stream.counter;

import fuji.twitter.stream.consumer.SimpleKafkaConsumer;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 *
 * @author fuji-151a
 */
public class TwitterLogCounterApp {

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

    public static void main(String[] args) {
        TwitterLogCounterApp counter = new TwitterLogCounterApp();
        CmdLineParser parser = new CmdLineParser(counter);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println("Got Exception: " + e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        if (counter.usageFlag) {
            System.out.println("Usage:");
            System.out.println(" Shell [options]");
            System.out.println(" Shell [options] script [arguments]");
            System.out.println();
            System.out.println("Options:");
            parser.printUsage(System.out);
            return;
        }
        SimpleKafkaConsumer consumer
                = new SimpleKafkaConsumer(counter.configFile);
        LogCounter logCounter = new LogCounter(consumer, counter.rootPath);
        try {
            logCounter.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
