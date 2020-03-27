package com.example;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.services.DirectoryProcessor;
import com.example.services.TwoFilesProcessor;

public class WordsMergerApplication {

    public static void main(String[] args) {
        try {
            DirectoryProcessor directoryProcessor = new TwoFilesProcessor();
            Path directoryPath = Paths.get(
                        "/Users/aishwaryt/Documents/workspace-sts-3.9.6.RELEASE/WordsMerger/medium_example");
            File outputFile = directoryProcessor.processDirectory(
                        directoryPath.toFile());
            System.out.println("Output File:" + outputFile.getAbsolutePath());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
