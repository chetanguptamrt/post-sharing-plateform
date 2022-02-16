const doFollow =(username)=> {
	$.ajax({
		url: "/follow/do",
		method: "POST",
		data: {"username": username},
		success: function(data) {
			if(data==="notLogin") {
				alert("Please login...");
				location.href = "/signin";
			} else if(data==="done"){
				location.reload();
			} else if(data==="private"){
				$("#f-case-1").hide();
				$("#f-case-2").show();
				$("#f-case-3").hide();
				$("#f-case-4").hide();
				$("#f-case-5").hide();
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
const doUnfollow =(username)=> {
	$.ajax({
		url: "/follow/remove",
		method: "POST",
		data: {"username": username},
		success: function(data) {
			if(data==="notLogin") {
				alert("Please login...");
				location.href = "/signin";
			} else if(data==="done"){
				location.reload();
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
const cancelFollowRequest =(username)=> {
	$.ajax({
		url: "/follow/cancel",
		method: "POST",
		data: {"username": username},
		success: function(data) {
			if(data==="notLogin") {
				alert("Please login...");
				location.href = "/signin";
			} else if(data==="done"){
				$("#f-case-1").hide();
				$("#f-case-2").hide();
				$("#f-case-3").hide();
				$("#f-case-4").show();
				$("#f-case-5").hide();
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
const getUserFollowing =(f, username)=> {
	if(f)
		$.ajax({
			url: "/get-user/following",
			method: "POST",
			data: {"username": username},
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
					$("#show-following").html('<div style="border-top: 1px solid #e8e8e8;"></div>');
					$("#show-following").append(a);
					$("#show-following").append('<div class="text-center"><small>End of the list.</small></div>');
					
				} else {
					$("#show-following").html('<div class="text-center">No Following</div>');
				}
			}, 
			error: function() {
				alert("Something went wrong!");
				location.reload();
			}
		});
}
const getUserFollowers =(f, username)=> {
	if(f)
		$.ajax({
			url: "/get-user/followers",
			method: "POST",
			data: {"username": username},
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
					$("#show-followers").html('<div style="border-top: 1px solid #e8e8e8;"></div>');
					$("#show-followers").append(a);
					$("#show-followers").append('<div class="text-center"><small>End of the list.</small></div>');
					
				} else {
					$("#show-followers").html('<div class="text-center">No Followers</div>');
				}
			}, 
			error: function() {
				alert("Something went wrong!");
				location.reload();
			}
		});
}
