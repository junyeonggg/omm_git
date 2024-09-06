// 전역 변수 선언
var isIdChecked = false;    // 아이디 중복 체크 상태
var isNicknameChecked = false; // 닉네임 중복 체크 상태
var isEmailChecked = false; // 이메일 중복 체크 상태
var isIdAvailable = false; // 기본값: 사용 불가로 설정
var isEmailAvailable = false; // 이메일 사용 가능 여부
var isTelAvailable = false; // 전화번호 사용 가능 여부
var isTelChecked = false;

// 닉네임 중복 체크 함수
async function checkNickname() {
    var snd_data = $("#user_nickname").val().trim();
    if (snd_data === "") {
        $("#nickname-area").html("<p>닉네임을 입력해 주세요.</p>");
        isNicknameChecked = false;
        isIdAvailable = false;
        return;
    }
    if (snd_data.length < 2 || snd_data.length > 6) {
        $("#nickname-area").html("<p>닉네임은 2~6자로 입력해주세요.</p>");
        isNicknameChecked = false;
        isIdAvailable = false;
        return;
    }
    try {
        const data = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkNickname",
            data: { data: snd_data }
        });
        if (data.trim() === "true") {
            $("#nickname-area").html("<p>사용 가능한 닉네임입니다.</p>");
            isNicknameChecked = true;
            isIdAvailable = true;
        } else {
            $("#nickname-area").html("<p>사용할 수 없는 닉네임입니다.</p>");
            isNicknameChecked = true;
            isIdAvailable = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isNicknameChecked = false;
        isIdAvailable = false;
    }
}

// 이메일 중복 체크 함수
async function checkEmail() {
    var snd_data = $("#user_email").val().trim();
    if (snd_data === "") {
        $("#email-area").html("<p>이메일을 입력해 주세요.</p>");
        isEmailChecked = false;
        isEmailAvailable = false;
        return;
    }
    try {
        const data = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkEmail",
            data: { data: snd_data }
        });
        if (data.trim() === "true") {
            $("#email-area").html("<p>사용 가능한 이메일입니다.</p>");
            isEmailChecked = true;
            isEmailAvailable = true;
        } else {
            $("#email-area").html("<p>사용할 수 없는 이메일입니다.</p>");
            isEmailChecked = true;
            isEmailAvailable = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isEmailChecked = false;
        isEmailAvailable = false;
    }
}

// 전화번호 중복 체크 함수
async function checkTel() {
    var tel_prefix = $("#tel_list").val().trim(); // 국번
    var tel_number = $("#user_tel").val().trim(); // 중간번호

    if (tel_prefix === "" || tel_number === "") {
        $("#tel-area").html("<p>전화번호를 입력해 주세요.</p>");
        isIdChecked = false;
        isTelAvailable = false;
        return;
    }
    // 전체 전화번호를 생성합니다.
    var full_tel = tel_prefix + tel_number;

    try {
        const data = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkTel",
            data: { data: full_tel }
        });

        if (data.trim() === "true") {
            $("#tel-area").html("<p>사용 가능한 전화번호입니다.</p>");
            isIdChecked = true;
            isTelAvailable = true;
        } else {
            $("#tel-area").html("<p>사용할 수 없는 전화번호입니다.</p>");
            isIdChecked = true;
            isTelAvailable = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isIdChecked = false;
        isTelAvailable = false;
    }
}
function update_email() {
        var email_id = document.getElementById('user_email_id').value.trim();
        var email_domain = document.getElementById('user_email_domain').value.trim();
        var custom_domain = document.getElementById('custom_domain').value.trim();
        var domain_select = document.getElementById('domain_list').value;
        var full_email = '';

        // 직접 입력된 도메인이 있으면 그것을 사용
        if (custom_domain) {
            email_domain = custom_domain;
        } else if (domain_select && domain_select !== 'custom') {
            email_domain = domain_select;
        }

        // 이메일 아이디와 도메인이 모두 입력되었을 때만 결합
        if (email_id && email_domain) {
            full_email = email_id + '@' + email_domain;
        }

        document.getElementById('user_email').value = full_email;
 }

function handle_domain_change() {
        var domain_select = document.getElementById('domain_list');
        var custom_domain_input = document.getElementById('custom_domain');
        var email_domain_input = document.getElementById('user_email_domain');

        if (domain_select.value === 'custom') {
            custom_domain_input.style.display = 'inline';
            email_domain_input.style.display = 'none';
            custom_domain_input.focus();
        } else {
            custom_domain_input.style.display = 'none';
            email_domain_input.style.display = 'inline';
            email_domain_input.value = domain_select.value;
            update_email();
        }
}
 function handle_tel_change() {
    var tel_list = document.getElementById('tel_list').value;
    var tel_mid = document.getElementById('tel_mid').value.trim();
    var tel_end = document.getElementById('tel_end').value.trim();
    var fullPhoneNumber = '';

        // 전화번호가 완성된 경우
        if (tel_list && tel_mid && tel_end) {
            fullPhoneNumber = tel_list + '-' + tel_mid + '-' + tel_end;
        }
        // 숨겨진 필드에 전화번호 업데이트
        document.getElementById('user_tel').value = fullPhoneNumber;
    }
