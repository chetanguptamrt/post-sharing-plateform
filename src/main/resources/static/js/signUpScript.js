$(document).ready(function(){
    $("#sendOtp").on("submit",function(event){
        event.preventDefault();
        var f = $(this).serialize();
	    $("#username").prop("disabled","disabled");
	    $("#otpBtn").prop("disabled","disabled");
        $.ajax({
            url: "/registrationOTP",
            data: f,
            method: "post",
            success: function(data){
                if(data.trim()==="userAlready") {
                    $("#username").prop("disabled","");
                    $("#otpBtn").prop("disabled","");
                    $("#status").show();
                    $("#status").html("User Already Registered!");
                } else if(data.trim()==="fillDetail") {
                    $("#username").prop("disabled","");
                    $("#otpBtn").prop("disabled","");
                    $("#status").show();
                    $("#status").html("Fill detail carefully!");
                } else if(data.trim()==="done") {
                    $("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("OTP send on email");
                    $("#email").val($("#username").val());
                    $("#username").prop("disabled","disabled");
                    $("#otpBtn").prop("disabled","disabled");
                    $("#registerBtn").prop("disabled","");
                } else {
                    $("#username").prop("disabled","");
                    $("#otpBtn").prop("disabled","");
                    $("#status").show();
                    $("#status").html("Something went wrong!");
                }
            },
            error: function(){
                $("#username").prop("disabled","");
                $("#otpBtn").prop("disabled","");
                $("#status").show();
                $("#status").html("Something went wrong!");
            }
        });
    });

    $("#addUser").on("submit",function(event){
        event.preventDefault();
        var f = $(this).serialize();
        $.ajax({
            url: "/registrationuser",
            data: f,
            method: "post",
            success: function(data){
                $("#status").removeClass("text-success");
                $("#status").addClass("text-danger");
                if(data.trim()==="userAlready") {
                    $("#status").show();
                    $("#status").html("User Already Registered!");
                }else if(data.trim()==="fillDetail") {
                    $("#status").show();
                    $("#status").html("Fill detail carefully!");
                } else if(data.trim()==="notMatched") {
                    $("#status").show();
                    $("#status").html("OTP does not matched!");
                } else if(data.trim()==="done") {
                    location.href="/signin?registered";
                } else {
                    $("#status").show();
                    $("#status").html("Something went wrong!");
                }
            },
            error: function(){
                $("#status").show();
                $("#status").html("Something went wrong!");
            }
        });
    });
});