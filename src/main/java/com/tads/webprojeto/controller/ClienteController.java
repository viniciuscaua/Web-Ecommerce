package com.tads.webprojeto.controller;

import com.tads.webprojeto.dominio.ProdutoDAO;
import com.tads.webprojeto.aplicacao.Produto;
import org.springframework.stereotype.Controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.DecimalFormat;

@Controller
public class ClienteController {

    @RequestMapping(value = "/listarProdutosCliente", method = RequestMethod.GET)
    public void getAllProdutosCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listarProdutos();

        response.setContentType("text/html");
        var writer = response.getWriter();
        writer.println("<html> <head> <title> Lista de Produtos </title> <style>" +
                "table { border-collapse: collapse; width: 80%; margin: 0 auto; }" + 
                "th, td { border: 1px solid black;}" +
                "th" + "button { margin-top: 20px; margin-left: 10px}" +
                "</style></head> <body> <h2 style=\"text-align: center;\">Lista de Produtos</h2> <table>");

        writer.println("<tr>");
        writer.println("<th>Nome</th>");
        writer.println("<th>Descrição</th>");
        writer.println("<th>Preço</th>");
        writer.println("<th>Estoque</th>");
        writer.println("<th>Carrinho</th>");
        writer.println("</tr>");

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (Produto produto : produtos) {
            writer.println("<tr>");
            writer.println("<td>" + produto.getNome() + "</td>");
            writer.println("<td>" + produto.getDescricao() + "</td>");
            writer.println("<td>R$ " + decimalFormat.format(produto.getPreco()) + "</td>");
            if (produto.getQuantidade() == 0){
                writer.println("<td>Sem Estoque</td>");
            } else {
                writer.println("<td>" + produto.getQuantidade() + "</td>");
            }
            writer.println("<td><a href='/carrinhoServlet?id=" + produto.getId() + "&comando=add'>Adicionar</a></td>");

            //writer.println("<td><a href='carrinhoServlet/id=" + produto.getId() + "/comando=add' title='http://localhost:8080/carrinhoServlet?id=" + produto.getId() + "?comando=add'>Adicionar</a></td>");
            writer.println("</tr>");
        }

        writer.println("<tr>");
        writer.println("<td colspan=\"5\" style=\"text-align: center;\">");
        writer.println("<button onclick=\"window.location.href='verCarrinho'\">Ver Carrinho</button>");
        writer.println("<button onclick=\"window.location.href='homeCliente.html'\">Voltar para Home</button></td>");
        writer.println("</tr>");

        writer.println("</body> </html>");

    }

}
