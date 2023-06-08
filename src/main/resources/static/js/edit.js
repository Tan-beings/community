const alert = $(".alert-input");
const titleInput = $("#title");
const descriptionInput = $("#description");
const tagInput = $("#tag");
function checkValidData(){
    console.log(titleInput.val());
    console.log(descriptionInput.val());
    console.log(tagInput.val());
    if(titleInput.val() == null || titleInput.val().trim() === ""){
        alert.html("标题输入不能为空");
        alert.removeClass("hidden");
        return false;
    }
    if(descriptionInput.val() == null || descriptionInput.val().trim() === ""){
        alert.html("内容输入不能为空");
        alert.removeClass("hidden");
        return false;
    }
    if(tagInput.val() == null || tagInput.val().trim() === ""){
        alert.html("标签输入不能为空");
        alert.removeClass("hidden");
        return false
    }
    return true;
}