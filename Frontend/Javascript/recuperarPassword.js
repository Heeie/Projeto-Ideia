
document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("formRecuperacao");
    const emailInput = document.getElementById("email");
    const mensagem = document.getElementById("mensagem");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        const email = emailInput.value.trim();

        mensagem.className = "";
        mensagem.style.display = "none";

        if (!emailInput.checkValidity() || !email) {
            emailInput.reportValidity();
            return;
        }

        mensagem.textContent =
            "O formulário está válido. A recuperação por email ainda não está ligada ao servidor.";

        mensagem.className = "erro";
        mensagem.style.display = "block";
    });

});
