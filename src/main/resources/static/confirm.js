function deleteUser(event) {
    if(!confirm("You are about to delete your user profile. This action is inevitable")) {
        event.preventDefault()
    }
}

function updateRole(event) {
    if(confirm("Are you sure you want to update the role?")) {
        document.querySelector("#updateRole_form").submit()
    } else  {
        const selectedOption = event.target.options[event.target.options.selectedIndex]
        selectedOption.selected = false
    }
}

function blockUser(event) {
    if(!confirm("Are you sure you want to block the user?")) {
        event.preventDefault()
    }
}