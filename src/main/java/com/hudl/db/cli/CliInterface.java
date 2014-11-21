package com.hudl.db.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hudl.db.BackupScrubber;
import org.apache.commons.cli.*;

import java.io.File;

/**
 *
 */
public class CliInterface {


    public static void main(String[] args) {

        BackupScrubber scrubber = null;
        try {
            Injector injector = Guice.createInjector();
            scrubber = injector.getInstance(BackupScrubber.class);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot initialize the utility...", e);
        }

        CommandLineParser parser = new BasicParser();
        Options options = buildOptions();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("h") || commandLine.hasOption("v") || !commandLine.hasOption("b")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("hudl-utils", options);
                System.exit(0);
            }


            File backupDirectory = new File(commandLine.getOptionValue("b"));
            scrubber.scrub(backupDirectory);

        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static Options buildOptions() {

        Options options = new Options();

        Option backup = new Option("b", "backup", true, "The path of the backup bson file. The scrubbed file will be return in the same directory as the original file, with a -scrubbed added to the name of the file.");
        Option help = new Option("h", "help", false, "Tool usage");
        Option version = new Option("v", "version 0.4.8.15.16.23.42", false, "Tool usage");

        options.addOption(backup);
        options.addOption(help);
        options.addOption(version);

        return options;
    }
}
