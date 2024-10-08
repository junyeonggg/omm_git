 function validateForm() {
      var user_name = document.getElementById("user_name").value;
      var user_email = document.getElementById("user_email").value;

             // 모든 필수 입력 필드가 비어있는지 확인
             if (user_name == "" || user_email == "") {
                 document.getElementById("msg").innerHTML = "<p style='color: red;'>모든 필드를 입력해야 합니다.</p>";
                 return false; // 폼 제출 방지
             }

             // 이메일 형식 확인
             var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
             if (!emailPattern.test(user_email)) {
                 document.getElementById("msg").innerHTML = "<p style='color: red;'>유효한 이메일 주소를 입력해주세요.</p>";
                 return false; // 폼 제출 방지
             }
             return true; // 폼 제출 허용
         }

function validatePwForm(){
    var user_id = document.getElementById("user_id").value;
    var user_email = document.getElementById("user_email").value;
        // 모든 필수 입력 필드가 비어있는지 확인
        if (user_id == "" || user_email == "") {
        document.getElementById("msg").innerHTML = "<p style='color: red;'>모든 필드를 입력해야 합니다.</p>";
        return false; // 폼 제출 방지
        }
    const url = "/find_pw"
    $.ajax({
        type:"post",
        url : url,
        data: {
        'user_id':user_id,
        'user_email':user_email
        },
      success : data=>{
            if(data){
                window.alert("이메일이 발송되었습니다.")
            } else {
                window.alert("이메일 발송에 실패했습니다.")
            }
        }
    })
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