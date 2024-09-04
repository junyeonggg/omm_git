/* 키워드 검색시 실행*/
function searchKeyword(){
	const keyword = document.querySelector("#keyword").value
	window.location.href=`http://localhost:8080/recipe_list?keyword=${keyword}`;
	
}