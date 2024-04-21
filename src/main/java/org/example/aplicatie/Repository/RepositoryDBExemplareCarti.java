package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.ExemplarCarte;
import org.example.aplicatie.Domain.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBExemplareCarti implements RepositoryExemplarCarte{
    private RepositoryCarte repositoryCarti;
    private UtilsDB dbUtils;

    public RepositoryDBExemplareCarti(Properties props, RepositoryCarte repositoryCarti) {
        this.dbUtils = new UtilsDB(props);
        this.repositoryCarti = repositoryCarti;
    }

    @Override
    public List<ExemplarCarte> findExemplarByCarte(Integer idCarte) {
        Connection con = dbUtils.getConnection();
        List<ExemplarCarte> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from ExemplareCarti where id_carte=?")) {
            preStmt.setInt(1, idCarte);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    var idEntity = result.getInt("id_exemplar");
                    var carte = repositoryCarti.findOne(idCarte).get();
                    Status statut = Status.valueOf(result.getString("status"));
                    String cod_exemplar = result.getString("cod_exemplar");
                    ExemplarCarte utilizator = new ExemplarCarte(carte, cod_exemplar, statut);
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
    public List<ExemplarCarte> findExemplareDisponibile(Integer idCarte) {
        Connection con = dbUtils.getConnection();
        List<ExemplarCarte> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from ExemplareCarti where id_carte=? and status='DISPONIBIL'")) {
            preStmt.setInt(1, idCarte);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    var idEntity = result.getInt("id_exemplar");
                    var carte = repositoryCarti.findOne(idCarte).get();
                    Status statut = Status.valueOf(result.getString("status"));
                    String cod_exemplar = result.getString("cod_exemplar");
                    ExemplarCarte utilizator = new ExemplarCarte(carte, cod_exemplar, statut);
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
    public List<ExemplarCarte> findExemplareImprumutate(Integer idCarte) {
        Connection con = dbUtils.getConnection();
        List<ExemplarCarte> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from ExemplareCarti where id_carte=? and status='IMPRUMUTAT'")) {
            preStmt.setInt(1, idCarte);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    var idEntity = result.getInt("id_exemplar");
                    var carte = repositoryCarti.findOne(idCarte).get();
                    Status statut = Status.valueOf(result.getString("status"));
                    String cod_exemplar = result.getString("cod_exemplar");
                    ExemplarCarte utilizator = new ExemplarCarte(carte, cod_exemplar, statut);
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
    public Optional<ExemplarCarte> save(ExemplarCarte entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into ExemplareCarti(id_carte, status, cod_exemplar) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setInt(1, entity.getCarte().getId());
            preStmt.setString(2, entity.getStatus().toString());
            preStmt.setString(3, entity.getCodExemplar());
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
    public Optional<ExemplarCarte> update(ExemplarCarte entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE ExemplareCarti SET id_carte = ?, status = ?, cod_exemplar = ? WHERE id_exemplar=?")) {
            preStmt.setInt(1, entity.getCarte().getId());
            preStmt.setString(2, entity.getStatus().toString());
            preStmt.setString(3, entity.getCodExemplar());
            preStmt.setInt(4, entity.getId());
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
    public Optional<ExemplarCarte> delete(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        var utilizator = findOne(idEntity);
        try (PreparedStatement preStmt = con.prepareStatement("delete from ExemplareCarti where id_exemplar=?")) {
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            return utilizator;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ExemplarCarte> findOne(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from ExemplareCarti where id_exemplar=?")) {
            preStmt.setInt(1, idEntity);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    var idCarte = result.getInt("id_carte");
                    var carte = repositoryCarti.findOne(idCarte).get();
                    Status statut = Status.valueOf(result.getString("status"));
                    String cod_exemplar = result.getString("cod_exemplar");
                    ExemplarCarte utilizator = new ExemplarCarte(carte, cod_exemplar, statut);
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
    public List<ExemplarCarte> findAll() {
        Connection con = dbUtils.getConnection();
        List<ExemplarCarte> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from ExemplareCarti")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idEntity = result.getInt("id_exemplar");
                    var idCarte = result.getInt("id_carte");
                    var carte = repositoryCarti.findOne(idCarte).get();
                    Status statut = Status.valueOf(result.getString("status"));
                    String cod_exemplar = result.getString("cod_exemplar");
                    ExemplarCarte utilizator = new ExemplarCarte(carte, cod_exemplar, statut);
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
