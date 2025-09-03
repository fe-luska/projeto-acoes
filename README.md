# Projeto Acoes

Projeto desenvolvido em Java para monitorar um ativo da B3 e emitir um alerta no email sugerindo uma compra ou venda com base nos valores definidos.

O monitoramento é feito de forma contínua em um intervalo de tempo fixo que pode ser alterado via código. Os dados são retornados do servidor IbovFinancials, que contém os ativos da B3 e permite até 200 requisições por dia no plano gratuíto.
Para obter uma chave gratuita basta apenas criar uma conta em https://www.ibovfinancials.com/

O email é enviado utilizando a biblioteca JakartaMail e os parametros devem ser passados por um arquivo '.env'. É feito 3 tentativas de envio caso ocorra alguma falha no cliente de email.


------
### Estruturação do Projeto

O Projeto segue uma estrutura padrão do Maven. A pasta `src/main/java/projetoAcoes` contém os arquivos finais dividios entre três pacotes: client, model e service:
- Pacote `client`: armazena as classes de cliente de email e cliente da API dos ativos.
- Pacote `model`: armazena duas classes que são utilizadas para armazenar informações sobre ações e alertas.
- Pacote `service`: contém uma classe de serviço que cuida da lógica da verificação do alerta e envio de email caso antingido os preços definidos no alerta.

A pasta `src/test/java/projetoAcoes` contém os arquivos de teste:
- Há um mockup do cliente de API dos ativos para simular preços para realizar os testes.
- Há uma classe MonitorServiceTest para testar a lógica de enviar o email caso o alerta de compra/venda seja antingido ou não enviar email caso não seja antingido.
- 3 testes de integridade estão escritos no arquivo MonitorServiceTest. Obs: os testes utilizam o cliente de email verdadeiro, de fato enviando emails durante testes.
- Os testes utilizam o framework de automação de testes JUnit 5.



------
### Como Configurar e Executar


#### 1. Depois de clonar o repositório, crie um arquivo `.env` na pasta raíz do projeto e preencha com as seguintes informações:

```
# Chave da API de Cotações IbovFinancials
IBOV_FINANCIALS_TOKEN="sua_chave_da_api_aqui"

EMAIL_HOST="smtp.gmail.com"
EMAIL_PORT="587"
EMAIL_USERNAME="seu-email@gmail.com"
EMAIL_PASSWORD="sua_senha_de_app_de_16_digitos"
EMAIL_DESTINO="email-destinatário"
```

#### 2. Compile

Use o Maven para compilar o código e criar um arquivo `.jar` executável.

```sh
mvn clean package
```

Isso criará um arquivo na pasta `target/` com um nome como `ProjetoAcoes-0.0.1-SNAPSHOT.jar`.
Os testes serão executados e dois emails devem ter sido enviados no email destinatário.

#### 3. Execute

```
java -jar <arquivo.jar> <ativo> <preço_venda> <preço_compra>
```
 
Exemplo:

```
java -jar target/ProjetoAcoes-1.0-SNAPSHOT-jar-with-dependencies.jar PETR4 45.00 38.50
```

#### Testes

Para rodar os testes:

```sh
mvn test
```