function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 주소 변수와 참고항목 변수를 초기화
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우
                    addr = data.jibunAddress;
                }

                // 도로명 주소일 때 참고항목을 조합
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 참고항목이 있을 경우 괄호로 감싸서 최종 문자열을 만든다
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                }

                // 우편번호와 주소 정보를 해당 필드에 입력
                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("sample6_address").value = addr;
                document.getElementById("sample6_extraAddress").value = extraAddr;



                // 커서를 상세주소 필드로 이동
                document.getElementById("sample6_detailAddress").focus();
                document.querySelector("#sample6_detailAddress").value="";
            }
        }).open();
}
function sendmail(){
    user_email_id = document.querySelector("#user_email_id").value
    domain_list = document.querySelector("#domain_list").value
    user_email = user_email_id+"@"+domain_list;
    const url = "/sendmail"
    $.ajax({
        type:"post",
        url : url,
        data : {'user_email':user_email},
        success : data=>{
            if(data){
                window.alert("이메일이 발송되었습니다.")
            } else {
                window.alert("이메일 발송에 실패했습니다.")
            }
        }
    })
}
function verifyEmailCode(){
    const code = document.querySelector("#verification_code").value
    const url = "/checkEmailCode";
    user_email_id = document.querySelector("#user_email_id").value
    domain_list = document.querySelector("#domain_list").value
    user_email = user_email_id+"@"+domain_list;
    $.ajax({
        type:"post",
        url : url,
        data : {'user_email':user_email,'code':code},
        success : data=>{
            if(data){
                window.alert("이메일 인증이 확인되었습니다.")
            }else{
                window.alert("인증번호를 확인해 주세요.")
            }
        }
    })
}

// 닉네임 입력 필드의 값이 변경될 때 중복 체크 상태 초기화 및 강제 중복 체크 실행
$(document).ready(function() {
    $("#user_nickname").on('keydown', function(event) {
        if(!isNicknameChecked) {
            isNicknameChecked = false; // 새로운 값을 입력할 때 중복 체크 상태 초기화
            isIdAvailable = false; // 닉네임 사용 가능 여부 초기화
            $("#nickname-area").html(""); // 메시지 영역 초기화
        }
    });
});

// 이메일 입력 필드의 값이 변경될 때 중복 체크 상태 초기화 및 강제 중복 체크 실행
$(document).ready(function() {
    $("#user_email").on('keydown', function(event) {
        if(isEmailChecked) {
            isEmailChecked = false; // 새로운 값을 입력할 때 중복 체크 상태 초기화
            isIdAvailable = false; // 이메일 사용 가능 여부 초기화
            $("#email-area").html(""); // 메시지 영역 초기화
        }
    });
});

// 전화번호 입력 필드의 값이 변경될 때 중복 체크 상태 초기화 및 강제 중복 체크 실행
$(document).ready(function() {
    $("#user_tel").on('keydown', function(event) {
        if(isTelChecked) {
            isTelAvailable = false; // 새로운 값을 입력할 때 중복 체크 상태 초기화
            isIdAvailable = false; // 전화번호 사용 가능 여부 초기화
            $("#tel-area").html(""); // 메시지 영역 초기화
        }
    });
});
// 수정 버튼 클릭 시 폼 제출 함수
function updateButton() {
    const originalNickname = document.querySelector("#original_nickname").value;
    const originalEmail = document.querySelector("#original_email").value;
    const originalTel = document.querySelector("#original_tel").value;
    const currentNickname = document.querySelector("#user_nickname").value.trim();
    const currentEmailId = document.querySelector("#user_email_id").value.trim();
    const currentUserDomain = document.querySelector("#user_email_domain").value.trim();
    const currentDomain = document.querySelector("#domain_list").value;
    const customDomain = document.querySelector("#custom_domain").value.trim();
    const currentTel = document.querySelector("#user_tel").value.trim();

    var currentEmail = "";
    if (currentDomain == 'custom') {
        currentEmail = currentEmailId + "@" + customDomain;
    } else {
        currentEmail = currentEmailId + "@" + currentUserDomain;
    }

    // 이메일이 변경되었을 때
    if (currentEmail !== originalEmail && isEmailChecked) {
        window.alert("이메일 중복 체크를 먼저 해주세요.");
        return false;
    }
    // 닉네임이 변경되었을 때
    if (currentNickname !== originalNickname && isNicknameChecked) {
        window.alert("닉네임 중복 체크를 먼저 해주세요.");
        return false;
    }
    // 전화번호가 변경되었을 때
    if (currentTel !== originalTel && isTelAvailable) {
        window.alert("전화번호 중복 체크를 먼저 해주세요.");
        return false;
    }
    if (isNicknameChecked) {
        window.alert("현재 사용중인 닉네임입니다.");
        return false;
    }
    if (isEmailChecked) {
        window.alert("현재 사용중인 이메일입니다.");
        return false;
    }
    if (isTelChecked) {
        window.alert("현재 사용중인 전화번호입니다.");
        return false;
    }
    // 이메일을 hidden 필드에 설정
    document.querySelector("#user_email").value = currentEmail;
    var form = $('#profileForm');
    const a = document.querySelector("#user_email_id").value
    var c = ""
    if(document.querySelector("#domain_list").value == 'custom'){
        c = document.querySelector("#custom_domain").value
    }else{
        c = document.querySelector("#domain_list").value
    }
    var user_email = a+"@"+c;
    document.querySelector("#user_email").value = user_email;

       form.submit();
        window.alert("회원 정보가 성공적으로 업데이트되었습니다.")
}
function resetButton() {
    document.getElementById('profileForm').reset();
}