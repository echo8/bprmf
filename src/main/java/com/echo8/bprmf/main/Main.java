package com.echo8.bprmf.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import com.echo8.bprmf.BPRMF;
import com.echo8.bprmf.data.CsvDataFileFormat;
import com.echo8.bprmf.data.DataFileFormat;
import com.echo8.bprmf.data.DataLoader;
import com.echo8.bprmf.data.MovieLensDataFileFormat;
import com.echo8.bprmf.utils.RandomUtils;

public class Main {

    public static void main(String[] args) throws Exception {
        Options options = CommandLineOptions.get();
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(CommandLineOptions.HELP)) {
            printHelp(options);
            return;
        }

        BPRMF bprmf = getBprmf(cmd);

        if (cmd.hasOption(CommandLineOptions.SEED)) {
            RandomUtils.init(Long.parseLong(cmd.getOptionValue(CommandLineOptions.SEED)));
        }

        if (cmd.hasOption(CommandLineOptions.TRAIN) && !cmd.hasOption(CommandLineOptions.RECOMMEND)) {
            train(cmd, bprmf);
        } else if (cmd.hasOption(CommandLineOptions.RECOMMEND)
                && !cmd.hasOption(CommandLineOptions.TRAIN)) {
            recommend(cmd, bprmf);
        } else {
            throw new ParseException(
                    "You must run bprmf with either the '-train' or '-recommend' option.");
        }
    }

    private static void train(CommandLine cmd, BPRMF bprmf) throws Exception {
        File trainFile = new File(cmd.getOptionValue(CommandLineOptions.TRAIN));
        DataFileFormat dataFileFormat = getTrainFileFormat(cmd);
        File modelFile = getOutputFile(cmd);

        bprmf.setFeedbackData(DataLoader.loadFromFile(trainFile, dataFileFormat));
        bprmf.train();
        bprmf.save(new ObjectOutputStream(new FileOutputStream(modelFile)));
    }

    private static void recommend(CommandLine cmd, BPRMF bprmf) throws Exception {
        List<String> rawUserIdList = getRawUserIdList(cmd);

        File modelFile = getModelFile(cmd);
        File resultFile = getOutputFile(cmd);

        bprmf.load(new ObjectInputStream(new FileInputStream(modelFile)));

        PrintWriter writer = new PrintWriter(resultFile);

        for (String rawUserId : rawUserIdList) {
            Integer userId = bprmf.getFeedbackData().rawUserIdToUserId(rawUserId);

            if (userId != null) {
                Set<Integer> trainItemIdSet = bprmf.getFeedbackData().getItemIdSetForUserId(userId);
                for (int i = 0; i < bprmf.getFeedbackData().getNumItems(); i++) {
                    if (!trainItemIdSet.contains(i)) {
                        float score = bprmf.predict(userId, i);
                        writer.println(String.format("%s,%s,%s", rawUserId, bprmf.getFeedbackData()
                                .getRawItemId(i), score));
                    }
                }
            }
        }

        writer.close();
    }

    private static List<String> getRawUserIdList(CommandLine cmd) throws IOException {
        return FileUtils.readLines(new File(cmd.getOptionValue(CommandLineOptions.RECOMMEND)));
    }

    private static File getModelFile(CommandLine cmd) throws ParseException {
        String modelFilePath = cmd.getOptionValue(CommandLineOptions.MODEL);
        if (modelFilePath != null) {
            return new File(modelFilePath);
        } else {
            throw new ParseException(
                    "You must use the '-model' option to specify the model file used when making recommendations.");
        }
    }

    private static File getOutputFile(CommandLine cmd) throws ParseException {
        String outputFilePath = cmd.getOptionValue(CommandLineOptions.OUTPUT);
        if (outputFilePath != null) {
            return new File(outputFilePath);
        } else {
            throw new ParseException(
                    "You must use the '-output' option to specify where the model/results will be saved to.");
        }
    }

    private static DataFileFormat getTrainFileFormat(CommandLine cmd) throws ParseException {
        String trainFileFormat = cmd.getOptionValue(CommandLineOptions.TRAINFORMAT);
        if (trainFileFormat != null) {
            switch (trainFileFormat) {
                case "csv":
                    return new CsvDataFileFormat();
                case "movielens":
                    return new MovieLensDataFileFormat();
                default:
                    throw new ParseException("Did not recognize this train file format: "
                            + trainFileFormat);
            }
        } else {
            return new CsvDataFileFormat();
        }
    }

    private static BPRMF getBprmf(CommandLine cmd) {
        BPRMF bprmf = new BPRMF();

        String learnRate = cmd.getOptionValue(CommandLineOptions.LEARNRATE);
        if (learnRate != null) {
            bprmf.setLearnRate(Float.parseFloat(learnRate));
        }

        String iters = cmd.getOptionValue(CommandLineOptions.ITERS);
        if (iters != null) {
            bprmf.setNumIterations(Integer.parseInt(iters));
        }

        String factors = cmd.getOptionValue(CommandLineOptions.FACTORS);
        if (factors != null) {
            bprmf.setNumFactors(Integer.parseInt(factors));
        }

        String regBias = cmd.getOptionValue(CommandLineOptions.REGBIAS);
        if (regBias != null) {
            bprmf.setRegBias(Float.parseFloat(regBias));
        }

        String regU = cmd.getOptionValue(CommandLineOptions.REGU);
        if (regU != null) {
            bprmf.setRegU(Float.parseFloat(regU));
        }

        String regI = cmd.getOptionValue(CommandLineOptions.REGI);
        if (regI != null) {
            bprmf.setRegI(Float.parseFloat(regI));
        }

        String regJ = cmd.getOptionValue(CommandLineOptions.REGJ);
        if (regJ != null) {
            bprmf.setRegJ(Float.parseFloat(regJ));
        }

        if (cmd.hasOption(CommandLineOptions.SKIPJUPDATE)) {
            bprmf.setUpdateJ(false);
        }

        return bprmf;
    }

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp("bprmf", options);
    }
}
