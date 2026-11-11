document.addEventListener("DOMContentLoaded", async () => {
    const form = document.getElementById("formPesquisa"), input = document.getElementById("termoPesquisa");
    const category = document.getElementById("categoria"), order = document.getElementById("ordenacao");
    const list = document.getElementById("listaIdeias"), empty = document.getElementById("semResultados");
    input.value = new URLSearchParams(location.search).get("q") || "";
    try { await Ideias.load(); }
    catch (error) { list.innerHTML = `<p class="avisoErro">${Ideias.escape(error.message)}</p>`; document.getElementById("resumoResultados").textContent = "Não foi possível carregar a pesquisa."; return; }
    const render = () => {
        let items = Ideias.community().filter(item => {
            const haystack = `${item.titulo} ${item.descricao} ${item.autor} ${item.categoria}`.toLocaleLowerCase("pt-PT");
            return haystack.includes(input.value.trim().toLocaleLowerCase("pt-PT")) && (category.value === "todas" || item.categoria === category.value);
        });
        if (order.value === "titulo") items.sort((a,b) => a.titulo.localeCompare(b.titulo, "pt"));
        if (order.value === "recentes") items.sort((a,b) => Number(b.id)-Number(a.id));
        if (order.value === "gostos") items.sort((a,b) => Ideias.get(`gostos:${b.id}`, 0) - Ideias.get(`gostos:${a.id}`, 0));
        list.replaceChildren();
        document.getElementById("resumoResultados").textContent = `${items.length} ${items.length === 1 ? "ideia encontrada" : "ideias encontradas"}`;
        empty.hidden = items.length > 0; list.hidden = items.length === 0;
        items.forEach(item => {
            const card = document.createElement("article"); card.className = "cartaoIdeia";
            card.innerHTML = `<div class="imagemCartao">${item.imagem ? `<img src="${Ideias.escape(item.imagem)}" alt="Imagem da ideia: ${Ideias.escape(item.titulo)}">` : "💡"}</div><div class="conteudoCartao"><span class="categoriaCartao">${Ideias.escape(item.categoria || "Outra")}</span><h3 class="tituloCartao">${Ideias.escape(item.titulo)}</h3><p class="descricaoCartao">${Ideias.escape(item.descricao)}</p><div class="metaCartao"><span class="autorCartao">Por ${Ideias.escape(item.autor)}</span><a href="ideiaPage.html?id=${encodeURIComponent(item.id)}">Ver ideia →</a></div></div>`;
            list.append(card);
        });
    };
    form.addEventListener("submit", event => { event.preventDefault(); history.replaceState(null, "", `?q=${encodeURIComponent(input.value.trim())}`); render(); });
    input.addEventListener("input", render); category.addEventListener("change", render); order.addEventListener("change", render);
    document.getElementById("limparPesquisa").addEventListener("click", () => { input.value = ""; category.value = "todas"; order.value = "relevancia"; render(); });
    render();
});
