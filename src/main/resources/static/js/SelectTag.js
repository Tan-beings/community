'use strict'

const showTagSelectBtn =$(".show-select-tags");
const hideTagSelectBtn = $(".hide-select-tags");
const tagsSelectPanel = $(".tags-selected-box");
const tagsDisplayedPanel = $(".tags-displayed");
const tagIds = [];
const inputTagIdsSelected = $("input[name='question_tag']")

showTagSelectBtn.on("click",function(e){
    $(this).hide();
    hideTagSelectBtn.show();
    tagsSelectPanel.show();
});

hideTagSelectBtn.on("click",function(e){
    $(this).hide();
    showTagSelectBtn.show();
    tagsSelectPanel.hide();
});

$(document).click(function(e){
    console.log($(e.target));
    if($(e.target).hasClass("show-select-tags")){
        return false;
    }
    if(e.target.closest(".tags-selected-box") == null && tagsSelectPanel.css("display") === "block"){
        hideTagSelectBtn.hide();
        showTagSelectBtn.show();
        tagsSelectPanel.hide();
    }
})

tagsSelectPanel.on("click","div span",function(e){
    if($(this).hasClass("selected")){
        return false;
    }
    if(tagIds.length > 3){
        return false;
    }
    const tagId = $(this).data("id");
    const tagText = $(this).html();
    const li = $(`<li class="tag-displayed-item" data-id="${tagId}">${tagText}</li>`);
    tagsDisplayedPanel.append(li);
    tagIds.push(tagId);
    $(this).addClass("selected");
    inputTagIdsSelected.val(tagIds.join(","));
})

tagsDisplayedPanel.on("click","li",function(e){
    const tagId = $(this).data("id");
    const idIndex = tagIds.indexOf(tagId);
    tagIds.splice(idIndex,1);
    $(this).remove();
    tagsSelectPanel.find(`div span[data-id=${tagId}]`).removeClass("selected");
    inputTagIdsSelected.val(tagIds.join(","));
    console.log(inputTagIdsSelected.val());
})