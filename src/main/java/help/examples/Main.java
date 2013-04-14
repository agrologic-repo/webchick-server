/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package help.examples;

import java.util.Arrays;
import java.util.List;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class Main {
    public static void foo() throws MyException {
        System.out.print("I am foo");
    }
    public static void main(String[] args) {
        foo();
        Sub s = new Sub();
        s.foo();

        List<String> list = Arrays.asList("Red","Blue", "Green");
        for(String color:list) {
            System.out.println(color);
        }

    }
}
