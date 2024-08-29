function recipe_detail(self){
	recipe_id = self.getAttribute("data-recipe-id")
	window.location.replace(`/recipe/${recipe_id}`)
}