package org.example.aplicatie.Repository.DBRepository;

import org.example.aplicatie.Domain.Carte;
import org.example.aplicatie.Repository.RepositoryCarte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBCarti implements RepositoryCarte {
    private UtilsDB dbUtils;
    
    public RepositoryDBCarti(Properties props) {
        this.dbUtils = new UtilsDB(props);
    }
    
    @Override
    public List<Carte> findCarteTitluAutor(String titlu, String autor) {
        Connection con = dbUtils.getConnection();
        List<Carte> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Carti where autor like ? and titlu like ?")) {
            preStmt.setString(1, "%" + autor + "%");
            preStmt.setString(2, "%" + titlu + "%");
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idEntity = result.getInt("ID_Carte");
                    String titlu2 = result.getString("TITLU");
                    String autor2 = result.getString("AUTOR");
                    Carte utilizator = new Carte(titlu2, autor2);
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
    public Optional<Carte> save(Carte entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Carti(TITLU, AUTOR) values (?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getTitlu());
            preStmt.setString(2, entity.getAutor());
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
    public Optional<Carte> update(Carte entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Carti SET titlu = ?, autor = ? WHERE id_Carte=?")) {
            preStmt.setString(1, entity.getTitlu());
            preStmt.setString(2, entity.getAutor());
            preStmt.setInt(3, entity.getId());
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
    public Optional<Carte> delete(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        var utilizator = findOne(idEntity);
        try (PreparedStatement preStmt = con.prepareStatement("delete from Carti where id_Carte=?")) {
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            return utilizator;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Carte> findOne(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Carti where id_Carte=?")) {
            preStmt.setInt(1, idEntity);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String titlu = result.getString("TITLU");
                    String autor = result.getString("AUTOR");
                    Carte utilizator = new Carte(titlu, autor);
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
    public List<Carte> findAll() {
        Connection con = dbUtils.getConnection();
        List<Carte> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Carti")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idEntity = result.getInt("ID_Carte");
                    String titlu = result.getString("TITLU");
                    String autor = result.getString("AUTOR");
                    Carte utilizator = new Carte(titlu, autor);
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
