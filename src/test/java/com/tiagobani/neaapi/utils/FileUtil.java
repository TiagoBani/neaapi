package com.tiagobani.neaapi.utils;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileUtil {

    public static String loadEmployeesWithSpringInternalClass(String filename) throws IOException {
        File resource = ResourceUtils.getFile("classpath:"+filename);
        return new String(Files.readAllBytes(resource.toPath()));
    }

    @Test
    public void shouldCanLoadFile() throws IOException {
        String file = FileUtil.loadEmployeesWithSpringInternalClass("feed_one_date.json");

        assertNotNull(file);
    }
}
