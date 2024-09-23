// product.js

// 리뷰 수정 함수
function editReview(button) {
    // 리뷰를 수정할 데이터를 추출
    const commentId = button.getAttribute('data-comment-id'); // 리뷰 ID
    const newContent = prompt("새로운 리뷰 내용을 입력하세요:"); // 새 리뷰 내용

    if (newContent) {
        $.ajax({
            type: "POST",
            url: "/editReview",
            data: {
                comment_id: commentId,
                new_content: newContent
            },
            success: function(response) {
                // 수정 성공 후 페이지 새로 고침
                alert("리뷰가 수정되었습니다.");
                window.location.reload();
            },
            error: function() {
                alert("리뷰 수정에 실패하였습니다.");
            }
        });
    }
}


// 리뷰 삭제 함수
function deleteReview(button) {
    // 리뷰를 삭제할 데이터를 추출
    const commentId = button.getAttribute('data-comment-id'); // 리뷰 ID

    if (confirm("정말로 이 리뷰를 삭제하시겠습니까?")) {
        $.ajax({
            type: "POST",
            url: "/deleteReview",
            data: {
                comment_id: commentId
            },
            success: function(response) {
                // 삭제 성공 후 페이지 새로 고침
                alert("리뷰가 삭제되었습니다.");
                window.location.reload();
            },
            error: function() {
                alert("리뷰 삭제에 실패하였습니다.");
            }
        });
    }
}
