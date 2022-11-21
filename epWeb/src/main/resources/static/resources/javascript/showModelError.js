function showModelError(message, title, isProblemListField) {
    //set title message
    document.getElementById("errorModelLabel").innerHTML = title;
    
    //set error message
    message = message.replace(/\n/g , "<br>");
    document.getElementById("errorMessage").innerHTML = message;
    document.getElementById("showProblemListField").hidden = !isProblemListField;
    // show model
    $("#modelError").modal();
}