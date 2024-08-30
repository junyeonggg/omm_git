function test(){
    $.ajax({
        type: "GET",
        url: "https://openapi.naver.com/v1/search/shop.json",
        data: { query: "식품" },  // 검색할 키워드
        headers: {
            "X-Naver-Client-Id": "u1ntTdkwo6fEi1SemD2a",  // 네이버 클라이언트 ID
            "X-Naver-Client-Secret": "dqNPaisslH"  // 네이버 클라이언트 시크릿
        },
        success: function(response) {
            console.log(response);  // 검색 결과 출력
        },
        error: function(xhr, status, error) {
            console.log("Error:", status, error);
        }
    });
}

