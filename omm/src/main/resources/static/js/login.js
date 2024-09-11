function resetButton() {
    document.getElementById('loginForm').reset();
}

function findid() {
	window.open('/find_id', '아이디찾기', 'width=500,height=600,scrollbars=no,resizable=no,history=no,status=no,menubar=no');
}
function findpw() {
	window.open('/find_pw', '비밀번호찾기', 'width=500,height=600,scrollbars=no,resizable=no,history=no,status=no,menubar=no');
}
function loginButton() {
	const form = document.loginForm;
	const user_id = document.getElementById('user_id').value;
	const user_pw = document.getElementById('user_pw').value;
//	if (user_id.length < 4 || user_id.length > 12) {
//		window.alert("아이디는 4~12자로 입력해주세요.");
//		form.user_id.focus();
//		return false;
//	}

//	if (user_pw.length < 8 || user_pw.length > 16) {
//		window.alert("패스워드는 8~16자로 입력해주세요");
//		form.user_pw.focus();
//		return false;
//	}
	form.submit();
    return true;
}


// 구굴 로그인 인증 토큰 받기

