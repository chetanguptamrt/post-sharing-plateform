$(document).ready(function(){
	$("#addPost").on("submit",function(event){
        event.preventDefault();
        var form = $("#addPost")[0];
        var formData = new FormData(form);
        $("#status").show();
        $("#status").removeClass("text-danger");
        $("#status").addClass("text-success");
        $("#status").html("Uploading...");
        $.ajax({
            url: "/post/upload",
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
           success: function(data){
				if(data.trim()==="done"){
					location.href = "/profile";
				} else if(data.trim()==="invalidFileType"){
	                $("#status").removeClass("text-success");
	                $("#status").addClass("text-danger");
	                $("#status").html("Invalid file type.");
				} else if(data.trim()==="fileError"){
	                $("#status").removeClass("text-success");
	                $("#status").addClass("text-danger");
	                $("#status").html("File error.");
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
    });
	$("#editPost").on("submit",function(event){
        event.preventDefault();
        var formData = $(this).serialize();
        $("#status").show();
        $("#status").removeClass("text-danger");
        $("#status").addClass("text-success");
        $("#status").html("Updating...");
        $.ajax({
            url: "/post/update",
            type: "POST",
            data: formData,
            success: function(data){
				if(data.trim()==="done"){
	                $("#status").removeClass("text-danger");
	                $("#status").addClass("text-success");
	                $("#status").html("Update successfully.");
				} else if(data.trim()==="notFound"){
	                $("#status").removeClass("text-success");
	                $("#status").addClass("text-danger");
	                $("#status").html("Post not found.");
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
    });
});