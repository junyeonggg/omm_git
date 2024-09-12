package com.omm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.omm.dao.RecipeDao;
import com.omm.dto.CommentDto;
import com.omm.dto.CookingSequenceDto;
import com.omm.dto.ImgDto;
import com.omm.dto.RecipeDto;
import com.omm.dto.Recipe_ingre;

@Service
public class RecipeService {
	@Autowired
	private RecipeDao recipeDao;

	public void insertRecipe(RecipeDto recipe) {
		recipeDao.insertRecipe(recipe);
	}

	public int selectAll(String keyword) {
		return recipeDao.selectAll(keyword);
	}

	public List<RecipeDto> selectRecipeByPagingSearch(int startRecord, int recordSize, String keyword) {
		return recipeDao.selectRecipeByPagingSearch(startRecord, recordSize, keyword);
	}

	public void insertIngre(Recipe_ingre ingre) {
		recipeDao.insertIngre(ingre);
	}

	public int findRecipeIdByMangeId(String mange_id) {
		return recipeDao.findRecipeIdByMangeId(mange_id);
	}

	public void insertRecipeSequence(CookingSequenceDto sequence) {
		recipeDao.insertRecipeSequence(sequence);
	}

	public RecipeDto findRecipeByRecipe_id(int recipe_id) {

		return recipeDao.findRecipeByRecipe_id(recipe_id);
	}

	public List<CookingSequenceDto> selectRecipeSequenceByRecipeId(int recipe_id) {
		return recipeDao.selectRecipeSequenceByRecipeId(recipe_id);
	}

	public String getUserNicknameByUserId(String user_id) {
		return recipeDao.getUserNicknameByUserId(user_id);
	}

	public List<CommentDto> getCommentsByTargetIdAndRefType(int recipe_id, int type_no) {
		return recipeDao.getCommentsByTargetIdAndRefType(recipe_id, type_no);
	}

	public List<Recipe_ingre> selectRecipeIngreByRecipeId(int recipe_id) {
		return recipeDao.selectRecipeIngreByRecipeId(recipe_id);
	}

	public List<String> selectRecipeIngreTypeByRecipeId(int recipe_id) {
		return recipeDao.selectRecipeIngreTypeByRecipeId(recipe_id);
	}

	
	
	public boolean increView(String table_name, String column_name, String target_column, String target_id) {
		return recipeDao.increView(table_name, column_name, target_column, target_id);
	}

	public void likeSet(String user_id, int reference_type, String target_id) {
		recipeDao.likeSet(user_id, reference_type, target_id);
	}

	public void likeUnSet(String user_id, int reference_type, String target_id) {
		recipeDao.likeUnSet(user_id, reference_type, target_id);
	}

	public boolean getLikeStatus(String user_id, int reference_type, int target_id) {
		int cnt = recipeDao.getLikeStatus(user_id, reference_type, target_id);
		if (cnt > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void decreView(String table_name, String column_name, String target_column, String target_id) {
		recipeDao.decreView(table_name, column_name, target_column, target_id);
	}

	public List<String> getMethod() {
		return recipeDao.getMethod();
	}

	public List<String> getStatus() {
		return recipeDao.getStatus();
	}
	public List<String> getIngre() {
		return recipeDao.getIngre();
	}

	public int getInsertRecipeId(String user_id) {
		return recipeDao.getInsertRecipeId(user_id);
	}

	public int getTargetId(CookingSequenceDto cookingSequenceDto) {
		return recipeDao.getTargetId(cookingSequenceDto);
	}

	public void insertImg(ImgDto imgDto) {
		recipeDao.insertImg(imgDto);
	}

	public int getImgId(ImgDto imgDto) {
		return recipeDao.getImgId(imgDto);
	}

	public void updateImgIdAtSequence(int target_id,int img_id) {
		recipeDao.updateImgIdAtSequence(target_id,img_id);
	}

	public ImgDto getImg(int img_id) {
		return recipeDao.getImgByImgId(img_id);
	}

	public boolean updateComment(String newCommentText, int commentId) {
			try {
				recipeDao.updateComment(newCommentText, commentId);
				return true; // 성공적으로 업데이트됨
			} catch (Exception e) {
				// 예외 처리
				e.printStackTrace();
				return false; // 업데이트 실패
			}
    }

	public boolean deleteComment(int commentId) {
		try {
			recipeDao.deleteComment(commentId);
			return true; // 성공적으로 삭제됨
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
			return false; // 삭제 실패
		}
	}

	public List<String> getIngreType() {
		return recipeDao.joinIngreType();
	}

	public boolean deleteRecipe(int recipe_id) {
		try {
			recipeDao.deleteRecipe(recipe_id);
			return true; // 성공적으로 삭제됨
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
			return false; // 삭제 실패
		}
	}

	public Recipe_ingre selectIngreByIngreId(int ingre_id) {
		return recipeDao.selectIngreByIngreId(ingre_id);
	}

	// 업데이트
	public void updateIngre(Recipe_ingre db_ingre) {
		recipeDao.updateIngre(db_ingre);
	}
}
