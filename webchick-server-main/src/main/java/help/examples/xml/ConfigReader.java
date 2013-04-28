
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package help.examples.xml;

//~--- non-JDK imports --------------------------------------------------------

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ConfigReader {
    String appender       = null;
    String datasourcename = null;
    String ipaddress      = null;
    String logfilename    = null;

    @Override
    public String toString() {

        // TODO Auto-generated method stub
        return "Datasource Name : " + datasourcename + " \nIP Address : " + ipaddress + " \nLogfilename : "
               + logfilename + " \nAppender : " + appender;
    }

    /**
     * @param args
     * @throws FileNotFoundException
     */
//    public static void main(String[] args) throws FileNotFoundException {
//        XStream         xs  = new XStream(new DomDriver());
//        FileInputStream fis = new FileInputStream("c:/rajiv/sample.xml");
//
//        xs.aliasField("datasource-name", ConfigReader.class, "datasourcename");
//        xs.alias("config", ConfigReader.class);
//
//        ConfigReader r = (ConfigReader) xs.fromXML(fis);
//
//        System.out.println(r.toString());
//    }
}



