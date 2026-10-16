
document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("formu");
    const nomeInput = document.getElementById("nome");
    const passwordInput = document.getElementById("password");

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const nome = nomeInput.value.trim();
        const password = passwordInput.value;

        if (!nome || !password) {
            alert("Preenche o nome de utilizador e a palavra-passe.");
            return;
        }

        const dadosLocais = localStorage.getItem(nome);

        if (!dadosLocais) {
            alert("Utilizador não encontrado!");
            return;
        }

        let userSalvo;

        try {
            userSalvo = JSON.parse(dadosLocais);
        } catch (error) {
            alert("Os dados desta conta são inválidos. Tenta novamente ou regista a conta de novo.");
            return;
        }

        if (
            !userSalvo ||
            typeof userSalvo.password !== "string"
        ) {
            alert("Não foi possível validar esta conta.");
            return;
        }

        if (userSalvo.password === password) {

            sessionStorage.setItem("userLoged", nome);

            window.location.href = "body.html";

        } else {
            alert("Palavra-passe incorreta!");
        }
    });

});
