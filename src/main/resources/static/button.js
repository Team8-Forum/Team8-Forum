function deleteUser(event) {
    if(!confirm("You are about to delete your user profile. This action is inevitable")) {
        event.preventDefault()
    }
}
function adminUser(event) {
    if(!confirm("Are you sure you want to make this user admin?")) {
        event.preventDefault()
    }
}
function blockUser(event) {
    if(!confirm("Are you sure you want to block the user?")) {
        event.preventDefault()
    }
}