function inquire_detail(self){
	const inquire_id = self.getAttribute("data-inquire-id")
	console.log("inquire_id : ",inquire_id)
	window.location.replace(`/inquire/${inquire_id}`);
	
}