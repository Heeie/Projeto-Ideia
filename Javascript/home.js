

    document.addEventListener("DOMContentLoaded", function () {


        // VERIFICA A SESSÃO: Tenta ler o utilizador guardado
    const utilizador = sessionStorage.getItem("userLoged");

    // Se não existir sessão ativa, redireciona de volta para o login
    if (utilizador === null) {
        alert("Acesso negado! Por favor, faça login primeiro.");
        window.location.href = "login.html"; 
        return;
    }

    // Se a sessão existir, pode dar as boas-vindas no HTML
    console.log("Bem-vindo de volta, " + utilizador);
    // Exemplo: document.getElementById("boas-vindas").innerText = `Olá, ${utilizador}!`;

        const aba = document.getElementById("abaOpcoes");
        const botao = document.getElementById("ops");
        const pesquisa = document.getElementById("barraPesquisa");

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

            console.log("Pesquisar:", texto);

            alert("A pesquisar por: " + texto);

        });

    });



