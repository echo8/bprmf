package com.echo8.bprmf.main;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.echo8.bprmf.conf.Defaults;

public class CommandLineOptions {
    public static final String HELP = "help";
    public static final String TRAIN = "train";
    public static final String RECOMMEND = "recommend";
    public static final String OUTPUT = "output";
    public static final String MODEL = "model";
    public static final String TRAINFORMAT = "trainformat";
    public static final String LEARNRATE = "learnrate";
    public static final String ITERS = "iters";
    public static final String FACTORS = "factors";
    public static final String REGBIAS = "regbias";
    public static final String REGU = "regu";
    public static final String REGI = "regi";
    public static final String REGJ = "regj";
    public static final String SEED = "seed";
    public static final String SKIPJUPDATE = "skipjupdate";

    @SuppressWarnings("static-access")
    public static Options get() {
        Options options = new Options();

        options.addOption(HELP, false, "print this message");

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("train model with given file").create(TRAIN));

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("generate recommendations for users in given file")
                .create(RECOMMEND));

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("output the model/recommendations to given file").create(OUTPUT));

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("model file to use when making recommendations").create(MODEL));

        options.addOption(OptionBuilder.withArgName("format").hasArg()
                .withDescription("format of training file: csv (default) or movielens")
                .create(TRAINFORMAT));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(String.format("set learn rate (default: %s)", Defaults.LEARN_RATE))
                .create(LEARNRATE));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set number of training iterations (default: %s)",
                                Defaults.NUM_ITERATIONS)).create(ITERS));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set number of factors (default: %s)", Defaults.NUM_FACTORS))
                .create(FACTORS));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set item bias regularization (default: %s)",
                                Defaults.REG_BIAS)).create(REGBIAS));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set user update regularization (default: %s)",
                                Defaults.REG_U)).create(REGU));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set item i update regularization (default: %s)",
                                Defaults.REG_I)).create(REGI));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set item j update regularization (default: %.5f)",
                                Defaults.REG_J)).create(REGJ));

        options.addOption(OptionBuilder.withArgName("value").hasArg()
                .withDescription("initialize random number generator with value").create(SEED));

        options.addOption(SKIPJUPDATE, false, "skip the update for item j");

        return options;
    }
}
