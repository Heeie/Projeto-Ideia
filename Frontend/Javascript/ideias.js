/* Cache e utilitários locais das páginas; as ideias vêm sempre da API. */
window.Ideias = (() => {
    let ideas = [];
    const get = (name, fallback) => { try { return JSON.parse(localStorage.getItem(name)) ?? fallback; } catch { return fallback; } };
    const set = (name, value) => localStorage.setItem(name, JSON.stringify(value));
    const user = () => IdeiasAPI.current()?.nome || sessionStorage.getItem("userLoged");
    const userId = () => IdeiasAPI.current()?.id || Number(sessionStorage.getItem("userId")) || null;
    const load = async () => { ideas = (await IdeiasAPI.getIdeas()).map(IdeiasAPI.mapIdea).sort((a,b) => Number(b.id)-Number(a.id)); return ideas; };
    const all = () => ideas;
    const community = () => ideas;
    const favorites = () => get(`favoritos:${userId() || user() || "visitante"}`, []);
    const toggleFavorite = id => {
        const key = `favoritos:${userId() || user() || "visitante"}`;
        const list = favorites();
        const next = list.includes(String(id)) ? list.filter(item => item !== String(id)) : [...list, String(id)];
        set(key, next);
        return next.includes(String(id));
    };
    const escape = (value = "") => String(value).replace(/[&<>"']/g, char => ({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[char]));
    return { get, set, user, userId, load, all, community, favorites, toggleFavorite, escape };
})();
