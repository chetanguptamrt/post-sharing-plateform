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
const removeFollower =(username)=> {
	$.ajax({
		url: "/follow/remove-follower",
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
const acceptFollowRequest =(username)=> {
	$.ajax({
		url: "/follow/accept",
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
const declineFollowRequest =(username)=> {
	$.ajax({
		url: "/follow/decline",
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
