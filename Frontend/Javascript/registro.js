
document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("formu");

    const nomeInput = document.getElementById("nome");
    const emailInput = document.getElementById("email");
    const passwordInput = document.getElementById("password");

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const name = nomeInput.value.trim();
        const mail = emailInput.value.trim();
        const pass = passwordInput.value;

        if (!name || !mail || !pass) {
            alert("Preenche todos os campos.");
            return;
        }

        if (name.length < 3 || name.length > 30) {
            alert("O nome de utilizador deve ter entre 3 e 30 caracteres.");
            return;
        }

        if (pass.length < 8) {
            alert("A palavra-passe deve ter pelo menos 8 caracteres.");
            return;
        }

        // Verifica se o nome já existe.
        if (localStorage.getItem(name) !== null) {
            alert("Este nome de utilizador já está registado. Escolhe outro.");
            nomeInput.focus();
            return;
        }

        // Verifica se o email já está associado a uma conta.
        for (let i = 0; i < localStorage.length; i++) {
            const chave = localStorage.key(i);

            try {
                const utilizador = JSON.parse(localStorage.getItem(chave));

                if (
                    utilizador &&
                    typeof utilizador.email === "string" &&
                    utilizador.email.toLowerCase() === mail.toLowerCase()
                ) {
                    alert("Este email já está associado a uma conta.");
                    emailInput.focus();
                    return;
                }
            } catch (error) {
                // Ignora entradas que não sejam dados de utilizadores.
            }
        }

        const usuario = {
            nome: name,
            email: mail,
            password: pass,
            ideias: []
        };

        try {
            localStorage.setItem(name, JSON.stringify(usuario));
        } catch (error) {
            alert("Não foi possível guardar a conta neste navegador.");
            return;
        }

        alert("Conta criada com sucesso!");

        window.location.href = "login.html";
    });

});
