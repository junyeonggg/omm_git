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

document.addEventListener('DOMContentLoaded', function() {
	// 댓글 수정 함수
	window.editReplyBtn = function(button) {
		var commentId = button.getAttribute('data-comment-id');
		console.log(commentId)
		var newCommentText = prompt("수정할 댓글을 입력하세요:", ""); // 사용자가 새 댓글을 입력하도록 합니다
		if (newCommentText !== null && newCommentText.trim() !== "") {
			// AJAX 요청을 통해 댓글을 업데이트합니다
			$.ajax({
				url: '/updateComment', // 댓글 업데이트를 처리할 서버의 엔드포인트
				method: 'POST',
				data: {
					'commentId': commentId,
					'newCommentText': newCommentText
				},
				success: function(response) {
					// 성공적으로 댓글이 업데이트된 경우, 페이지를 새로 고침하거나 댓글을 업데이트합니다
					alert("댓글이 수정되었습니다.");
					location.reload(); // 또는 특정 댓글만 업데이트하려면 해당 부분을 수정하세요
				},
				error: function(xhr, status, error) {
					alert("댓글 수정에 실패했습니다. 상태 코드: " + xhr.status);
					// 에러가 발생한 경우 처리
					alert("댓글 수정에 실패했습니다. 다시 시도해 주세요.");
				}
			});
		}
	};

	// 댓글 삭제 함수
	window.deleteReplyBtn = function(button) {
		var commentId = button.getAttribute('data-comment-id');
		if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
			// AJAX 요청을 통해 댓글을 삭제합니다
			$.ajax({
				url: '/deleteComment', // 댓글 삭제를 처리할 서버의 엔드포인트
				method: 'POST',
				data: {
					commentId: commentId
				},
				success: function(response) {
					// 성공적으로 댓글이 삭제된 경우, 페이지를 새로 고침하거나 댓글을 제거합니다
					alert("댓글이 삭제되었습니다.");
					location.reload(); // 또는 특정 댓글만 제거하려면 해당 부분을 수정하세요
				},
				error: function(xhr, status, error) {
					// 에러가 발생한 경우 처리
					alert("댓글 삭제에 실패했습니다. 다시 시도해 주세요.");
				}
			});
		}
	};
});

