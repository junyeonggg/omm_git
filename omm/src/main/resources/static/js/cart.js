function changeQuantity(self,price,food_id){
	const target = self.parentElement.nextElementSibling.firstChild;
	var target_value = self.value * price;
	let fomatter = new Intl.NumberFormat('ko-KR');
	target_value = fomatter.format(target_value);
	target.value=target_value;
	// db 업데이트
	$.ajax({
		type : "post",
		url : "/changeCnt",
		data : {"food_id" : food_id,"quantity" : self.value}
	})
	
}

function deleteFoodFromCart(cart_id){
	$.ajax({
		type:"post",
		url : "/delete/cart",
		data : {cart_id:cart_id},
		complete : window.location.reload()
		
	})
}

function modalUp(){
	document.querySelector("#modal").style.display="flex";
}
function checkboxclick(self,food_id){
	$.ajax({
		type: "post",
		url : "/changeCheck",
		data : {food_id:food_id,check:self.checked}
		
	})
	
}