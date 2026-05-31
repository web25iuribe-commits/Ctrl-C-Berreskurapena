document.addEventListener("DOMContentLoaded", () => {
  const logoutModalId = "logoutConfirmModal";
  const existingModal = document.getElementById(logoutModalId);

  if (!existingModal) {
    const modalHtml = `
      <div class="modal fade" id="${logoutModalId}" tabindex="-1" aria-labelledby="logoutConfirmLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="logoutConfirmLabel">¿Estás seguro de que quieres salir?</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              Si confirmas, se cerrará tu sesión y volverás a la pantalla de inicio de sesión.
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <a href="#" id="logoutConfirmButton" class="btn btn-danger">Salir</a>
            </div>
          </div>
        </div>
      </div>
    `;

    document.body.insertAdjacentHTML("beforeend", modalHtml);
  }

  const logoutModalEl = document.getElementById(logoutModalId);
  const logoutConfirmButton = document.getElementById("logoutConfirmButton");
  const bootstrapModal = new bootstrap.Modal(logoutModalEl);
  let targetHref = "/logout";

  function showLogoutModal(event) {
    event.preventDefault();
    const target = event.currentTarget;
    targetHref = target.getAttribute("href") || "/logout";
    logoutConfirmButton.setAttribute("href", targetHref);
    bootstrapModal.show();
  }

  document
    .querySelectorAll("a[href='/logout'], a[href=\"/logout\"]")
    .forEach((link) => {
      link.addEventListener("click", showLogoutModal);
    });

  document.querySelectorAll("[data-action='irten']").forEach((button) => {
    button.addEventListener("click", showLogoutModal);
  });
});
