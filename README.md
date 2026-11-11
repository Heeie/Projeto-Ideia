# Ideias

Plataforma de demonstração para guardar, publicar e descobrir ideias. O repositório contém um frontend HTML/CSS/JavaScript em `Frontend` e uma API REST Spring Boot em `ProjetoJv-Server_Client`.

## Frontend

O registo, login, perfil e ciclo de vida das ideias usam a API Spring Boot. Inicia primeiro a API e depois serve a pasta `Frontend` numa origem local permitida pelo CORS:

```powershell
cd Frontend
python -m http.server 5500
```

Abre `http://localhost:5500/capa.html`. O cliente JavaScript usa `http://localhost:8080` por omissão; podes substituir o endereço definindo `window.IDEIAS_API_BASE` antes de `Javascript/api.js`. Também podes iniciar o servidor incluído com `node serve.js` dentro de `Frontend`.

Favoritos, gostos, comentários, grupos, amizades, mensagens, preferências de notificação e notificações são persistidos pela API. As páginas sociais requerem sessão iniciada. O navegador mantém apenas o cache temporário das ideias e os dados de sessão.

## API Spring Boot

Requisitos: Java 17 e Maven.

```powershell
cd ProjetoJv-Server_Client
mvn spring-boot:run
```

A documentação OpenAPI fica disponível em `/swagger-ui.html` quando o servidor está ativo. Autenticação e registo: `POST /api/auth/registo` e `POST /api/auth/login`. A lista pública `GET /api/ideias` mostra apenas ideias públicas; as operações de criação, edição e remoção exigem token. `GET /api/ideias/minhas` devolve as ideias da sessão. Os perfis ficam em `/api/utilizadores/{id}/perfil`; `GET /api/utilizadores` fornece apenas identificadores e nomes para pesquisa de contactos. As respostas nunca incluem palavras-passe; os erros usam estados HTTP adequados e um corpo JSON com `status` e `message`.

As áreas sociais incluem `/api/utilizadores/{id}/favoritos`, `/api/ideias/{id}/gostos` e `/api/ideias/{id}/comentarios`; gestão de grupos em `/api/grupos`; pedidos e relações em `/api/amizades`; conversas e mensagens em `/api/conversas`; e `/api/utilizadores/{id}/preferencias` e `/api/utilizadores/{id}/notificacoes`. Pedidos de amizade, comentários e mensagens criam notificações para o destinatário.

O servidor usa uma base H2 em ficheiro (`ProjetoJv-Server_Client/data/ideias`) por predefinição; os testes continuam com a base H2 em memória. As variáveis `IDEIAS_DB_URL`, `IDEIAS_DB_USER` e `IDEIAS_DB_PASSWORD` permitem configurar outro destino.

Para compilar: `mvn clean compile`. A suite `mvn test` cobre CRUD, registo/login, imagem, atualização de perfil, preflight CORS e fluxos das áreas sociais.

## Estado e limitações

As palavras-passe são derivadas com PBKDF2 e nunca são devolvidas pela API. O login e o registo emitem tokens Bearer com validade de 12 horas; os endpoints privados exigem sessão e comparam a conta autenticada com o utilizador indicado. A sessão é mantida em memória, pelo que reiniciar a API termina as sessões ativas e esta configuração destina-se a uma única instância.

Para produção, ativa `SPRING_PROFILES_ACTIVE=prod`, define `IDEIAS_DB_URL`, `IDEIAS_DB_USER`, `IDEIAS_DB_PASSWORD` e `IDEIAS_CORS_ALLOWED_ORIGINS` (origens separadas por vírgulas) e serve o frontend por HTTPS numa origem indicada. O perfil de produção desativa H2 Console e Swagger e valida o esquema em vez de o alterar automaticamente. Cria/aplica migrações de base de dados antes do primeiro arranque. Não uses as credenciais H2 de desenvolvimento em produção.

A recuperação de palavra-passe por email continua assinalada como indisponível na página, pois o projeto não inclui um fornecedor de email nem credenciais SMTP; não é seguro permitir redefinição apenas com o endereço de email.
