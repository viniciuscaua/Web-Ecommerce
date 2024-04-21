package com.tads.webprojeto.dominio;

import com.tads.webprojeto.aplicacao.Cliente;

import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ClienteDAO {

    private Connection conexao;

    public ClienteDAO() {
        try {
            conexao = Conexao.getConnection();
        } catch (SQLException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public Cliente verificarLogin(String email, String senha) {
        ResultSet rs = null;
        Cliente c = null;
    
        try {
            String sql = "SELECT * FROM \"clientes\" WHERE email_cliente = ? AND senha_cliente = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
    
            rs = ps.executeQuery();
            if (rs.next()) {
                c = new Cliente(rs.getString("nome_cliente"), rs.getString("email_cliente"), rs.getString("senha_cliente"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    
        return c;
    }

    public void registrarCliente(Cliente cliente) {
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO \"clientes\" (nome_cliente, email_cliente, senha_cliente) VALUES (?, ?, ?)";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getSenha());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}