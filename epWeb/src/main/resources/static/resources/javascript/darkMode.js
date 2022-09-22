document.addEventListener('DOMContentLoaded', () => {
    const darkModeStorage = localStorage.getItem('dark-mode')
    const body = document.querySelector('body')
    const inputDarkMode = document.getElementById('input-dark-mode')

    if(darkModeStorage){
        body.setAttribute("dark", "true")
    }

    inputDarkMode.addEventListener('change', () => {
        if(inputDarkMode.checked){
            body.setAttribute("dark", "true")
            localStorage.setItem('dark-mode', true)
        }else{
            body.removeAttribute("dark")
            localStorage.removeItem('dark-mode')
        }
   })
})


document.addEventListener('DOMContentLoaded', () => {
    const darkModeStorage = localStorage.getItem('dark-mode')
    const card = document.querySelector('.card')
    const inputDarkMode = document.getElementById('input-dark-mode')

    if(darkModeStorage){
        card.setAttribute("dark", "true")
    }

    inputDarkMode.addEventListener('change', () => {
        if(inputDarkMode.checked){
            card.setAttribute("dark", "true")
            localStorage.setItem('dark-mode', true)
        }else{
            card.removeAttribute("dark")
            localStorage.removeItem('dark-mode')
        }
   })
})