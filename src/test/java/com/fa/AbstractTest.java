package com.fa;

import com.fa.io.DataManager;
import com.fa.io.XMLTest;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.net.URL;

public class AbstractTest {

    @BeforeEach
    public void setup() {
        File file = new File("C:\\Workspace\\Java\\FlashcardApplication\\build\\resources\\test");
        file.delete();
        URL dataUrl = XMLTest.class.getResource("/testData");
        AppEnv.setDataDirectory(new File(dataUrl.getFile()));
        DataManager.setCurrentProfile(DataManager.getProfile(1));
    }

}
