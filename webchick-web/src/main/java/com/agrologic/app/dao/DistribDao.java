
package com.agrologic.app.dao;


import com.agrologic.app.model.DistribDto;
import java.sql.SQLException;
import java.util.List;

public interface DistribDao {

    public void insert(DistribDto gas) throws SQLException;

    public void remove(Long id) throws SQLException;

    public DistribDto getById(Long id) throws SQLException;

    public List<DistribDto> getAllByFlockId(Long flockId) throws SQLException;
}



