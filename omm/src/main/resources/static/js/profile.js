// 전역 변수 선언
var isNicknameChecked = false; // 닉네임 중복 체크 상태
var isEmailChecked = false; // 이메일 중복 체크 상태
var isTelChecked = false; // 전화번호 중복 체크 상태
var isEmailVerified = false; // 이메일 인증 상태
var isValidated = false; // 기본 사용 불가


// 닉네임 중복 체크 함수
async function checkNickname() {
    var nickname = $("#user_nickname").val().trim();
    if (nickname === "") {
        $("#nickname-area").html("<p>닉네임을 입력해 주세요.</p>");
        isNicknameChecked = false;
        isValidated = true;
        return;
    }
    if (nickname.length < 2 || nickname.length > 12) {
        $("#nickname-area").html("<p>닉네임은 2~12자로 입력해주세요.</p>");
        isNicknameChecked = false;
        isValidated = false;
        return;
    }
    try {
        const response = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkNickname",
            data: { data: nickname }
        });
        if (response.trim() === "true") {
            $("#nickname-area").html("<p>사용 가능한 닉네임입니다.</p>");
            isNicknameChecked = true;
            isValidated = true;
        } else {
            $("#nickname-area").html("<p>사용할 수 없는 닉네임입니다.</p>");
            isNicknameChecked = true;
            isValidated = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isNicknameChecked = false;
    }
}

// 이메일 중복 체크 함수
async function checkEmail() {
update_email()

    var email = $("#user_email").val().trim();

    if (email === "") {
        $("#email-area").html("<p>이메일을 입력해 주세요.</p>");
        isEmailChecked = false;
        isValidated = true;
        return;
    }
    try {
        const response = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkEmail",
            data: { data: email }
        });
        if (response.trim() === "true") {
            $("#email-area").html("<p>사용 가능한 이메일입니다.</p>");
            isEmailChecked = true;
            isValidated = true;
        } else {
            $("#email-area").html("<p>사용할 수 없는 이메일입니다.</p>");
            isEmailChecked = true;
            isValidated = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isEmailChecked = false;
    }
}

// 전화번호 중복 체크 함수
async function checkTel() {
    var telPrefix = $("#tel_list").val().trim(); // 국번
    var telNumber = $("#user_tel").val().trim(); // 중간번호

    if (telPrefix === "" || telNumber === "") {
        $("#tel-area").html("<p>전화번호를 입력해 주세요.</p>");
        isTelChecked = false;
        isValidated = true;
        return;
    }
    var fullTel = telPrefix + telNumber;

    try {
        const response = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkTel",
            data: { data: fullTel }
        });

        if (response.trim() === "true") {
            $("#tel-area").html("<p>사용 가능한 전화번호입니다.</p>");
            isTelChecked = true;
            isValidated = true;
        } else {
            $("#tel-area").html("<p>사용할 수 없는 전화번호입니다.</p>");
            isTelChecked = true;
            isValidated = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isTelChecked = false;
    }
}

// 이메일 업데이트 함수
function update_email() {
    var emailId = $("#user_email_id").val().trim();
    var emailDomain = $("#user_email_domain").val().trim();
    var customDomain = $("#custom_domain").val().trim();
    var domainSelect = $("#domain_list").val();
    var fullEmail = '';

    if (customDomain) {
        emailDomain = customDomain;
    } else if (domainSelect && domainSelect !== 'custom') {
        emailDomain = domainSelect;
    }

    if (emailId && emailDomain) {
        fullEmail = emailId + '@' + emailDomain;
        $("#user_email").val(fullEmail);
    }else {
             $("#user_email").val(""); // 이메일 값이 없으면 빈 값으로 설정
    }
}

