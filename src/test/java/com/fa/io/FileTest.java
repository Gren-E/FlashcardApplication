package com.fa.io;

import com.fa.AbstractTest;
import com.fa.AppEnv;
import com.fa.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FileTest extends AbstractTest {

    @Test
    public void fileTest() throws IOException {
        File file = new File(AppEnv.getDataDirectory(), "Test.txt");
        Assertions.assertTrue(FileUtil.createIfNotExisting(file));
        FileUtil.deleteDirectory(file);
        Assertions.assertFalse(file.exists());
    }
}
