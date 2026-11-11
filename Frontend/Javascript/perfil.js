document.addEventListener("DOMContentLoaded",async()=>{
 const user=IdeiasAPI.current();if(!user){location.href="login.html";return;}
 const form=document.getElementById("perfilForm"),feedback=document.getElementById("feedback");
 form.elements.nome.value=user.nome;form.elements.email.value=user.email;
 form.addEventListener("submit",async event=>{
  event.preventDefault();const button=form.querySelector("button[type=submit]");button.disabled=true;
  try{const updated=await IdeiasAPI.updateProfile(user.id,{nome:form.elements.nome.value.trim(),email:form.elements.email.value.trim()});IdeiasAPI.signIn(updated);feedback.textContent="Perfil atualizado na API.";}
  catch(error){feedback.textContent=error.message;}
  finally{button.disabled=false;}
 });
});
