function showModelError(message, title) {
    //set title message
    document.getElementById("errorModelLabel").innerHTML = title;
    
    //set error message
    message = message.replace(/\n/g , "<br>");
    document.getElementById("errorMessage").innerHTML = message;
    
    // show model
    $("#modelError").modal();
}