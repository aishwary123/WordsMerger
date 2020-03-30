package com.example.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.example.exceptions.FileInpuException;

public class TwoFileMergerTest {

    @Test(expected = FileInpuException.class)
    public void testMergeFile_NoCorrectFileSpecified()
        throws IOException {

        TwoFileMerger twoFileMerger = new TwoFileMerger(null, null);
        twoFileMerger.mergeFiles();
    }

    @Test
    public void testMergeFile_FirstFileNotSpecified()
        throws IOException {

        File testFile = Paths.get("small_example", "0.dat").toFile();
        TwoFileMerger twoFileMerger = new TwoFileMerger(null, testFile);
        File outputFile = twoFileMerger.mergeFiles();
        Assert.assertEquals(testFile, outputFile);
    }

    @Test
    public void testMergeFile_SecondFileNotSpecified()
        throws IOException {

        File testFile = Paths.get("small_example", "0.dat").toFile();
        TwoFileMerger twoFileMerger = new TwoFileMerger(testFile, null);
        File outputFile = twoFileMerger.mergeFiles();
        Assert.assertEquals(testFile, outputFile);
    }

    @Test
    public void testMergeFile_BothCorrectSpecified()
        throws IOException {

        File testFile = Paths.get("small_example", "0.dat").toFile();
        TwoFileMerger twoFileMerger = new TwoFileMerger(testFile, testFile);
        File outputFile = twoFileMerger.mergeFiles();
        long linesCount = Files.lines(outputFile.toPath()).count();
        Assert.assertEquals(254, linesCount);
    }

}
