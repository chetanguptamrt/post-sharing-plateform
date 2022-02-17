const deletePost =(id)=> {
	$.ajax({
		url: "/post/remove",
		method: "POST",
		data: {"id": id},
		success: function(data) {
			if(data==="notLogin") {
				alert("Please login...");
				location.href = "/signin";
			} else if(data==="done"){
				location.href = "/profile";
			} else {
				alert("Something went wrong!");
				location.reload();
			}
		}, 
		error: function() {
			alert("Something went wrong!");
			location.reload();
		}
	});
}
const likePost =(id, flag)=>{
	if(flag)
		$.ajax({
			url: "/post/like",
			method: "POST",
			data: {"id": id},
			success: function(data) {
				console.log(data)
				if(data==="notLogin") {
					alert("Please login...");
					location.href = "/signin";
				} else if(data==="done"){
					$("#like").removeClass("text-dark");
					$("#like").addClass("text-primary");
				} else if(data==="dislike"){
					$("#like").removeClass("text-primary");
					$("#like").addClass("text-dark");
				} else {
					alert("Something went wrong!");
					location.reload();
				}
			}, 
			error: function() {
				alert("Something went wrong!");
				location.reload();
			}
		});
	else {
		alert("Please login...");
		location.href = "/signin";		
	}
}
const getPostLike =(f, id)=> {
	if(f)
		$.ajax({
			url: "/post/likes",
			method: "POST",
			data: {"id": id},
			success: function(data) {
				let result = data;
				let a = "";
				for(let i = 0; i<result.length; i++) {
					a+=`<div class="row m-0 pt-1 pb-1" style="border-bottom: 1px solid #e8e8e8;">
							<div class="col-7">
						    	<img src="/img/profile/50x50_`+result[i].profileImagePath+`" style="width: 38px" alt="img" class="rounded-pill border" />
								<label>`+result[i].name+`</label>
							</div>
							<div class="col-5 mt-1 text-right">
								<a href="/u/`+result[i].userName+`" target="_blank" class="btn btn-sm btn-primary">view profile</a>
							</div>
						</div>`
				}
				if(result.length>0) {
					$("#show-like").html('<div style="border-top: 1px solid #e8e8e8;"></div>');
					$("#show-like").append(a);
					$("#show-like").append('<div class="text-center"><small>End of the list.</small></div>');
					
				} else {
					$("#show-like").html('<div class="text-center">No Likes</div>');
				}
			}, 
			error: function() {
				alert("Something went wrong!");
				location.reload();
			}
		});
}
