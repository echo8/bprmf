package com.echo8.bprmf.main;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import com.echo8.bprmf.conf.Defaults;

public class CommandLineOptions {
    public static final String OPTION_HELP = "help";
    public static final String OPTION_TRAIN = "train";
    public static final String OPTION_RECOMMEND = "recommend";
    public static final String OPTION_OUTPUT = "output";
    public static final String OPTION_MODEL = "model";
    public static final String OPTION_TRAINFORMAT = "trainformat";
    public static final String OPTION_LEARNRATE = "learnrate";
    public static final String OPTION_ITERS = "iters";
    public static final String OPTION_FACTORS = "factors";
    public static final String OPTION_REGBIAS = "regbias";
    public static final String OPTION_REGU = "regu";
    public static final String OPTION_REGI = "regi";
    public static final String OPTION_REGJ = "regj";
    public static final String OPTION_SKIPJUPDATE = "skipjupdate";

    @SuppressWarnings("static-access")
    public static Options get() {
        Options options = new Options();

        options.addOption(OPTION_HELP, false, "print this message");

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("train model with given file").create(OPTION_TRAIN));

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("generate recommendations for users in given file")
                .create(OPTION_RECOMMEND));

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("output the model/recommendations to given file")
                .create(OPTION_OUTPUT));

        options.addOption(OptionBuilder.withArgName("file").hasArg()
                .withDescription("model file to use when making recommendations")
                .create(OPTION_MODEL));

        options.addOption(OptionBuilder.withArgName("format").hasArg()
                .withDescription("format of training file: csv (default) or movielens")
                .create(OPTION_TRAINFORMAT));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set learn rate (default: %s)", Defaults.DEFAULT_LEARN_RATE))
                .create(OPTION_LEARNRATE));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set number of training iterations (default: %s)",
                                Defaults.DEFAULT_NUM_ITERATIONS)).create(OPTION_ITERS));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set number of factors (default: %s)",
                                Defaults.DEFAULT_NUM_FACTORS)).create(OPTION_FACTORS));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set item bias regularization (default: %s)",
                                Defaults.DEFAULT_REG_BIAS)).create(OPTION_REGBIAS));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set user update regularization (default: %s)",
                                Defaults.DEFAULT_REG_U)).create(OPTION_REGU));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set item i update regularization (default: %s)",
                                Defaults.DEFAULT_REG_I)).create(OPTION_REGI));

        options.addOption(OptionBuilder
                .withArgName("value")
                .hasArg()
                .withDescription(
                        String.format("set item j update regularization (default: %.5f)",
                                Defaults.DEFAULT_REG_J)).create(OPTION_REGJ));

        options.addOption(OPTION_SKIPJUPDATE, false, "skip the update for item j");

        return options;
    }
}
