package com.fa;

import com.fa.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;


public class AppEnv {

    private static final Logger LOG = Logger.getLogger(AppEnv.class);

    private static File dataDirectory;

    public static synchronized File getFlashcardInfoFile() {
        File file = new File(dataDirectory, "FlashcardsInfo.xml");
        boolean fileExists = FileUtil.createIfNotExisting(file);
        if (!fileExists) {
            LOG.error("Could not create file.");
            return null;
        }
        return file;
    }

    public static synchronized File getFlashcardStatsFile() {
        File file = new File(dataDirectory, "FlashcardsStats.xml");
        boolean fileExists = FileUtil.createIfNotExisting(file);
        if (!fileExists) {
            LOG.error("Could not create file.");
            return null;
        }
        return file;
    }

    public static void setDataDirectory(File file) {
        dataDirectory = file;
    }

    public static File getDataDirectory() {
        return dataDirectory;
    }
}
