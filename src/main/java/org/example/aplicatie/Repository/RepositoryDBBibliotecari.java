package org.example.aplicatie.Repository;

import org.example.aplicatie.Domain.Bibliotecar;
import org.example.aplicatie.Domain.Cititor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class RepositoryDBBibliotecari implements RepositoryUtilizator<Bibliotecar>{
    private UtilsDB dbUtils;

    public RepositoryDBBibliotecari(Properties props) {
        this.dbUtils = new UtilsDB(props);
    }

    @Override
    public Optional<Bibliotecar> findByUsername(String username) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Bibliotecari where USERNAME=?")) {
            preStmt.setString(1, username);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int idEntity = result.getInt("ID_BIBLIOTECAR");
                    String nume = result.getString("NUME");
//                    String username = result.getString("USERNAME");
                    String password = result.getString("PAROLA");
                    String adresa = result.getString("ADRESA");
                    String telefon = result.getString("TELEFON");
                    String cnp = result.getString("CNP");
                    Bibliotecar utilizator = new Bibliotecar(username, password, nume, telefon, cnp, adresa);
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
    public Optional<Bibliotecar> findByUsernameAndPassword(String username, String password) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Bibliotecari where USERNAME=? AND PAROLA=?")) {
            preStmt.setString(1, username);
            preStmt.setString(2, password);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int idEntity = result.getInt("ID_BIBLIOTECAR");
                    String nume = result.getString("NUME");
//                    String username = result.getString("USERNAME");
//                    String password = result.getString("PAROLA");
                    String adresa = result.getString("ADRESA");
                    String telefon = result.getString("TELEFON");
                    String cnp = result.getString("CNP");
                    Bibliotecar utilizator = new Bibliotecar(username, password, nume, telefon, cnp, adresa);
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
    public Optional<Bibliotecar> save(Bibliotecar entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Bibliotecari(USERNAME, PAROLA, NUME, ADRESA, TELEFON, CNP) values (?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setString(3, entity.getNume());
            preStmt.setString(4, entity.getAdresa());
            preStmt.setString(5, entity.getTelefon());
            preStmt.setString(6, entity.getCnp());
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
    public Optional<Bibliotecar> update(Bibliotecar entity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Bibliotecari SET username = ?, parola = ?, NUME = ?, adresa = ?, telefon = ?, cnp = ? WHERE id_bibliotecar=?")) {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setString(3, entity.getNume());
            preStmt.setString(4, entity.getAdresa());
            preStmt.setString(5, entity.getTelefon());
            preStmt.setString(6, entity.getCnp());
            preStmt.setInt(7, entity.getId());
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
    public Optional<Bibliotecar> delete(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        var utilizator = findOne(idEntity);
        try (PreparedStatement preStmt = con.prepareStatement("delete from Bibliotecari where id_bibliotecar=?")) {
            preStmt.setInt(1, idEntity);
            int result = preStmt.executeUpdate();
            return utilizator;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Bibliotecar> findOne(Integer idEntity) {
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Bibliotecari where id_bibliotecar=?")) {
            preStmt.setInt(1, idEntity);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String nume = result.getString("NUME");
                    String username = result.getString("USERNAME");
                    String password = result.getString("PAROLA");
                    String adresa = result.getString("ADRESA");
                    String telefon = result.getString("TELEFON");
                    String cnp = result.getString("CNP");
                    Bibliotecar utilizator = new Bibliotecar(username, password, nume, telefon, cnp, adresa);
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
    public List<Bibliotecar> findAll() {
        Connection con = dbUtils.getConnection();
        List<Bibliotecar> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Bibliotecari")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idEntity = result.getInt("ID_CITITOR");
                    String nume = result.getString("NUME");
                    String username = result.getString("USERNAME");
                    String password = result.getString("PAROLA");
                    String adresa = result.getString("ADRESA");
                    String telefon = result.getString("TELEFON");
                    String cnp = result.getString("CNP");
                    Bibliotecar utilizator = new Bibliotecar(username, password, nume, telefon, cnp, adresa);
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
