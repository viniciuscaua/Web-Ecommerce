# Aplicação Web de Comércio Eletrônico
Este é um projeto de aplicação web de comércio eletrônico desenvolvido utilizando Java e banco de dados. A aplicação oferece funcionalidades básicas para clientes e lojistas, como cadastro, login, visualização e compra de produtos.

## Funcionalidades
1. *Cadastro de Clientes*: Os clientes podem se cadastrar na plataforma fornecendo nome, email e senha.
   
2. *Login de Clientes e Lojistas*: Clientes e lojistas podem fazer login na plataforma com seus respectivos emails e senhas.

3. *Listagem de Produtos*: A aplicação exibe uma lista de produtos disponíveis para compra. Os clientes podem adicionar produtos ao carrinho a partir dessa lista.
  
4. *Carrinho de Compras*: Os clientes podem visualizar os produtos adicionados ao carrinho, remover produtos ou adicionar mais unidades. O carrinho é mantido por cookies com duração de 48 horas.
  
5. *Finalização de Compra*: Ao finalizar a compra, o sistema calcula o total a ser pago e atualiza o estoque de produtos no banco de dados.
  
6. *Visualização de Produtos por Lojistas*: Os lojistas podem visualizar os produtos cadastrados e seus respectivos estoques.
  
7. *Cadastro de Produtos por Lojistas*: Os lojistas podem cadastrar novos produtos na plataforma.

## Tecnologias Utilizadas
Java, Spring Framework, Banco de dados (PostgreSQL), HTML, CSS

## Estrutura do Projeto
- `src/main/java`: Contém os arquivos Java do projeto.
- `src/main/webprojeto`: Contém os arquivos (classes, consultas SQL, autenticação e requisições) do projeto. 
- `src/main/resources`: Contém os arquivos HTML e CSS para as páginas da aplicação.
- `database.sql`: Script SQL para criação do banco de dados e tabelas.

## Pré-requisitos
- JDK instalado
- Ambiente de desenvolvimento Java configurado
- Servidor de banco de dados configurado

## Configuração
- Clone o repositório do GitHub: git clone https://github.com/seu_usuario/nome_do_repositorio.git
- Importe o projeto para a sua IDE Java.
- Configure as informações de acesso ao banco de dados no arquivo application.properties.
- Execute o script database.sql para criar o banco de dados e as tabelas necessárias.
- Compile e execute a aplicação.
  
##Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Autor
Vinicius Cauã Soares Fonseca

## Licença
Este projeto está licenciado sob a Licença MIT.

