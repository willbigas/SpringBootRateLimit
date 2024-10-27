# Spring Boot Rate Limiter com AOP

Este é um projeto Spring Boot que utiliza Aspect-Oriented Programming (AOP) para implementar um **Rate Limiter**. O Rate Limiter é usado para limitar o número de requisições a endpoints específicos de uma API, permitindo controlar a taxa de acesso e proteger a aplicação contra excesso de chamadas (rate limiting).

## Funcionalidades

- **Limitação de Taxa**: Restringe o número de chamadas a um endpoint com base no IP de origem.
- **Configuração Dinâmica**: Permite definir limites de taxa (`rateLimit`) e duração (`rateDuration`) por meio de propriedades.
- **Exceções Personalizadas**: Retorna uma mensagem de erro personalizada quando o limite de taxa é excedido.

## Tecnologias Utilizadas

- **Java 17** ou superior
- **Spring Boot**
- **Spring AOP**: Para interceptar chamadas de métodos e aplicar lógica de limitação de taxa.
- **Spring Web**: Para criar endpoints REST.
- **Maven**: Para gerenciamento de dependências.

## Estrutura do Projeto

O projeto é composto por:

- `RateLimitAspect`: Aspecto AOP que intercepta as requisições e aplica a lógica de limitação de taxa.
- `RateLimitException`: Exceção personalizada lançada quando o limite de requisições é excedido.
- `@WithRateLimitProtection`: Anotação personalizada usada para definir quais endpoints são protegidos pelo Rate Limiter.

## Configuração

No arquivo de propriedades (`application.properties`), defina os valores padrão para o rate limiting:

```properties
# Limite de requisições permitidas por IP
app.rate.limit=200

# Duração em milissegundos (exemplo: 60000 ms = 1 minuto)
app.rate.durationinms=60000
```

## Testes

Você pode testar o Rate Limiter usando **Postman** ou **cURL** no terminal.

### Testando com Postman

1. **Configurar o Endpoint no Postman**:
   - Abra o Postman e crie uma nova requisição apontando para o endpoint HTTP protegido com `@WithRateLimitProtection`, como `http://localhost:8080/api`.

2. **Configurar Test Runner para Requisições Rápidas**:
   - No Postman, clique em "Runner" no canto superior direito (ícone de "raquete").
   - Selecione sua requisição, defina o número de execuções (por exemplo, 50 requisições).
   - Configure um intervalo de tempo curto entre as execuções, como 100 ms.
   - Clique em "Run" para iniciar o teste.

3. **Verificar a Resposta de Erro**:
   - Quando o limite de requisições for excedido, você verá uma resposta de erro com a exceção `RateLimitException`, indicando que o Rate Limiter foi acionado.

```
Exemplo de retorno
{
    "id": "632aa4fb-7c73-4b71-96b5-82931de6fc59",
    "status": 429,
    "error": "TOO_MANY_REQUESTS",
    "message": "To many request at endpoint /api from IP 0:0:0:0:0:0:0:1! Please try again after 60000 milliseconds!",
    "timestamp": "2024-10-27T03:54:37.4253715",
    "path": "/api"
}
```

### Testando com cURL no Terminal

Para testar com o `curl`, você pode usar o seguinte comando no terminal para enviar várias requisições consecutivas:

```bash
Linux/Mac

for i in {1..50}; do curl -X GET http://localhost:8080/api; sleep 0.1; done

Windows - Powershell
for ($i = 1; $i -le 51; $i++) { Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api"; Start-Sleep -Milliseconds 100 } 



