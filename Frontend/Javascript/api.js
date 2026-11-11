/* Cliente HTTP da API Spring Boot. Serve o frontend em http://localhost:5500. */
window.IdeiasAPI = (() => {
    const base = (window.IDEIAS_API_BASE || "http://localhost:8080").replace(/\/$/, "");
    async function request(path, options = {}) {
        let response;
        const user = current();
        const authHeaders = user?.accessToken && !path.startsWith("/api/auth/")
            ? { Authorization: `Bearer ${user.accessToken}` } : {};
        try {
            response = await fetch(`${base}${path}`, {
                ...options,
                headers: { Accept: "application/json", ...(options.body ? { "Content-Type": "application/json" } : {}), ...authHeaders, ...options.headers }
            });
        } catch {
            throw new Error("Não foi possível contactar o servidor. Confirma que a API está ativa em http://localhost:8080.");
        }
        if (response.status === 204) return null;
        const data = await response.json().catch(() => ({}));
        if (!response.ok) throw new Error(data.message || `O servidor respondeu com o erro ${response.status}.`);
        return data;
    }
    const current = () => {
        try { return JSON.parse(sessionStorage.getItem("utilizadorAtual")) || null; }
        catch { return null; }
    };
    const signIn = user => {
        const previous = current();
        user = { ...user, accessToken: user.accessToken || previous?.accessToken, tokenType: user.tokenType || previous?.tokenType || "Bearer" };
        sessionStorage.setItem("utilizadorAtual", JSON.stringify(user));
        sessionStorage.setItem("userLoged", user.nome);
        sessionStorage.setItem("userId", String(user.id));
        sessionStorage.setItem("userEmail", user.email);
    };
    const signOut = () => {
        const user = current();
        if (user?.accessToken) fetch(`${base}/api/auth/logout`, { method: "POST", headers: { Authorization: `Bearer ${user.accessToken}` } }).catch(() => {});
        ["utilizadorAtual", "userLoged", "userId", "userEmail"].forEach(key => sessionStorage.removeItem(key));
    };
    const createUser = data => request("/api/auth/registo", { method: "POST", body: JSON.stringify(data) });
    const login = data => request("/api/auth/login", { method: "POST", body: JSON.stringify(data) });
    const getIdeas = () => request("/api/ideias");
    const getMyIdeas = () => request("/api/ideias/minhas");
    const createIdea = data => request("/api/ideias", { method: "POST", body: JSON.stringify(data) });
    const updateIdea = (id, data) => request(`/api/ideias/${encodeURIComponent(id)}`, { method: "PUT", body: JSON.stringify(data) });
    const deleteIdea = id => request(`/api/ideias/${encodeURIComponent(id)}`, { method: "DELETE" });
    const updateProfile = (id, data) => request(`/api/utilizadores/${encodeURIComponent(id)}/perfil`, { method: "PUT", body: JSON.stringify(data) });
    const social = {
        favorites: id => request(`/api/utilizadores/${encodeURIComponent(id)}/favoritos`),
        favorite: (userId, ideaId, save) => request(`/api/utilizadores/${userId}/favoritos/${ideaId}`, { method: save ? "PUT" : "DELETE" }),
        likes: (ideaId, userId) => request(`/api/ideias/${ideaId}/gostos${userId ? `?utilizadorId=${userId}` : ""}`),
        like: (ideaId, userId, value) => request(`/api/ideias/${ideaId}/gostos/${userId}`, { method: value ? "PUT" : "DELETE" }),
        comments: (ideaId, userId) => request(`/api/ideias/${ideaId}/comentarios${userId ? `?utilizadorId=${userId}` : ""}`),
        comment: (ideaId, data) => request(`/api/ideias/${ideaId}/comentarios`, { method: "POST", body: JSON.stringify(data) }),
        commentLike: (commentId, userId, value) => request(`/api/comentarios/${commentId}/gostos/${userId}`, { method: value ? "PUT" : "DELETE" }),
        groups: userId => request(`/api/grupos?utilizadorId=${userId}`),
        createGroup: data => request("/api/grupos", { method: "POST", body: JSON.stringify(data) }),
        groupMember: (groupId, userId, join) => request(`/api/grupos/${groupId}/membros/${userId}`, { method: join ? "PUT" : "DELETE" }),
        friendships: userId => request(`/api/amizades?utilizadorId=${userId}`),
        requestFriend: data => request("/api/amizades", { method: "POST", body: JSON.stringify(data) }),
        acceptFriend: (id, userId) => request(`/api/amizades/${id}/aceitar?utilizadorId=${userId}`, { method: "PUT" }),
        removeFriend: (id, userId) => request(`/api/amizades/${id}?utilizadorId=${userId}`, { method: "DELETE" }),
        conversations: userId => request(`/api/conversas?utilizadorId=${userId}`),
        createConversation: data => request("/api/conversas", { method: "POST", body: JSON.stringify(data) }),
        messages: (conversationId, userId) => request(`/api/conversas/${conversationId}/mensagens?utilizadorId=${userId}`),
        sendMessage: (conversationId, data) => request(`/api/conversas/${conversationId}/mensagens`, { method: "POST", body: JSON.stringify(data) }),
        preferences: userId => request(`/api/utilizadores/${userId}/preferencias`),
        savePreferences: (userId, data) => request(`/api/utilizadores/${userId}/preferencias`, { method: "PUT", body: JSON.stringify(data) }),
        notifications: userId => request(`/api/utilizadores/${userId}/notificacoes`),
        readNotification: (id, userId) => request(`/api/notificacoes/${id}/lida?utilizadorId=${userId}`, { method: "PUT" })
    };
    const categoryNames = { COZINHA:"Cozinha", DESPORTO:"Desporto", LAZER:"Lazer", TECNOLOGIA:"Tecnologia", ARTE:"Arte", COMUNIDADE:"Comunidade", EDUCACAO:"Educação", AMBIENTE:"Ambiente", NEGOCIO:"Negócio", NEGOCIOS:"Negócios", JOGOS:"Jogos", CRIATIVIDADE:"Criatividade", OUTRA:"Outra" };
    const categoryValues = Object.fromEntries(Object.entries(categoryNames).map(([key,value]) => [value,key]));
    const mapIdea = idea => ({ id:idea.id, titulo:idea.nome, descricao:idea.descricao || "", categoria:categoryNames[idea.categoria] || idea.categoria, categoriaApi:idea.categoria, estado:idea.estado, autor:idea.clienteNome || `Utilizador ${idea.clienteId}`, clienteId:idea.clienteId, criadoEm:idea.criadoEm || "", imagem:idea.imagem || "" });
    return { base, request, current, signIn, signOut, createUser, login, getIdeas, getMyIdeas, createIdea, updateIdea, deleteIdea, updateProfile, social, categoryValues, mapIdea };
})();
