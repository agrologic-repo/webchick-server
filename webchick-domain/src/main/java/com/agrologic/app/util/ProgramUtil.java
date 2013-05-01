package com.agrologic.app.util;

import com.agrologic.app.model.Program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


