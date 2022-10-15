document.addEventListener('DOMContentLoaded', () => {
    const body = document.querySelector('body');
    const inputDarkMode = document.getElementById('input-dark-mode');
     
    if(localStorage.getItem('dark-mode')){
        body.setAttribute("dark", "true");
    }

    inputDarkMode.addEventListener('change', () => {
        var darkModeStorage = localStorage.getItem('dark-mode');
        darkModeStorage ? body.removeAttribute("dark") : body.setAttribute("dark", "true"); 
        darkModeStorage ? localStorage.removeItem('dark-mode'): localStorage.setItem('dark-mode', true);
   });
});


document.addEventListener('DOMContentLoaded', () => {
    const card = document.querySelector('.card');
    const inputDarkMode = document.getElementById('input-dark-mode');

    if(localStorage.getItem('dark-mode')){
        card.setAttribute("dark", "true");
    }

    inputDarkMode.addEventListener('change', () => {
        var darkModeStorage = localStorage.getItem('dark-mode');
        darkModeStorage ? card.removeAttribute("dark") : card.setAttribute("dark", "true"); 
        darkModeStorage ? localStorage.removeItem('dark-mode'): localStorage.setItem('dark-mode', true);
   });
});