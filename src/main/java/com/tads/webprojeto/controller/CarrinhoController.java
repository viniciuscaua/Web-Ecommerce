package com.tads.webprojeto.controller;

import com.tads.webprojeto.aplicacao.CarrinhoStorage;
import com.tads.webprojeto.dominio.ProdutoDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class CarrinhoController {
    String idProdutos = "";

    @GetMapping("/gerenciarCarrinho")
    public void controlCarrinho(@RequestParam("id") String idProduto, @RequestParam("comando") String comando, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;

        if (comando.equals("add")) {
            ProdutoDAO pDAO = new ProdutoDAO();
            int quantidade = pDAO.buscarEstoque(Integer.parseInt(idProduto));

            if (quantidade == 0) {
                response.sendRedirect("/listaProdutos?msg=Produto sem estoque");
                return;
            }
            // Percorre os cookies existentes
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(nomeCarrinho)) {
                        // Caso exista um cookie do cliente atual, pega o que está guardado nele
                        idProdutos = cookie.getValue();
                        break;
                    }
                }
            }
            // Adiciona o produto adicionado ao cookie já existente
            idProdutos += idProduto + "_";

            // Destrui o cookie antes de atualiza-lo
            Cookie cookieToDelete = new Cookie(nomeCarrinho, null); 
            cookieToDelete.setMaxAge(0); 
            response.addCookie(cookieToDelete); 

            // Cria um novo cookie guardando os ids dos produtos
            Cookie carrinho = new Cookie(nomeCarrinho, idProdutos);
            carrinho.setMaxAge(60 * 60 * 48);
            response.addCookie(carrinho);
            response.sendRedirect("/listaProdutos?msg=Produto adicionado");

        } else if (comando.equals("remove")) {
            idProduto = idProduto + "_";
            idProdutos = idProdutos.replaceFirst(idProduto, ""); // Retira o id do produto da string

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(nomeCarrinho)) {
                        // Caso exista um cookie do cliente atual, pega o que está guardado nele
                        idProdutos = cookie.getValue();
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }

            // Verifica se o carrinho ainda tem produtos
            if (idProdutos != "") { // Caso não tenha ficado vazio, instancia novamente o cookie com o resto dos ids
                Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
                carrinho.setMaxAge(172800);
                response.addCookie(carrinho);
                response.sendRedirect("/verCarrinho?msg=Produto removido");
            } else if (idProdutos.equals("")) { // Caso tenha esvaziado o carrinho, apaga o cookie
                Cookie carrinho = new Cookie(CarrinhoStorage.cookieCarrinho, idProdutos);
                carrinho.setMaxAge(0);
                response.addCookie(carrinho);
                response.sendRedirect("/listaProdutos?msg=Carrinho vazio");
            }
        }
    }

    @GetMapping("/gerenciarCarrinhoFromVerCarrinho")
    public void controlCarrinhoFromVerCarrinho(@RequestParam("id") String idProduto, @RequestParam("comando") String comando,
                                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;

        if (comando.equals("add")) {
            ProdutoDAO pDAO = new ProdutoDAO();
            int quantidade = pDAO.buscarEstoque(Integer.parseInt(idProduto));
            // Percorre os cookies existentes
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(nomeCarrinho)) {
                        // Caso exista um cookie do cliente atual, pega o que está guardado nele
                        idProdutos = cookie.getValue();
                        break;
                    }
                }
            }
            idProdutos += idProduto + "_";

            Cookie carrinho = new Cookie(nomeCarrinho, idProdutos);
            carrinho.setMaxAge(60 * 60 * 48);
            response.addCookie(carrinho);
            response.sendRedirect("/verCarrinho?msg=Produto adicionado");

        } else if (comando.equals("remove")) {
            System.out.println("Cookie antes: " + idProdutos);
            System.out.println("Produto: " + idProduto);

            idProduto = idProduto + "_";

            StringBuilder idProdutosBuilder = new StringBuilder(idProdutos);
            int index = idProdutosBuilder.indexOf(idProduto);

            if (index != -1) {
                idProdutosBuilder.delete(index, index + idProduto.length());
                idProdutos = idProdutosBuilder.toString();
            }

            // Verifica se o carrinho ainda tem produtos
            if (idProdutos != "") { // Caso não tenha ficado vazio, instancia novamente o cookie com o resto dos ids
                if (nomeCarrinho != null && !nomeCarrinho.isEmpty()) {
                    Cookie carrinho = new Cookie(nomeCarrinho, idProdutos);
                    response.addCookie(carrinho);
                    response.sendRedirect("/verCarrinho?msg=Produto removido");
                } else {
                    String nomePadrao = CarrinhoStorage.cookieCarrinho;
                    Cookie carrinho = new Cookie(nomePadrao, idProdutos);
                    carrinho.setMaxAge(60 * 60 * 48);
                    response.addCookie(carrinho);
                    response.sendRedirect("/verCarrinho?msg=Produto removido");
                }

            } else if (idProdutos.equals("")) {
                Cookie carrinho = new Cookie(nomeCarrinho, idProdutos);
                carrinho.setMaxAge(0);
                response.addCookie(carrinho);
                response.sendRedirect("/listaProdutos?msg=Carrinho vazio");
            }
        }
    }


    @GetMapping("/finalizarCompra")
    public void finalizarCompra(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Integer> arrayIds = CarrinhoStorage.idsCarrinho;
        ArrayList<Integer> arrayQuantidades = CarrinhoStorage.quantidadeProdutosCarrinho;
        ProdutoDAO pDAO = new ProdutoDAO();
        int quantidade;
        int novaQuantidade;

        for (int i = 0; i < arrayIds.size(); i++) {
            quantidade = pDAO.buscarEstoque(arrayIds.get(i));
            novaQuantidade = quantidade - arrayQuantidades.get(i);

            pDAO.updateEstoque(arrayIds.get(i), novaQuantidade);
        }

        // Apagando o cookie do carrinho
        String nomeCarrinho = CarrinhoStorage.cookieCarrinho;
        // Percorre os cookies existentes e atualiza as quantidades do estoque
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(nomeCarrinho)) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                idProdutos = "";
                CarrinhoStorage.idsCarrinho = new ArrayList<>();
                CarrinhoStorage.quantidadeProdutosCarrinho = new ArrayList<>();
                response.sendRedirect("homeCliente.html?msg=Compra realizada");
            }
        }

    }
}