document.addEventListener('DOMContentLoaded', () => {
    const zoomModeStorage = localStorage.getItem('zoom-mode')
    const html = document.querySelector('html')
    const inputZoomMode = document.getElementById('input-zoom-mode')

    if(zoomModeStorage){
        html.setAttribute("zoom", "true")
    }

    inputZoomMode.addEventListener('change', () => {
        if(inputZoomMode.checked){
            html.setAttribute("zoom", "true")
            localStorage.setItem('zoom-mode', true)
        }else{
            html.removeAttribute("zoom")
            localStorage.removeItem('zoom-mode')
        }
   })
})