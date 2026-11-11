document.addEventListener("DOMContentLoaded",()=>{
 const form=document.getElementById("formu"),message=document.getElementById("mensagem");
 form.addEventListener("submit",async event=>{
  event.preventDefault();const button=form.querySelector("button[type=submit]");button.disabled=true;message.textContent="A iniciar sessão…";
  try{const user=await IdeiasAPI.login({email:form.elements.nome.value.trim(),password:form.elements.password.value});IdeiasAPI.signIn(user);location.href="body.html";}
  catch(error){message.textContent=error.message;message.className="erro";}
  finally{button.disabled=false;}
 });
});
