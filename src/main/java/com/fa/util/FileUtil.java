package com.fa.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    public static boolean createIfNotExisting(File file) {
        if (file.exists()) {
            return true;
        }

        try {
            Files.createFile(file.toPath());
        } catch (IOException e) {
            LOG.error("Could not create file.", e);
            return false;
        }

        return true;
    }

    public static void deleteDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    Files.delete(file.toPath());
                    LOG.info("File was deleted: " + file);
                }
            }
        }

        Files.delete(directory.toPath());
        LOG.info("Directory was deleted: " + directory);
    }

}