// 도메인 변경 핸들러
function handle_domain_change() {
    var domainSelect = $("#domain_list").val();
    var customDomainInput = $("#custom_domain");
    var emailDomainInput = $("#user_email_domain");
    var email_id_input = document.getElementById('user_email_id');

    // 이메일 영역 초기화
    isEmailChecked = false;
    $("#email-area").html(""); // 이메일 메시지 영역 초기화

    if (domainSelect === 'custom') {
        customDomainInput.show().focus();
        emailDomainInput.hide();
    } else {
        customDomainInput.hide();
        emailDomainInput.show().val(domainSelect);
        update_email();
    }
}

// 전화번호 변경 핸들러
function handle_tel_change() {
    var telPrefix = $("#tel_list").val();
    var telMid = $("#tel_mid").val().trim();
    var telEnd = $("#tel_end").val().trim();
    var fullPhoneNumber = '';

    if (telPrefix && telMid && telEnd) {
        fullPhoneNumber = telPrefix + '-' + telMid + '-' + telEnd;
        $("#user_tel").val(fullPhoneNumber);
    }
}

// 주소 찾기 함수
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            }

            $("#sample6_postcode").val(data.zonecode);
            $("#sample6_address").val(addr);
            $("#sample6_extraAddress").val(extraAddr);
            $("#sample6_detailAddress").focus().val("");
        }
    }).open();
}

// 이메일 인증 메일 전송 함수
function sendmail() {
    var emailId = $("#user_email_id").val().trim();
    var domainSelect = $("#domain_list").val();
    var customDomain = $("#custom_domain").val().trim();
    var userEmail = '';

    // 이메일 입력 값 확인
    if (!emailId) {
        window.alert("이메일 아이디를 입력해 주세요.");
        return;
    }

    if (domainSelect === 'custom' && !customDomain) {
        window.alert("이메일 도메인을 입력해 주세요.");
        return;
    } else if (domainSelect !== 'custom' && !domainSelect) {
        window.alert("도메인을 선택해 주세요.");
        return;
    }
    if(isEmailChecked && !isValidated){
        window.alert("현재 사용중인 이메일입니다.")
        return;
    }

    // 이메일 주소 생성
    userEmail = domainSelect === 'custom' ? emailId + "@" + customDomain : emailId + "@" + domainSelect;

    $.ajax({
        type: "post",
        url: "/sendmail",
        data: { 'user_email': userEmail },
        success: function(data) {
            if (data) {
                window.alert("이메일이 발송되었습니다.");
                isEmailVerified = false; // 인증 대기 상태로 설정
            } else {
                window.alert("이메일 발송에 실패했습니다.");
            }
        }
    });
}

// 이메일 인증 코드 확인 함수
function verifyEmailCode() {
    var code = $("#verification_code").val().trim();
    var emailId = $("#user_email_id").val().trim();
    var domainSelect = $("#domain_list").val();
    var customDomain = $("#custom_domain").val().trim();
    var userEmail = '';

    // 이메일 입력 값 확인
    if (!emailId) {
        window.alert("이메일 아이디를 입력해 주세요.");
        return;
    }

    if (domainSelect === 'custom' && !customDomain) {
        window.alert("이메일 도메인을 입력해 주세요.");
        return;
    } else if (domainSelect !== 'custom' && !domainSelect) {
        window.alert("도메인을 선택해 주세요.");
        return;
    }

    // 인증 코드 입력 값 확인
    if (!code) {
        window.alert("인증 코드를 입력해 주세요.");
        return;
    }

    // 이메일 주소 생성
    userEmail = domainSelect === 'custom' ? emailId + "@" + customDomain : emailId + "@" + domainSelect;

    $.ajax({
        type: "post",
        url: "/checkEmailCode",
        data: { 'user_email': userEmail, 'code': code },
        success: function(data) {
            if (data) {
                window.alert("이메일 인증이 확인되었습니다.");
                isEmailVerified = true; // 인증 완료 상태로 설정
            } else {
                window.alert("인증번호를 확인해 주세요.");
                isEmailVerified = false; // 인증 실패 상태로 설정
            }
        }
    });
}

