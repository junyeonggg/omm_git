/* 키워드 검색시 실행*/
function searchKeyword() {
	const keyword = document.querySelector("#keyword").value
	window.location.href = `http://localhost:8080/recipe_list?keyword=${keyword}`;

}


// user_id
// comment_content
// comment_create_date
// target_id
// parent_comment_id
// comment_rating
// reference_type
function insertReplyBtn(self, reference_type) {
	const user_id = self.getAttribute("data-user-id")

	// 댓글인지 대댓글인지 판별 (댓글이면 댓글textarea, 대댓글이면 대댓글textarea의 값을 가져옴)
	const type = self.getAttribute("data-type")
	const comment_content = document.querySelector(`#${type}`).value;
	const user_nickname = self.getAttribute("data-user-nickname");
	const comment_parent_id = self.getAttribute("data-parent-comment-id")
	const target_id = self.getAttribute("data-target-id");

	var rating = 0;
	const rating_radio_list = document.getElementsByName("rating")
	for (radio of rating_radio_list) {
		if (radio.checked & radio.value > rating) {
			rating = radio.value
		}
	}
	$.ajax({
		type: "post",
		url: "/addreply",
		data: {
			'user_id': user_id,
			'comment_content': comment_content,
			'target_id': target_id,
			'parent_comment_id': comment_parent_id,
			'reference_type': reference_type,
			'comment_rating': rating
		},
		success: () => {
			window.alert("댓글을 등록하였습니다.");
			window.location.reload()
		},
		error: () => {
			window.alert("댓글 등록에 실패하였습니다.")
		}

	})


}
function increView(table_name, column_name, target_column, target_id) {
	$.ajax({
		type: "post",
		url: "/increView",
		data: {
			'table_name': table_name,
			'column_name': column_name,
			'target_column': target_column,
			'target_id': target_id
		}
	})

}
function likeSet(self,reference_type, recipe_id) {
	$.ajax({
		type: "post",
		url: "/likeSet",
		data: {
			'reference_type' : reference_type,
			'target_id': recipe_id,
			'checked': self.checked
		}

	})


}