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