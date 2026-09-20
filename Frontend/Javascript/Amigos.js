document.addEventListener("DOMContentLoaded", function () {
    const amigos = document.getElementById("Amigos");
    const pesquisarAmigos = document.getElementById("PesquisarAmigo");
    const pedidosAmigos = document.getElementById("PedidoAmizade");
   
    const amigosBtn = document.getElementById("amigosPage");
    const pesquisaBtn = document.getElementById("pesquisaPage");
    const pedidosBtn = document.getElementById("pedidosPage");

    // amigosPage , pesquisaPage , pedidosPage
    pesquisarAmigos.style.display = "none";
    pedidosAmigos.style.display = "none";
    amigos.style.display = "block"; 

    amigosBtn.addEventListener("click", function () {
        if ( amigos.style.display === "none") {
            amigos.style.display = "block";
            pesquisarAmigos.style.display = "none";
            pedidosAmigos.style.display = "none";
        } else {
            amigos.style.display = "block";
        }
    });

    pesquisaBtn.addEventListener("click", function () {
        if ( pesquisarAmigos.style.display === "none") {
            amigos.style.display = "none";
            pesquisarAmigos.style.display = "block";
            pedidosAmigos.style.display = "none";
        } else {
            pesquisarAmigos.style.display = "none";
        }
    });
    
    pedidosBtn.addEventListener("click", function () {
        if ( pedidosAmigos.style.display === "none") {
            amigos.style.display = "none";
            pesquisarAmigos.style.display = "none" ;
            pedidosAmigos.style.display = "block";
        } else {
            pedidosAmigos.style.display = "none";
        }
    });
});



