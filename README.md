# Ideias

Plataforma de demonstração para guardar, publicar e descobrir ideias. O repositório contém um frontend HTML/CSS/JavaScript em `Frontend` e uma API REST Spring Boot em `ProjetoJv-Server_Client`.

## Frontend

O registo, login, perfil e ciclo de vida das ideias usam a API Spring Boot. Inicia primeiro a API e depois serve a pasta `Frontend` numa origem local permitida pelo CORS:

```powershell
cd Frontend
python -m http.server 5500
```

Abre `http://localhost:5500/capa.html`. O cliente JavaScript usa `http://localhost:8080` por omissão; podes substituir o endereço definindo `window.IDEIAS_API_BASE` antes de `Javascript/api.js`.

Favoritos, gostos, comentários, grupos, amizades, mensagens, preferências de notificação e notificações são persistidos pela API. As páginas sociais requerem sessão iniciada. O navegador mantém apenas o cache temporário das ideias e os dados de sessão.

## API Spring Boot

Requisitos: Java 17 e Maven.

```powershell
cd ProjetoJv-Server_Client
mvn spring-boot:run
```

A documentação OpenAPI fica disponível em `/swagger-ui.html` quando o servidor está ativo. Autenticação e registo: `POST /api/auth/registo` e `POST /api/auth/login`. Os endpoints CRUD incluem `GET/POST /api/ideias`, `GET/PUT/DELETE /api/ideias/{id}` e operações equivalentes em `/api/utilizadores`. As respostas de utilizador nunca incluem a palavra-passe; os erros usam estados HTTP adequados e um corpo JSON com `status` e `message`.

As áreas sociais incluem `/api/utilizadores/{id}/favoritos`, `/api/ideias/{id}/gostos` e `/api/ideias/{id}/comentarios`; gestão de grupos em `/api/grupos`; pedidos e relações em `/api/amizades`; conversas e mensagens em `/api/conversas`; e `/api/utilizadores/{id}/preferencias` e `/api/utilizadores/{id}/notificacoes`. Pedidos de amizade, comentários e mensagens criam notificações para o destinatário.

O servidor usa uma base H2 em ficheiro (`ProjetoJv-Server_Client/data/ideias`) por predefinição; os testes continuam com a base H2 em memória. As variáveis `IDEIAS_DB_URL`, `IDEIAS_DB_USER` e `IDEIAS_DB_PASSWORD` permitem configurar outro destino.

Para compilar: `mvn clean compile`. A suite `mvn test` cobre CRUD, registo/login, imagem, atualização de perfil, preflight CORS e fluxos das áreas sociais.

## Estado e limitações

As palavras-passe são derivadas com PBKDF2 e nunca são devolvidas pela API. O endpoint de login ainda não emite tokens nem protege os endpoints CRUD com autorização. Usa o servidor apenas em desenvolvimento até ser adicionada autorização por utilizador e configuração de produção.
