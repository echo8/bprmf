package com.echo8.bprmf.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import com.echo8.bprmf.BPRMF;
import com.echo8.bprmf.data.CsvDataFileFormat;
import com.echo8.bprmf.data.DataFileFormat;
import com.echo8.bprmf.data.DataLoader;
import com.echo8.bprmf.data.MovieLensDataFileFormat;

public class Main {

    public static void main(String[] args) throws Exception {
        Options options = getOptions();
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help")) {
            printHelp(options);
            return;
        }

        BPRMF bprmf = getBprmf(cmd);

        if (cmd.hasOption("train") && !cmd.hasOption("recommend")) {
            File trainFile = new File(cmd.getOptionValue("train"));
            DataFileFormat dataFileFormat = getTrainFileFormat(cmd);

            File modelFile;
            if (cmd.hasOption("output")) {
                modelFile = new File(cmd.getOptionValue("output"));
            } else {
                throw new ParseException("You must use the '-output' option to specify where the model will be written to.");
            }

            bprmf.setPosFeedbackData(DataLoader.loadFromFile(trainFile, dataFileFormat));
            bprmf.train();
            bprmf.save(new ObjectOutputStream(new FileOutputStream(modelFile)));
        } else if (cmd.hasOption("recommend") && !cmd.hasOption("train")) {
            List<String> userIdList = FileUtils.readLines(new File(cmd.getOptionValue("recommend")));

            File modelFile;
            if (cmd.hasOption("model")) {
                modelFile = new File(cmd.getOptionValue("model"));
            } else {
                throw new ParseException("You must use the '-model' option to specify the model file used when making recommendations.");
            }

            File resultFile;
            if (cmd.hasOption("output")) {
                resultFile = new File(cmd.getOptionValue("output"));
            } else {
                throw new ParseException("You must use the '-output' option to specify where the recommendations will be written to.");
            }

            bprmf.load(new ObjectInputStream(new FileInputStream(modelFile)));

            PrintWriter writer = new PrintWriter(resultFile);

            for (String rawUserId : userIdList) {
                Integer userId = bprmf.getPosFeedbackData().rawUserIdToUserId(rawUserId);

                if (userId != null) {
                    for (int i = 0; i < bprmf.getPosFeedbackData().getNumItems(); i++) {
                        float score = bprmf.predict(userId, i);
                        writer.println(String.format("%s,%s,%s", 
                            rawUserId, bprmf.getPosFeedbackData().getRawItemId(i), score));
                    }
                }
            }

            writer.close();
        } else {
            throw new ParseException("You must run bprmf with either the '-train' or '-recommend' option.");
        }
    }

    private static DataFileFormat getTrainFileFormat(CommandLine cmd) throws ParseException {
        if (cmd.hasOption("trainformat")) {
            String trainFileFormat = cmd.getOptionValue("trainformat");
            switch (trainFileFormat) {
            case "csv":
                return new CsvDataFileFormat();
            case "movielens":
                return new MovieLensDataFileFormat();
            default:
                throw new ParseException("Did not recognize this train file format: " + trainFileFormat);
            }
        } else {
            return new CsvDataFileFormat();
        }
    }

    private static BPRMF getBprmf(CommandLine cmd) {
        BPRMF bprmf = new BPRMF();

        if (cmd.hasOption("learnrate")) {
            String learnRate = cmd.getOptionValue("learnrate");
            bprmf.setLearnRate(Float.parseFloat(learnRate));
        }

        if (cmd.hasOption("iters")) {
            String iters = cmd.getOptionValue("iters");
            bprmf.setNumIterations(Integer.parseInt(iters));
        }

        if (cmd.hasOption("factors")) {
            String factors = cmd.getOptionValue("factors");
            bprmf.setNumFactors(Integer.parseInt(factors));
        }

        if (cmd.hasOption("regbias")) {
            String regBias = cmd.getOptionValue("regbias");
            bprmf.setRegBias(Float.parseFloat(regBias));
        }

        if (cmd.hasOption("regu")) {
            String regU = cmd.getOptionValue("regu");
            bprmf.setRegU(Float.parseFloat(regU));
        }

        if (cmd.hasOption("regi")) {
            String regI = cmd.getOptionValue("regi");
            bprmf.setRegI(Float.parseFloat(regI));
        }

        if (cmd.hasOption("regj")) {
            String regJ = cmd.getOptionValue("regj");
            bprmf.setRegJ(Float.parseFloat(regJ));
        }

        if (cmd.hasOption("skipjupdate")) {
            bprmf.setUpdateJ(false);
        }

        return bprmf;
    }

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp("bprmf", options);
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {
        Options options = new Options();

        options.addOption("help", false, "print this message");

        options.addOption(OptionBuilder.withArgName("file")
            .hasArg()
            .withDescription("train model with given file")
            .create("train"));

        options.addOption(OptionBuilder.withArgName("file")
            .hasArg()
            .withDescription("generate recommendations for users in given file")
            .create("recommend"));

        options.addOption(OptionBuilder.withArgName("file")
            .hasArg()
            .withDescription("output the model/recommendations to given file")
            .create("output"));

        options.addOption(OptionBuilder.withArgName("file")
            .hasArg()
            .withDescription("model file to use when making recommendations")
            .create("model"));

        options.addOption(OptionBuilder.withArgName("format")
            .hasArg()
            .withDescription("format of training file: csv (default) or movielens")
            .create("trainformat"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set learn rate (default: )")
            .create("learnrate"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set number of training iterations (default: )")
            .create("iters"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set number of factors (default: )")
            .create("factors"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set item bias regularization (default: )")
            .create("regbias"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set user update regularization (default: )")
            .create("regu"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set item i update regularization (default: )")
            .create("regi"));

        options.addOption(OptionBuilder.withArgName("value")
            .hasArg()
            .withDescription("set item j update regularization (default: )")
            .create("regj"));

        options.addOption("skipjupdate", false, "skip the update for item j");

        return options;
    }
}
