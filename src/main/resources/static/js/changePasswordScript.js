$(document).ready(function(){
	$("#update-password").on("submit",function(event){
        event.preventDefault();
        $("#status").show();
        if($("#nPassword").val()==$("#rPassword").val()){
			var form = $(this).serialize();
	        $.ajax({
	            url: "/setting/update-password",
	            type: "POST",
	            data: form,
	           	success: function(data){
					if(data.trim()==="done"){
						$("#status").removeClass("text-danger");
		                $("#status").addClass("text-success");
		                $("#status").html("Successfully changed password.");
					} else if(data.trim()==="invalid"){
		                $("#status").removeClass("text-success");
		                $("#status").addClass("text-danger");
		                $("#status").html("Old password doesn't matched.");
					} else {
		                $("#status").removeClass("text-success");
		                $("#status").addClass("text-danger");
		                $("#status").html("Something went wrong!!");	
					}
	            },
	            error: function(){
	                $("#status").removeClass("text-success");
	                $("#status").addClass("text-danger");
	                $("#status").html("Something went wrong. Please try again later.");
	            }
	        });
		} else {
	        $("#status").removeClass("text-success");
            $("#status").addClass("text-danger");
            $("#status").html("New Password doesn't match with Old Password.");
		}
    });
});