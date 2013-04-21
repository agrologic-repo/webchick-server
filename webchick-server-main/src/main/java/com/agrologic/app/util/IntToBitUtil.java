
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.BitSet;

/**
 * Title: IntToBitUtil <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * @version     1.0 <br>
 */
public class IntToBitUtil {
    public static int[] bits2Ints(BitSet bs) {
        int[] temp = new int[bs.size() / 32];

        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < 32; j++) {
                if (bs.get(i * 32 + j)) {
                    temp[i] |= 1 << j;
                }
            }
        }

        return temp;
    }

    public static String toBinary(int num) {
        StringBuilder sb     = new StringBuilder();
        int           number = num;

        for (int i = 0; i < 32; i++) {
            sb.append(((num & 1) == 1)
                      ? '1'
                      : '0');
            number >>= 1;
        }

        return sb.reverse().toString();
    }

//    public static void main(String[] arg) {
//        int    num  = 2910;
//        String bits = toBinary(num);
//
//        System.out.println(bits);
//        System.out.println();
//
//        BitSet bs = new BitSet();
//
//        bs.set(31);
//        bs.set(15);
//        bs.set(18);
//
//        int[] intArray = bits2Ints(bs);
//
//        for (int i = 0; i < intArray.length; i++) {
//            System.out.println(toBinary(intArray[i]));
//        }
//    }
}


//~ Formatted by Jindent --- http://www.jindent.com
