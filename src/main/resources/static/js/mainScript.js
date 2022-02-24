$(document).ready(function(){
    $('video').bind('contextmenu dragstart',function() { return false; });
    $('img').bind('contextmenu dragstart',function() { return false; });
    $("#search").on("keyup",function(){
		let s = $("#search").val();
		if(s.trim()!=="") {
			$.ajax({
				url: "/search",
				method: "POST",
				data: {"q":s},
				success: function(data) {
					let temp = "";
					for(let i=0; i<data.length; i++) {
			    		temp += `<a href="/u/${data[i].username}" style="display: block; font-size: 18px; padding: 2px 4px" class="border text-dark text-decoration-none">
									<img src="/img/profile/50x50_${data[i].profile}" style="width: 35px;" class="rounded-pill border"/>
									<span>${data[i].name}</span>
								</a>`;
					}
					if(data.length==0){
						temp="no result found..."
					}
					$("#show-search").show();
					$("#show-search").html(temp);
				},
				error: function() {
					alert("Something went wrong!!");
				}
			})
		} else {
			$("#show-search").hide();
		}
    });
});
