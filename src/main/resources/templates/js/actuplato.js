document.querySelectorAll(".btn-edit").forEach((btn) => {
    btn.addEventListener("click", (e) => {
        e.preventDefault();
        var id = btn.getAttribute("data-id");
        var name=btn.getAttribute("data-nombre");
        let id_oculto = document.getElementById("id_empleado");
        let nombre=document.getElementById("actuNombre");

        let texto=document.getElementById("actuDescripcion");
        var text=btn.getAttribute("data-descripcion");

        let prec=document.getElementById("actuprecio");
        var precio=btn.getAttribute("data-precio");

        nombre.value=name;
        id_oculto.value = id;
        texto.value=text;   
        prec.value=precio;

        console.log(nombre.value);
        //console.log(id_oculto);
    });
})