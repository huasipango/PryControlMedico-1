package com.medico.app.web.models.services;


import com.medico.app.web.models.dao.IDosisDAO;
import com.medico.app.web.models.entities.Dosis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DosisService implements IDosisService{

    @Autowired
    private IDosisDAO dao;

    @Override
    @Transactional
    public void save(Dosis medicamento) {
        // TODO Auto-generated method stub
        dao.save(medicamento);
    }

    @Override
    @Transactional
    public Dosis findById(Integer id) {
        // TODO Auto-generated method stub
        return dao.findById(id).get();
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        // TODO Auto-generated method stub
        dao.deleteById(id);
    }

    @Override
    @Transactional
    public List<Dosis> findAll() {
        // TODO Auto-generated method stub
        return (List<Dosis>) dao.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public List<Dosis> findByIdDetalleReceta(Integer idDetalleReceta) {
        return dao.findByIdDetalleReceta(idDetalleReceta);
    }

    @Override
    @Transactional
    public List<Dosis> findNotTakenPills(Integer idDetalleReceta) {
        return dao.findNotTakenPills(idDetalleReceta);
    }
}