
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package help.examples.xml;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class Employee {
    private String department;
    private String designation;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Name : " + this.name + "\nDesignation : " + this.designation + "\nDepartment : " + this.department;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
