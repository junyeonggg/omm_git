function deleteUser(button) {
	var userNickname = button.getAttribute('data-user_nickname');
	var userId = button.getAttribute('data-user_id'); // 버튼의 data-user_id 속성에서 user_id 가져오기
	window.alert(userId + " 탈퇴가 완료되었습니다.");
    // alert이 닫힌 후 1초(1000ms) 뒤에 페이지 이동
    setTimeout(function() {
        location.href = "/deleteUser/" + userNickname;
    }, 1000); // 1초 후 페이지 이동
}