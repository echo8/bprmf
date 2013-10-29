package com.echo8.bprmf.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.echo8.bprmf.type.UserItemPair;

public class DataLoader {
    public static PosFeedbackData loadFromFile(File dataFile,
            DataFileFormat dataFileFormat) throws IOException {
        PosFeedbackData posFeedbackData = new PosFeedbackData();

        BufferedReader bufferedReader =
            new BufferedReader(new FileReader(dataFile));
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                UserItemPair userItemPair = dataFileFormat.parseLine(line);
                if (userItemPair != null) {
                    posFeedbackData.addFeedback(userItemPair);
                }
            }
        } finally {
            bufferedReader.close();
        }

        return posFeedbackData;
    }
}
