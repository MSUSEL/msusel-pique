package pique.utility;

import java.io.FileReader;
import java.util.Properties;

public class PiqueProperties {

    public static Properties getProperties(){

        Properties prop = new Properties();
        try {
            prop.load(new FileReader("src/main/resources/pique-properties.properties"));

        }catch(Exception e){
            e.printStackTrace();
        }
        return prop;
    }
}
