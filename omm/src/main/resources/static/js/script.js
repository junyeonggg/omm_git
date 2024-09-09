<div th:if="${#fields.hasErrors('user_pw')}" th:errors="*{user_pw}"></div>
                <div id="pw-area" class="feedback"></div>
                <div th:if="${#fields.hasErrors('user_id')}" th:errors="*{user_id}"></div>