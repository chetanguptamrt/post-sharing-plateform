$(document).ready(function(){
	let pageNo=1;
    $("#load").on("click",function(){
        $.ajax({
            url: "/setting/like-post-load",
            data: {"pageNo": pageNo},
            method: "post",
            success: function(data){
				console.log(data)
                pageNo+=1;
                let content = "";
                for(let i=0; i<data.size; i++) {
					console.log(data.posts[i])
					content += `<div class="col-3 p-0 border profile-post">
									<a href="/p/${data.posts[i].id}">`;
					if(data.posts[i].format=="image"){
						content += `<img class="img-custom" src="/img/post/${data.posts[i].pathOfPost}" alt="post" />`;	
					} else if(data.posts[i].format=="video"){
						content += `<video class="img-custom" src="/img/post/${data.posts[i].pathOfPost}" ></video>
									<span class="img-video text-white"><i class="fa fa-video-camera" aria-hidden="true"></i></span>`;
					}
					content += `	</a>
								</div>`
				}
				$("#like-post").append(content);
				if(data.pageNo*10+data.size>=data.totalElement){
					$("#load").hide();
				}
            },
            error: function(){
                alert("Something went wrong!");
            }
        });
    });
});