package com.ucl.js;


import com.ucl.js.document.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TokenizerTest {

    Map<String, String> properties = new HashMap<>();

    @Before
    public void init() {
        properties.put("sourceDirectoryPath", "/home/mrhmisu/UCL-MS/Test-Data/index/react-master/scripts");
        properties.put("headerFilePath", "/home/mrhmisu/UCL-MS/Test-Data/header.file");
        properties.put("tokenFilePath", "/home/mrhmisu/UCL-MS/Test-Data/token.file");
        properties.put("granularity", "function");
        properties.put("language", "javascript");
        properties.put("maximumLine", "5000");
        properties.put("minimumLine", "6");
        properties.put("maximumToken", "5000");
        properties.put("minimumToken", "5");
        properties.put("numberOfThread", "4");
    }

    @Test
    public void configurationTest() {
        Configuration configuration = Executor.getConfiguration(this.properties);
        if (Executor.checkConfiguration(configuration)) {
            Executor.executeTokenizer(configuration);
        }
    }

    @Test
    public void strip() {
        String str = "\"id\"";
        String strippedToken = str.replaceAll("(\'|\"|\\\\|:)", "");
        System.out.println(strippedToken);
    }

}
