// 전역 변수 선언
var isNicknameChecked = false; // 닉네임 중복 체크 상태
var isEmailChecked = false; // 이메일 중복 체크 상태
var isTelChecked = false; // 전화번호 중복 체크 상태
var isEmailVerified = false; // 이메일 인증 상태
var isIdChecked = false;
var isIdValidated = false; // 기본 사용 불가
var isNickValidated = false; // 기본 사용 불가
var isEmailValidated = false; // 기본 사용 불가
var isTelValidated = false; // 기본 사용 불가




// 아이디 중복 체크 함수
async function checkId() {
    var snd_data = $("#user_id").val().trim();
    if (snd_data === "") {
        $("#id-area").html("<p>아이디를 입력해 주세요.</p>");
        isIdChecked = false; // 아이디 입력이 없으므로 중복 체크 미완료 상태
        isIdValidated = true; // 아이디 확인이 필요함
        return;
    }
    if (snd_data.length < 4 || snd_data.length > 12) {
        $("#id-area").html("<p>아이디는 4~12자로 입력해주세요.</p>");
        isIdChecked = false; // 아이디 입력이 없으므로 중복 체크 미완료 상태
        isIdValidated = false; // 아이디 확인이 필요함
        return;
    }
    try {
        const data = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkId",
            data: { data: snd_data }
        });
    // 유효성 검사 추가
        if (!/^[a-z0-9]+$/.test(snd_data)) {
        $("#id-area").html("<p>아이디는 소문자와 숫자만 포함해야 합니다.</p>");
        isIdChecked = false; // 아이디 입력이 없으므로 중복 체크 미완료 상태
        isIdValidated = false; // 아이디 확인이 필요함
        return;
        }
        if (data === "true") {
            $("#id-area").html("<p>사용 가능한 아이디입니다.</p>");
            isIdChecked = true; // 중복 체크 완료, 아이디 사용 가능
            isIdValidated = true; // 아이디가 사용 가능함
        } else {
            $("#id-area").html("<p>사용할 수 없는 아이디입니다.</p>");
            isIdChecked = true; // 중복 체크 완료, 아이디 사용 불가
            isIdValidated = false; // 아이디가 사용 불가함
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isIdChecked = false; // 에러 발생 시 중복 체크 미완료 상태
        isIdValidated = false; // 아이디 상태 확인 불가
    }
}