function insertReplyBtn(self, reference_type) {
	const user_id = self.getAttribute("data-user-id")

	// 댓글인지 대댓글인지 판별 (댓글이면 댓글textarea, 대댓글이면 대댓글textarea의 값을 가져옴)
	const type = self.getAttribute("data-type")
	const comment_content = document.querySelector(`#${type}`).value.trim();
	const user_nickname = self.getAttribute("data-user-nickname");
	const comment_parent_id = self.getAttribute("data-parent-comment-id")
	const target_id = self.getAttribute("data-target-id");

    // 댓글 내용이 빈 칸 또는 공백만으로 이루어진 경우 확인
        if (comment_content.length === 0) {
            alert("댓글을 작성해주세요.");
            return;
        }
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
function likeSet(self, reference_type, recipe_id) {
	$.ajax({
		type: "post",
		url: "/likeSet",
		data: {
			'reference_type': reference_type,
			'target_id': recipe_id,
			'checked': self.checked
		}
	})
}

function insertSequenceTr() {
	const target = document.querySelector("#step_tbl");
	var trEl = document.createElement("tr");
	const no_list = document.querySelectorAll(".org_step_no")
	let num = 0;
	if(no_list.length == 0){
	    num = 1;
	}else{
	    let last_no = no_list[no_list.length - 1].value
        num = Number(last_no) + 1;
	}



	trEl.innerHTML = `<td class="step_no_td"><input class="org_step_no" type="text" value="${num}" readonly="readonly"></td>
						<td class="step_text_td"><textarea class="sequence_text" cols="140" rows="5" placeholder="김치를 먼저 볶아 주세요."></textarea></td>
						<td><input class="sequence_img" type="file"></td>
						<td><input type="button" onclick="deleteSequenceTr(this)" value="X"></td>`
	target.appendChild(trEl);

}

function deleteSequenceTr(self) {
	const target = document.querySelector("#step_tbl");
	target.removeChild(self.parentElement.parentElement)
}

function insertIngreTr() {
	const target = document.querySelector("#ingre_tbl");
	trEl = document.createElement("tr");
	trEl.innerHTML = `<td><input class="ingre_name" type="text" placeholder="재료 이름"></td>
					<td><input class="ingre_info" type="text" placeholder="재료 양"></td>
					<td><input type="button" onclick="deleteIngreTr(this)" value="X"></td>`;
	target.appendChild(trEl);
}
function deleteIngreTr(self) {
	//const target = document.querySelector("#ingre_tbl");
	const target = self.parentElement.parentElement.parentElement
	console.log(target)
	console.log(self)
	console.log(self.parentElement)
	console.log(self.parentElement.parentElement)
	target.removeChild(self.parentElement.parentElement)
}
// 레시피 수정 ( 기존 재료 업데이트 )
function updateRecipeIngre() {
	const org_inger_id_list = document.querySelectorAll(".org_inger_id");
	const org_inger_name_list = document.querySelectorAll(".org_inger_name");
	const org_inger_info_list = document.querySelectorAll(".org_inger_info");
	let recipe_ingre = [];
	for (let i = 0; i < org_inger_id_list.length; i++) {
		let dic = {
			ingre_id: org_inger_id_list[i].value,
			ingre_name: org_inger_name_list[i].value,
			ingre_info: org_inger_info_list[i].value,
		};
		recipe_ingre.push(dic);
	}
	$.ajax({
		type: "post",
		url: "/recipe/edit/ingre",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(recipe_ingre), // 배열을 JSON 문자열로 변환
		dataType: "json",
	})


	//formRecipe.append('recipe_ingre', JSON.stringify(recipe_ingre));
}



function insertRecipeForm() {
	var formRecipe = new FormData();

	// 기본 정보
	formRecipe.append("recipe_title", document.querySelector("#recipe_title").value);
	formRecipe.append("recipe_food_name", document.querySelector("#recipe_food_name").value);
	formRecipe.append("recipe_describe", document.querySelector("#recipe_describe").value);
	formRecipe.append("recipe_method", document.querySelector("#recipe_method").value);
	formRecipe.append("recipe_status", document.querySelector("#recipe_status").value);
	formRecipe.append("recipe_ingredient", document.querySelector("#recipe_ingredient").value);
	// Serving이 빈칸일 때
	if (document.querySelector("#recipe_serving").value == "") {
		formRecipe.append("recipe_serving", "");
	} else {
		formRecipe.append("recipe_serving", document.querySelector("#recipe_serving").value + "인분");
	}
	var time_list = document.querySelectorAll(".time")
	var time = ""
	// time이 빈칼일 때
	if (!(time_list[0].value == "")) {
		time_list.forEach(t => {
			time += t.value;
		});
	}
	formRecipe.append('recipe_time', time);
	formRecipe.append("recipe_level", document.querySelector("#recipe_level").value);

	// 재료 리스트
	const recipe_ingre_name_list = document.querySelectorAll(".ingre_name");
	const recipe_ingre_info_list = document.querySelectorAll(".ingre_info");
	let recipe_ingre = [];
	for (let i = 0; i < recipe_ingre_name_list.length; i++) {
		let dic = {
			ingre_name: recipe_ingre_name_list[i].value,
			ingre_info: recipe_ingre_info_list[i].value,
			ingre_type: '[재료]'
		};
		recipe_ingre.push(dic);
	}
	formRecipe.append('recipe_ingre', JSON.stringify(recipe_ingre));

	// 시퀀스 리스트
	const sequence_text_list = document.querySelectorAll(".sequence_text");
	const sequence_img_list = document.querySelectorAll(".sequence_img");

	let cookingSequenceDto = [];
	for (let i = 0; i < sequence_text_list.length; i++) {
		let sequenceId = i + 1;  // Sequence ID generation (or retrieve it from the DOM if available)
		let dic = {
			sequence_id: sequenceId,
			sequence_text: sequence_text_list[i].value,
			img_id: sequence_img_list[i].files.length > 0 ? i : 0, // Assign image ID or 0 if no image
			recipe_id: 0,  // This would be set dynamically based on your application logic
			sequence_step_no: i + 1
		};
		cookingSequenceDto.push(dic);
	}
	formRecipe.append('cookingSequenceDto', JSON.stringify(cookingSequenceDto));

	// 이미지
	console.log(sequence_img_list.length)
	for (let i = 0; i < sequence_img_list.length; i++) {
		const fileInput = sequence_img_list[i];
		const fileInput_value = fileInput.value;
		if (!(fileInput_value.endsWith(".jpg") | fileInput_value.endsWith(".jpeg") | fileInput_value.endsWith(".png") | fileInput_value == "")) {
			return window.alert("이미지 파일은 jpg / jpeg / png 의 형식만 가능합니다.")
		}
		if (fileInput.files.length > 0) {
			for (let file of fileInput.files) {
				formRecipe.append(`sequence_img_${i}`, file);  // Append each file with a unique key
			}
		} else {
			formRecipe.append(`sequence_img_${i}`, new Blob([], { type: "application/json" }));
		}
	}

	$.ajax({
		type: "post",
		url: "/recipe/insert",
		contentType: false,
		processData: false,
		data: formRecipe,
		success: recipe_id => {
			window.alert("레시피가 생성되었습니다.")
			window.location.href = `/recipe_list/${recipe_id}`;
		},
		error: recipe_id => {
			console.error("Error:", error);
		}
	});
}

function replace(recipe_id) {
	window.location.href = `/recipe/edit?recipe_id=${recipe_id}`;
}

function deleteRecipe(recipeId) {
	if (confirm("정말로 이 레시피를 삭제하시겠습니까?")) {
		$.ajax({
			url: '/deleteRecipe',
			method: 'POST',
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			data: {
				recipe_id: recipeId
			},
			success: function(response) {
				alert("레시피가 삭제되었습니다.");
				window.location.href = '/recipe_list'
			},
			error: function(xhr, status, error) {
				alert("레시피 삭제에 실패했습니다. 상태 코드: " + xhr.status);
			}
		});
	}
}
