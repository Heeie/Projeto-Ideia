
const listaConversas = document.getElementById("listaConversas");
const areaMensagens = document.getElementById("areaMensagens");
const formMensagem = document.getElementById("formMensagem");
const textoMensagem = document.getElementById("textoMensagem");
const pesquisarConversa = document.getElementById("pesquisarConversa");

const nomeContacto = document.getElementById("nomeContacto");
const avatarContacto = document.getElementById("avatarContacto");
const semConversas = document.getElementById("semConversas");

const dialogNovaMensagem = document.getElementById("dialogNovaMensagem");
const formNovaConversa = document.getElementById("formNovaConversa");
const nomeNovoContacto = document.getElementById("nomeNovoContacto");

const CHAVE_DADOS = "ideiasMensagensDemo";

let conversaAtual = null;
let conversas = [];

function carregarConversas() {
    try {
        const guardadas = JSON.parse(localStorage.getItem(CHAVE_DADOS));

        if (Array.isArray(guardadas)) {
            conversas = guardadas;
        }
    } catch (erro) {
        console.warn("Não foi possível ler as conversas guardadas.", erro);
    }

    if (conversas.length === 0) {
        conversas = [
            {
                id: "1",
                nome: "Ana Silva",
                avatar: "👩",
                mensagens: [
                    { texto: "Olá! Vi a tua ideia e achei muito interessante.", tipo: "recebida", hora: "10:35" },
                    { texto: "Obrigado! Ainda estou a desenvolvê-la.", tipo: "enviada", hora: "10:38" },
                    { texto: "Se quiseres, podemos trabalhar nela juntos.", tipo: "recebida", hora: "10:42" }
                ]
            },
            {
                id: "2",
                nome: "Miguel Costa",
                avatar: "👨",
                mensagens: [
                    { texto: "Olá! Tenho uma ideia para uma aplicação.", tipo: "recebida", hora: "Ontem" },
                    { texto: "Conta-me mais sobre ela!", tipo: "enviada", hora: "Ontem" }
                ]
            },
            {
                id: "3",
                nome: "Sofia Martins",
                avatar: "👩‍💻",
                mensagens: [
                    { texto: "Já viste o novo grupo de programação?", tipo: "recebida", hora: "Seg." },
                    { texto: "Ainda não, vou ver!", tipo: "enviada", hora: "Seg." }
                ]
            }
        ];

        guardarConversas();
    }
}

function guardarConversas() {
    localStorage.setItem(CHAVE_DADOS, JSON.stringify(conversas));
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

function obterUltimaMensagem(conversa) {
    const ultima = conversa.mensagens.at(-1);
    return ultima ? ultima.texto : "Sem mensagens";
}

function desenharListaConversas() {
    const filtro = pesquisarConversa.value.trim().toLocaleLowerCase("pt-PT");

    listaConversas.replaceChildren();

    const filtradas = conversas.filter(conversa =>
        conversa.nome.toLocaleLowerCase("pt-PT").includes(filtro)
    );

    semConversas.hidden = filtradas.length > 0;

    filtradas.forEach(conversa => {
        const botao = criarElemento("button", "conversa");
        botao.type = "button";
        botao.dataset.id = conversa.id;

        if (conversa.id === conversaAtual) {
            botao.classList.add("ativa");
        }

        const avatar = criarElemento("span", "avatar", conversa.avatar);
        const info = criarElemento("span", "info-conversa");
        const nome = criarElemento("strong", "", conversa.nome);
        const ultima = criarElemento("span", "ultimaMensagem", obterUltimaMensagem(conversa));
        const hora = criarElemento("span", "hora", conversa.mensagens.at(-1)?.hora || "");

        info.append(nome, ultima);
        botao.append(avatar, info, hora);

        botao.addEventListener("click", () => selecionarConversa(conversa.id));

        listaConversas.appendChild(botao);
    });
}

function desenharMensagens() {
    const conversa = conversas.find(item => item.id === conversaAtual);

    if (!conversa) {
        areaMensagens.replaceChildren(
            criarElemento("p", "separador-data", "Seleciona uma conversa")
        );
        return;
    }

    areaMensagens.replaceChildren();

    areaMensagens.appendChild(
        criarElemento("p", "separador-data", "Conversa")
    );

    conversa.mensagens.forEach(mensagem => {
        const caixa = criarElemento("div", `mensagem ${mensagem.tipo}`);
        const texto = criarElemento("p", "", mensagem.texto);
        const hora = criarElemento("span", "", mensagem.hora);

        caixa.append(texto, hora);
        areaMensagens.appendChild(caixa);
    });

    areaMensagens.scrollTop = areaMensagens.scrollHeight;
}

function selecionarConversa(id) {
    const conversa = conversas.find(item => item.id === id);

    if (!conversa) return;

    conversaAtual = id;

    nomeContacto.textContent = conversa.nome;
    avatarContacto.textContent = conversa.avatar;

    desenharListaConversas();
    desenharMensagens();
}

function horaAtual() {
    return new Intl.DateTimeFormat("pt-PT", {
        hour: "2-digit",
        minute: "2-digit"
    }).format(new Date());
}

formMensagem.addEventListener("submit", evento => {
    evento.preventDefault();

    const texto = textoMensagem.value.trim();

    if (!texto || !conversaAtual) return;

    const conversa = conversas.find(item => item.id === conversaAtual);

    if (!conversa) return;

    conversa.mensagens.push({
        texto,
        tipo: "enviada",
        hora: horaAtual()
    });

    guardarConversas();
    desenharListaConversas();
    desenharMensagens();

    textoMensagem.value = "";
    textoMensagem.focus();
});

pesquisarConversa.addEventListener("input", desenharListaConversas);

document.getElementById("novaMensagem").addEventListener("click", () => {
    formNovaConversa.reset();
    dialogNovaMensagem.showModal();
    nomeNovoContacto.focus();
});

document.getElementById("cancelarNovaConversa").addEventListener("click", () => {
    dialogNovaMensagem.close();
});

formNovaConversa.addEventListener("submit", evento => {
    evento.preventDefault();

    const nome = nomeNovoContacto.value.trim();

    if (!nome) return;

    const existente = conversas.find(
        conversa => conversa.nome.toLocaleLowerCase("pt-PT") === nome.toLocaleLowerCase("pt-PT")
    );

    if (existente) {
        selecionarConversa(existente.id);
        dialogNovaMensagem.close();
        return;
    }

    const novaConversa = {
        id: crypto.randomUUID(),
        nome,
        avatar: "👤",
        mensagens: []
    };

    conversas.unshift(novaConversa);

    guardarConversas();
    selecionarConversa(novaConversa.id);

    dialogNovaMensagem.close();
});

document.getElementById("apagarConversa").addEventListener("click", () => {
    const conversa = conversas.find(item => item.id === conversaAtual);

    if (!conversa || conversa.mensagens.length === 0) return;

    const confirmar = confirm(
        `Queres apagar as mensagens guardadas desta conversa com ${conversa.nome}?`
    );

    if (!confirmar) return;

    conversa.mensagens = [];

    guardarConversas();
    desenharListaConversas();
    desenharMensagens();
});

carregarConversas();

conversaAtual = conversas[0]?.id || null;

if (conversaAtual) {
    selecionarConversa(conversaAtual);
} else {
    desenharListaConversas();
    desenharMensagens();
}
