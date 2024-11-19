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
// Escuchar el evento de pago
document.querySelector('#pagar-carrito').addEventListener('click', realizarPago);

function realizarPago(e) {
     e.preventDefault();

     // Calcular el monto total del carrito
     const total = articulosCarrito.reduce((total, plato) => total + parseFloat(plato.precio.replace('USD', '').trim()) * plato.cantidad, 0);

     // Crear un objeto para enviar al backend
     const datosPago = {
          description: "Compra de platillos",
          amount: total * 100, // Stripe trabaja con centavos
          currency: 'USD',
     };

     // Realizar una solicitud al backend para crear un PaymentIntent
     fetch('http://localhost:3600/stripe/paymentinten', {
          method: 'POST',
          headers: {
               'Content-Type': 'application/json',
          },
          body: JSON.stringify(datosPago),
     })
          .then(response => response.json())
          .then(data => {
               // Aquí puedes redirigir al usuario al proceso de pago de Stripe
               const paymentIntentId = data.id; // Asumimos que el backend devuelve el ID del PaymentIntent
               confirmPayment(paymentIntentId);
          })
          .catch(error => {
               console.error('Error al realizar el pago:', error);
          });
}

function confirmPayment(paymentIntentId) {
     // Confirmar el pago en el backend
     fetch(`http://localhost:3600/stripe/confirm/${paymentIntentId}`, {
          method: 'POST',
     })
          .then(response => response.json())
          .then(data => {
               console.log('Pago confirmado:', data);
               // Aquí puedes redirigir al usuario a una página de éxito
               window.location.href = "/success"; // Redirigir a una página de éxito
          })
          .catch(error => {
               console.error('Error al confirmar el pago:', error);
          });
}
