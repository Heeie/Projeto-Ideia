document.getElementById("formu").addEventListener("submit", function(event) {

    event.preventDefault();

    const name = document.getElementById("nome").value;
    const mail = document.getElementById("email").value;
    const pass = document.getElementById("password").value;

    console.log(name);
    console.log(mail);
    console.log(pass);

    // 1. Verifica se o utilizador já existe no localStorage
    if (localStorage.getItem(name) !== null) {
        alert("Este nome de utilizador já está registado! Escolha outro.");
        return; // Interrompe a execução e não deixa criar a conta
    }

    // 2. Cria o objeto do novo utilizador
    const usuario = { nome: name, email: mail , password: pass,  ideias: []};

    // 3. Converte o objeto para string e guarda no localStorage
    localStorage.setItem(name, JSON.stringify(usuario));

    alert("Conta criada com sucesso!");

    // 4. Redireciona para a página de login após o registo (opcional)
    window.location.href = "login.html"; 
});
