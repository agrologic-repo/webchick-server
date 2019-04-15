package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 *
 */
public class DataDaoImpl implements DataDao {
    protected final Logger logger = LoggerFactory.getLogger(DataDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public DataDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("datatable");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert(Data data) throws SQLException {
        logger.debug("Inserting data with name [{}]", data.getTitle());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("DataId", data.getId());
        valuesToInsert.put("Type", data.getType());
        valuesToInsert.put("Status", data.isStatus());
        valuesToInsert.put("ReadOnly", data.isReadonly());
        valuesToInsert.put("Title", data.getTitle());
        valuesToInsert.put("Format", data.getFormat());
        valuesToInsert.put("Label", data.getLabel());
        valuesToInsert.put("IsSpecial", data.getSpecial());
        valuesToInsert.put("IsRelay", data.getIsRelay());
        valuesToInsert.put("HistoryOpt", data.getHistoryOpt());
        valuesToInsert.put("HistoryDnum", data.getHistoryDNum());
        jdbcInsert.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Data data) throws SQLException {
        logger.debug("update data with id [{}]", data.getId());
        String sql = "update datatable set Label=? where DataID=?";
        jdbcTemplate.update(sql, new Object[]{data.getLabel(), data.getId()});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long dataId) throws SQLException {
        logger.debug("remove data with id [{}]", dataId);
        String sql = "delete from datatable where DataID=?";
        jdbcTemplate.update(sql, new Object[]{dataId});
    }

    @Override
    public void insert(final Collection<Data> dataCollection) throws SQLException {
        // there is duplicate data elements in dataList we need only unique elements
        Collection<Data> uniqueDataList = Util.getUniqueElements(dataCollection);
        String sql = "INSERT INTO DATATABLE "
                + "(DATAID, TYPE, STATUS, READONLY, TITLE, FORMAT, LABEL, ISRELAY, ISSPECIAL, HISTORYOPT, HISTORYDNUM) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Data> dadaList = new ArrayList(uniqueDataList);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Data data : dadaList) {
            if (data.getTitle() == null){
                int test = 0;
            }
            Object[] values = new Object[]{
                    data.getId(),
                    data.getType(),
                    data.isStatus(),
                    data.getReadonly(),
                    data.getTitle(),
                    data.getFormat(),
                    data.getLabel(),
                    data.getIsRelay(),
                    data.getSpecial(),
                    data.getHistoryOpt(),
                    data.getHistoryDNum()
            };
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    @Override
    public void insertSpecialList(List<Data> specialList, Long programId, Long langId) throws SQLException {
        // there is duplicate data elements in dataList we need only unique elements
        Collection<Data> uniqueDataList = Util.getUniqueElements(specialList);
        String sql = "INSERT INTO SPECIALDATALABELS (DATAID, PROGRAMID, LANGID, SPECIALLABEL ) VALUES (?, ?, ?, ?)";
        List<Data> dadaList = new ArrayList(uniqueDataList);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Data data : dadaList) {
            Object[] values = new Object[]{
                    data.getId(),
                    programId,
                    langId,
                    data.getUnicodeLabel()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTranslation(Collection<Data> dataList) throws SQLException {
        String sql = "insert into databylanguage values (?, ?, ?) ";
        List<Data> dadaList = new ArrayList(dataList);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Data data : dadaList) {
            Object[] values = new Object[]{
                    data.getId(),
                    data.getLangId() == null ? 1L : data.getLangId(),
                    data.getUnicodeLabel()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTableData(Long tableId, Long screenId, Long programId, Collection<Data> dataList) throws SQLException {
        String sql = "INSERT INTO TABLEDATA (DATAID, TABLEID, SCREENID, PROGRAMID, DISPLAYONTABLE, POSITION ) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        List<Data> dadaList = new ArrayList(dataList);
        List<Object[]> batch = new ArrayList<Object[]>();
        for (Data data : dadaList) {
            Object[] values = new Object[]{
                    data.getId(),
                    tableId,
                    screenId,
                    programId,
                    data.getDisplay(),
                    data.getPosition()};
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertDataToTable(Long programId, Long screenId, Long tableId, Long dataId, String display,
                                  Integer position) throws SQLException {
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("DataId", dataId);
        valuesToInsert.put("TableId", tableId);
        valuesToInsert.put("ScreenId", screenId);
        valuesToInsert.put("ProgramId", programId);
        valuesToInsert.put("displayontable", display);
        valuesToInsert.put("Position", position);
        SimpleJdbcInsert jdbcInsertDataTable = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsertDataTable.setTableName("tabledata");
        jdbcInsertDataTable.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertDataList(Long newProgramId, Long oldProgramId) throws SQLException {
        String sql = "insert into tabledata (DataID,TableID,ScreenID,ProgramID,DisplayOnTable,Position ) "
                + "(select DataID,TableID, ScreenID, ?, DisplayOnTable, Position from tabledata "
                + "where ProgramID=?)";

        jdbcTemplate.update(sql, newProgramId, oldProgramId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertSpecialData(Long programId, Long dataId, Long langId, String label) throws SQLException {
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("DataId", dataId);
        valuesToInsert.put("ProgramId", programId);
        valuesToInsert.put("LangId", langId);
        valuesToInsert.put("SpecialLabel", label);
        SimpleJdbcInsert jdbcInsertSpecialTable = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsertSpecialTable.setTableName("specialdatalabels");
        jdbcInsertSpecialTable.execute(valuesToInsert);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertDataTranslation(Long dataId, Long langId, String translate) throws SQLException {
//        String sql = "insert into databylanguage values (?,?,?) ";
        String sql = "insert into databylanguage values (?,?,?) on duplicate key update UnicodeLabel=values(UnicodeLabel)";
        jdbcTemplate.update(sql, dataId, langId, translate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearControllerData(Long controllerId) throws SQLException {
        String sql = "delete from controllerdata where controllerid=? ";
        jdbcTemplate.update(sql, controllerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveData(Long screenId, Long programId, Long tableId) throws SQLException {
        String sql = "update tabledata set screenid=? where programid=? and tableid=? ";
        jdbcTemplate.update(sql, screenId, programId, tableId);
    }

    @Override
    public void migrate(String statment) throws SQLException {
        jdbcTemplate.update(statment);
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//         public Data getById(Long dataId) throws SQLException, EmptyResultDataAccessException {
//        String sql = "select * from datatable where dataid=?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{dataId}, RowMappers.data());
//    }

    @Override
    public Data getById(Long dataId) throws SQLException{
        String sql = "select * from datatable where dataid=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{dataId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data getById(Long dataId, Long langId) throws SQLException {
        String sql = "select * from datatable inner join databylanguage on datatable.DataID=databylanguage.DataID "
                + "and datatable.DataID=? and databylanguage.LangID=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{dataId, langId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data getGrowDay(Long controllerId) throws SQLException {
        String sql = "select * from datatable as d inner join" +
                " (select DataID, value from controllerdata where ControllerID=?) "
                + "as cd on d.DataID=cd.DataID and d.DataID=800";
        List<Data> result = jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
        if(result.size() != 0){ //added 13/08/2017
            return result.get(0);
        } else { //added 13/08/2017
            return null; //added 13/08/2017
        }// added 13/08/2017
    }

    @Override
    public Data getResetTime(Long controllerId) {
        String sql = "select * from datatable as d inner join" +
                " (select DataID, value from controllerdata where ControllerID=?) "
                + "as cd on d.DataID=cd.DataID and d.DataID=3009";
        List<Data> result = jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
        if(result.size() != 0){
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Data get_reset_time_by_flock_id(Long flockId) {
        String sql = "select * from datatable as d inner join (select DataID, value from controllerdata where ControllerID=(SELECT controllerid FROM flocks where flockid=?)) as cd on d.DataID=cd.DataID and d.DataID=3009";
        List<Data> result = jdbcTemplate.query(sql, new Object[]{flockId}, RowMappers.data());
        if(result.size() != 0){
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data getSetClockByController(Long controllerId) throws SQLException {
        String sql = "select * from datatable "
                + " inner join controllerdata on datatable.DataID=controllerdata.DataID"
                + " and datatable.DataID= 1309 and controllerdata.ControllerID=?";
        List<Data> result = jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public Data getSetDateByController(long controllerId) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data getChangedDataValue(Long controllerId) throws SQLException {
        String sql = "select * from datatable as d inner join " +
                "(select DataID, value from newcontrollerdata where ControllerID=?) "
                + "as cd on d.DataID=cd.DataID";
        List<Data> res = jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
        if (res.isEmpty()) {
            return null;
        }
        return res.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> find(Long type) throws SQLException {
        String sql = "select * from datatable where type  like ?";
        return jdbcTemplate.query(sql, new Object[]{"%" + type + "%"}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getAll() throws SQLException {
        String sql = "select * from datatable";
        return jdbcTemplate.query(sql, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getRelays() throws SQLException {
        return getSpecial("Relays");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getAlarms() throws SQLException {
        return getSpecial("Alarms");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getSystemStates() throws SQLException {
        return getSpecial("System States");
    }

    //*****************************************************************************************************
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getSpecial(String string) throws SQLException {
        String sql = "select * from datatable where IsSpecial in "
                + "(select ID from Special where Text=?)";
        return jdbcTemplate.query(sql, new Object[]{string}, RowMappers.data());
    }
    //*****************************************************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getTableDataList(Long programId, Long screenId, Long tableId, String display)
            throws SQLException {
        String sql = "select datatable.DataID , datatable.Type ,datatable.Status , datatable.ReadOnly, " +
                "datatable.Title,datatable.Format ,datatable.Label ,datatable.IsSpecial ,datatable.IsRelay , " +
                "tabledata.TableID,tabledata.ProgramID ,tabledata.ScreenID ,tabledata.DisplayOnTable, " +
                "tabledata.Position from tabledata,datatable " +
                "where tabledata.dataid=datatable.dataid and tabledata.programid=? and " +
                "tabledata.screenid=? and tabledata.tableid=? and tabledata.DisplayOnTable like ? order by position asc ";

        return jdbcTemplate.query(sql, new Object[]{programId, screenId, tableId,
                        (display == null) ? "%%" : "%" + display + "%"},
                RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getTableDataList(Long programId, Long screenId, Long tableId, Long langId, String display)
            throws SQLException {
        String sql = "select * from tabledata "
                + "left join specialdatalabels on specialdatalabels.dataid=tabledata.dataid "
                + "and specialdatalabels.programid=tabledata.programid "
                + "and specialdatalabels.langid=? "
                + "left join datatable on datatable.dataid=tabledata.dataid "
                + "left join databylanguage on databylanguage.dataid=tabledata.dataid and databylanguage.langid=? "
                + "where tabledata.programid=? "
                + "and tabledata.screenid=? and tabledata.displayontable  like ? and tableid=? order by position";
        return jdbcTemplate.query(sql, new Object[]{langId, langId, programId, screenId,
                (display == null) ? "%%" : "%" + display + "%", tableId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getHistoryDataList() throws SQLException {
        String sql = "select * from datatable "
                + "inner join databylanguage on datatable.DataID=databylanguage.DataID"
                + " and databylanguage.LangID=1 and datatable.isspecial=5 order by datatable.DataID";
        return jdbcTemplate.query(sql, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getHistoryDataListByCriteria(String criteria, Long langId) throws SQLException {
        String sql = "select * from datatable  as dt " +
                "left join databylanguage as dbl on dbl.dataid=dt.dataid and dbl.langid=? where historyopt like ?";
        return jdbcTemplate.query(sql, new Object[]{langId, "%" + criteria + "%"}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getAllWithTranslation() throws SQLException {
        String sql = " select * from datatable left join databylanguage on datatable.dataid=databylanguage.dataid "
                + "order by datatable.dataid ";
        return jdbcTemplate.query(sql, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getControllerDataValues(Long controllerId) throws SQLException {
        String sql = "select * from datatable as dt inner join controllerdata "
                + "as cd on dt.dataid=cd.dataid and cd.controllerid=?";
        return jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int removeControllerDataValues(Long controllerId) throws SQLException { // added 13/09/2017
        String sql = "delete from controllerdata where controllerid=?";
        return jdbcTemplate.update(sql, controllerId);
    }// added 13/09/2017


//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public Collection<Data> getPerHourHistoryDataByControllerValues(Long controllerId) throws SQLException {
//        String sql = "select * from datatable where dataid in "
//                + "(select dataid from controllerdata  where controllerid=? )"
//                + " and historyopt like '%HOUR%' and historydnum <>'' order by type";
//        return jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getPerHourHistoryDataByControllerValues(Long controllerId) throws SQLException {
        String sql = "select * from datatable where dataid in "
                + "(select dataid from controllerdata  where controllerid=? )"
                + " and historyopt like '%HOUR%' and historydnum <>'' order by type";
        return jdbcTemplate.query(sql, new Object[]{controllerId}, RowMappers.data());
    }

    @Override
    public Collection<Data> getPerHourHistoryData() throws SQLException {
        String sql = "select * from datatable where historyopt like '%HOUR%'";
        return jdbcTemplate.query(sql, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, Long> getUpdatedControllerDataValues(Long controllerId) throws SQLException {
        String sql = "select * from controllerdata where controllerid=?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, new Object[]{controllerId});
        Map<Long, Long> resultMap = new HashMap<Long, Long>();
        for (Map<String, Object> m : results) {
            Object id = m.get("DATAID");
            Object val = m.get("VALUE");
            Long dataId = Long.parseLong(id.toString());
            Long value = Long.parseLong(val.toString());
            resultMap.put(dataId, value);
        }
        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getOnlineTableDataList(Long controllerId, Long programId, Long screenId, Long tableId, Long langId) throws SQLException {
        String sql = "select * from datatable "
                + "inner join tabledata on tabledata.programid=? "
                + " and tabledata.screenid=? "
                + " and tabledata.tableid=? "
                + " and tabledata.dataid=datatable.dataid  "
                + " and tabledata.displayontable='yes'"
                + " left join databylanguage on datatable.dataid=databylanguage.dataid and databylanguage.langid=? "
                + " left join(select * from specialdatalabels where programid=? and langid=?)"
                + " as sdl on sdl.DataID=datatable.DataID"
                + " inner join "
                + " controllerdata on controllerdata.dataid=datatable.dataid and controllerdata.controllerid=? "
                + " order by position";
        return jdbcTemplate.query(sql, new Object[]{programId, screenId, tableId, langId, programId, langId, controllerId},RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getOnScreenDataList(Long programId, Long screenId, Long tableId, Long langId)
            throws SQLException {
        logger.debug("Get data by program with id [{}], screen id [{}] , table id [{}] and language id [{}] ",
                new Object[]{programId, screenId, tableId, langId,});
        String sql = "select * from tabledata as td "
                + "left join datatable on datatable.dataid=td.dataid "
                + "left join databylanguage on datatable.dataid=databylanguage.dataid and databylanguage.langid=? "
                + "left join specialdatalabels on specialdatalabels.dataid=td.dataid "
                + "and specialdatalabels.programid=td.programid and specialdatalabels.langid=? "
                + "where td.programid=? "
                + "and td.screenid=? "
                + "and td.tableid=? "
                + "and td.displayontable='yes' "
                + "order by td.position ";
        return jdbcTemplate.query(sql, new Object[]{langId, langId, programId, screenId, tableId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uncheckNotUsedDataOnAllScreens(Long programId, Long controllerId) throws SQLException {
        String sql = "update tabledata set displayontable='no' where programid=? and dataid not in "
                + " (select dataid from controllerdata where controllerid=?)";
        jdbcTemplate.update(sql, programId, controllerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDataFromTable(Long programId) throws SQLException {
        String sql = "delete from tabledata where ProgramID=?";
        jdbcTemplate.update(sql, programId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDataFromTable(Long programId, Long screenId, Long tableId) throws SQLException {
        String sql = "delete from tabledata where ProgramID=? and ScreenID=? and TableID=?";
        jdbcTemplate.update(sql, programId, screenId, tableId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeDataFromTable(Long programId, Long screenId, Long tableId, Long dataId) throws SQLException {
        String sql = "delete from tabledata where ProgramID=? and ScreenID=? and TableID=? and DataID=?";
        jdbcTemplate.update(sql, programId, screenId, tableId, dataId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSpecialDataFromTable(Long programId, Long dataId) throws SQLException {
        String sql = "delete from specialdatalabels where ProgramID=? and DataID=?";
        jdbcTemplate.update(sql, programId, dataId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveChanges(final Long programId, final Long screenId, final Long tableId, final Map<Long, String> showMap,
                            Map<Long, Integer> positionMap) throws SQLException {
        String sql = "update tabledata set DisplayOnTable=?, Position=? where DataID=? and TableID=? and ScreenId=? " +
                "and ProgramId=?";
        final List<Long> dataIds = new ArrayList(showMap.size());
        final List<String> showFlags = new ArrayList(showMap.size());
        final List<Integer> positions = new ArrayList(showMap.size());
        Set<Long> keys = showMap.keySet();
        for (Long dataId : keys) {
            dataIds.add(dataId);
            showFlags.add(showMap.get(dataId));
            positions.add(positionMap.get(dataId));
        }

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, showFlags.get(i));
                ps.setInt(2, positions.get(i));
                ps.setLong(3, dataIds.get(i));
                ps.setLong(4, tableId);
                ps.setLong(5, screenId);
                ps.setLong(6, programId);
            }

            @Override
            public int getBatchSize() {
                return showMap.size();
            }
        });
    }

    @Override
    public void moveUp(Long programId, Long screenId, Long tableId, Long dataId, Integer position) throws SQLException {
        String sqlPrevData = "select * from tabledata td join datatable  dt on td.dataid=dt.dataid " +
                " where td.programid=? and td.screenid=? and td.tableid=? and td.position=?;";

        Integer tempPos = position;
        while (tempPos > 0) {
            List<Data> result = jdbcTemplate.query(sqlPrevData, new Object[]{programId, screenId, tableId, tempPos - 1}, RowMappers.data());
            if (result.size() > 0) {
                Data prevPosData = result.get(0);

                String sqlChangePosActualData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
                jdbcTemplate.update(sqlChangePosActualData, new Object[]{tempPos - 1, programId, screenId, tableId, dataId});

                String sqlChangePosPrevData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
                jdbcTemplate.update(sqlChangePosPrevData, new Object[]{position, programId, screenId, tableId, prevPosData.getId()});
                break;
            } else {
                tempPos--;
            }
        }

//        List<Data> result = jdbcTemplate.query(sqlPrevData, new Object[]{programId, screenId, tableId, position - 1}, RowMappers.data());
//        if (result.size() > 0) {
//            Data prevPosData = result.get(0);
//
//            String sqlChangePosActualData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
//            jdbcTemplate.update(sqlChangePosActualData, new Object[]{position - 1, programId, screenId, tableId, dataId});
//
//            String sqlChangePosPrevData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
//            jdbcTemplate.update(sqlChangePosPrevData, new Object[]{position, programId, screenId, tableId, prevPosData.getId()});
//        }
    }

    @Override
    public void moveDown(Long programId, Long screenId, Long tableId, Long dataId, Integer position) throws SQLException {
        String sqlPrevData = "select * from tabledata  td join datatable  dt on td.dataid=dt.dataid " +
                "where td.programid=? and td.screenid=? and td.tableid=? and td.position=?;";

        Integer tempPos = position;
        while (tempPos > 0) {
            List<Data> result = jdbcTemplate.query(sqlPrevData, new Object[]{programId, screenId, tableId, tempPos + 1}, RowMappers.data());
            if (result.size() > 0) {
                Data nextPosData = result.get(0);
                String sqlChangePosNextData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
                jdbcTemplate.update(sqlChangePosNextData, new Object[]{position, programId, screenId, tableId, nextPosData.getId()});

                String sqlChangePosActualData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
                jdbcTemplate.update(sqlChangePosActualData, new Object[]{tempPos + 1, programId, screenId, tableId, dataId});
                break;
            } else {
                tempPos++;
            }
        }

//        List<Data> result = jdbcTemplate.query(sqlPrevData, new Object[]{programId, screenId, tableId, position + 1}, RowMappers.data());
//        if (result.size() > 0) {
//            Data nextPosData = result.get(0);
//            String sqlChangePosNextData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
//            jdbcTemplate.update(sqlChangePosNextData, new Object[]{position, programId, screenId, tableId, nextPosData.getId()});
//
//            String sqlChangePosActualData = "update tabledata set position=? where programid=? and screenid=? and tableid=? and dataid=? and displayontable='yes'";
//            jdbcTemplate.update(sqlChangePosActualData, new Object[]{position + 1, programId, screenId, tableId, dataId});
//
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getProgramDataRelays(Long programId) throws SQLException {
        String sql = "select * from datatable where DataID in " +
                "(select distinct DataID  from programrelays where ProgramID=?)";

        return jdbcTemplate.query(sql, new Object[]{programId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getProgramDataAlarms(Long programId) throws SQLException {
        String sql = "select * from datatable where DataID in "
                + "(select distinct DataID from programalarms where ProgramID=?)";
        return jdbcTemplate.query(sql, new Object[]{programId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getProgramDataSystemStates(Long programId) throws SQLException {
        String sql = "select * from datatable where DataID in "
                + "(select distinct DataID  from programsysstates where ProgramID=?)";
        return jdbcTemplate.query(sql, new Object[]{programId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getSpecialData(Long programId, Long langId) throws SQLException {
        String sql = "select * from specialdatalabels "
                + "inner join datatable on datatable.dataid=specialdatalabels.dataid "
                + "where programId=? and langId=? ";
        return jdbcTemplate.query(sql, new Object[]{programId, langId}, RowMappers.data());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void copySpecialData(Long newProgramId, Long selectedProgramId) {
        logger.debug("Insert special data ");
        final String sqlQuery = "insert into specialdatalabels (DataID,ProgramID,LangID,SpecialLabel) " +
                " (select DataID,?,LangID,SpecialLabel from specialdatalabels where programid=?)";
        jdbcTemplate.update(sqlQuery, new Object[]{newProgramId, selectedProgramId});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Data> getAllBySpecial(Integer special) throws SQLException {
        String sql = "select * from datatable where isspecial = ?";
        return jdbcTemplate.query(sql, new Object[]{special}, RowMappers.data());
    }

    @Override
    public Data get_data_by_d_num(String d_num) throws SQLException {

        String sql = "SELECT * FROM datatable where historydnum LIKE '%;"+ d_num +"%' or historydnum like '%"+ d_num +";%' or historydnum like '"+ d_num +"'";

        List<Data> result = jdbcTemplate.query(sql, new Object[]{}, RowMappers.data());
        if(result.size() > 1){
            return null;
        }

        return result.get(0);
    }

    @Override
    public Data get_data_by_d_num_with_unicode_l(String d_num, Long lang_id) throws SQLException {

        String sql = "select * from datatable left join databylanguage on datatable.DataID=databylanguage.DataID " +
                "and databylanguage.langid="+ lang_id + " where datatable.historydnum LIKE '%;"
                + d_num +"' or historydnum like '%"+ d_num +";%' or historydnum like '"+ d_num + "'";
        logger.info(sql);
        List<Data> result = jdbcTemplate.query(sql, new Object[]{}, RowMappers.data());
        if(result.size() > 1){
            return null;
        }

        return result.get(0);
    }

//    @Override
//    public Data getById(Long dataId, Long langId) throws SQLException {
//        String sql = "select * from datatable inner join databylanguage on datatable.DataID=databylanguage.DataID "
//                + "and datatable.DataID=? and databylanguage.LangID=?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{dataId, langId}, RowMappers.data());
//    }



    //select * from datatable inner join databylanguage on datatable.DataID=databylanguage.DataID and datatable.DataID=(SELECT dataid FROM datatable where historydnum LIKE '%;D20%' or historydnum like '%D20;%' or historydnum like 'D20') and databylanguage.LangID=1

    public String get_label_by_d_num(String d_num)throws SQLException{

        String sql = "SELECT label FROM datatable where historydnum LIKE '%;"+ d_num +"%' or historydnum like '%"+ d_num +";%' or historydnum like '"+ d_num +"'";

        List<String> result = jdbcTemplate.query(sql, new Object[]{}, RowMappers.data_string());

        if (result.size() == 1){
            return result.get(0);
        }

        return null;
    }
}
