package com.fa.mode.exam;

import com.fa.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExamBuilderTest extends AbstractTest {

    @Test
    public void examBuilderTest() {
        ExamBuilder builder = new ExamBuilder();
        Assertions.assertThrows(UnsupportedOperationException.class, builder::build);

        builder.setDurationInMinutes(5).setNumberOfQuestions(10).setCategories(new String[] {"AnotherCategory"});
        Assertions.assertThrows(UnsupportedOperationException.class, builder::build);
        builder.setNumberOfQuestions(5);
        Assertions.assertDoesNotThrow(builder::build);
    }
}
