// 전역 변수 선언
var isNicknameChecked = false; // 닉네임 중복 체크 상태
var isNickValidated = false; // 기본 사용 불가
var isTelChecked = false; // 전화번호 중복 체크 상태
var isTelValidated = false; // 기본 사용 불가

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
 function handle_tel_change() {
    var tel_list = document.getElementById('tel_list').value;
    var fullPhoneNumber = '';
        fullPhoneNumber = tel_list + $("#user_tel").val(fullPhoneNumber);
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

function resetButton() {
    document.getElementById('joinForm').reset();
}
$(document).ready(function(){
$("#user_nickname").on('input', function() {
        isNicknameChecked = false;
        $("#nickname-area").html("");
    });

$("#user_tel").on('input', function() {
        isTelChecked = false;
        $("#tel-area").html("");
    });
});
function socialsubmitForm(){
    var currentNickname = $("#user_nickname").val().trim();
    var currentAddress = $("#sample6_address").val().trim();
    var currentTelList = $("#tel_list").val().trim();
    var currentUserTel = $("#user_tel").val().trim();
    var gender = $("input[name='user_gender']:checked").val();
    var currentBirth = $("#user_birth").val().trim();
    var detailAddress = document.getElementById("sample6_detailAddress").value;
    var submitBtn = document.getElementById('submitBtn');

    // 회원가입 폼
    var formJoin = new FormData();

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
    if (!gender) {
          window.alert("성별을 선택해주세요.");
          return false;
   }
    if(currentBirth === ""){
         window.alert("생년월일을 입력해주세요.")
         $("#user_birth").focus()
         return false;
   }
   if(!isNicknameChecked) {
         window.alert("닉네임 중복 체크를 해주세요.");
         $("#user_nickname").focus();
         return false;
   }
   if(!isTelChecked) {
         window.alert("전화번호 중복 체크를 해주세요.");
         $("#user_tel").focus();
         return false;
   }
    if(isNicknameChecked && !isNickValidated){
        window.alert("현재 사용중인 닉네임입니다.");
        return false;
    }
     if(isTelChecked && !isTelValidated){
        window.alert("현재 사용중인 전화번호입니다.");
        return false;
     }
     const user_id = document.querySelector("#user_id").value;
//     const user_email = document.querySelector("#user_email").value;
//     const user_name = document.querySelector("#user_name").value;
     formJoin.append('user_id', user_id);
//     formJoin.append('user_email', user_email);
//     formJoin.append('user_name', user_name);

    // 닉네임
    const user_nickname = document.querySelector("#user_nickname").value;
    formJoin.append('user_nickname',user_nickname)

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
        url : "http://localhost:8080/socialjoin",
        contentType: false,
        processData: false,
        data: formJoin,
        success : data=>{
            window.alert("회원가입이 완료되었습니다.")
            if(data=='true'){
                $.ajax({
                    type : "post",
                    url : "http://localhost:8080/login",
                    data: {'username':user_id,'password':user_id},
                    success : data=>{
                            window.location.replace("http://localhost:8080/")
                            }
                })
            }
        }
    })
}


