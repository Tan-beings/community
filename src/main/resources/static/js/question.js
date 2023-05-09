"use strict";
function post(){
    const content = $("[name='comment_content']").val();
    const parentId = $("[name='question_id']").val();
    $.ajax({
        url:"/doComment",
        type:"post",
        data:JSON.stringify({
            "parentId":parentId,
            "content":content,
            "type":1
        }),
        dataType:"json",
        contentType:"application/json",
        success:function(res){
            // 如果用户没有登录
            if(res.code === 1004){
                 const loginCheck = confirm(res.message);
                 if(loginCheck){
                     window.open("https://github.com/login/oauth/authorize?client_id=4e3f6fd327f408f7263f&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                     localStorage.setItem("closable",true);
                 }
            }
        },
        error:function (res){
            console.log(res);
            console.log('有错误产生');
        }
    })
}

