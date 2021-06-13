# Em construção - Gerenciador de Assembleias para Votação

## ⚙ Iniciando

Clone este repositório em seu computador: https://github.com/thiagobdp/gerenciador-de-assembleias


## Configurando MySql

### Instalar Visual Studio 2015, 2017 e 2019

Versões mais recentes do Visual C++:
- https://support.microsoft.com/pt-br/topic/os-downloads-do-visual-c-mais-recentes-com-suporte-2647da03-1eea-4433-9aff-95f26a218cc0

A versão utilizada neste projeto é o link abaixo: 

- https://aka.ms/vs/16/release/vc_redist.x64.exe

### Instalar MySQL 8.0.25

MySQL Installer 8.0.25:
- https://dev.mysql.com/downloads/windows/installer/8.0.html

ou diretamente pelo link:
- https://dev.mysql.com/get/Downloads/MySQLInstaller/mysql-installer-community-8.0.25.0.msi

Sugestão de senha para root:
- mysql password: root

## Criar um novo banco de dados e um usuário para a aplicação
Abra o aplicativo MySQL 8.0 Command Line Client. Ao abrir será solicitada a senha de root fornecida no passo anterior.

Execute os seguintes comando no mysql prompt ( mysql> ):

1- Cria o novo banco 
* `create database bd_gerenciador_assembleias;`

2- Cria o novo usuário

* `create user 'pmanageruser'@'%' identified by 'pmanageruser';`

3- Fornece todos privilégios para o novo usuário no novo banco de dados
* `grant all on bd_gerenciador_assembleias.* to 'pmanageruser'@'%';`

## Objetivo

Criado o sistema de votação de assembleias.

É possível consultar a documentação Swagger pelo link abaixo quando executando em localhost:
* http://localhost:8080/swagger-ui.html

## Tarefa Bônus 1 - Integração com sistemas externos

Criei o projeto git abaixo para servir como sistema externo para integração
* https://github.com/thiagobdp/gerenciador-de-usuario

O serviço está disponível na URL
* https://thiagobdpusuarioscpf.herokuapp.com/users/{cpf}

E a documentação Swagger está disponível na URL
* https://thiagobdpusuarioscpf.herokuapp.com/swagger-ui.html

Ao realizar o voto, o sistema validará nessa API externa se o CPF informado é válido. Caso seja valido, retornará permitindo o voto. Caso seja inválido, retornará erro.
















