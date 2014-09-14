package com.agrologic.app.service.impl;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkCriteria;
import com.agrologic.app.model.CellinkState;
import com.agrologic.app.service.CellinkManagerService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Valery on 12/08/2014.
 */
public class CellinkManagerServiceImpl implements CellinkManagerService {
    private CellinkDao cellinkDao;

    public CellinkManagerServiceImpl(CellinkDao cellinkDao) {
        this.cellinkDao = cellinkDao;
    }

    @Override
    public void insert(Cellink cellink) throws SQLException {
        cellinkDao.insert(cellink);
    }

    @Override
    public void update(Cellink cellink) throws SQLException {
        cellinkDao.update(cellink);
    }

    @Override
    public void remove(Long id) throws SQLException {
        cellinkDao.remove(id);
    }

    @Override
    public void insert(Collection<Cellink> cellinks) throws SQLException {
        cellinkDao.insert(cellinks);
    }

    @Override
    public Cellink getById(Long id) throws SQLException {
        return cellinkDao.getById(id);
    }

    @Override
    public Cellink validate(String name, String password) throws SQLException {
        return cellinkDao.validate(name, password);
    }

    @Override
    public Cellink getActualCellink() throws SQLException {
        return cellinkDao.getActualCellink();
    }

    @Override
    public int getState(Long id) throws SQLException {
        return cellinkDao.getById(id).getState();
    }

    @Override
    public Timestamp getUpdatedTime(Long id) throws SQLException {
        return cellinkDao.getById(id).getTime();
    }

    @Override
    public int count() throws SQLException {
        return cellinkDao.count();
    }

    @Override
    public int count(CellinkCriteria criteria) throws SQLException {
        return cellinkDao.count(criteria);
    }

    @Override
    public void disconnect(Long id) throws SQLException {
        // if cellink state was just started
        cellinkDao.changeState(id, CellinkState.STATE_START, CellinkState.STATE_STOP);
        // if cellink state was running
        cellinkDao.changeState(id, CellinkState.STATE_START, CellinkState.STATE_STOP);
    }

    @Override
    public Collection<Cellink> getAll() throws SQLException {
        return cellinkDao.getAll();
    }

    @Override
    public Collection<Cellink> getAllUserCellinks(Long userId) throws SQLException {
        return cellinkDao.getAllUserCellinks(userId);
    }

    @Override
    public Collection<Cellink> getAll(CellinkCriteria criteria) throws SQLException {
        return cellinkDao.getAll(criteria);
    }
}
