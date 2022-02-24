$(document).ready(function(){
	window.addEventListener("scroll",function(){
	let limitBottom = document.documentElement.offsetHeight - window.innerHeight;
	  if(document.documentElement.scrollTop >= limitBottom){
		flag = true
		load()
	  }
	})
	let flag = true
	let pageNo = 0
	load()
	function load() {
		if(flag) {
		pageNo += 1
		flag = false;
		$.ajax({
			url: "/load-home",
			method: "GET",
			data: {"page":pageNo},
			success: function(data) {
				let temp = "";
				for(let i=0; i<data.length; i++) {
		    		temp += `<div class="post mt-2">
					    		<div class="row mt-2 mb-2 ml-1 mr-1">
					    			<div class="col-10 pl-2">
						    			<a href="/u/${data[i].username}" class="text-dark text-decoration-none" style="cursor: pointer; display: block" target="_blank">
							    			<img src="/img/profile/50x50_${data[i].profile}" class="rounded-pill border" style="width: 42px;" alt="img" />
							    			<label style="font-size: larger; font-weight: 500;">${data[i].name}</label>
							    			<small class="text-danger">(edited)</small>
						    			</a>
						    		</div>
						    		<div class="col-2 text-right">
						    			<button class="btn mr-2 mt-2" style="font-size: x-larger;" data-toggle="dropdown" aria-expanded="false">
						    				<i class="fa fa-ellipsis-v" aria-hidden="true"></i>
						    			</button>
										<div class="dropdown-menu dropdown-menu-right pl-2 pr-2">
											<a href="/u/${data[i].username}" class="dropdown-item" target="_blank"><i class="fa fa-user" aria-hidden="true"></i>&nbsp;&nbsp;View Profile</a>
											<a href="/p/${data[i].id}" class="dropdown-item" target="_blank"><i class="fa fa-user" aria-hidden="true"></i>&nbsp;&nbsp;View Post</a>
										</div>
						    		</div>
					    		</div>
					    		<pre class="mb-1 mt-2 ml-1 mr-1" >${data[i].caption}</pre>
					    		<div class="text-center" >`;
					if(data[i].format=="image"){
						temp += `	<img src="/img/post/${data[i].pathOfPost}" class="img-fluid" alt="post" width="480px"/>`;	
					} else if(data[i].format=="video"){
						temp += `	<div style="position: absolute;width:100%; padding-top: 100px; z-index:99;">
										<div style="position: related;">
											<a href="/p/${data[i].id}" class="border border-dark text-dark rounded-pill" style="font-size:36px; border-width: 2px!important; padding-bottom: 2px;">
												<i class="fa fa-play" aria-hidden="true" style="margin: 0px 10px 0px 14px"></i>
											</a>
										</div>
									</div>
									<video src="/img/post/${data[i].pathOfPost}" class="img-fluid" width="480px" ></video>`;
					}   			
					temp +=   `<div class="pt-2 pb-2 pl-0 pr-0">
									<div style="cursor: pointer;" data-toggle="modal" data-target="#likeModal">
										<label class="m-0"><span>${data[i].totalLikes}</span> likes</label>
									</div>
								</div>
							</div>
						</div>
						`;
				}
				$("#home-post").append(temp);
			},
			error: function() {
				alert("Something went wrong!!");
			}
		});
		}
	}
		
});
