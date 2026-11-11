

    document.addEventListener("DOMContentLoaded", async function () {


        // VERIFICA A SESSÃO: Tenta ler o utilizador guardado
    const utilizador = sessionStorage.getItem("userLoged");

    if (utilizador !== null) console.log("Bem-vindo de volta, " + utilizador);
    // Exemplo: document.getElementById("boas-vindas").innerText = `Olá, ${utilizador}!`;

        const aba = document.getElementById("abaOpcoes");
        const botao = document.getElementById("ops");
        const pesquisa = document.getElementById("barraPesquisa");
        document.getElementById("terminarSessao").addEventListener("click", () => {
            IdeiasAPI.signOut();
            window.location.href = "capa.html";
        });

        // esconder menu ao iniciar
        aba.style.display = "none";

        // abrir/fechar menu
        botao.addEventListener("click", function () {

            if (aba.style.display === "none") {
                aba.style.display = "block";
            } else {
                aba.style.display = "none";
            }

        });

        

        document.addEventListener("click", function (event) {

    // Se o menu estiver aberto
    if (aba.style.display === "block") {

        // Se o clique não foi no menu nem no botão
        if (!aba.contains(event.target) && !botao.contains(event.target)) {
            aba.style.display = "none";
        }

    }

});

        // pesquisa
        pesquisa.addEventListener("submit", function (event) {

            event.preventDefault();

            const texto = document.getElementById("topico").value;

            window.location.href = "resultadosPesquisa.html?q=" + encodeURIComponent(texto.trim());

        });

        const lista = document.getElementById("ideiasScroll");
        try { await Ideias.load(); }
        catch (error) { lista.innerHTML = `<p class="avisoErro">${Ideias.escape(error.message)}</p>`; return; }
        const userAtual = IdeiasAPI.current();
        let favoritos = userAtual ? (await IdeiasAPI.social.favorites(userAtual.id)).map(item => String(item.id)) : [];
        lista.replaceChildren();
        if (!Ideias.community().length) {
            const empty = document.createElement("p");
            empty.className = "avisoVazio";
            empty.textContent = "Ainda não há ideias publicadas. Sê a primeira pessoa a partilhar uma ideia!";
            lista.append(empty);
        }
        Ideias.community().forEach(ideia => {
            const card = document.createElement("article");
            card.className = "ideia";
            card.tabIndex = 0;
            card.setAttribute("role", "link");
            card.innerHTML = `<div class="imgIdeia">${ideia.imagem ? `<img src="${Ideias.escape(ideia.imagem)}" alt="Imagem da ideia: ${Ideias.escape(ideia.titulo)}">` : "💡"}</div><div class="textosIdeia"><h2 class="tituloIdeia">${Ideias.escape(ideia.titulo)}</h2><p class="descriIdeia">${Ideias.escape(ideia.descricao)}</p><span class="autorIdeia">Por: ${Ideias.escape(ideia.autor)}</span></div><button class="favoritar" type="button" aria-label="Favoritar ideia">${favoritos.includes(String(ideia.id)) ? "★" : "☆"}</button>`;
            const open = () => location.href = `ideiaPage.html?id=${encodeURIComponent(ideia.id)}`;
            card.addEventListener("click", event => { if (!event.target.closest("button")) open(); });
            card.addEventListener("keydown", event => { if (event.key === "Enter") open(); });
            card.querySelector("button").addEventListener("click", async event => {
                event.stopPropagation();
                if (!userAtual) { alert("Inicia sessão para guardar favoritos."); return; }
                const save = !favoritos.includes(String(ideia.id));
                try { await IdeiasAPI.social.favorite(userAtual.id, ideia.id, save); favoritos = save ? [...favoritos, String(ideia.id)] : favoritos.filter(id => id !== String(ideia.id)); event.currentTarget.textContent = save ? "★" : "☆"; }
                catch (error) { alert(error.message); }
            });
            lista.append(card);
        });

    });



