
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.util;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.Program;

//~--- JDK imports ------------------------------------------------------------

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ProgramUtil {
    public static Program makeProgram(ResultSet rs) throws SQLException {
        Program program = new Program();

        program.setId(rs.getLong("ProgramID"));
        program.setName(rs.getString("Name"));
        program.setCreatedDate(rs.getString("Created"));
        program.setModifiedDate(rs.getString("Modified"));

        return program;
    }

    public static Collection<Program> makeProgramList(ResultSet rs) throws SQLException {
        List<Program> programs = new ArrayList<Program>();

        while (rs.next()) {
            programs.add(makeProgram(rs));
        }

        return programs;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
