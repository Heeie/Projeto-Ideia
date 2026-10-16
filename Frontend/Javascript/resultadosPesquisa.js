
document.addEventListener("DOMContentLoaded", () => {

    const formPesquisa = document.getElementById("formPesquisa");
    const termoPesquisa = document.getElementById("termoPesquisa");
    const categoria = document.getElementById("categoria");
    const ordenacao = document.getElementById("ordenacao");
    const listaIdeias = document.getElementById("listaIdeias");
    const resumoResultados = document.getElementById("resumoResultados");
    const semResultados = document.getElementById("semResultados");
    const limparPesquisa = document.getElementById("limparPesquisa");

    // Dados temporários para testar a interface.
    // Posteriormente, estes dados virão da API Spring Boot.
    const ideias = [
        {
            id: 1,
            titulo: "Aplicação para estudantes",
            categoria: "Educação",
            descricao: "Uma aplicação para organizar trabalhos, projetos, horários e tarefas escolares.",
            autor: "Maria",
            gostos: 25,
            data: "2026-10-12",
            imagem: "images/ideia1.jpg"
        },
        {
            id: 2,
            titulo: "Jogo de perguntas rápidas",
            categoria: "Jogos",
            descricao: "Um jogo de perguntas e desafios curtos para competir com amigos.",
            autor: "Heie",
            gostos: 42,
            data: "2026-10-15",
            imagem: "images/ideia2.jpg"
        },
        {
            id: 3,
            titulo: "Plataforma de ideias",
            categoria: "Tecnologia",
            descricao: "Um espaço onde qualquer pessoa pode partilhar ideias e colaborar em projetos.",
            autor: "João",
            gostos: 31,
            data: "2026-10-10",
            imagem: "images/lampada-de-ideia.png"
        },
        {
            id: 4,
            titulo: "Uma cidade mais verde",
            categoria: "Ambiente",
            descricao: "Uma iniciativa para incentivar a reciclagem e melhorar os espaços verdes.",
            autor: "Ana",
            gostos: 18,
            data: "2026-10-08",
            imagem: "images/ideia1.jpg"
        },
        {
            id: 5,
            titulo: "Pequenos negócios locais",
            categoria: "Negócios",
            descricao: "Uma plataforma para ajudar pequenos negócios a encontrar novos clientes.",
            autor: "Pedro",
            gostos: 36,
            data: "2026-10-14",
            imagem: "images/ideia2.jpg"
        },
        {
            id: 6,
            titulo: "Desenhar em conjunto",
            categoria: "Criatividade",
            descricao: "Uma ferramenta simples para criar desenhos e partilhar ideias visuais.",
            autor: "Inês",
            gostos: 12,
            data: "2026-10-11",
            imagem: "images/ideia1.jpg"
        }
    ];

    // Proteção básica contra erros de armazenamento.
    function lerFavoritos() {
        try {
            const dados = JSON.parse(
                localStorage.getItem("ideiasFavoritas") || "[]"
            );

            return Array.isArray(dados) ? dados : [];
        } catch {
            return [];
        }
    }

    let favoritos = lerFavoritos();

    function guardarFavoritos() {
        try {
            localStorage.setItem(
                "ideiasFavoritas",
                JSON.stringify(favoritos)
            );
        } catch {
            // A página continua a funcionar durante a sessão.
        }
    }

    function criarElemento(tag, classe, texto) {
        const elemento = document.createElement(tag);

        if (classe) {
            elemento.className = classe;
        }

        if (texto !== undefined) {
            elemento.textContent = texto;
        }

        return elemento;
    }

    function criarCartao(ideia) {
        const cartao = criarElemento("article", "cartaoIdeia");

        const imagemDiv = criarElemento("div", "imagemCartao");
        const imagem = document.createElement("img");

        imagem.src = ideia.imagem;
        imagem.alt = `Imagem da ideia: ${ideia.titulo}`;
        imagem.loading = "lazy";

        imagem.addEventListener("error", () => {
            imagemDiv.replaceChildren(
                criarElemento("span", "", "💡")
            );
            imagemDiv.style.fontSize = "48px";
        }, { once: true });

        imagemDiv.appendChild(imagem);

        const conteudo = criarElemento("div", "conteudoCartao");

        const etiqueta = criarElemento(
            "span",
            "categoriaCartao",
            ideia.categoria
        );

        const titulo = criarElemento(
            "h3",
            "tituloCartao",
            ideia.titulo
        );

        const descricao = criarElemento(
            "p",
            "descricaoCartao",
            ideia.descricao
        );

        const meta = criarElemento("div", "metaCartao");
        const autor = criarElemento(
            "span",
            "autorCartao",
            `Por: ${ideia.autor}`
        );

        const acoes = criarElemento("div", "acoesCartao");

        const abrir = criarElemento(
            "button",
            "btnAbrir",
            "Ver ideia →"
        );

        abrir.type = "button";

        abrir.addEventListener("click", () => {
            // O ID permite carregar a ideia correta na página de detalhes.
            window.location.href =
                `ideiaPage.html?id=${encodeURIComponent(ideia.id)}`;
        });

        const favoritar = criarElemento(
            "button",
            "btnFavoritar",
            favoritos.includes(ideia.id) ? "★" : "☆"
        );

        favoritar.type = "button";
        favoritar.setAttribute(
            "aria-label",
            favoritos.includes(ideia.id)
                ? "Remover dos favoritos"
                : "Adicionar aos favoritos"
        );

        favoritar.classList.toggle(
            "ativo",
            favoritos.includes(ideia.id)
        );

        favoritar.addEventListener("click", () => {
            if (favoritos.includes(ideia.id)) {
                favoritos = favoritos.filter(id => id !== ideia.id);
            } else {
                favoritos.push(ideia.id);
            }

            guardarFavoritos();

            const ativo = favoritos.includes(ideia.id);

            favoritar.textContent = ativo ? "★" : "☆";
            favoritar.classList.toggle("ativo", ativo);
            favoritar.setAttribute(
                "aria-label",
                ativo
                    ? "Remover dos favoritos"
                    : "Adicionar aos favoritos"
            );
        });

        acoes.append(abrir, favoritar);
        meta.append(autor, acoes);

        conteudo.append(etiqueta, titulo, descricao, meta);
        cartao.append(imagemDiv, conteudo);

        return cartao;
    }

    function renderizarResultados() {
        const termo = termoPesquisa.value.trim().toLocaleLowerCase("pt-PT");
        const categoriaSelecionada = categoria.value;

        let resultados = ideias.filter(ideia => {
            const correspondeTexto = [
                ideia.titulo,
                ideia.descricao,
                ideia.autor,
                ideia.categoria
            ].some(valor =>
                valor.toLocaleLowerCase("pt-PT").includes(termo)
            );

            const correspondeCategoria =
                categoriaSelecionada === "todas" ||
                ideia.categoria === categoriaSelecionada;

            return correspondeTexto && correspondeCategoria;
        });

        switch (ordenacao.value) {
            case "recentes":
                resultados.sort(
                    (a, b) => new Date(b.data) - new Date(a.data)
                );
                break;

            case "gostos":
                resultados.sort((a, b) => b.gostos - a.gostos);
                break;

            case "titulo":
                resultados.sort((a, b) =>
                    a.titulo.localeCompare(b.titulo, "pt-PT")
                );
                break;

            case "relevancia":
            default:
                if (termo) {
                    resultados.sort((a, b) => {
                        const score = ideia => {
                            const titulo = ideia.titulo
                                .toLocaleLowerCase("pt-PT");

                            if (titulo === termo) return 3;
                            if (titulo.includes(termo)) return 2;
                            return 1;
                        };

                        return score(b) - score(a) ||
                            b.gostos - a.gostos;
                    });
                } else {
                    resultados.sort((a, b) => b.gostos - a.gostos);
                }
        }

        listaIdeias.replaceChildren();

        resultados.forEach(ideia => {
            listaIdeias.appendChild(criarCartao(ideia));
        });

        resumoResultados.textContent =
            resultados.length === 1
                ? "1 ideia encontrada"
                : `${resultados.length} ideias encontradas`;

        semResultados.hidden = resultados.length !== 0;
        listaIdeias.hidden = resultados.length === 0;
    }

    formPesquisa.addEventListener("submit", event => {
        event.preventDefault();

        const params = new URLSearchParams(window.location.search);
        const termo = termoPesquisa.value.trim();

        if (termo) {
            params.set("q", termo);
        } else {
            params.delete("q");
        }

        const novaQuery = params.toString();

        history.replaceState(
            null,
            "",
            novaQuery
                ? `${window.location.pathname}?${novaQuery}`
                : window.location.pathname
        );

        renderizarResultados();
    });

    termoPesquisa.addEventListener("input", renderizarResultados);
    categoria.addEventListener("change", renderizarResultados);
    ordenacao.addEventListener("change", renderizarResultados);

    limparPesquisa.addEventListener("click", () => {
        termoPesquisa.value = "";
        categoria.value = "todas";
        ordenacao.value = "relevancia";

        const params = new URLSearchParams(window.location.search);
        params.delete("q");

        const novaQuery = params.toString();

        history.replaceState(
            null,
            "",
            novaQuery
                ? `${window.location.pathname}?${novaQuery}`
                : window.location.pathname
        );

        renderizarResultados();
        termoPesquisa.focus();
    });

    // Recupera o termo enviado pela página inicial.
    const parametros = new URLSearchParams(window.location.search);
    termoPesquisa.value = parametros.get("q") || "";

    renderizarResultados();
});
