'use strict'
const box_list_group = $(".list-group");
const a_notification_link = $(".notification-link")
box_list_group.on("click",function(e){
    e.preventDefault();
    const clicked = e.target.closest(".notification-item");
    if(clicked == null){
        return;
    }
    const href = $($(clicked).find("a")[1]).attr("href");
    const sign = $(clicked).find(".notification-read-flag");
    sign.css("display","none");
    location.href = href;
})