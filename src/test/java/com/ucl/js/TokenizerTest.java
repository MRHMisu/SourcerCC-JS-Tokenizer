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
        properties.put("headerFilePath", "/home/mrhmisu/UCL-MS/Test-Data/js-tokenizer/header.file");
        properties.put("tokenFilePath", "/home/mrhmisu/UCL-MS/Test-Data/js-tokenizer/token.file");
        properties.put("granularity", "function");
        properties.put("language", "javascript");
        properties.put("maximumLines", "5000");
        properties.put("minimumLines", "6");
        properties.put("maximumTokens", "5000");
        properties.put("minimumTokens", "6");
        properties.put("numberOfThreads", "4");
    }

    @Test
    public void configurationTest() {
        Configuration configuration = Executor.getConfiguration(this.properties);
        if (Executor.validateConfiguration(configuration)) {
            Executor.executeTokenizer(configuration);
        }
    }


}
