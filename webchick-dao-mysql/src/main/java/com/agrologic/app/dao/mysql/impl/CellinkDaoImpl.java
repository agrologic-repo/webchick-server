package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.dao.mappers.Util;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkCriteria;
import com.agrologic.app.model.UserRole;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link CellinkDao} that is based on JdbcTemplate and working
 * with database.
 *
 * @author Valery Manakhimov
 */

public class CellinkDaoImpl implements CellinkDao {
        protected final Logger logger = LoggerFactory.getLogger(CellinkDaoImpl.class);
        protected final JdbcTemplate jdbcTemplate;
        protected final SimpleJdbcInsert jdbcInsert;

        public CellinkDaoImpl(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
            this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            this.jdbcInsert.setTableName("cellinks");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insert(Cellink cellink) throws SQLException {
            logger.debug("Inserting cellink with name [{}]", cellink.getName());
            Map<String, Object> valuesToInsert = new HashMap<String, Object>();
            if (cellink.getId() != null) {
                valuesToInsert.put("cellinkid", cellink.getId());
            }
            valuesToInsert.put("name", cellink.getName());
            valuesToInsert.put("password", cellink.getPassword());
            valuesToInsert.put("userid", cellink.getUserId());
            valuesToInsert.put("sim", cellink.getSimNumber());
            valuesToInsert.put("type", cellink.getType());
            valuesToInsert.put("version", cellink.getVersion());
            valuesToInsert.put("time", cellink.getTime());
            valuesToInsert.put("state", cellink.getState());
            valuesToInsert.put("screenid", cellink.getScreenId());
            valuesToInsert.put("actual", cellink.isActual());
            jdbcInsert.execute(valuesToInsert);
        }

//        /**
//         * {@inheritDoc}
//         */
//        @Override
//        public void update(Cellink cellink) throws SQLException {
//        logger.debug("Update cellink with id [{}]", cellink.getId());
//        jdbcTemplate.update("update cellinks set Name=?, Password=?, UserID=?, Time=?,Port=?, SIM=?, Ip=? ,State=?, Version=?,"
//                        + " Actual=?  where CellinkID=?",
//                new Object[]{cellink.getName(), cellink.getPassword(), cellink.getUserId(), cellink.getTime(),
//                        cellink.getPort(), cellink.getSimNumber(), cellink.getIp(), cellink.getState(), cellink.getVersion(), cellink.isActual(),
//                        cellink.getId()});
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Cellink cellink) throws SQLException {
        logger.debug("Update cellink with id [{}]", cellink.getId());
        jdbcTemplate.update("update cellinks set Name=?, Password=?, UserID=?, Time=?,Port=?, SIM=?, Ip=? ,State=?, Version=?,"
                        + " Actual=?, Type=? where CellinkID=?",
                new Object[]{cellink.getName(), cellink.getPassword(), cellink.getUserId(), cellink.getTime(),
                        cellink.getPort(), cellink.getSimNumber(), cellink.getIp(), cellink.getState(), cellink.getVersion(), cellink.isActual(), cellink.getType(),
                        cellink.getId()});
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void update_without_version(Cellink cellink) throws SQLException {
//        logger.debug("Update cellink with id [{}]", cellink.getId());
//        jdbcTemplate.update("update cellinks set Name=?, Password=?, UserID=?, Time=?,Port=?, SIM=?, Ip=? ,State=?,"
//                        + " Actual=?, Type=? where CellinkID=?",
//                new Object[]{cellink.getName(), cellink.getPassword(), cellink.getUserId(), cellink.getTime(),
//                        cellink.getPort(), cellink.getSimNumber(), cellink.getIp(), cellink.getState(), cellink.isActual(), cellink.getType(),
//                        cellink.getId()});
//    }

    public void state_update(Cellink cellink) throws SQLException {
        logger.debug("Update cellink state with id [{}]", cellink.getId());
        jdbcTemplate.update("update cellinks set State=?", new Object[]{cellink.getState()});
    }


        /**
         * {@inheritDoc}
         */
        @Override
        public void remove(Long id) throws SQLException {
            Validate.notNull(id, "Id can not be null");
            logger.debug("Delete cellink with id [{}]", id);
            jdbcTemplate.update("delete from cellinks where CellinkID=?", new Object[]{id});
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void insert(Collection<Cellink> cellinks) throws SQLException {
            // there is duplicate cellink elements in cellinkList we need only unique elements
            Collection<Cellink> cellinkCollection = Util.getUniqueElements(cellinks);
            for (Cellink cellink : cellinkCollection) {
                insert(cellink);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
    public Cellink validate(String name, String password) throws SQLException {
        logger.debug("Get cellink with name [{}]", name);
        String sqlQuery = "select * from cellinks where Name = ? and Password = ?";
        List<Cellink> cellinks = jdbcTemplate.query(sqlQuery, new Object[]{name, password}, RowMappers.cellink());
        if (cellinks.isEmpty()) {
            Cellink cellink = new Cellink();
            cellink.setName(name);
            cellink.setPassword(password);
            return cellink;
        }
        return cellinks.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cellink validate_with_version(String name, String password, String version) throws SQLException {
        logger.debug("Get cellink with name [{}]", name);
        String sqlQuery = "select * from cellinks where Name = ? and Password = ? and Version = ?";
        List<Cellink> cellinks = jdbcTemplate.query(sqlQuery, new Object[]{name, password, version}, RowMappers.cellink());
        if (cellinks.isEmpty()) {
            Cellink cellink = new Cellink();
            cellink.setName(name);
            cellink.setPassword(password);
            cellink.setVersion(version);
            return cellink;
        }
        return cellinks.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cellink getById(Long id) throws SQLException {
        logger.debug("Get cellink with id [{}]", id);
        String sqlQuery = "select * from cellinks where CellinkID=?";
        List<Cellink> cellinks = jdbcTemplate.query(sqlQuery, new Object[]{id}, RowMappers.cellink());
        if (cellinks.isEmpty()) {
            return null;
        }
        return cellinks.get(0);
    }

    @Override
    public Cellink getActualCellink() throws SQLException {
        logger.debug("Get cellink that was set as actual");
        String sqlQuery = "select * from cellinks where actual=1";
        List<Cellink> cellinks = jdbcTemplate.query(sqlQuery, RowMappers.cellink());
        if (cellinks.isEmpty()) {
            return null;
        }
        return cellinks.get(0);
    }

    @Override
    public Collection<Cellink> getAll() throws SQLException {
        logger.debug("Get all cellinks");
        String sqlQuery = "select * from cellinks ";
        return jdbcTemplate.query(sqlQuery, RowMappers.cellink());
    }

    @Override
    public Collection<Cellink> getAllUserCellinks(Long userID) throws SQLException {
        logger.debug("Get cellink belongs to user with id  [{}]", userID);
        String sqlQuery = "select * from cellinks where userid=?";
        return jdbcTemplate.query(sqlQuery, new Object[]{userID}, RowMappers.cellink());
    }

    @Override
    public void changeState(Long cellinkId, Integer oldState, Integer newState) throws SQLException {
        logger.debug("Update cellink state with cellink id [{}]", cellinkId);
        jdbcTemplate.update("update cellinks set state=? where cellinkid=? and state=? ",
                new Object[]{newState, cellinkId, oldState});
    }

    @Override
    public int getState(Long id) throws SQLException {
        return getById(id).getState();
    }

    @Override
    public Timestamp getUpdatedTime(Long id) throws SQLException {
        return getById(id).getTime();
    }

    @Override
    public int count() throws SQLException {
        return count(new CellinkCriteria());
    }

    @Override
    public int count(CellinkCriteria criteria) throws SQLException {
        logger.debug("Count all cellink units by criteria ");
        String sqlQuery;
        Object[] objects = null;

        if (criteria.getRole() != null) {
            UserRole userRole = UserRole.get(criteria.getRole());
            switch (userRole) {
                default:
                case USER:
                    sqlQuery = "select count(*) from cellinks where userid=? ";
                    objects = new Object[]{criteria.getUserId()};
                    break;
                case ADMIN:
                    sqlQuery = "select count(*) from cellinks ";
                    break;
                case DISTRIBUTOR:
                    sqlQuery = "select count(*) from cellinks where userid in (select userid from users where company=?) and " +
                            "name like ?  ";
                    objects = new Object[]{criteria.getCompany(),
                            criteria.getName() == null ? "%%" : "%" + criteria.getName() + "%"};
                    break;
            }
        } else {
            sqlQuery = "select count(*) from cellinks where  "
                    + "(state = ? or ? is null) and "
                    + "type like ? and "
                    + "name like ? limit ?  , 25 ";
            objects = new Object[]{
                    criteria.getState(), criteria.getState(),
                    criteria.getType() == null ? "%%" : "%" + criteria.getType() + "%",
                    criteria.getName() == null ? "%%" : "%" + criteria.getName() + "%",
                    criteria.getIndex() == null ? 0 : criteria.getIndex()};
        }

        if (objects == null ) {
            return jdbcTemplate.queryForObject(sqlQuery, objects , Integer.class);
        } else {
            return jdbcTemplate.queryForObject(sqlQuery, objects , Integer.class);
        }
    }

    @Override
    public Collection<Cellink> getAll(CellinkCriteria criteria) throws SQLException {
        logger.debug("Get all cellinks by criteria ");
        String sqlQuery;
        Object[] objects;

        if (criteria.getRole() != null) {
            UserRole userRole = UserRole.get(criteria.getRole());
            switch (userRole) {
                default:
                case USER:
                    sqlQuery = "select * from cellinks where userid=? ";
                    objects = new Object[]{criteria.getUserId()};
                    break;
                case ADMIN:
                    sqlQuery = "select * from cellinks where  "
                            + "(state = ? or ? is null) and "
                            + "type like ? and "
                            + "name like ?  "
                            + "limit ?  , 25 ";

                    objects = new Object[]{
                            criteria.getState(), criteria.getState(),
                            criteria.getType() == null ? "%%" : "%" + criteria.getType() + "%",
                            criteria.getName() == null ? "%%" : "%" + criteria.getName() + "%",
                            criteria.getIndex() == null ? 0 : criteria.getIndex()};
                    break;
                case DISTRIBUTOR:
                    sqlQuery = "select * from cellinks where userid in (select userid from users where company=?) and " +
                            "name like ?  ";
                    objects = new Object[]{criteria.getCompany(),
                            criteria.getName() == null ? "%%" : "%" + criteria.getName() + "%"};
                    break;
            }
        } else {
            sqlQuery = "select * from cellinks where  "
                    + "(state = ? or ? is null) and "
                    + "type like ? and "
                    + "name like ? limit ?  , 25 ";
            objects = new Object[]{
                    criteria.getState(), criteria.getState(),
                    criteria.getType() == null ? "%%" : "%" + criteria.getType() + "%",
                    criteria.getName() == null ? "%%" : "%" + criteria.getName() + "%",
                    criteria.getIndex() == null ? 0 : criteria.getIndex()};
        }
        return jdbcTemplate.query(sqlQuery, objects, RowMappers.cellink());
    }
}
