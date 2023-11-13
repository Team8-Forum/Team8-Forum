const profilePictureSwitch = document.querySelector('#profilePictureSwitch')
profilePictureSwitch.addEventListener("click", function  (e) {
    const loggedUser_dropDown = document.querySelector('#loggedUser_dropDown')
    if(loggedUser_dropDown.classList.contains("hide")){
        loggedUser_dropDown.classList.remove("hide")
    }else {
        loggedUser_dropDown.classList.add("hide")
    }
})