package com.agrologic.app.utils;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class Unicode2ASCII {
    public static String toHTML(String paramString) {
        String str1        = paramString;
        String str2        = "";
        char[] arrayOfChar = str1.toCharArray();

        for (int i = 0; i < arrayOfChar.length; ++i) {
            char c = arrayOfChar[i];

            if (c > 255) {
                str2 = str2 + "&#" + c + ";";
            } else {
                str2 = str2 + c;
            }
        }

        return str2;
    }

    public static String toJAVA(String paramString) {
        String str1        = paramString;
        String str2        = "";
        char[] arrayOfChar = str1.toCharArray();

        for (int i = 0; i < arrayOfChar.length; ++i) {
            char c = arrayOfChar[i];

            if (c > 255) {
                String str3 = Integer.toHexString(c);

                if (str3.length() < 4) {
                    str3 = "0" + str3;
                }

                str2 = str2 + "\\u" + str3;
            } else {
                str2 = str2 + c;
            }
        }

        return str2;
    }

    public static String fromHTMLToJava(String paramString) {
        String str1         = paramString;
        String str2         = "";
        String ignoreChars  = "&#x";
        String replaceChars = "-";

        str2 = str1.replaceAll(ignoreChars, replaceChars);

//      ignoreChars = "x";
//      replaceChars = "u";
//      str2 = str2.replaceAll(ignoreChars, replaceChars);
        str2 = str2.replaceAll(";", "");

        String str4        = "";
        char[] arrayOfChar = str2.toCharArray();

        for (int i = 0; i < arrayOfChar.length; ++i) {
            char c = arrayOfChar[i];

            if (c == '-') {
                str4 = str4 + "\\u";
            } else {
                str4 = str4 + c;
            }
        }

        byte[] buf = str4.getBytes();
        String s   = new String(buf, 0, buf.length);

        return str4;
    }

    public static void writeOutput(String str) {
        try {
            FileOutputStream fos = new FileOutputStream("test.txt");
            Writer           out = new OutputStreamWriter(fos, "UTF8");

            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//  public static void writeProperty(String str) {
//
//      Locale locale = Locale.US;
//      String s = locale.toString();
//      String fileName = "graphs_"+ s + ".properties";
//      //graph_en_US.properties
//      PropertyFileUtil.setProperty(fileName, "", "label", str);
//
//  }
    public static String readInput() {
        StringBuilder buffer = new StringBuilder();

        try {
            FileInputStream   fis = new FileInputStream("test.txt");
            InputStreamReader isr = new InputStreamReader(fis, "UTF8");
            Reader            in  = new BufferedReader(isr);
            int               ch;

            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }

            in.close();

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Translates the given String into ASCII code.
     *
     * @param input the input which contains native characters like umlauts etc
     * @return the input in which native characters are replaced through ASCII code
     */
    public static String asciiToNative(String input) {
        if (input == null) {
            return null;
        }

        StringBuffer buffer             = new StringBuffer(input.length());
        boolean      precedingBackslash = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (precedingBackslash) {
                switch (c) {
                case 'f' :
                    c = '\f';

                    break;

                case 'n' :
                    c = '\n';

                    break;

                case 'r' :
                    c = '\r';

                    break;

                case 't' :
                    c = '\t';

                    break;

                case 'u' :
                    String hex = input.substring(i + 1, i + 5);

                    c = (char) Integer.parseInt(hex, 16);
                    i += 4;
                }

                precedingBackslash = false;
            } else {
                precedingBackslash = (c == '\\');
            }

            if (!precedingBackslash) {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    public static void main(String[] args) {

        // String jaString = new String("\u05e9\u05dc\u05d5\u05dd");
        String jaString = new String("&#x05e9;&#x05dc;&#x05d5;&#x05dd;");

        jaString = Unicode2ASCII.fromHTMLToJava(jaString);
        writeOutput(jaString);

        String inputString   = readInput();
        String displayString = jaString + " " + inputString;

        new ShowString(displayString, "Conversion Demo");
    }
}



