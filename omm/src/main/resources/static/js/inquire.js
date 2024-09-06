function inquire_detail(self) {
	const inquire_id = self.getAttribute("data-inquire-id")
	console.log("inquire_id : ", inquire_id)
	window.location.replace(`/inquire/${inquire_id}`);

}

function check_user(user_id) {
	if (user_id != null) {
		location.href = "/inquire/write";

	} else {
		document.querySelector("#nlogin_modal").style = 'display:flex';
	}


}
function close_modal() {
	document.querySelector("#nlogin_modal").style = 'display:none';

}
function sendInquireForm() {
	const form = document.forms.inquireForm;
	window.alert("문의를 작성하였습니다.")
	form.submit();
}

function updateInquire(self) {
	const input_list = document.querySelectorAll(".input_click")
	for (l of input_list) {
		l.style = "";
	}
	document.querySelector(".input_click").focus();
	self.value = "수정완료";
	self.setAttribute("onclick","sendUpdateInquireForm()")
}
function sendUpdateInquireForm(){
	const form = document.forms.updateForm;
	form.submit();
	window.alert("수정 되었습니다.") 
	// 마지막 수정 파일 
	
}