function resetButton() {
    document.getElementById('loginForm').reset();
}

function loginButton() {
	const form = document.loginForm;
	const user_id = document.getElementById('user_id').value;
	const user_pw = document.getElementById('user_pw').value;

	if (user_id.length < 4 || user_id.length > 12) {
		window.alert("아이디는 4~12자로 입력해주세요.");
		form.user_id.focus();
		return false;
	}

	if (user_pw.length < 8 || user_pw.length > 16) {
		window.alert("패스워드는 8~16자로 입력해주세요");
		form.user_pw.focus();
		return false;
	}
    return true;
}

function codeTest() {
    // OAuth 2.0 인증 URL
    const clientId = '6379a78e09703b800a317f97af0045c0'; // Kakao 애플리케이션의 클라이언트 ID
    const redirectUri = encodeURIComponent('http://localhost:8080/test'); // 인증 후 리디렉션될 URI
    const responseType = 'code'; // 인증 코드 발급
    const authUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=${responseType}`;

    // 사용자의 브라우저를 인증 URL로 리디렉션
    window.location.href = authUrl;
}