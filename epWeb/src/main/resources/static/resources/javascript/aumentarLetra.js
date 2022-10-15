document.addEventListener('DOMContentLoaded', () => {
    const html = document.querySelector('html');
    const inputZoomMode = document.getElementById('input-zoom-mode');

    if(localStorage.getItem('zoom-mode')){
        html.setAttribute("zoom", "true");
    }

    inputZoomMode.addEventListener('change', () => {
        var zoomModeStorage = localStorage.getItem('zoom-mode');
        zoomModeStorage ? html.removeAttribute("zoom") : html.setAttribute("zoom", "true");
        zoomModeStorage ? localStorage.removeItem('zoom-mode') : localStorage.setItem('zoom-mode', true);
   })
})