// 전역 변수 선언
var isIdChecked = false;    // 아이디 중복 체크 상태
var isNicknameChecked = false; // 닉네임 중복 체크 상태
var isEmailChecked = false; // 이메일 중복 체크 상태
var isIdAvailable = false; // 기본값: 사용 불가로 설정
var isEmailAvailable = false; // 이메일 사용 가능 여부
var pastId = "";

// 아이디 중복 체크 함수
async function checkId() {
    var snd_data = $("#user_id").val().trim();
    if (snd_data === "") {
        $("#id-area").html("<p>아이디를 입력해 주세요.</p>");
        isIdChecked = false; // 아이디 입력이 없으므로 중복 체크 미완료 상태
        isIdAvailable = false; // 아이디 확인이 필요함
        return;
    }
    if (snd_data.length < 4 || snd_data.length > 12) {
        $("#id-area").html("<p>아이디는 4~12자로 입력해주세요.</p>");
        isIdChecked = false; // 아이디 입력이 없으므로 중복 체크 미완료 상태
        isIdAvailable = false; // 아이디 확인이 필요함
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
        isIdAvailable = false; // 아이디 확인이 필요함
        return;
        }
        if (data === "true") {
            $("#id-area").html("<p>사용 가능한 아이디입니다.</p>");
            isIdChecked = true; // 중복 체크 완료, 아이디 사용 가능
            isIdAvailable = true; // 아이디가 사용 가능함
        } else {
            $("#id-area").html("<p>사용할 수 없는 아이디입니다.</p>");
            isIdChecked = true; // 중복 체크 완료, 아이디 사용 불가
            isIdAvailable = false; // 아이디가 사용 불가함
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isIdChecked = false; // 에러 발생 시 중복 체크 미완료 상태
        isIdAvailable = false; // 아이디 상태 확인 불가
    }
}

// 닉네임 중복 체크 함수
async function checkNickname() {
    var snd_data = $("#user_nickname").val().trim();
    if (snd_data === "") {
        $("#nickname-area").html("<p>닉네임을 입력해 주세요.</p>");
        isIdChecked = false;
        isIdAvailable = false;
        return;
    }
    if (snd_data.length < 2 || snd_data.length > 6) {
        $("#nickname-area").html("<p>닉네임는 2~6자로 입력해주세요.</p>");
        isIdChecked = false;
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
        if (data === "true") {
            $("#nickname-area").html("<p>사용 가능한 닉네임입니다.</p>");
            isIdChecked = true;
            isIdAvailable = true;
        } else {
            $("#nickname-area").html("<p>사용할 수 없는 닉네임입니다.</p>");
            isIdChecked = true;
            isIdAvailable = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isIdChecked = false;
        isIdAvailable = false;
    }
}

// 이메일 중복 체크 함수
async function checkEmail() {
    var snd_data = $("#user_email").val().trim();
    if (snd_data === "") {
        $("#email-area").html("<p>이메일을 입력해 주세요.</p>");
        isIdChecked = false;
        isIdAvailable = false;
        return;
    }
    try {
        const data = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkEmail",
            data: { data: snd_data }
        });
        if (data === "true") {
            $("#email-area").html("<p>사용 가능한 이메일입니다.</p>");
            isIdChecked = true;
            isIdAvailable = true;
        } else {
            $("#email-area").html("<p>사용할 수 없는 이메일입니다.</p>");
            isIdChecked = true;
            isIdAvailable = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isIdChecked = false;
        isIdAvailable = false;
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

                // 전체 주소를 user_addr 필드에 저장
                var postcode = data.zonecode;
                var fullAddress = addr + extraAddr;

                // 우편번호를 user_addr_zip 필드에 저장
                document.getElementById('user_addr_zip').value = postcode;

                // 전체 주소를 user_addr 필드에 저장
                document.getElementById('user_addr').value = fullAddress;

                // 커서를 상세주소 필드로 이동
                document.getElementById("sample6_detailAddress").focus();

            }
        }).open();
}
// 인증 이메일을 전송하는 함수
function sendVerificationEmail() {
    var email = document.getElementById('user_email').value;

    if (email) {
        fetch('/sendVerificationEmail', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('verification_message').innerText = '인증 이메일이 전송되었습니다.';
            } else {
                document.getElementById('verification_message').innerText = '인증 이메일 전송 실패: ' + data.message;
            }
        })
        .catch(error => {
            document.getElementById('verification_message').innerText = '인증 이메일 전송 실패: ' + error;
        });
    } else {
        document.getElementById('verification_message').innerText = '이메일을 입력해 주세요.';
    }
}

// 인증 코드 확인 함수
function verifyEmailCode() {
    var email = document.getElementById('user_email').value;
    var code = document.getElementById('verification_code').value;

    if (email && code) {
        fetch('verifyEmailCode', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: JSON.stringify({ user_email: email, code: code }).toString()
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('verification_message').innerText = '인증이 완료되었습니다.';
                document.getElementById('submitBtn').disabled = false; // 인증이 완료되면 회원가입 버튼 활성화
            } else {
                document.getElementById('verification_message').innerText = '인증 코드 확인 실패: ' + data.message;
            }
        })
        .catch(error => {
            document.getElementById('verification_message').innerText = '인증 코드 확인 실패: ' + error;
        });
    } else {
        document.getElementById('verification_message').innerText = '이메일과 인증 코드를 입력해 주세요.';
    }
}


function check_id() {
    // 아이디 중복 확인 로직 구현
}

function check_nickname() {
    // 닉네임 중복 확인 로직 구현
}

function check_email() {
    // 이메일 중복 확인 로직 구현
}

function resetButton() {
    document.getElementById('joinForm').reset();
}

function submitForm(){
    // 상세주소를 user_addr_detail 필드에 저장
    var detailAddress = document.getElementById("sample6_detailAddress").value;
    var submitBtn = document.getElementById('submitBtn');
    if (!submitBtn.disabled) {
        document.getElementById('joinForm').submit();
    } else {
        document.getElementById('verification_message').innerText = '이메일 인증을 완료해 주세요.';
    }

    document.getElementById('user_addr_detail').value = detailAddress;
    document.getElementById('user_addr_detail').value = detailAddress;

    // 폼 제출
    document.getElementById('joinForm').submit();
}


