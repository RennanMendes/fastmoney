# <center> Fastmoney </center>

<center>

[![GitHub license](https://img.shields.io/github/license/RennanMendes/fastmoney.svg)](https://github.com/RennanMendes/fastmoney/blob/master/LICENSE)
![Static Badge](https://img.shields.io/badge/tested_with-JUnit-red)
![Static Badge](https://img.shields.io/badge/status-in_progress-brightgreen)

</center>

##  Sobre

O objetivo deste projeto é criar um mini Caixa Eletrônico (ATM) que ofereça aos usuários a capacidade de realizar operações bancárias, como depósitos, saques, transferências e verificação de extrato, de forma segura e conveniente. Além disso, o sistema visa garantir a integridade das contas dos usuários e a proteção de seus dados pessoais.

### Regras de negócio

 - Acesso ao sistema somente para usuários logados;
 - Permitido apenas uma conta por CPF;
 - Movimentações financeiras mediante a verificação de PIN;
 - Transferências e saques somente com saldo suficiente;
 - Transferências somente para usuários ativos;
 - Valor de saques e transferências limitado a R$ 500,00 entre às 20h e 5h;
 - Recuperação de senha através de email;
 - Usuários receberão notificações a cada operação realizada em suas contas;

## Tecnologias utilizadas

<center>

| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-plain.svg" alt="Java Icon" width="40" height="40" /> | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" alt="Spring Icon" width="48" height="48" /> | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-plain.svg" alt="`PostgreSQL` Icon" width="40" height="40" /> | <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/intellij/intellij-original.svg" alt="Intellij Icon" width="40" height="40" /> |
| :--: | :--: | :--: | :--: |
| Java | Spring | PostgreSQL | IntelliJ |

</center>

## Instalação

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Maven](https://maven.apache.org/)
- [Git](https://git-scm.com/)

Siga estas etapas para instalar e executar o projeto:
1. Clone o repositório: `git clone git@github.com:RennanMendes/fastmoney.git`

2. Compile o projeto usando o Maven: `mvn clean install`

3. Execute o projeto: `java -jar target/fastmoney.jar`

O aplicativo Spring será executado na porta padrão 8080. Você pode acessá-lo em http://localhost:8080.


## Arquitetura MVC

A escolha da arquitetura MVC para a realização deste projeto se baseia em diversas considerações que visam garantir um desenvolvimento eficiente e de alta qualidade. Abaixo, está alguns dos motivos que fundamentam a escolha:

 - **Simplicidade Inicial:** A arquitetura MVC oferece uma estrutura inicialmente mais simples para o projeto, crucial para o entendimento rápido das partes interessadas.

 - **Separação de Responsabilidades:** A arquitetura MVC promove separação clara de responsabilidades entre os componentes, permitindo a reutilização de componentes de forma eficaz e facilitando a criação de testes de unidade. 

 - **Considerações de Tamanho e Complexidade:** Embora existam arquiteturas mais complexas adequadas para projetos muito grandes e com alto fluxo de dados, o MVC ainda é escalável o suficiente para atender às necessidades deste projeto. 

Também foi utilizado o design pattern Strategy de forma a isolar as regras de negócio da aplicação, permitindo assim, adicionar, remover ou editar uma regra de forma rápida, prática e sem influenciar nas demais regras que já foram testadas e estão funcionando.

## Banco de Dados PostgreSQL

A escolha do banco de dados relacional para este projeto é devido a sua modelagem organizada, coesão, integridade e segurança dos dados.

### Alguns dos princípios utilizados no desenvolvimento do projeto:
`S.O.L.I.D`

`K.I.S.S`

`Tell don’t ask`

`Fail fast`

