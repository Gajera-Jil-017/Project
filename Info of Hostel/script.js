function login(){
    let name = document.getElementById("Input1").value.trim();
    let password = document.getElementById("Input2").value.trim();

    if (name == "" || password == ""){
        alert("Please enter inputs")
    }
    else{
        alert("Login Successfully!");
    }
}

function signup(){
    let name = document.getElementById("Input1").value.trim();
    let email = document.getElementById("Input2").value.trim();
    let password = document.getElementById("Input3").value.trim();

    if (name === "" || email === "" || password === ""){
        alert("Please enter inputs")
    }
    else{
        alert("Login Successfully!");
    }
}

function f1(id){
    id.style.transform = "scale(100%)";
}

function f2(id){
    id.style.transform = "scale(105%)";
}

function f3(id){
    id.style.backgroundColor = "#eef0f5";
}

function f4(id){
    id.style.backgroundColor = "white";
}

function f5(){
    alert("Message send successfully")
}