// 입력 필드 변경 시 중복 체크 상태 초기화
$(document).ready(function() {
    $("#user_nickname").on('input', function() {
        isNicknameChecked = false;
        $("#nickname-area").html("");
    });

    // 이메일 아이디 입력 필드의 값이 변경될 때
    $("#user_email_id").on('input', function() {
        isEmailChecked = false;
        $("#email-area").html(""); // 메시지 영역 초기화
        update_email(); // 이메일 주소 업데이트
    });

    // 이메일 도메인 입력 필드의 값이 변경될 때
    $("#user_email_domain").on('input', function() {
        isEmailChecked = false;
        $("#email-area").html(""); // 메시지 영역 초기화
        update_email(); // 이메일 주소 업데이트
    });

    // 사용자 정의 도메인 입력 필드의 값이 변경될 때
    $("#custom_domain").on('input', function() {
        isEmailChecked = false;
        $("#email-area").html(""); // 메시지 영역 초기화
        update_email(); // 이메일 주소 업데이트
    });

    // 도메인 리스트가 변경될 때
    $("#domain_list").on('change', function() {
        handle_domain_change(); // 도메인 처리 함수 호출
    });

    $("#user_tel").on('input', function() {
        isTelChecked = false;
        $("#tel-area").html("");
    });
});

