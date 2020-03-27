package com.example.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.exceptions.FileChunkingException;
import com.example.messages.CustomMessages;

public abstract class DirectoryProcessor {

    private int executorSize = 10;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public DirectoryProcessor() {

    }

    public DirectoryProcessor(int executorSize) {

        this.executorSize = executorSize;

    }

    public abstract IFileMerger getFileMerger(final BlockingQueue<File> blockingQueue);

    public File processDirectory(File directory) {
        if (!directory.isDirectory()) {
            logger.error(CustomMessages.INCORRECT_DIRECTORY_SPECIFIED);
        }
        File[] files = directory.listFiles();
        List<File> chunkedFiles = invokeFileChunking(files);
        BlockingQueue<File> fileList = new ArrayBlockingQueue<>(
                    2 * files.length);
        for (File file : chunkedFiles) {
            fileList.add(file);
        }
        return invokeMerge(fileList);

    }

    public File invokeMerge(BlockingQueue<File> queue) {

        ExecutorService executorService = Executors.newFixedThreadPool(
                    executorSize);
        List<Future<Void>> futureList = new CopyOnWriteArrayList<>();
        boolean pendingThread = true;
        do {

            if (queue.size() > 1) {
                IFileMerger fileMerger = getFileMerger(queue);

                Future<Void> future = executorService.submit(() -> {

                    File result = fileMerger.mergeFiles();
                    queue.add(result);

                }, null);
                futureList.add(future);

            }
            pendingThread = false;
            for (Future<Void> futureRegistered : futureList) {
                if (futureRegistered.isDone()) {
                    futureList.remove(futureRegistered);
                }
                pendingThread = pendingThread || !futureRegistered.isDone();
            }

        } while (queue.size() > 1 || pendingThread);

        executorService.shutdown();
        return queue.peek();

    }

    public List<File> invokeFileChunking(File[] files) {

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(
                        executorSize);

            CompletionService<File> executorCompletionService = new ExecutorCompletionService<>(
                        executorService);
            List<Future<File>> futureList = new CopyOnWriteArrayList<>();
            for (File file : files) {
                Future<File> future = executorCompletionService.submit(() -> {
                    FileChunker fileChunker = new FileChunker();
                    return fileChunker.createChunk(file);
                });
                futureList.add(future);
            }
            List<File> outputFilesPostChunk = new ArrayList<>();
            for (int i = 0; i < futureList.size(); i++) {
                File outputFile = executorCompletionService.take().get();
                if (outputFile.isFile()) {
                    outputFilesPostChunk.add(outputFile);
                } else {
                    for (File file : outputFile.listFiles()) {
                        outputFilesPostChunk.add(file);
                    }
                }
            }
            executorService.shutdown();
            return outputFilesPostChunk;
        } catch (Exception exception) {
            logger.error(
                        CustomMessages.EXCEPTION_DURING_FILE_CHUNKING_INVOCATION.concat(
                                    CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                        exception.getMessage());
            throw new FileChunkingException(
                        CustomMessages.EXCEPTION_DURING_FILE_CHUNKING_INVOCATION);
        }

    }
}
