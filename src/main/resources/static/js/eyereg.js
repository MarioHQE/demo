const visualizar = document.getElementById("visualizar");
const passwordInput = document.getElementById("password-input");
const visualizar2 = document.getElementById("visualizar2");
const passwordInput2 = document.getElementById("password-input2");

visualizar.addEventListener("click", () => {
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        visualizar.classList.remove("fa-eye");
        visualizar.classList.add("fa-eye-slash");
    } else {
        passwordInput.type = "password";
        visualizar.classList.remove("fa-eye-slash");
        visualizar.classList.add("fa-eye");
    }
});
visualizar2.addEventListener("click", () => {
    if (passwordInput2.type === "password") {
        passwordInput2.type = "text";
        visualizar2.classList.remove("fa-eye");
        visualizar2.classList.add("fa-eye-slash");
    } else {
        passwordInput2.type = "password";
        visualizar2.classList.remove("fa-eye-slash");
        visualizar2.classList.add("fa-eye");
    }
});