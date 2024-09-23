from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
from scipy.sparse import csr_matrix

app = Flask(__name__)
CORS(app)  # 모든 도메인 허용

# 모델 로드
model = joblib.load('C:\\TeamProject\\name\\omm_git\\omm\\python\\models\\knn_recommend.h5')  # 미리 저장한 KNN 모델

# 최대 재료 ID (수동 설정, 필요 시 변경)
max_ingredient_id = 45768  # 예: 최대 재료 ID 값 설정

@app.route('/recipeRecommend', methods=['POST'])
def recipe_recommend():
    data = request.json
    user_input = data.get('ingredients', [])  # JSON에서 재료 입력을 가져옴

    # 사용자 입력을 희소 벡터로 변환
    user_vector = [1 if i in user_input else 0 for i in range(max_ingredient_id + 1)]
    user_input_sparse = csr_matrix([user_vector])

    # 추천 레시피 찾기
    distances, indices = model.kneighbors(user_input_sparse, n_neighbors=10)

    # 유효한 인덱스만 확인하고 int로 변환
    valid_indices = [int(idx) for idx in indices[0] if idx < model._fit_X.shape[0]]

    # 추천 레시피 ID 반환
    return jsonify(valid_indices)  # 추천된 인덱스를 반환




if __name__ == '__main__':
    app.run()
