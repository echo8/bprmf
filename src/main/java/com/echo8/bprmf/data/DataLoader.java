package com.echo8.bprmf.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.echo8.bprmf.type.UserItemPair;

public class DataLoader {
    public static FeedbackData loadFromFile(File dataFile, DataFileFormat dataFileFormat)
            throws IOException {
        FeedbackData feedbackData = new FeedbackData();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                UserItemPair userItemPair = dataFileFormat.parseLine(line);
                if (userItemPair != null) {
                    feedbackData.addFeedback(userItemPair);
                }
            }
        } finally {
            bufferedReader.close();
        }

        return feedbackData;
    }
}
