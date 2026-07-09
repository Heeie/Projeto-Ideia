document.getElementById("formu").addEventListener("submit", function(event) {
    event.preventDefault();
    
    const name = document.getElementById("nome").value;
    const pass = document.getElementById("password").value;
    
    // 1. Procura o utilizador no localStorage
    const dadosLocais = localStorage.getItem(name);

    // 2. Verifica se o utilizador realmente existe
    if (!dadosLocais) {
        alert("Utilizador não encontrado!");
        return; // Para a execução aqui
    }

    // 3. Converte os dados para objeto de forma segura
    const userSalvo = JSON.parse(dadosLocais);

    // 4. Valida a palavra-passe
    if (userSalvo.password === pass) {
        alert("Login feito com sucesso!!!!");
        sessionStorage.setItem("userLoged", name);
        window.location.href = "body.html";
    } else {
        alert("Palavra-passe incorreta! Falhou!!!");
    } 
});
