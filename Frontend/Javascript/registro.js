document.addEventListener("DOMContentLoaded",()=>{
 const form=document.getElementById("formu");
 form.addEventListener("submit",async event=>{
  event.preventDefault();const button=form.querySelector("button[type=submit]");button.disabled=true;
  try{const user=await IdeiasAPI.createUser({nome:form.elements.nome.value.trim(),email:form.elements.email.value.trim(),password:form.elements.password.value});IdeiasAPI.signIn(user);location.href="body.html";}
  catch(error){let message=form.querySelector(".api-error");if(!message){message=document.createElement("p");message.className="api-error";message.setAttribute("role","alert");form.append(message);}message.textContent=error.message;}
  finally{button.disabled=false;}
 });
});
