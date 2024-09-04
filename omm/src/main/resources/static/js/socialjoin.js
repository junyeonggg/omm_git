// 전역 변수 선언
var isNicknameChecked = false; // 닉네임 중복 체크 상태
var isEmailChecked = false; // 이메일 중복 체크 상태
var isIdAvailable = false; // 기본값: 사용 불가로 설정
var isEmailAvailable = false; // 이메일 사용 가능 여부
var pastId = "";

// 닉네임 중복 체크 함수
async function checkNickname() {
    var snd_data = $("#user_nickname").val().trim();
    if (snd_data === "") {
        $("#nickname").html("<p>닉네임을 입력해 주세요.</p>");
        isIdAvailable = false;
        return;
    }
    if (snd_data.length < 2 || snd_data.length > 6) {
        $("#nickname-area").html("<p>닉네임는 2~6자로 입력해주세요.</p>");
        isIdAvailable = false;
        return;
    }
    try {
        console.log("보냄")
        const data = await $.ajax({
            type: "get",
            dataType: "text",
            url: "http://localhost:8080/checkNickname",
            data: { data: snd_data },
            success : data => {
                console.log(data)
                if (data === "true") {
                    $("#nickname-area").html("<p>사용 가능한 닉네임입니다.</p>");
                    isIdAvailable = true;
                } else {
                    $("#nickname-area").html("<p>사용할 수 없는 닉네임입니다.</p>");
                    isIdAvailable = false;
                }
            }
        });
        if (data === "true") {
            $("#nickname-area").html("<p>사용 가능한 닉네임입니다.</p>");
            isIdAvailable = true;
        } else {
            $("#nickname-area").html("<p>사용할 수 없는 닉네임입니다.</p>");
            isIdAvailable = false;
        }
    } catch (error) {
        window.alert("에러가 발생했습니다.");
        isIdAvailable = false;
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

function resetButton() {
    document.getElementById('joinForm').reset();
}

function socialsubmitForm(){

    // 회원가입 폼
    var formJoin = new FormData();
    // 아이디
    const user_id = document.querySelector("#user_id").value;
    formJoin.append('user_id',user_id)
    console.log("user_id : ",user_id)

    //비밀번호
    formJoin.append('user_pw',user_id)
    console.log("user_pw : ",user_id)

/*    // 이름
    const user_name = document.querySelector("#user_name").value;
    formJoin.append('user_name',user_name)
    console.log("user_name : ",user_name)
    // 닉네임
    const user_nickname = document.querySelector("#user_nickname").value;
    formJoin.append('user_nickname',user_nickname)
    console.log("user_nickname : ",user_nickname)
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
    console.log("user_email : ",user_email)
    // 주소 부분
    const user_addr_zip = document.querySelector("#user_addr_zip").value;
    const user_addr = document.querySelector("#user_addr").value;
    const user_addr_detail = document.querySelector("#sample6_detailAddress").value;
    formJoin.append('user_addr_zip',user_addr_zip);
    formJoin.append('user_addr',user_addr);
    formJoin.append('user_addr_detail',user_addr_detail);
    console.log("user_addr_zip : ",user_addr_zip)
    console.log("user_addr : ",user_addr)
    console.log("user_addr_detail : ",user_addr_detail)
    //전화번호
    const user_tel_front = document.querySelector("#tel_list").value;
    const user_tel_back = document.querySelector("#user_tel").value;
    const user_tel = user_tel_front+user_tel_back;
    formJoin.append('user_tel',user_tel);
    console.log("user_tel : ",user_tel)
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
    console.log("user_gender : ",user_gender)
    // 생년월일
    const user_birth = document.querySelector("#user_birth").value;
    formJoin.append('user_birth',user_birth);
    console.log("user_birth : ",user_birth)*/
    $.ajax({
        type : "post",
        url : "http://localhost:8080/socialjoin",
        contentType: false,
        processData: false,
        data: formJoin,
        success : data=>{
            window.alert("회원가입이 완료되었습니다.")
            console.log("data : "+data)
            console.log("user_id : "+user_id)
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






    // 폼 제출
    // document.getElementById('joinForm').submit();
}


