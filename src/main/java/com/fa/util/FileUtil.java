package com.fa.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    public static boolean createIfNotExisting(File file) {
        if (!file.exists()) {
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                LOG.error("Could not create file.", e);
                return false;
            }
        }

        return true;
    }

}
