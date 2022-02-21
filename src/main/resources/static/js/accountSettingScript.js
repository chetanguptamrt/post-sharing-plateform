$(document).ready(function(){
	$("#username").on("keyup",function(){
		if(this.value!=="") {
	        $("#username-status").show();
	        $.ajax({
	            url: "/setting/check-username",
	            type: "POST",
	            data: {"username": this.value},
	            success: function(data){
					if(data.trim()==="done"){
						$("#username-status").removeClass("text-danger");
				        $("#username-status").addClass("text-success");
				        $("#username-status").html("Username available.");
				        	
					} else {
		                $("#username-status").removeClass("text-success");
		                $("#username-status").addClass("text-danger");
		                $("#username-status").html("Username not available.");	
					}
	            }
	        });	
		} else {
			$("#username-status").show();
            $("#username-status").removeClass("text-success");
            $("#username-status").addClass("text-danger");
            $("#username-status").html("Username can not blank.");	
		}
    });
    $("#edit").on("submit", function(event){
		event.preventDefault();
		f = $(this).serialize();
		$.ajax({
			url: "/setting/update-account",
			method: "POST",
			data: f,
			success: function(data){
				if(data=="done"){
					$("#edit-status").show();
		            $("#edit-status").addClass("text-success");
		            $("#edit-status").removeClass("text-danger");
		            $("#edit-status").html("Successfully Updated.");
				} else if(data=="fillDetail"){
					$("#edit-status").show();
					$("#edit-status").removeClass("text-success");
		            $("#edit-status").addClass("text-danger");
		            $("#edit-status").html("Please fill required details.");
				}  else if(data=="notUsername"){
					$("#edit-status").show();
					$("#edit-status").removeClass("text-success");
		            $("#edit-status").addClass("text-danger");
		            $("#edit-status").html("Username not available.");
				} else {
					$("#edit-status").show();
					$("#edit-status").removeClass("text-success");
		            $("#edit-status").addClass("text-danger");
		            $("#edit-status").html("Something went wrong.");	 
				}
			},
			error: function(){
				$("#edit-status").show();
	            $("#edit-status").removeClass("text-success");
	            $("#edit-status").addClass("text-danger");
	            $("#edit-status").html("Something went wrong. Please try again later.");	
			}
		});
	});
    $("#visibility").on("submit", function(event){
		event.preventDefault();
		f = {
			"accountMode":$("#customSwitch1").prop("checked"),
			"showEmail":$("#customSwitch2").prop("checked"),
			"showPhone":$("#customSwitch3").prop("checked"),
			"showDOB":$("#customSwitch4").prop("checked")
		};
		$.ajax({
			url: "/setting/update-visibility",
			method: "POST",
			data: f,
			success: function(data){
				if(data=="done"){
					$("#visibility-status").show();
		            $("#visibility-status").addClass("text-success");
		            $("#visibility-status").removeClass("text-danger");
		            $("#visibility-status").html("Successfully Updated.");
				} else {
					$("#visibility-status").show();
					$("#visibility-status").removeClass("text-success");
		            $("#visibility-status").addClass("text-danger");
		            $("#visibility-status").html("Something went wrong.");	 
				}
			},
			error: function(){
				$("#visibility-status").show();
	            $("#visibility-status").removeClass("text-success");
	            $("#visibility-status").addClass("text-danger");
	            $("#visibility-status").html("Something went wrong. Please try again later.");	
			}
		});
	});
});