package com.tads.webprojeto.dominio;

import com.tads.webprojeto.aplicacao.Produto;

import org.springframework.stereotype.Repository;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {

    private Connection conexao;

    public ProdutoDAO() {
        try {
            conexao = Conexao.getConnection();
        } catch (SQLException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
    
    private static final String SQL_BUSCAR_TODOS = "SELECT id, nome, descricao, preco, quantidade FROM \"produtos\"";
    private static final String SQL_DELETAR_PRODUTO = "DELETE FROM \"produtos\" WHERE id = ?";
    private static final String SQL_ALTERAR_PRODUTO = "UPDATE \"produtos\" SET nome = ?, descricao = ?, preco = ?, quantidade = ? WHERE id = ?";

    public List<Produto> buscarTodos() {
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_TODOS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                Produto produto = new Produto(id, nome, descricao, preco, quantidade);
                produtos.add(produto);
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public List<Produto> listarProdutos() {
        List<Produto> listaProdutos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT * FROM \"produtos\"";
            ps = conexao.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade"),
                        rs.getString("descricao"));
                listaProdutos.add(produto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return listaProdutos;
    }

    public void inserirProduto(Produto produto) {
        String SQL_INSERIR_PRODUTO = "INSERT INTO \"produtos\" (nome, descricao, preco, quantidade) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR_PRODUTO)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());

            stmt.executeUpdate();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void deletarProduto(int idProduto) {
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETAR_PRODUTO)) {
            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
    }

    public void alterarProduto(Produto produto) {
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ALTERAR_PRODUTO)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
    }

    public Produto buscarPorId(int idProduto) {
        String SQL_BUSCAR_POR_ID = "SELECT id, nome, descricao, preco, quantidade FROM \"produtos\" WHERE id = ?";
        Produto produto = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setInt(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String descricao = rs.getString("descricao");
                    double preco = rs.getDouble("preco");
                    int quantidade = rs.getInt("quantidade");

                    produto = new Produto(id, nome, descricao, preco, quantidade);
                }
            }
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
            // Lidar com exceções adequadamente
        }
        return produto;
    }

    public int buscarQuantidade(int id) {
        int quantidade = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT quantidade FROM produtos WHERE id = ?";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                quantidade = rs.getInt("quantidade");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(ps, rs);
        }

        return quantidade;
    }

    public void updateQuantidade(int id, int quantidade) {
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE produtos SET quantidade = ? WHERE id = ?";
            ps = conexao.prepareStatement(sql);
            ps.setInt(1, quantidade);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeResources(ps);
        }
    }

    // Adicione outros métodos conforme necessário

    private void closeResources(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void closeResources(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}