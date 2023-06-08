"use strict";
// 加载推荐列表
$(window).load(function(){
    const authorId = $("#author-id").val();
    $.getJSON(`/getAuthorQuestions/${authorId}`,function(data){
        let items = [];
        if(data.data.length > 0) {
            $.each(data.data, function (key, question) {
                const html = `<li class="list-group-item"><a target="_blank" href="/question/${question.id}">${question.title}</a></li>`;

                $(html).appendTo($(".author-questions-list"));
            });
        }
    })

    const tagList = [];
    const tempTagList = document.querySelectorAll(".question-tags-list-item span");
    tempTagList.forEach((e) => tagList.push(e.dataset.id));
    const tags = tagList.join(",");
    const questionId = $("[name='question_id']").val();
    console.log(tags);

    $.getJSON(`/getRelatedQuestions/${questionId}/${tags}`,function(data){
        let items = [];
        console.log(data.data);
        if(data.data.length > 0) {
            $.each(data.data, function (key, question) {
                const html = `<li class="list-group-item"><a target="_blank" href="/question/${question.id}">${question.title}</a></li>`;
                $(html).appendTo($(".recommend-questions-list"));
            });
        }
    })
});





function post(parentId,type,content){
    const receiver = $("#author-id").val();
    $.ajax({
        url:"/doComment",
        type:"post",
        data:JSON.stringify({
            "parentId":parentId,
            "content":content,
            "type":type,
            "receiver":receiver
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
            }else if(res.code === 200){
                 console.log("yes");
                 location.reload();
            }
        },
        error:function (res){
            console.log(res);
            console.log('有错误产生');
        }
    })
}


const questionCommentsPost =  function(){
    // debugger;
    const content = $("[name='comment_content']").val();
    if(content === ""){
        alert("回复不能为空哦");
        return;
    }
    if(content.length > 20){
        alert("回复字数不能超过20个！");
        return;
    }
    const parentId = $("[name='question_id']").val();
    post(parentId,1,content);

}

const getSubComments = function(parentCommentId,childrenCommentBox){
    console.log(childrenCommentBox);
    $.getJSON( `/getSubComments/${parentCommentId}`, function( data ) {
        let items = []
        if(data.data.length >= 1) {
            $.each(data.data, function (key, comment) {
                const html = `<li>` +
                    `<a class='aw-user-name' data-id='${comment.id}' href='#'> ` +
                    `<img alt='' data-bd-imgshare-binded='1' src='../static/imgs/girl2.jpeg'> </a> ` +
                    `<div> ` +
                    `<p class='clearfix'> ` +
                    `<span class='pull-right'> ` +
                    `<a href='javascript:;'onClick=''>回复</a>` +
                    `</span> ` +
                    `<a class='aw-user-name author' data-id='18896' href='#'>${comment.user.name} </a>` +
                    `•<span>${new Date(comment.gmtCreatetime).toLocaleString()}</span> ` +
                    `</p> ` +
                    `<p class='clearfix'>${comment.content}</p> ` +
                    `</div> ` +
                    `</li>`
                items.push(html);
            });
        }
        $("<ul/>", {
            html: items.join("")
        }).appendTo(childrenCommentBox);
    });

}


function collapseComment(ele){
    const parentCommentId = $(ele).data("id");
    const parentCommentBox= $(`#aw-comment-box-answer-${parentCommentId}`);
    const childrenCommentBox = $(parentCommentBox).find(".aw-comment-list");
    if(! parentCommentBox.hasClass("comment-box-hidden")){
        parentCommentBox.addClass("comment-box-hidden");
        childrenCommentBox.empty();
    }else{
        parentCommentBox.removeClass("comment-box-hidden");
        getSubComments(parentCommentId,childrenCommentBox);
        const commentBtn = $(parentCommentBox).find(".comment-btn");
        $(commentBtn[0]).on("click",function(e){
            const commentContent = $(parentCommentBox).find(".aw-comment-txt").val();
            post(parentCommentId,2,commentContent);
        });
    }
}


const showQuestions = () => {
    $(".author-questions-list").toggleClass("open");
    $(".show-author-questions-btn").toggleClass("icon-arrow-left-bold")
                                   .toggleClass("icon-arrow-down");
};

