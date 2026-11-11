document.addEventListener("DOMContentLoaded",async()=>{
 const name=Ideias.user(), list=document.getElementById("ideiasScroll"), empty=document.getElementById("semIdeias");
 if(!name){location.href="login.html";return;}
 try{await Ideias.load();}catch(error){list.innerHTML=`<p role="alert">${Ideias.escape(error.message)}</p>`;return;}
 const own=Ideias.all().filter(item=>String(item.clienteId)===String(Ideias.userId())); document.getElementById("contadorIdeias").textContent=`${own.length} ${own.length===1?"ideia criada":"ideias criadas"}`;
 list.replaceChildren(); empty.hidden=own.length>0;
 own.slice().sort((a,b)=>new Date(b.criadoEm)-new Date(a.criadoEm)).forEach(item=>{const card=document.createElement("article");card.className="ideia";card.innerHTML=`<div class="imgIdeia" aria-hidden="true">💡</div><div class="textosIdeia"><span class="ctgIdeia">${Ideias.escape(item.categoria)}</span><h3 class="tituloIdeia">${Ideias.escape(item.titulo)}</h3><p class="descriIdeia">${Ideias.escape(item.descricao)}</p></div><button class="botao-editar" type="button">✏️ Editar</button>`;card.addEventListener("click",e=>{if(!e.target.closest("button"))location.href=`ideiaPage.html?id=${encodeURIComponent(item.id)}`;});card.querySelector("button").addEventListener("click",()=>location.href=`ideiaPage.html?id=${encodeURIComponent(item.id)}&editar=1`);list.append(card);});
});
