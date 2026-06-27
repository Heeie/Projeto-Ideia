document.getElementById("formu").addEventListener("submit", function(event) {

    event.preventDefault();
    
    // 1. Vai buscar o nome do utilizador com sessão iniciada
    const user = sessionStorage.getItem("userLoged");

    // CORREÇÃO: IDs alterados para corresponder exatamente ao seu HTML
    const tituloIdeia = document.getElementById("ideia").value; // ID era 'ideia' no HTML
    const descrIdeia = document.getElementById("descri").value;  // ID é 'descri' no HTML
    
    // 2. Puxa o objeto do utilizador do localStorage de forma segura
    const dadosLocais = localStorage.getItem(user);
    if (!dadosLocais) {
        alert("Erro: Utilizador não encontrado na sessão!");
        return;
    }
    
    const dadosUsuario = JSON.parse(dadosLocais);

    // 3. Se a lista de ideias não existir, cria-a como um array vazio
    if (!dadosUsuario.ideias) {
        dadosUsuario.ideias = [];
    }

    // 4. Cria o objeto da nova ideia
    const novaIdeia = {
        titulo: tituloIdeia,
        descricao: descrIdeia
    };

    // 5. Adiciona a nova ideia à lista do utilizador
    dadosUsuario.ideias.push(novaIdeia);

    // 6. Guarda o utilizador atualizado de volta no localStorage
    localStorage.setItem(user, JSON.stringify(dadosUsuario));

    alert("Ideia guardada com sucesso!");

    // 7. Limpa os campos do formulário para o utilizador poder escrever outra
    document.getElementById("ideia").value = "";
    document.getElementById("descri").value = "";
});
