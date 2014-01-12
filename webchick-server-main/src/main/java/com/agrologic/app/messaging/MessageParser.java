package com.agrologic.app.messaging;

import com.agrologic.app.model.Data;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.StringTokenizer;

public class MessageParser {
    private Data dataToSend;
    private HashMap<Long, Data> parsedDataMap;
    private Logger logger = Logger.getLogger(MessageParser.class);
    public static final int HIGH_16BIT_ON_MASK = 0x8000;
    public static final int HIGH_32BIT_OFF_MASK = 0x0000FFFF;
    public static final int SHIFT_16_BIT = 16;
    public static final int MINIMUM_TOKENS = 2;

    public MessageParser(HashMap<Long, Data> parsedDataMap) {
        this.parsedDataMap = parsedDataMap;
    }

    public HashMap<Long, Data> getParsedDataMap() {
        return parsedDataMap;
    }

    public void setDataToSend(Data dataToSend) {
        this.dataToSend = dataToSend;
    }

    public Data getDataToSend() {
        return dataToSend;
    }

    /**
     * Update online data when request was to change value in controller.
     *
     * @param response the response message
     */
    public void parseShortResponse(Message response, Message request) {
        StringTokenizer token = new StringTokenizer(response.toString(), " ");

        LOOP:
        while (token.countTokens() >= MINIMUM_TOKENS) {// loop for running in
            try {
                DataValueParser dataValueParser = new DataValueParser(token).invoke();
                long receivedDataId = dataValueParser.getReceivedDataId();
                int receivedValue = dataValueParser.getReceivedValue();

                // find specified data and update the value
                if (parsedDataMap.containsKey(receivedDataId)) {
                    int sentValue = getValueFromRequestMessage(request);
                    if (sentValue == receivedValue) {
                        // here we actually must to update data
                        parsedDataMap.get(receivedDataId).setValue((long) receivedValue);
                    }
                }
            } catch (Exception e) {
                logger.error("Exception generated in method " +
                        "parseShortResponse(Message response, boolean skipDiagnostic)", e);
            }
        }
    }

    /**
     * Returns value of data, that was sent to the controller in order to change the controller data .
     *
     * @param request the request
     * @return the value
     */
    private int getValueFromRequestMessage(Message request) {
        int fs = request.toString().indexOf(" ");
        int ls = request.toString().lastIndexOf(" ");
        String rv = request.toString().substring(fs + 1, ls);
        return Integer.parseInt(rv);
    }

    /**
     * Parsing response data and skip diagnostic data pair that is in last pairs. If the response came also with
     * diagnostic , the diagnostic pair skipped .Diagnostic data intended to display the amount of data that have been
     * changed since previous request.
     *
     * @param response       the response to be parsing
     * @param withDiagnostic true if response hold diagnostic data, otherwise false
     */
    public void parseResponse(Message response, boolean withDiagnostic) {
        StringTokenizer token = new StringTokenizer(response.toString(), " ");

        LOOP:
        while (token.countTokens() >= MINIMUM_TOKENS) {// loop for running in
            if (withDiagnostic == true && token.countTokens() == MINIMUM_TOKENS) {
                break;
            }
            try {
                DataValueParser dataValueParser = new DataValueParser(token).invoke();
                long receivedDataId = dataValueParser.getReceivedDataId();
                int receivedValue = dataValueParser.getReceivedValue();

                if (parsedDataMap.containsKey(receivedDataId)) {
                    if (parsedDataMap.get(receivedDataId).isLongType()) {
                        receivedValue = dataValueParser.invoke(token, receivedValue).getReceivedValue();
                    }
                    // here we actually must to update data
                    parsedDataMap.get(receivedDataId).setValue((long) receivedValue);
                }
            } catch (Exception e) {
                logger.error("Exception generated in method " +
                        "parseResponse(Message response, boolean skipDiagnostic)", e);
            }
        }
    }

    /**
     * Create fixed response string of history data in order to bug on Image II controller . The bug that the data is
     * not sending in the correct format .
     *
     * @param response the response
     * @return fixed response string
     */
    public String parseHistoryIfComProtocolBinary(Message response) {
        StringBuilder fixedResponse = new StringBuilder();
        String[] stringToFix = response.toString().split(" ");
        for (int i = 0; i < stringToFix.length; i++) {
            if ((i != 1) && ((i - 1) % 3) != 0) {
                fixedResponse.append(stringToFix[i]).append(" ");
            }
        }
        return fixedResponse.toString();
    }

    /**
     * Create fixed response string of history 24 hour data in order to bug on Image II controller . The bug that the
     * data is not sending in the correct format .
     *
     * @param response the response
     * @return fixed response string
     */
    public String parseHistory24IfComProtocolBinary(Message response) {
        StringBuilder fixedResponse = new StringBuilder();
        String[] stringToFix = response.toString().split(" ");
        if (stringToFix.length == 2) {
            fixedResponse.append(stringToFix[1]).append(" ");
        } else if (stringToFix.length >= 48) {
            for (int i = 0; i < stringToFix.length; i++) {
                if (((i + 1) % 2) == 0) {
                    fixedResponse.append(stringToFix[i]).append(" ");
                }
            }
        }
        return fixedResponse.toString();
    }

    /**
     * Return value after two's compliment operation .
     *
     * @param val the number
     * @return number after two's compliment operation
     */
    private int twosCompliment(int val) {
        int tVal = val;
        if (tVal != -1) {
            //two's compliment action
            tVal = Math.abs(tVal);
            tVal = ~tVal;
            tVal &= HIGH_32BIT_OFF_MASK;
            tVal += 1;
        }
        return tVal;
    }

    /*
     * DataValueParser is inner helper class that used to parsing response to the pair variables (data-value pair).
     * The class also provides parsing method that use to parse long data .
     */
    private class DataValueParser {
        private StringTokenizer token;
        private long receivedDataId;
        private int receivedValue;

        public DataValueParser(StringTokenizer token) {
            this.token = token;
        }

        public long getReceivedDataId() {
            return receivedDataId;
        }

        public int getReceivedValue() {
            return receivedValue;
        }

        public DataValueParser invoke() {
            String dataIdString = token.nextToken();               // get key data
            String valueString = token.nextToken();                // get value data

            receivedDataId = Long.parseLong(dataIdString);
            receivedValue = Integer.parseInt(valueString);

            if (((int) receivedDataId & 0xC000) != 0xC000) {
                receivedDataId = ((int) receivedDataId & 0xFFF);   // remove type to get an index 4096&0xFFF -> 0
            } else {
                receivedDataId = ((int) receivedDataId & 0xFFFF);
            }
            return this;
        }

        public DataValueParser invoke(StringTokenizer token, int receivedValue) {
            String valueString;
            token.nextToken();// skip this key

            int highValue = receivedValue;
            boolean negative = ((highValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
            if (negative) {
                // two's compliment action
                highValue = twosCompliment(highValue);
            }
            highValue <<= SHIFT_16_BIT;
            valueString = token.nextToken();// get low value

            int lowValue = Integer.parseInt(valueString);
            negative = ((lowValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
            if (negative) {
                // two's compliment action
                lowValue = twosCompliment(lowValue);
            }
            this.receivedValue = highValue + lowValue;
            return this;
        }
    }
}
