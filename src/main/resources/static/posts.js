const formCloseBtn = document.querySelector("#createPost_close_btn")

formCloseBtn.addEventListener("click", function () {
    const popupWrapper = document.querySelector(".popup_wrapper")
    popupWrapper.classList.add("hide")
})


const showFormBtn = document.querySelector("#show_form")
if(showFormBtn) {
    showFormBtn.addEventListener("click", function () {
        const popupWrapper = document.querySelector(".popup_wrapper")
        popupWrapper.classList.remove("hide")
    })
}

const creationFormPopupBackground = document.querySelector("#creationForm_background")
creationFormPopupBackground.addEventListener("click", function () {
    const popupWrapper = document.querySelector(".popup_wrapper")
    popupWrapper.classList.add("hide")
})

const createPostButton = document.querySelector("#createPost_btn")
createPostButton.addEventListener("click",function (e) {
    const title = document.querySelector("#createPost_form").title.value
    const content = document.querySelector("#createPost_form").content.value
    const titleErrorLabel = document.querySelector("#titleError_label")
    const contentErrorLabel = document.querySelector("#contentError_label")
    titleErrorLabel.classList.add("hide")
    contentErrorLabel.classList.add("hide")

    if( title==null || title.length > 64 || title.length < 16){
        e.preventDefault()
        titleErrorLabel.classList.remove("hide")
    }
    if( content==null || content.length > 8192 || content.length < 32){
        e.preventDefault()
        contentErrorLabel.classList.remove("hide")
    }
})

// filter
var reset_input_values = document.querySelectorAll('.filter_input');
for (var i = 0; i < reset_input_values.length; i++) {
    reset_input_values[i].value = '';
}
let currentSelectedOption = "#username"