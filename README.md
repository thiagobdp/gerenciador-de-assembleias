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


## Tarefa Bônus 2 - Mensageria e filas

### Instalar JRE 64bits

Para evitar o problema no Kafka de OutOfMemory, é necessário instalar a versão 64bits do Java disponível nos dois links abaixo. Esse sistema foi testado no OS Windows 10
* https://www.java.com/pt-BR/download/manual.jsp
* https://javadl.oracle.com/webapps/download/AutoDL?BundleId=244584_d7fc238d0cbf4b0dac67be84580cfb4b

Definir variável de ambiente JAVA_HOME
1. No windows, em "Propriedades do Sistema" clicar em "Variáveis de Ambiente"
2. Definir a variável para a nova instalação da JRE 64bits
3. Nome da variável: `JAVA_HOME`
4. Valor da variável: `C:\Program Files\Java\jre1.8.0_291` -> Neste caso substitua pelo diretório em que seu java 64bits está instalado

### Configurando Apache Kafka

Baixar a versão do link abaixo
* https://ftp.unicamp.br/pub/apache/kafka/2.8.0/kafka_2.13-2.8.0.tgz

Descompactar no diretório "C:/". Se utilizar outro diretório, na hora de executar, o windows pode reclamar que o caminho é muito longo para executar.

O programa 7zip realiza realiza o descompactamento

* https://www.7-zip.org/download.html

Ao descompactar, irá gerar um arquivo chamado "kafka_2.13-2.8.0.tar". Também é necessário descompatar esse arquivo utilizando o 7zip.

O resultado final será um diretório chamado "kafka_2.13-2.8.0"

Iniciar o zookeeper
1. abrir prompt de comando do Windows (cmd)
2. executar o comando: `cd C:\kafka_2.13-2.8.0`
3. executar o comando: `bin\windows\zookeeper-server-start.bat config\zookeeper.properties`
4. Se ocorrer o erro `'Error: missing server' JVM at C:\Program Files (x86)\Java\jre1.8.0_291\bin\server\jvm.dll'. Please install or use the JRE or JDK that contains these missing components.` Executar o passo 5, se não, pular para o passo 9
5. Via Explorador de Arquivos (Windows Explorer) navegar até o diretório "C:\Program Files (x86)\Java\jre1.8.0_291\bin"
6. Criar um diretório vazio chamado "server"
7. Copiar todo o conteúdo do diretório "client" para o novo diretório "server". Atenção para copiar somente o conteúdo do diretório "client". O próprio diretório "client" não deve ser copiado, somente seu conteúdo.
8. Executar novamente o passo 4.
9. Se o Zookeeper foi iniciado com sucesso, uma das linhas exibidas no terminal será: `INFO binding to port 0.0.0.0/0.0.0.0:2181 (org.apache.zookeeper.server.NIOServerCnxnFactory)`
11. Não fechar (apenas minimizar) este terminal pois o Zookeeper ficará sendo executado nele.

Iniciar o Kafka
1. Abrir novo prompt de comando do Windows (cmd)
2. executar o comando: `cd C:\kafka_2.13-2.8.0`
3. executar o comando: `bin\windows\kafka-server-start.bat config\server.properties`
4. Se o Kafka foi iniciado com sucesso, uma das linhas exibidas no terminal será: `INFO [KafkaServer id=0] started (kafka.server.KafkaServer)`
5. Por padrão o kafka é executado na porta 9092

#### Criar tópico via terminal

Será criado um tópico via terminal pois a aplicação será responsável somente por enviar a mensagem.
1. Abrir prompt de comando do Windows (cmd)
2. Executar o comando: `cd C:\kafka_2.13-2.8.0`
3. Executar o comando: `bin\windows\kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic NOVO_RESULTADO_VOTACAO`
4. Se o topico for criado com sucesso, aparecerá a mensagem: `Created topic NOVO_RESULTADO_VOTACAO.`
5. Para listar os tópicos, executar o comando `bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092`

#### Iniciar consumidor via terminal

Para facilitar o desenvolvimento, o consumidor das mensagens enviadas pela aplicação "Gerenciador de Assembleias para Votação" para o tópico criado no item acima, será o terminal do windows.
1. Abrir prompt de comando do Windows (cmd)
2. Executar o comando: `cd C:\kafka_2.13-2.8.0`
3. Executar o comando: `bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic NOVO_RESULTADO_VOTACAO --from-beginning`
4. Com o comando acima, o consumidor exibirá todas as mensagem que já foram enviadas e ficará ouvindo por novas mensagens
5. Sempre que uma sessão de votação fechar, será enviada a mensagem para o kafka e será exibida neste terminal pelo consumidor

#### Envio da mensagem pelo produtor
A mensagem é enviada quando o sistema identifica que a sessão passou do prazo. 

Para fins de simplificação, a validação do prazo é realizada quando alguma operação é feita na pauta, ou seja, quando algum dos serviços abaixo são executados:
* /pauta/{id}
* /voto/votar

Desta forma, após abrir a sessão, para que a mesagem seja enviada, é necessário executar algum dos serviços acima após passar o horário da sessão.




