
document.addEventListener("DOMContentLoaded", () => {

    // =========================
    // GOSTAR DA IDEIA
    // =========================

    const likeIdeia = document.getElementById("likeIdeia");
    const totalGostos = document.getElementById("totalGostos");

    let ideiaGostada = false;
    let gostos = Number(totalGostos.textContent) || 0;

    likeIdeia.addEventListener("click", () => {
        ideiaGostada = !ideiaGostada;
        gostos += ideiaGostada ? 1 : -1;

        totalGostos.textContent = gostos;
        likeIdeia.classList.toggle("ativo", ideiaGostada);
        likeIdeia.innerHTML =
            `${ideiaGostada ? "♥ Gostaste" : "♡ Gostar"} <span id="totalGostos">${gostos}</span>`;
    });


    // =========================
    // FAVORITAR
    // =========================

    const favoritarIdeia = document.getElementById("favoritarIdeia");
    let favorita = false;

    favoritarIdeia.addEventListener("click", () => {
        favorita = !favorita;

        favoritarIdeia.classList.toggle("ativo", favorita);
        favoritarIdeia.textContent = favorita
            ? "★ Nos favoritos"
            : "☆ Favoritar";
    });


    // =========================
    // EDITAR IDEIA
    // =========================

    document.getElementById("edit").addEventListener("click", () => {
        const titulo = document.querySelector(".tituloIdeia");
        const descricao = document.querySelector(".descriIdeia");

        const novoTitulo = prompt("Novo título:", titulo.textContent);

        if (novoTitulo === null || !novoTitulo.trim()) {
            return;
        }

        const novaDescricao = prompt(
            "Nova descrição:",
            descricao.textContent.trim()
        );

        if (novaDescricao === null || !novaDescricao.trim()) {
            return;
        }

        titulo.textContent = novoTitulo.trim();
        descricao.textContent = novaDescricao.trim();
    });


    // =========================
    // ADICIONAR COMENTÁRIO
    // =========================

    const commentForm = document.getElementById("commentForm");
    const textoComentario = document.getElementById("textoComentario");
    const listaComentarios = document.getElementById("listaComentarios");
    const totalComentarios = document.getElementById("totalComentarios");

    commentForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const texto = textoComentario.value.trim();

        if (!texto) {
            textoComentario.focus();
            return;
        }

        const comentario = document.createElement("article");
        comentario.className = "comment";

        const avatar = document.createElement("div");
        avatar.className = "avatar avatar-utilizador";
        avatar.textContent = "H";

        const corpo = document.createElement("div");
        corpo.className = "commentBody";

        const cabecalho = document.createElement("div");
        cabecalho.className = "commentHeader";

        const informacao = document.createElement("div");

        const nome = document.createElement("strong");
        nome.className = "nomeComentario";
        nome.textContent = "Heie";

        const data = document.createElement("span");
        data.className = "dataComentario";
        data.textContent = "Agora";

        informacao.append(nome, data);

        const textoElement = document.createElement("p");
        textoElement.className = "textComment";
        textoElement.textContent = texto;

        const acoes = document.createElement("div");
        acoes.className = "acoesComentario";

        const gostar = document.createElement("button");
        gostar.className = "LikeComment";
        gostar.type = "button";
        gostar.innerHTML = '♡ Gostar <span class="likesComentario">0</span>';

        const editar = document.createElement("button");
        editar.className = "EditComment";
        editar.type = "button";
        editar.textContent = "Editar";

        acoes.append(gostar, editar);
        cabecalho.append(informacao);
        corpo.append(cabecalho, textoElement, acoes);
        comentario.append(avatar, corpo);

        // Coloca o comentário mais recente no início.
        listaComentarios.prepend(comentario);

        totalComentarios.textContent =
            Number(totalComentarios.textContent) + 1;

        textoComentario.value = "";

        textoComentario.focus();
    });


    // =========================
    // GOSTAR E EDITAR COMENTÁRIOS
    // Inclui os comentários existentes
    // e os criados dinamicamente.
    // =========================

    listaComentarios.addEventListener("click", (event) => {

        const botao = event.target.closest("button");

        if (!botao) {
            return;
        }

        const comentario = botao.closest(".comment");

        if (!comentario) {
            return;
        }

        if (botao.classList.contains("LikeComment")) {
            const contador = botao.querySelector(".likesComentario");

            const gostou = botao.classList.toggle("ativo");
            const total = Number(contador.textContent) || 0;

            contador.textContent = total + (gostou ? 1 : -1);

            botao.firstChild.textContent = gostou
                ? "♥ Gostaste "
                : "♡ Gostar ";

            return;
        }

        if (botao.classList.contains("EditComment")) {
            const texto = comentario.querySelector(".textComment");

            const novoTexto = prompt(
                "Editar comentário:",
                texto.textContent
            );

            if (novoTexto !== null && novoTexto.trim()) {
                texto.textContent = novoTexto.trim();
            }
        }

    });

});
