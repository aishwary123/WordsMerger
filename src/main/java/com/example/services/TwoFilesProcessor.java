package com.example.services;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.example.exceptions.FileMergingException;
import com.example.messages.CustomMessages;

/**
 * IT WILL PROVIDE {@code TwoFileMerger} AS THE FILE MERGER INSTANCE.S
 * 
 * @author aishwaryt
 */
public class TwoFilesProcessor extends DirectoryProcessor {

    public TwoFilesProcessor(final int executorSize) {
        super(executorSize);
    }

    @Override
    public IFileMerger getFileMerger(BlockingQueue<File> queue) {

        try {
            File file1 = queue.poll();
            File file2 = queue.poll();
            return new TwoFileMerger(file1, file2);
        } catch (IOException ioException) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DURING_FILE_MERGER_INSTANCE_CREATION.concat(
                                    CustomMessages.EXCEPTION_MESSAGE_PLACEHOLDER),
                        ioException.getMessage());
            throw new FileMergingException(
                        CustomMessages.EXCEPTION_DURING_FILE_MERGER_INSTANCE_CREATION);
        }

    }
}
