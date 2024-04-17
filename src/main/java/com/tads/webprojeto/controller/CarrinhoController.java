package com.tads.webprojeto.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.tads.webprojeto.CarrinhoStorage;
import com.tads.webprojeto.dominio.ProdutoDAO;

@Controller
public class CarrinhoController {

    String idProdutos = "";
    String stringId;

    @GetMapping("/carrinhoServlet")
    public void controlCarrinho(@RequestParam int id, @RequestParam String comando, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        stringId = String.valueOf(id);
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;

        if (comando.equals("add")) {
            ProdutoDAO pDao = new ProdutoDAO();
            int quantidade = pDao.buscarQuantidade(id);

            if (quantidade == 0) {
                response.sendRedirect("/listarProdutosCliente?msg=Produto sem estoque");
            }

            // pega todos o cookies do servidor e percorre cada um
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(String.valueOf(nomeCarrinho))) {
                        // caso existe um cookie do cliente atual pega o que está guardado nele
                        idProdutos = cookie.getValue();
                        break;
                    }
                }
            }

            idProdutos += stringId + "_";

            // cria o cookie guardando os ids dos produtos
            Cookie carrinho = new Cookie(String.valueOf(nomeCarrinho), idProdutos);
            carrinho.setMaxAge(172800); // tempo de vida em segundos
            response.addCookie(carrinho);
            response.sendRedirect("/listarProdutosCliente");
        } else if (comando.equals("remove")) {
            stringId = stringId + "_";
            idProdutos = idProdutos.replaceFirst(stringId, ""); // retira o id do prouto removido a string

            if (idProdutos != "") { // caso não tenha ficado vazia instancia novamente o cookia com o resto dos ids
                Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
                carrinho.setMaxAge(172800);
                response.addCookie(carrinho);
                response.sendRedirect("/verCarrinho");
            } else if (idProdutos == "") { // caso tenha zerado o carrinho, apaga o cookie
                Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
                carrinho.setMaxAge(0);
                response.addCookie(carrinho);
                response.sendRedirect("homeCliente.html");
            }
        }
    }

    @GetMapping("/carrinhoServletFromVerCarrinho")
    public void controlCarrinhoFromVerCarrinho(@RequestParam int id, @RequestParam String comando,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        stringId = String.valueOf(id);
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;

        if (comando.equals("add")) {
            ProdutoDAO pDao = new ProdutoDAO();
            int quantidade = pDao.buscarQuantidade(id);

            if (quantidade == 0) {
                response.sendRedirect("/listarProdutosCliente?msg=Produto sem estoque");
            }

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(String.valueOf(nomeCarrinho))) {
                        idProdutos = cookie.getValue();
                        break;
                    }
                }
            }

            idProdutos += stringId + "_";

            Cookie carrinho = new Cookie(String.valueOf(nomeCarrinho), idProdutos);
            carrinho.setMaxAge(120);
            response.addCookie(carrinho);
            response.sendRedirect("/verCarrinho");

        } else if (comando.equals("remove")) {
            stringId = stringId + "_";
            idProdutos = idProdutos.replaceFirst(stringId, "");

            if (idProdutos != "") {
                Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
                carrinho.setMaxAge(172800);
                response.addCookie(carrinho);
                response.sendRedirect("/verCarrinho");
            } else if (idProdutos == "") {
                Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
                carrinho.setMaxAge(0);
                response.addCookie(carrinho);
                response.sendRedirect("home_cliente.html");
            }
        }
    }

    @GetMapping("/finalizarCompra")
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Integer> arrayIds = CarrinhoStorage.idsCarrinho;
        ArrayList<Integer> arrayQuantidades = CarrinhoStorage.quantidadeProdutosCarrinho;
        ProdutoDAO pDao = new ProdutoDAO();
        int quantidade;
        int novaQuantidade;

        for (int i = 0; i < arrayIds.size(); i++) {
            quantidade = pDao.buscarQuantidade(arrayIds.get(i));
            novaQuantidade = quantidade - arrayQuantidades.get(i);

            pDao.updateQuantidade(arrayIds.get(i), novaQuantidade);
        }

        // apagando o cookie do carrinho
        Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
        carrinho.setMaxAge(0);
        response.addCookie(carrinho);
        idProdutos = "";
        response.sendRedirect("homeCliente.html?msg=Compra realizada");
    }
}