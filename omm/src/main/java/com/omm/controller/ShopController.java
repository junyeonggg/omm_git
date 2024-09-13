package com.omm.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omm.dto.CartDto;
import com.omm.dto.CategoryDto;
import com.omm.dto.CommentDto;
import com.omm.dto.FoodDto;
import com.omm.dto.MemberDto;
import com.omm.dto.OrderDto;
import com.omm.dto.PaymentRequest;
import com.omm.service.MemberService;
import com.omm.service.RecipeService;
import com.omm.service.ShopService;

@Controller
public class ShopController {
	@Autowired
	private ShopService shopService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private MemberService memberService;

	@GetMapping("/shop")
	public String shop(@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "searchCategory", required = false) String searchCategory,
			@RequestParam(value = "page", defaultValue = "1") String pageParam, // 기본값을 1로 설정
			Model model) {

		int pageSize = 20; // 한 페이지에 표시할 상품 수
		int page = 1; // 기본 페이지 번호를 1로 설정

		// pageParam을 정수로 변환하는 로직 추가
		try {
			page = Integer.parseInt(pageParam);
			if (page < 1) {
				page = 1; // 페이지 번호가 1보다 작으면 1로 설정
			}
		} catch (NumberFormatException e) {
			// 예외 발생 시 페이지 번호를 1로 설정
			page = 1;
		}

		int offset = (page - 1) * pageSize; // 1-based page를 0-based offset으로 변환

		// 데이터 로드
		List<FoodDto> foods;
		if (query != null && !query.trim().isEmpty()) {
			foods = shopService.searchFoods(query, null, pageSize, offset);
		} else if (searchCategory != null && !searchCategory.trim().isEmpty()) {
			foods = shopService.searchFoods(null, searchCategory, pageSize, offset);
		} else {
			foods = shopService.getFoods(pageSize, offset);
		}

		int totalItems = shopService.getTotalItems(query, searchCategory);
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);

		List<CategoryDto> categories = shopService.findAllCategories();

		// 네비게이션 범위 설정
		int startPage = ((page - 1) / 10) * 10 + 1; // 현재 페이지를 기준으로 시작 페이지 계산
		int endPage = Math.min(startPage + 9, totalPages); // 10개씩 페이지 버튼 표시

		// 모델에 값 추가
		model.addAttribute("foods", foods);
		model.addAttribute("categories", categories);
		model.addAttribute("currentPage", page); // 현재 페이지 번호 (1부터 시작)
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("query", query);
		model.addAttribute("searchCategory", searchCategory);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "shop";
	}

	@GetMapping("/product/{foodProductId}")
	public String productDetail(@PathVariable("foodProductId") String foodProductId, Model model, Principal principal) {
		FoodDto food = shopService.getFoodById(foodProductId);
		if (food == null) {
			model.addAttribute("errorMessage", "해당 상품을 찾을 수 없습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}
		String user_nickname = "";
		try {
			user_nickname = recipeService.getUserNicknameByUserId(principal.getName());
			model.addAttribute("user_nickname", user_nickname);
			model.addAttribute("user_id", principal.getName());
		} catch (Exception e) {
			model.addAttribute("user_nickname", "Null");
		}
		model.addAttribute("food", food);
		model.addAttribute("user_nickname", user_nickname);
		// 댓글 리스트
		// 해당 레시피의 댓글
		List<CommentDto> comment_list = recipeService.getCommentsByTargetIdAndRefType(food.getFoodId(), 2);
		model.addAttribute("comment_list", comment_list);
		return "product";
	}

	@ResponseBody
	@PostMapping("/addreply")
	public String addReply(CommentDto comment) {
		// user_id
		// comment_content
		// comment_create_date
		// target_id
		// parent_comment_id
		// comment_rating
		// reference_type

		shopService.insertReply(comment);

		return "";
	}

	@GetMapping("/cart")
	public String cartPage(Principal principal, Model model) {
		List<HashMap<String, Object>> cart_list = shopService.getCartByUserId(principal.getName());
		cart_list.forEach((cart) -> {
			cart.replace("food_lprice", Integer.valueOf((String) cart.get("food_lprice")));
		});

		model.addAttribute("cart_list", cart_list);
		return "cart";
	}

	@ResponseBody
	@PostMapping("/cart/insert")
	public boolean insertCart(CartDto cartDto) {
		boolean check = shopService.checkCartByUserIdAndFoodId(cartDto);
		if (check) {
			// 이미 있으면
			return check;
		} else {
			// 없으면 넣고
			shopService.insertCart(cartDto);
			return check;
		}
	}

	@ResponseBody
	@PostMapping("/delete/cart")
	public String deleteFromCart(@RequestParam("cart_id") int cart_id) {
		shopService.deleteCartByCartId(cart_id);

		return "";
	}

	@ResponseBody
	@PostMapping("/changeCheck")
	public String changeCheck(@RequestParam("check") boolean check, @RequestParam("food_id") int food_id,
			Principal principal) {
		shopService.changeCheck(check, food_id, principal.getName());
		return "";
	}

	@ResponseBody
	@PostMapping("/changeCnt")
	public String changeCnt(@RequestParam("quantity") int quantity, @RequestParam("food_id") int food_id,
			Principal principal) {
		shopService.changeCnt(quantity, food_id, principal.getName());
		return "";
	}

	// 장바구니 -> 주문 페이지
	@GetMapping("/order")
	public String orderPage(Model model, Principal principal,
			@RequestParam(value = "food_id", defaultValue = "-1") int food_id,
			@RequestParam(value = "food_quantity", defaultValue = "-1") int food_quantity) {
		List<HashMap<String, Object>> cart_list = null;
		// 총 결제 금액
		int totPrice = 0;
		if (food_id != -1) {
			cart_list = shopService.getFoodOrder(food_id);
			cart_list.get(0).put("food_quantity", food_quantity);
			cart_list.get(0).replace("food_lprice", Integer.parseInt((String) cart_list.get(0).get("food_lprice")));
			totPrice = (int) cart_list.get(0).get("food_lprice") * food_quantity;
		} else {
			try {
				cart_list = shopService.getCartByUserIdAndCheck(principal.getName());
			} catch (Exception e) {
				return "login";
			}
			cart_list.forEach((cart) -> {
				cart.replace("food_lprice", Integer.parseInt(((String) cart.get("food_lprice"))));
			});
			for (int i = 0; i < cart_list.size(); i++) {
				int intPrice = (int) cart_list.get(i).get("food_lprice");
				totPrice += (intPrice * (int) cart_list.get(i).get("food_quantity"));
			}
		} // 마지막 수정 2024-09-11 10:16
		MemberDto member = null;
		if (principal != null) {
			member = memberService.getMemberByUserId(principal.getName());
		}

		String uuid = UUID.randomUUID().toString().substring(0, 8);
		String order_id = principal.getName() + "_" + uuid;
		model.addAttribute("order_id", order_id);
		model.addAttribute("totPrice", totPrice);
		model.addAttribute("member", member);
		model.addAttribute("cart_list", cart_list);

		return "order";
	}

	@ResponseBody
	@PostMapping("/order")
	public String orderInsert(OrderDto orderDto, @RequestParam("food_id_list") List<String> food_id_list,
			Principal principal) {
		orderDto.setUser_id(principal.getName());
		shopService.insertOrder(orderDto, food_id_list);

		return "";
	}

	@GetMapping("/success")
	public String paySuccess(@RequestParam(value = "orderId") String orderid,
			@RequestParam(value = "paymentKey") String paymentKey, @RequestParam(value = "amount") String amount,
			@RequestParam("food_id_list") List<Integer> food_id_list, Model model) {
		model.addAttribute("order_id", orderid);
		model.addAttribute("food_id_list", food_id_list);
		return "success";
	}

	@PostMapping("/sandbox-dev/api/v1/payments/confirm")
	public ResponseEntity<String> confirmPayment(@RequestBody PaymentRequest paymentRequest) {
		boolean isPaymentSuccessful = processPayment(paymentRequest);

		if (isPaymentSuccessful) {
			return ResponseEntity.ok("결제 승인 성공");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 승인 실패");
		}
	}

	private boolean processPayment(PaymentRequest paymentRequest) {
		return true;
	}

	@GetMapping("/fail")
	public String payFail(@RequestParam(value = "code") String ERROR_CODE,
			@RequestParam(value = "message") String ERROR_MESSAGE, @RequestParam(value = "orderId") String ORDER_ID) {
		return "fail";
	}

	@ResponseBody
	@PostMapping("/editReview")
	public String editReview(@RequestParam("comment_id") int commentId,
							@RequestParam("new_content") String newContent) {
		try {
			boolean updated = shopService.updateReview(commentId, newContent); // 리뷰 업데이트
			if (updated) {
				return "{\"success\": true, \"message\": \"리뷰가 수정되었습니다.\"}"; // 성공 응답
			} else {
				return "{\"success\": false, \"message\": \"리뷰 수정에 실패했습니다.\"}"; // 실패 응답
			}
		} catch (Exception e) {
			return "{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}"; // 예외 처리 응답
		}
	}

	@ResponseBody
	@PostMapping("/deleteReview")
	public String deleteReview(@RequestParam("comment_id") int commentId) {
		try {
			boolean deleted = shopService.deleteReview(commentId); // 리뷰 삭제
			if (deleted) {
				return "{\"success\": true, \"message\": \"리뷰가 삭제되었습니다.\"}"; // 성공 응답
			} else {
				return "{\"success\": false, \"message\": \"리뷰 삭제에 실패했습니다.\"}"; // 실패 응답
			}
		} catch (Exception e) {
			return "{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}"; // 예외 처리 응답
		}
	}
}
