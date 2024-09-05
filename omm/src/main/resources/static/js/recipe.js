/* 키워드 검색시 실행*/
function searchKeyword(){
	const keyword = document.querySelector("#keyword").value
	window.location.href=`http://localhost:8080/recipe_list?keyword=${keyword}`;
	
}


// user_id
// comment_content
// comment_create_date
// target_id
// parent_comment_id
// comment_rating
// reference_type
function insertReplyBtn(self,reference_type){
	const user_id = self.getAttribute("data-user-id")
	const comment_content = document.querySelector("#comment_content").value;
	const user_nickname = self.getAttribute("data-user-nickname");
	const target_id = self.getAttribute("data-target-id");
	var rating = 0;
	const rating_radio_list = document.getElementsByName("rating")
	for(radio of rating_radio_list){
		if(radio.checked & radio.value > rating){
			rating = radio.value 
		}
	}
	$.ajax({
		type:"post",
		url : "/addreply",
		data : {'user_id' : user_id,
				'comment_content':comment_content,
				'target_id':target_id,
				'reference_type':reference_type,
				'comment_rating':rating
				},
		success : ()=>{
			window.alert("댓글을 등록하였습니다.");
		 	window.location.reload()
		 	},
		error : ()=>{
			window.alert("댓글 등록에 실패하였습니다.")}
		
	})
	
	
}