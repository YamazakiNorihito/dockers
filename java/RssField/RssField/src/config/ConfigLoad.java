package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import model.ConfigRss;

import java.util.HashMap;

public class ConfigLoad {
    ConfigRss _resultConfig = null;
    InputStream inputStream;

    public ConfigRss getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "config/resources/config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            _resultConfig = new ConfigRss();
            _resultConfig.maxTitleLength = Integer.parseInt(prop.getProperty("titlelen"));
            _resultConfig.maxBodyLength = Integer.parseInt(prop.getProperty("bodylen"));

            String[] reps = prop.getProperty("replaces").split(",");
            _resultConfig.Replaces = new HashMap<>();
            for (String repConf : reps) {
                String[] repSet = repConf.split("/");
                _resultConfig.Replaces.put(repSet[0], repSet[1]);
            }

        } finally {
            inputStream.close();
            inputStream = null;
        }
        return _resultConfig;
    }
}
