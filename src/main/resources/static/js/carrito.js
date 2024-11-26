const token = document.querySelector('input[name="token"]').value;
const correo = document.querySelector('input[name="correo"]').value;
document.getElementById('pagar-carrito').addEventListener('click', async () => {
     const carrito = []; // Obtén los datos del carrito desde tu tabla
     document.querySelectorAll('#lista-carrito tbody tr').forEach(row => {
          carrito.push({
               id_plato: Number(row.cells[0].innerText),
               nombre: row.cells[2].innerText,
               precio: row.cells[3].innerText,
               cantidad: Number(row.cells[4].innerText)
          });
          console.log(carrito.map(item => item.id_plato));
     });
     console.log(correo);
     const datosPedido = {
          correo: correo, // Cambiar por el correo del usuario autenticado
          id_platos: carrito.map(item => item.id_plato),
          estado: "ESPERA",
          cantidades: carrito.map(item => item.cantidad)
     };
     if (token !== null && token !== "") {
          const response = await fetch('/pedido/guardar', {
               method: 'POST',
               headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
               },
               body: JSON.stringify(datosPedido)
          });
          const result = await response.text();

          console.log(response);
          alert(result);
          if (response.status === 200) {
               window.location.reload();
          }
     }
     else {
          alert('No has iniciado sesión');
          window.location.href = '/login_admin';
     }


});
// Variables
const carrito = document.querySelector('#carrito');
const listaplato = document.querySelector('#lista-cursos');
const contenedorCarrito = document.querySelector('#lista-carrito tbody');
const vaciarCarritoBtn = document.querySelector('#vaciar-carrito');
let articulosCarrito = [];

// Listeners
cargarEventListeners();

function cargarEventListeners() {
     // Dispara cuando se presiona "Agregar Carrito"
     listaplato.addEventListener('click', agregarCurso);

     // Cuando se elimina un curso del carrito
     carrito.addEventListener('click', eliminarCurso);

     // Al Vaciar el carrito
     vaciarCarritoBtn.addEventListener('click', vaciarCarrito);

}


// Funciones
// Función que añade el curso al carrito
function agregarCurso(e) {
     e.preventDefault();
     // Delegation para agregar-carrito
     if (e.target.classList.contains('agregar-carrito')) {
          const plato = e.target.parentElement.parentElement;
          // Enviamos el curso seleccionado para tomar sus datos
          leerDatosCurso(plato);
     }
}

// Lee los datos del curso
function leerDatosCurso(plato) {
     const infoplato = {
          imagen: plato.querySelector('img').src,
          titulo: plato.querySelector('h4').textContent,
          precio: plato.querySelector('.precio span').textContent,
          id: plato.querySelector('a').getAttribute('data-id'),
          cantidad: 1
     }


     if (articulosCarrito.some(plato => plato.id === infoplato.id)) {
          const platos = articulosCarrito.map(plato => {
               if (plato.id === infoplato.id) {
                    plato.cantidad++;
                    return plato;
               } else {
                    return plato;
               }
          })
          articulosCarrito = [...platos];
     } else {
          articulosCarrito = [...articulosCarrito, infoplato];
     }

     // console.log(articulosCarrito)



     // console.log(articulosCarrito)
     carritoHTML();
}

// Elimina el curso del carrito en el DOM
function eliminarCurso(e) {
     e.preventDefault();
     if (e.target.classList.contains('borrar-curso')) {
          // e.target.parentElement.parentElement.remove();
          const platoId = e.target.getAttribute('data-id')

          // Eliminar del arreglo del carrito
          articulosCarrito = articulosCarrito.filter(plato => plato.id !== platoId);

          carritoHTML();
     }
}


// Muestra el curso seleccionado en el Carrito
function carritoHTML() {

     vaciarCarrito();

     articulosCarrito.forEach(plato => {
          const row = document.createElement('tr');
          row.innerHTML = `
             <td>${plato.id}</td>
              <td>  
                   <img src="${plato.imagen}" width=100>
              </td>
              <td>${plato.titulo}</td>
              <td>${plato.precio}</td>
              <td>${plato.cantidad} </td>
              <td>
                   <a href="#" class="borrar-curso" data-id="${plato.id}">X</a>
              </td>
         `;
          contenedorCarrito.appendChild(row);
     });

}

// Elimina los cursos del carrito en el DOM
function vaciarCarrito() {
     // forma lenta
     // contenedorCarrito.innerHTML = '';


     // forma rapida (recomendada)
     while (contenedorCarrito.firstChild) {
          contenedorCarrito.removeChild(contenedorCarrito.firstChild);
     }
}