// 닉네임 중복 체크 함수
async function checkNickname() {
    var nickname = $("#user_nickname").val().trim();
    if (nickname === "") {
        $("#nickname-area").html("<p>닉네임을 입력해 주세요.</p>");
        isNicknameChecked = false;
        isNickValidated = true;
        return;
    }
    if (nickname.length < 2 || nickname.length > 6) {
        $("#nickname-area").html("<p>닉네임은 2~6자로 입력해주세요.</p>");
        isNicknameChecked = false;
        isNickValidated = false;
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
            isNickValidated = true;
        } else {
            $("#nickname-area").html("<p>사용할 수 없는 닉네임입니다.</p>");
            isNicknameChecked = true;
            isNickValidated = false;
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
            isEmailValidated = true;
        } else {
            $("#email-area").html("<p>사용할 수 없는 이메일입니다.</p>");
            isEmailChecked = true;
            isEmailValidated = false;
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
        isTelValidated = true;
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
            isTelValidated = true;

        } else {
            $("#tel-area").html("<p>사용할 수 없는 전화번호입니다.</p>");
            isTelChecked = true;
            isTelValidated = false;
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
    $("#user_id").on('input', function() {
        isIdChecked = false;
        $("#id-area").html("");
    });
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

// 아이디 유효성 검사 함수
function validateId(id) {
    const idRegex = /^[a-z0-9]{4,12}$/;
    return idRegex.test(id);
}

// 비밀번호 유효성 검사 함수
function validatePassword(password) {
    const passwordRegex = /^(?=.*[!@#$%^&+=])[a-z0-9!@#$%^&+=]{8,16}$/;
    return passwordRegex.test(password);
}

// 페이지 로드 시 유효성 검사
document.addEventListener('DOMContentLoaded', function() {
    validateIdFeedback(); // 페이지 로드 시 아이디 유효성 검사
    validateFields(); // 페이지 로드 시 비밀번호 유효성 검사


    const userIdField = document.getElementById('user_id');
    const userPwField = document.getElementById('user_pw');

    if (userIdField) {
        userIdField.addEventListener('input', function() {
            validateIdFeedback();
        });
    } else {
        console.error('Element with ID "user_id" not found');
    }

    if (userPwField) {
        userPwField.addEventListener('input', function() {
            validateFields();
        });
    } else {
        console.error('Element with ID "user_pw" not found');
    }
});

// 유효성 검사와 피드백을 분리하여 처리
function validateIdFeedback() {
    const userId = document.getElementById('user_id').value.trim();
    const idFeedback = document.getElementById('id-area2');

    if (!validateId(userId)) {
        idFeedback.textContent = "아이디는 소문자와 숫자만 포함하며, 4~12자 사이여야 합니다.";
    } else {
    }
}

function validateFields() {
    const userPw = document.getElementById('user_pw').value.trim();
    const pwFeedback = document.getElementById('pw-area');

    if (!validatePassword(userPw)) {
        pwFeedback.textContent = "비밀번호는 8~16자 사이이며, 하나의 특수문자를 포함해야 하고, 대문자와 한글은 포함될 수 없습니다.";
    } else {
    }
}
function submitForm(self){
// 중복 체크 함수들 호출
    checkId();
    checkNickname();
    checkEmail();
    checkTel();

   //   각 필드에 대한 피드백 표시
    var feedbackMessages = [];
    if (!isIdChecked) feedbackMessages.push("아이디 중복 체크를 먼저 해주세요.");
    if (!isNicknameChecked) feedbackMessages.push("닉네임 중복 체크를 먼저 해주세요.");
    if (!isEmailChecked) feedbackMessages.push("이메일 중복 체크를 먼저 해주세요.");
    if (!isTelChecked) feedbackMessages.push("전화번호 중복 체크를 먼저 해주세요.");

    if (feedbackMessages.length > 0) {
        window.alert(feedbackMessages.join("\n"));
        return false; // 중복 체크가 완료되지 않았으므로 폼 제출 중지
    }
    update_email()

    // 이메일 인증 상태 확인
//    if (isEmailChecked && !isEmailVerified) {
//        window.alert("이메일 인증을 먼저 완료해 주세요.");
//        return false;
//    }
    var currentUserId = $("#user_id").val().trim();
    var currentPassword = $("#user_pw").val().trim();
    var currentUserName = $("#user_name").val().trim();
    var currentNickname = $("#user_nickname").val().trim();
    var currentEmailId = $("#user_email_id").val().trim();
    var currentDomain = $("#domain_list").val();
    var currentUserDomain = $("#user_email_domain").val().trim();
    var customDomain = $("#custom_domain").val().trim();
    var currentEmail = $("#user_email").val().trim();
    var currentTelList = $("#tel_list").val().trim();
    var currentUserTel = $("#user_tel").val().trim();
    var currentAddress = $("#sample6_address").val().trim();
    var currentBirth = $("#user_birth").val().trim();


//    var password = 'examplepass1!';
//    var passwordRegex = /^(?=.*[!@#$%^&+=])(?=.*[a-z0-9])[a-z0-9!@#$%^&+=]{8,16}$/;
//
//    if (!passwordRegex.test(password)) {
//    console.log("비밀번호는 유효하지 않습니다.");
//    } else {
//    console.log("비밀번호는 유효합니다.");
//}


    // 아이디 유효성 검사
    var idRegex = /^[a-z0-9]{4,12}$/;
    if (!idRegex.test(currentUserId)) {
        window.alert("아이디는 소문자와 숫자만 포함하며, 4~12자 사이여야 합니다.");
        $("#user_id").focus();
        return false;
    }

    // 비밀번호 유효성 검사
    var passwordRegex = /^(?=.*[!@#$%^&+=])(?=.*[a-z0-9])[a-z0-9!@#$%^&+=]{8,16}$/;
    if (!passwordRegex.test(currentPassword)) {
        window.alert("비밀번호는 8~16자 사이이며, 하나의 특수문자를 포함해야 하고, 대문자와 한글은 포함될 수 없습니다.");
        $("#user_pw").focus();
        return false;
    }

   if(currentPassword === ""){
        window.alert("비밀번호 값을 입력해주세요.")
        $("#user_pw").focus()
        return false;
   }
   if(currentUserName === ""){
         window.alert("이름을 입력해주세요.")
         $("#user_name").focus()
         return false;
   }
   if(currentNickname === ""){
            window.alert("닉네임을 입력해주세요.")
            $("#user_nickname").focus()
            return false;
      }
   if(currentAddress === ""){
         window.alert("주소를 입력해주세요.")
         $("#user_addr").focus()
         return false;
   }
   if(currentTelList == ""){
       window.alert("전화번호를 입력해주세요.")
       $("#tel_list").focus()
       return false;
   }
   if(currentUserTel == ""){
       window.alert("전화번호를 입력해주세요.")
       $("#user_tel").focus()
       return false;
      }
   var gender = $("input[name='user_gender']:checked").val();
      if (!gender) {
          window.alert("성별을 선택해주세요.");
          return false;
   }
   if(currentBirth === ""){
         window.alert("생년월일을 입력해주세요.")
         $("#user_birth").focus()
         return false;
   }
   if(isIdChecked && !isIdValidated){
        window.alert("현재 사용중인 아이디입니다.");
        return false;
    }
    if(isNicknameChecked && !isNickValidated){
        window.alert("현재 사용중인 닉네임입니다.");
        return false;
    }
     if(isEmailChecked && !isEmailValidated){
        window.alert("현재 사용중인 이메일입니다.");
        return false;
     }
     if(isTelChecked && !isTelValidated){
        window.alert("현재 사용중인 전화번호입니다.");
        return false;
     }

    // 상세주소를 user_addr_detail 필드에 저장
    var detailAddress = document.getElementById("sample6_detailAddress").value;
    var submitBtn = document.getElementById('submitBtn');
    const form = document.forms.joinForm
    // 회원가입 폼
    var formJoin = new FormData();
    // 아이디
    const user_id = document.querySelector("#user_id").value;
    formJoin.append('user_id',user_id)

    //비밀번호
    const user_pw = document.querySelector("#user_pw").value;
    formJoin.append('user_pw',user_pw)

    // 이름
    const user_name = document.querySelector("#user_name").value;
    formJoin.append('user_name',user_name)

    // 닉네임
    const user_nickname = document.querySelector("#user_nickname").value;
    formJoin.append('user_nickname',user_nickname)

    // 이메일
    const user_email_id = document.querySelector("#user_email_id").value;
    const user_email_domain = document.querySelector("#user_email_domain").value;
    const user_custom_email = document.querySelector("#custom_domain").value;
    var user_email = ""; // 변수 선언과 초기화
    if(document.querySelector("#domain_list").value == 'custom'){
         user_email = `${user_email_id}@${user_custom_email}`;
    }else{
         user_email = `${user_email_id}@${user_email_domain}`;
    }
    formJoin.append('user_email',user_email)

    // 주소 부분
    const user_addr_zip = document.querySelector("#sample6_postcode").value;
    const user_addr = document.querySelector("#sample6_address").value;
    const user_addr_detail = document.querySelector("#sample6_detailAddress").value;
    formJoin.append('user_addr_zip',user_addr_zip);
    formJoin.append('user_addr',user_addr);
    formJoin.append('user_addr_detail',user_addr_detail);

    //전화번호
    const user_tel_front = document.querySelector("#tel_list").value;
    const user_tel_back = document.querySelector("#user_tel").value;
    const user_tel = user_tel_front+user_tel_back;
    formJoin.append('user_tel',user_tel);

    // 성별
    const gender_radio_list = document.getElementsByName("user_gender");
    var user_gender = "";
    for(const radio of gender_radio_list){
        if(radio.checked){
            user_gender = radio.value;
            break;
        }
    }
    formJoin.append('user_gender',user_gender)

    // 생년월일
    const user_birth = document.querySelector("#user_birth").value;
    formJoin.append('user_birth',user_birth);

    $.ajax({
        type : "post",
        url : "http://localhost:8080/join",
        contentType: false,
        processData: false,
        data: formJoin,
        success : data=>{
            window.alert("회원가입이 완료되었습니다.")
            if(data=='true'){
                $.ajax({
                    type : "post",
                    url : "http://localhost:8080/login",
                    data: {'username':user_id,'password':user_pw},
                    success : data=>{
                            window.location.replace("http://localhost:8080/")
                            }
                })
            }else {
                window.alert("회원가입에 실패하였습니다.")
            }
        }
    })
}



