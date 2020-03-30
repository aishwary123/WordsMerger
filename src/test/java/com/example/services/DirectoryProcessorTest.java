package com.example.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.example.exceptions.FileInpuException;

public class DirectoryProcessorTest {

    private DirectoryProcessor directoryProcessor;

    @Before
    public void setUp() {
        directoryProcessor = Mockito.mock(DirectoryProcessor.class);
        Mockito.when(directoryProcessor.getExecutorSize()).thenReturn(10);
        Mockito.doCallRealMethod().when(directoryProcessor).processDirectory(
                    Mockito.any());
        Mockito.doCallRealMethod().when(directoryProcessor).invokeMerge(
                    Mockito.any());

    }

    @Test
    public void testInvokeMerge_success()
        throws IOException {

        File directory = Paths.get("small_example").toFile();
        File[] files = directory.listFiles();
        BlockingQueue<File> fileList = new ArrayBlockingQueue<>(
                    2 * files.length);
        for (File file : files) {
            fileList.add(file);
        }
        IFileMerger fileMergerMock = Mockito.mock(IFileMerger.class);

        Mockito.when(directoryProcessor.getFileMerger(fileList)).thenReturn(
                    fileMergerMock);

        File testFile = Paths.get("small_example", "0.dat").toFile();

        Mockito.when(fileMergerMock.mergeFiles()).thenAnswer(
                    new Answer<File>() {
                        @Override
                        public File answer(InvocationOnMock invocation)
                            throws Throwable {
                            fileList.poll();
                            fileList.poll();
                            return testFile;
                        }
                    });
        File outputFile = directoryProcessor.invokeMerge(fileList);
        long linesCount = Files.lines(outputFile.toPath()).count();
        Assert.assertEquals(127, linesCount);
    }

    @Test(expected = FileInpuException.class)
    public void testProcessDirectory_wrongPath()
        throws IOException {
        File directory = Paths.get("random").toFile();
        directoryProcessor.processDirectory(directory);
    }
}