// 비밀번호 유효성 검사 함수
function validatePassword(password) {
    const passwordRegex = /^(?=.*[!@#$%^&+=])[a-z0-9!@#$%^&+=]{8,16}$/;
    return passwordRegex.test(password);
}

function validateFields() {
    const userPw = document.getElementById('user_pw').value.trim();
    const pwFeedback = document.getElementById('pw-area');

    if (!validatePassword(userPw)) {
        pwFeedback.textContent = "비밀번호는 8~16자 사이이며, 하나의 특수문자를 포함해야 하고, 대문자와 한글은 포함될 수 없습니다.";
    } else {
    }
}
// 페이지 로드 시 유효성 검사
document.addEventListener('DOMContentLoaded', function() {
    validateFields(); // 페이지 로드 시 비밀번호 유효성 검사

    const userPwField = document.getElementById('user_pw');

    if (userPwField) {
        userPwField.addEventListener('input', function() {
            validateFields();
        });
    } else {
        console.error('Element with ID "user_pw" not found');
    }
});


// 수정 버튼 클릭 시 폼 제출 함수
function updateButton() {
    update_email();
    // 기존 정보
    var originalNickname = $("#original_nickname").val();
    var originalEmail = $("#original_email").val();
    var originalTel = $("#original_tel").val();
    var originalUserDomain = $("#original_user_domain").val().trim();
    var originalAddressZip = $("#original_user_addr_zip").val().trim();
    var originalAddress = $("#original_user_addr").val().trim();
    var originalAddressDetail = $("#original_user_addr_detail").val().trim();
    console.log("기존 디테일 :",originalAddressDetail)
    var originalPassword = $("#original_pw").val().trim();

    // 현재 정보 입력 부분
    var currentEmail = $("#user_email").val().trim();
    var currentNickname = $("#user_nickname").val().trim();
    var currentEmailId = $("#user_email_id").val().trim();
    var currentUserDomain = $("#user_email_domain").val().trim();
    var currentDomain = $("#domain_list").val();
    var customDomain = $("#custom_domain").val().trim();
    var currentTelPrefix = $("#tel_list").val();
    var currentTelNumber = $("#user_tel").val().trim();
    var currentTel = currentTelPrefix + currentTelNumber; // 전체 전화번호 조합
    var currentAddressZip = $("#sample6_postcode").val().trim();
    var currentAddress = $("#sample6_address").val().trim();
    var currentAddressDetail = $("#sample6_detailAddress").val().trim();
    console.log("바뀐 :",currentAddressDetail)
    var currentPassword = $("#user_pw").val().trim();

    // 도메인 결정
    var domainToUse = currentUserDomain || (currentDomain === 'custom' ? customDomain : currentDomain);
    var newEmail = currentEmailId + '@' + domainToUse;

    var originalEmailId = originalEmail.split('@')[0];
    var originalEmailDomain = originalEmail.split('@')[1];

    var domainChanged = domainToUse !== originalUserDomain;


    // 값 변경 여부 확인
    var emailIdChanged = originalEmailId != currentEmailId;
    var nicknameChanged = originalNickname !== currentNickname;
    var emailChanged = originalEmail !== currentEmail;
    var telChanged = originalTel !== currentTel;
    var addressZipChanged = originalAddressZip !== currentAddressZip;
    var addressChanged = originalAddress !== currentAddress;
    var addressDetailChanged = originalAddressDetail != currentAddressDetail;
    console.log("바뀌었는지 "+addressDetailChanged)
    var passwordChanged = originalPassword !== currentPassword && currentPassword !== ""; // 비밀번호가 변경되었거나 새로 입력된 경우

    if(isNicknameChecked && !isValidated){
        window.alert("현재 사용중인 닉네임입니다.");
        return false;
    }
     if(isEmailChecked && !isValidated){
        window.alert("현재 사용중인 이메일입니다.");
        return false;
     }
     if(isTelChecked && !isValidated){
        window.alert("현재 사용중인 전화번호입니다.");
        return false;
     }

    // 변경된 값이 없는 경우
    if (!nicknameChanged && !emailChanged && !telChanged && !addressZipChanged && !addressChanged && !addressDetailChanged && !passwordChanged && !domainChanged) {
        window.alert("수정값이 없습니다!");
        return false;
    }
    if (addressDetailChanged) {
        console.log("들어옴")
        document.querySelector("#sample6_detailAddress").value = currentAddressDetail;
    }
    // 이메일이 변경된 경우에만 중복 체크 및 인증을 요구
    if (emailChanged && domainChanged || emailIdChanged) {
        if (!isEmailChecked) {
            window.alert("이메일 중복 체크를 먼저 해주세요.");
            return false;
        }
        if (!isEmailVerified) {
            window.alert("이메일 인증을 먼저 완료해 주세요.");
            return false;
        }
    }

    if (currentNickname !== originalNickname && !isNicknameChecked) {
        window.alert("닉네임 중복 체크를 먼저 해주세요.");
        return false;
    }
    if (currentTel !== originalTel && !isTelChecked) {
        window.alert("전화번호 중복 체크를 먼저 해주세요.");
        return false;
    }
    // 비밀번호 유효성 검사
    var passwordRegex = /^(?=.*[!@#$%^&+=])(?=.*[a-z0-9])[a-z0-9!@#$%^&+=]{8,16}$/;
    if (passwordChanged && !passwordRegex.test(currentPassword)) {
        window.alert("비밀번호는 8~16자 사이이며, 하나의 특수문자를 포함해야 하고, 대문자와 한글은 포함될 수 없습니다.");
        $("#user_pw").focus();
        return false;
    }

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

function memberDrop() {
    // 탈퇴 확인 메시지
    var confirmation = confirm("정말로 회원을 탈퇴하시겠습니까?");

    if (confirmation) {
        const url = '/unregist';

        const data = new URLSearchParams();
        data.append('user_nickname', document.getElementById('user_nickname').value);

        // 서버에 POST 요청을 보내기
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded' // 폼 URL 인코딩
            },
            body: data.toString()
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`서버 응답 오류 (${response.status}): ${text}`);
                });
            }

            alert("정상적으로 회원 탈퇴가 완료되었습니다.");

            location.href = '/logout';
        })
        .catch(error => {

            alert("회원 탈퇴에 실패했습니다. 다시 시도해 주세요.");
            console.error('Error:', error);
        });
    } else {

        alert("회원 탈퇴가 취소되었습니다.");
    }
}


