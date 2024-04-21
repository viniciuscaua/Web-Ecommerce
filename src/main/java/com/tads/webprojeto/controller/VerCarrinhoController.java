package com.tads.webprojeto.controller;

import com.tads.webprojeto.aplicacao.CarrinhoStorage;
import com.tads.webprojeto.aplicacao.Produto;
import com.tads.webprojeto.dominio.ProdutoDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class VerCarrinhoController {

    @GetMapping(value = "/verCarrinho")
    public void verCarrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {

        var writer = response.getWriter();
        Cookie[] cookies = request.getCookies();
        String valorCookie = "";
        boolean vazio = true;
        double total = 0;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CarrinhoStorage.cookieCarrinho)) {
                    valorCookie = cookie.getValue();
                    vazio = false;
                    break;
                }
            }
        }
        if (vazio) {
            response.sendRedirect("/listaProdutos?msg=Carrinho vazio");
            return;
        }

        writer.println("<html><head><title>Seu carrinho:</title><style>" +
                "table { border-collapse: collapse; width: 80%; margin: 20px auto; }" + 
                "th, td { border: 1px solid black; padding: 8px; text-align: left; }" +
                "th { background-color: #f2f2f2; }" +
                "button { margin: 10px; padding: 8px 16px; font-size: 14px; }" +
                "</style></head><body>" +
                "<h2 style=\"text-align: center;\">Carrinho</h2>" +
                "<table>" +
                "<tr>" +
                "<th>Nome</th>" +
                "<th>Preço</th>" +
                "<th>Quantidade</th>" +
                "<th>Remover</th>" +
                "</tr>");

        Map<Integer, Integer> contagemIds = new HashMap<>();
        String[] ids = valorCookie.split("_");
        for (String id : ids) {
            int intId = Integer.parseInt(id);

            contagemIds.put(intId, contagemIds.getOrDefault(intId, 0) + 1);
        }

        /*for (Map.Entry<Integer, Integer> entry : contagemIds.entrySet()) {
            int id = entry.getKey();
            int quantidadeRepetida = entry.getValue();

            Produto p;
            ProdutoDAO pDAO = new ProdutoDAO();
            p = pDAO.buscarProdutoPorId(id);
            total += p.getPreco() * quantidadeRepetida;
            int estoque = pDAO.buscarQuantidade(id);
            writer.println("<tr><td>" + p.getNome() + "</td><td>R$ " + p.getPreco() + "</td><td>");

            if (quantidadeRepetida == estoque) {
                writer.println( + quantidadeRepetida +
                        "<td><a href='/gerenciarCarrinhoFromVerCarrinho?id=" + p.getId()
                        + "&comando=remove'>Remover</a></td></tr>");
            } else {
                writer.println(+ quantidadeRepetida + "</td>" +
                        "<td><a href='/gerenciarCarrinhoFromVerCarrinho?id=" + p.getId() + "&comando=remove'>Remover</a></td></tr>");
            }

            CarrinhoStorage.idsCarrinho.add(p.getId());
            CarrinhoStorage.quantidadeProdutosCarrinho.add(quantidadeRepetida);
        }*/

        for (Map.Entry<Integer, Integer> entry : contagemIds.entrySet()) {
            int id = entry.getKey();
            int quantidadeRepetida = entry.getValue();
        
            ProdutoDAO pDAO = new ProdutoDAO();
            Produto p = pDAO.buscarProdutoPorId(id);
            
            if (p != null) {
                total += p.getPreco() * quantidadeRepetida;
                writer.println("<tr><td>" + p.getNome() + "</td><td>R$ " + p.getPreco() + "</td><td>");
        
                int estoque = pDAO.buscarEstoque(id);
                if (quantidadeRepetida == estoque) {
                    writer.println(quantidadeRepetida +
                            "<td><a href='/gerenciarCarrinhoFromVerCarrinho?id=" + p.getId()
                            + "&comando=remove'>Remover</a></td></tr>");
                } else {
                    writer.println(quantidadeRepetida + "</td>" +
                            "<td><a href='/gerenciarCarrinhoFromVerCarrinho?id=" + p.getId() + "&comando=remove'>Remover</a></td></tr>");
                }
        
                CarrinhoStorage.idsCarrinho.add(p.getId());
                CarrinhoStorage.quantidadeProdutosCarrinho.add(quantidadeRepetida);
            } 
        }

        writer.println("</table>");
        writer.println("<div style=\"text-align: center;\">");
        writer.println("<button onclick=\"window.location.href='/listaProdutos'\">Acessar produtos novamente</button>");
        writer.println("<button onclick=\"window.location.href='/finalizarCompra'\">Finalizar sua compra</button>");
        writer.println("<button onclick=\"window.location.href='homeCliente.html'\">Ir para a Homepage</button>");
        writer.println("</div>");
        writer.println("<h3 style=\"text-align: center;\">Total a ser pago: R$ " + total + "</h3>");
        writer.println("</body></html>");
    }
}
