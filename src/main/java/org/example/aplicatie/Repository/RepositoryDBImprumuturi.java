package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Cititor;
import org.example.aplicatie.Domain.Imprumut;
import org.example.aplicatie.Domain.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBImprumuturi implements RepositoryImprumut {
    private UtilsDB dbUtils;
    private RepositoryUtilizator<Cititor> repositoryCititori;
    private RepositoryExemplarCarte repositoryExemplareCarti;

    public RepositoryDBImprumuturi(Properties propertiesDB, RepositoryUtilizator<Cititor> cititorRepositoryUtilizator, RepositoryExemplarCarte repositoryExemplareCarti) {
        dbUtils = new UtilsDB(propertiesDB);
        repositoryCititori = cititorRepositoryUtilizator;
        this.repositoryExemplareCarti = repositoryExemplareCarti;
    }

    @Override
    public Optional<Imprumut> returneazaImprumut(Integer idImprumut) {
        var imprumut = findOne(idImprumut).get();
        imprumut.setReturnat(true);
        return update(imprumut);
    }

    @Override
    public List<Imprumut> getImprumuturiNereturnate() {
        Connection con = dbUtils.getConnection();
        List<Imprumut> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Imprumuturi where returnata=false")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    var idEntity = result.getInt("id_imprumut");
                    var idCititor = result.getInt("id_cititor");
                    var cititor = repositoryCititori.findOne(idCititor).get();
                    var idExemplar = result.getInt("id_exemplar");
                    var exemplar = repositoryExemplareCarti.findOne(idExemplar).get();
                    LocalDate dataImprumut = result.getDate("data_imprumut").toLocalDate();
                    boolean returnat = result.getBoolean("returnata");
                    Imprumut utilizator = new Imprumut(cititor, exemplar, dataImprumut, returnat);
                    utilizator.setId(idEntity);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilizatori;
    }

    @Override
    public Boolean eImprumutat(Integer idExemplarCarte) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Imprumuturi where id_exemplar=? and returnata=false")) {
            preStmt.setInt(1, idExemplarCarte);
            try (ResultSet result = preStmt.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Imprumut> getImprumuturiByExemplar(Integer idExemplarCarte) {
        Connection con = dbUtils.getConnection();
        List<Imprumut> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Imprumuturi where id_exemplar=?")) {
            preStmt.setInt(1, idExemplarCarte);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    var idEntity = result.getInt("id_imprumut");
                    var idCititor = result.getInt("id_cititor");
                    var cititor = repositoryCititori.findOne(idCititor).get();
                    var idExemplar = result.getInt("id_exemplar");
                    var exemplar = repositoryExemplareCarti.findOne(idExemplar).get();
                    LocalDate dataImprumut = result.getDate("data_imprumut").toLocalDate();
                    boolean returnat = result.getBoolean("returnata");
                    Imprumut utilizator = new Imprumut(cititor, exemplar, dataImprumut, returnat);
                    utilizator.setId(idEntity);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilizatori;
    }

    @Override
    public Optional<Imprumut> save(Imprumut entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Imprumuturi(id_cititor, id_exemplar, data_imprumut, returnata) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setInt(1, entity.getCititor().getId());
            preStmt.setInt(2, entity.getExemplarCarte().getId());
            preStmt.setDate(3, java.sql.Date.valueOf(entity.getDataImprumut()));
            preStmt.setBoolean(4, entity.getReturnat());
            int result = preStmt.executeUpdate();
            try (ResultSet generatedKeys = preStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }
            if(result == 1){
                return Optional.of(entity);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Imprumut> update(Imprumut entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Imprumuturi SET id_cititor=?, id_exemplar=?, data_imprumut=?, returnata=? WHERE id_imprumut=?")) {
            preStmt.setInt(1, entity.getCititor().getId());
            preStmt.setInt(2, entity.getExemplarCarte().getId());
            preStmt.setDate(3, java.sql.Date.valueOf(entity.getDataImprumut()));
            preStmt.setBoolean(4, entity.getReturnat());
            preStmt.setInt(5, entity.getId());
            int result = preStmt.executeUpdate();
            if(result == 1){
                return Optional.of(entity);
            }
            else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Imprumut> delete(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        var utilizator = findOne(idEntity);
        try (PreparedStatement preStmt = con.prepareStatement("delete from Imprumuturi where id_imprumut=?")) {
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            return utilizator;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Imprumut> findOne(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Imprumuturi where id_imprumut=?")) {
            preStmt.setInt(1, idEntity);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    var idCititor = result.getInt("id_cititor");
                    var cititor = repositoryCititori.findOne(idCititor).get();
                    var idExemplar = result.getInt("id_exemplar");
                    var exemplar = repositoryExemplareCarti.findOne(idExemplar).get();
                    LocalDate dataImprumut = result.getDate("data_imprumut").toLocalDate();
                    boolean returnat = result.getBoolean("returnata");
                    Imprumut utilizator = new Imprumut(cititor, exemplar, dataImprumut, returnat);
                    utilizator.setId(idEntity);
                    return Optional.of(utilizator);
                }
                else{
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Imprumut> findAll() {
        Connection con = dbUtils.getConnection();
        List<Imprumut> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Imprumuturi")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    var idEntity = result.getInt("id_imprumut");
                    var idCititor = result.getInt("id_cititor");
                    var cititor = repositoryCititori.findOne(idCititor).get();
                    var idExemplar = result.getInt("id_exemplar");
                    var exemplar = repositoryExemplareCarti.findOne(idExemplar).get();
                    LocalDate dataImprumut = result.getDate("data_imprumut").toLocalDate();
                    boolean returnat = result.getBoolean("returnata");
                    Imprumut utilizator = new Imprumut(cititor, exemplar, dataImprumut, returnat);
                    utilizator.setId(idEntity);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilizatori;
    }

}
