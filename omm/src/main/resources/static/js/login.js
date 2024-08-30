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
//구글 로그인 인증 토큰 받기
function social_login(social){
     console.log(social);
     var clientId = "";
     var authUrl = "";
     var redirectUri = ""
     // OAuth 2.0 인증 URL
     if(social == "google"){
         clientId = '931563116169-26l7rc649fjpeoaf7jo1p8bjvqloco45'; // 구글 애플리케이션의 클라이언트 ID
         redirectUri = encodeURIComponent('http://localhost:8080/login/oauth2/code/google');


         authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code&scope=email profile`;
     }else if(social == "kakao"){
         clientId = "5hQZ7RIPioWmVDGJLcUI"; // 카카오 애플리케이션의 클라이언트 ID
         redirectUri = encodeURIComponent('http://localhost:8080/login/oauth2/code/kakao');
         authUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`;
     }else if(social == "naver"){
         clientId = "YOUR_NAVER_CLIENT_ID"; // 네이버 애플리케이션의 클라이언트 ID
         redirectUri = encodeURIComponent('http://localhost:8080/login/oauth2/code/naver');
         authUrl = `https://nid.naver.com/oauth2.0/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`;
     }
     // 사용자의 브라우저를 인증 URL로 리디렉션
     window.location.href = authUrl;
 }

