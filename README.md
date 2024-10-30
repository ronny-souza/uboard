# Como Utilizar a API

Esta aplicação conecta-se ao GitLab utilizando webhooks para automatizar a geração de release notes. Ela monitora eventos de pull requests (merge requests) e armazena as atividades relacionadas, mantendo um histórico detalhado das alterações. Ao receber um evento de criação de tag, a aplicação gera automaticamente release notes para o projeto específico, reunindo as informações mais relevantes de cada merge request e facilitando a comunicação das mudanças implementadas.

### Principais funcionalidades:

Escuta e armazena atividades de merge requests via webhooks.
Gera release notes automaticamente ao receber o evento de criação de uma tag.
Oferece uma visão consolidada das alterações em cada release, diretamente do GitLab.

### Conteúdo Deste Documento

* [1. Instalação](#1-instalação)
* [2. Execução](#2-execução)
    * [2.1. Criação do executável da aplicação](#21-criação-do-executável-da-aplicação)
    * [2.2. Executando a aplicação](#22-executando-a-aplicação)
    * [2.3. Acessando a documentação e executando operações](#23-acessando-a-documentação-e-executando-operações)
* [3. Configuração dos webhooks](#3-configuração-dos-webhooks)
    * [3.1. Configuração do webhook para merge requests](#31-configuração-do-webhook-para-merge-requests)
    * [3.2. Configuração do webhook para tags](#32-configuração-do-webhook-para-tags)


### 1. Instalação

A tabela abaixo exibe os requisitos para executar e testar a aplicação e seus endpoints em sua máquina.

| Descrição | Versão | Obrigatório |
| ------ | ------ | ------ |
| Java | v21 | Sim |
| Maven | v3.8.8 | Sim |

Após clonar o projeto, você pode seguir os passos da seção a seguir para a execução.

### 2. Execução

#### 2.1. Criação do executável da aplicação 
Para criar o executável, é necessário executar o comando Maven abaixo:

```
mvn clean package
```

#### 2.2. Executando a aplicação
Para executar a aplicação, é preciso cadastrar variáveis de ambiente para a conexão com o Gitlab e o banco de dados ocorrer.

| Variável | Descrição | Valor (exemplo) | 
| ------ | ------ | ------ |
| GITLAB_API_TOKEN | O token de acesso do Gitlab. | _SqA9dDeCMpaZapCyExL |
| GITLAB_API_URL | O endereço do Gitlab, seja privado ou público. | https://gitlab.com/api/v4 |
| UBOARD_DATABASE_PASSWORD | Senha do banco de dados. | - |


#### 2.3. Acessando a documentação e executando operações
Este serviço conta com uma documentação dinâmica e operacional por meio do Swagger com a especificação do OpenAPI. Para acessar, você pode seguir a seguinte URL:

```
http://localhost:8099/uboard/swagger-ui/index.html
```
* Você pode trocar a URL base pela URl que estiver utilizando em seu ambiente.

### 3. Configuração dos webhooks
Para as operações deste projeto, foram configurados dois webhooks, sendo um para eventos de pull request (merge request no Gitlab) e outro para eventos de criação de tag. Para configurar, basta acessar a tela de webhooks do seu projeto no Gitlab, e construir os webhooks como o exemplo abaixo:

#### 3.1. Configuração do webhook para merge requests
É preciso, na tela de webhooks adicionar a URL do projeto, para onde os eventos serão encaminhados, como `https://your-project.com/uboard/webhooks/merge-request`. E nas opções de evento, marcar a opção `Merge request events`. Você pode fazer testes de conexão utilizando a opção de testes do Gitlab.

#### 3.2. Configuração do webhook para tags
É preciso, na tela de webhooks adicionar a URL do projeto, para onde os eventos serão encaminhados, como `https://your-project.com/uboard/webhooks/tag`. E nas opções de evento, marcar a opção `Tag push events`. Você pode fazer testes de conexão utilizando a opção de testes do Gitlab.

