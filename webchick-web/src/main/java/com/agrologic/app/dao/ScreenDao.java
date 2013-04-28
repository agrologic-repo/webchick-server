package com.agrologic.app.dao;

import com.agrologic.app.model.ScreenDto;



import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public interface ScreenDao {
    public void insert(ScreenDto screen) throws SQLException;

    public void update(ScreenDto screen) throws SQLException;

    public void remove(Long programId, Long screenId) throws SQLException;

    public void insertExistScreen(ScreenDto screen) throws SQLException;

    public void insertExistActionSetScrean(ScreenDto screen) throws SQLException;

    /**
     * Insert into screen table all screens from old program to new program .
     * @param newProgramId the new program id
     * @param oldProgramId the old program id , the program id that was selected by user.
     * @throws SQLException if failed to insert the screens from the database.
     */
    public void insertDefaultScreens(Long newProgramId, Long oldProgramId) throws SQLException;

    public void insertTranslation(Long screenId, Long langId, String translation) throws SQLException;

    public void saveScreens(Map<Long, String> screens, Long programId) throws SQLException;

    public ScreenDto getById(Long programId, Long screenId) throws SQLException;

    public ScreenDto getById(Long programId, Long screenId, Long langId) throws SQLException;

    public ScreenDto getById(Long programId, Long screenId, Long langId, boolean showAll) throws SQLException;

    public void saveChanges(Map<Long, String> showMap, Map<Long, Integer> positionMap, Long programId)
            throws SQLException;

    public int getNextScreenPosByProgramId(Long programId) throws SQLException;

    public Long getSecondScreenAfterMain(Long programId) throws SQLException;

    public List<ScreenDto> getAllByProgramId(Long programId) throws SQLException;

    public List<ScreenDto> getAllScreensByProgramAndLang(Long programId, Long langId, boolean showAll)
            throws SQLException;
}



