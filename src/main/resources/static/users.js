// filter
//reset values
var reset_input_values = document.querySelectorAll('.userFilter_input');
for (var i = 0; i < reset_input_values.length; i++) {
    reset_input_values[i].value = '';
}
//pick from drop down
let currentSelectedOption = "#username"

// search field
function mapSearchValue(event){
    const hiddenField = document.querySelector(currentSelectedOption)
    hiddenField.value = event.target.value
}