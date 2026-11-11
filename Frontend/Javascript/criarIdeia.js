document.addEventListener("DOMContentLoaded",()=>{
 const form=document.getElementById("formu"), imageInput=document.getElementById("img"), preview=document.getElementById("preview"), previewContainer=document.getElementById("preview-container");
 const showError=message=>{let output=document.getElementById("apiError");if(!output){output=document.createElement("p");output.id="apiError";output.setAttribute("role","alert");form.append(output);}output.textContent=message;};
 imageInput.addEventListener("change",()=>{
  const file=imageInput.files[0];if(!file){preview.removeAttribute("src");previewContainer.style.display="none";return;}
  if(!["image/jpeg","image/png","image/webp"].includes(file.type)){showError("Usa uma imagem JPG, PNG ou WebP.");imageInput.value="";preview.removeAttribute("src");previewContainer.style.display="none";return;}
  if(file.size>2*1024*1024){showError("A imagem deve ter no máximo 2 MB.");imageInput.value="";preview.removeAttribute("src");previewContainer.style.display="none";return;}
  const reader=new FileReader();reader.addEventListener("load",()=>{preview.src=reader.result;previewContainer.style.display="block";});reader.readAsDataURL(file);
 });
 form.addEventListener("submit",async event=>{
  event.preventDefault();const user=IdeiasAPI.current();if(!user){alert("Inicia sessão para publicar uma ideia.");location.href="login.html";return;}
  const titulo=document.getElementById("ideia").value.trim(),descricao=document.getElementById("descri").value.trim();if(!titulo||!descricao)return;
  const button=form.querySelector("button[type=submit]");button.disabled=true;
  try{
   const categoria=IdeiasAPI.categoryValues[document.getElementById("categoria").value]||"OUTRA";
   const result=await IdeiasAPI.createIdea({nome:titulo,descricao,imagem:preview.src||null,categoria,estado:"PUBLICA",clienteId:user.id});
   location.href=`ideiaPage.html?id=${encodeURIComponent(result.id)}`;
  }catch(error){showError(error.message);button.disabled=false;}
 });
});
