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
	trEl.innerHTML = `<td class="step_no_td"><input type="text" value="1" readonly="readonly"></td>
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
	const target = document.querySelector("#ingre_tbl");
	target.removeChild(self.parentElement.parentElement)
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
	formRecipe.append("recipe_serving", document.querySelector("#recipe_serving").value+"인분");
	var time_list = document.querySelectorAll(".time")
	var time = ""
	time_list.forEach(t=>{
		time+=t.value;
	});
	formRecipe.append('recipe_time',time);
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
		if (fileInput.files.length > 0) {
			for (let file of fileInput.files) {
				formRecipe.append(`sequence_img_${i}`, file);  // Append each file with a unique key
			}
		}else{
			formRecipe.append(`sequence_img_${i}`, new Blob([], { type: "application/json" }));
		}
	}

	$.ajax({
		type: "post",
		url: "/recipe/insert",
		contentType: false,
		processData: false,
		data: formRecipe,
		success: function(recipe_id) {
			window.alert("레시피가 생성되었습니다.")
			window.location.href=`/recipe_list/${recipe_id}`;
		},
		error: function(error) {
			console.error("Error:", error);
		}
	});
}


