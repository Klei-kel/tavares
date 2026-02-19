# Tavares Agendamentos (Spring Boot + Thymeleaf)

Site simples (MVP) para o **Tavares Lava Rápido**:
- Agendamento público por link
- Duração fixa: **60 min**
- Funcionamento:
  - **Terça a sábado:** 08:00–17:30 (último início 16:30)
  - **Domingo:** 08:00–14:30 (último início 13:30)
  - **Segunda:** fechado
- Cliente só vê a **agenda liberada** (por padrão até o domingo desta semana)
- Admin libera a próxima semana com 1 clique
- **Integração opcional com Google Calendar** (cria evento ao agendar)

## Requisitos
- Java 17+
- Maven (ou use a extensão Java do VS Code que já costuma instalar/usar)

## Rodar no VS Code
1. Abra a pasta do projeto no VS Code
2. No terminal, rode:

```bash
mvn spring-boot:run
```

3. Acesse:
- Site público: http://localhost:8080/
- Admin: http://localhost:8080/admin/agenda
- Console H2: http://localhost:8080/h2  (JDBC: `jdbc:h2:file:./data/tavaresdb`)

Login admin padrão:
- usuário: `admin`
- senha: `admin123`
> Mude em `src/main/resources/application.yml` (app.admin.user / app.admin.password)

## Liberar próxima semana
No painel Admin, clique em **"Liberar próxima semana"**.

O sistema guarda um limite `released_until` (domingo).  
O cliente só consegue ver e reservar até essa data.

## Google Calendar (opcional)
Por padrão está desligado.

### 1) Crie um calendário da empresa
Ex.: "Agenda - Tavares Lava Rápido".

### 2) Use Service Account
- Crie uma service account no Google Cloud
- Ative a API do Google Calendar no projeto
- Baixe o arquivo JSON da chave
- Coloque em `./keys/service-account.json` (crie a pasta `keys`)

### 3) Compartilhe o calendário com a service account
No Google Calendar, compartilhe o calendário com o e-mail da service account com permissão de **fazer alterações**.

### 4) Configure o application.yml
Em `src/main/resources/application.yml`:

```yml
app:
  calendar:
    enabled: true
    calendarId: "SEU_CALENDAR_ID"
    serviceAccountKeyPath: "./keys/service-account.json"
    timezone: "America/Sao_Paulo"
```

Pronto: ao agendar, o sistema cria um evento no Google Calendar.


