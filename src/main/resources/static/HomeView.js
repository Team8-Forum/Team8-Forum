const homePagePostsSwitchBtn = document.querySelector("#homePosts_switch")
homePagePostsSwitchBtn.addEventListener("change", function (event) {
    const value = event.target.value
    const  mostCommentedPostsContainer = document.querySelector("#mostCommented_posts")

    if(value.includes("most-recent")) {
        mostCommentedPostsContainer.classList.add("hide")
    } else if(value.includes("most-commented")) {
        mostCommentedPostsContainer.classList.remove("hide")
    }
})
