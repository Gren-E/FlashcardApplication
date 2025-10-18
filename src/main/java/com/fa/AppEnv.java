package com.fa;

import com.fa.data.Profile;
import com.fa.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;

public class AppEnv {

    private static final Logger LOG = Logger.getLogger(AppEnv.class);

    private static File dataDirectory;

    public static void setDataDirectory(File file) {
        dataDirectory = file;
    }

    public static File getDataDirectory() {
        return dataDirectory;
    }

    public static File getProfileDirectory(Profile profile) {
        return new File(getDataDirectory(), "Profile_" + profile.getId());
    }

    public static File getFlashcardInfoFile(Profile profile) {
        return getXmlFile("FlashcardsInfo.xml", profile);
    }

    public static File getFlashcardStatsFile(Profile profile) {
        return getXmlFile("FlashcardsStats.xml", profile);
    }

    public static File getDailyActivityFile(Profile profile) {
        return getXmlFile("DailyActivity.xml", profile);
    }

    public static File getBoxesFile(Profile profile) {
        return getXmlFile("Boxes.xml", profile);
    }

    public static File getProfileFile() {
        return getXmlFile("Profiles.xml", null);
    }

    private static synchronized File getXmlFile(String fileName, Profile profile) {
        File directory = (profile == null) ? dataDirectory : getProfileDirectory(profile);
        File file = new File(directory, fileName);
        boolean fileExists = FileUtil.createIfNotExisting(file);
        if (!fileExists) {
            LOG.error("Could not create file: " + fileName);
            return null;
        }

        return file;
    }
}
