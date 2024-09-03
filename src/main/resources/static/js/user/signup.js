document.addEventListener("DOMContentLoaded", function () {
    // 전체 동의 체크박스 로직
    document.getElementById('all-terms').addEventListener('change', function () {
        let isChecked = this.checked;
        document.getElementById('terms1').checked = isChecked;
        document.getElementById('terms2').checked = isChecked;
        document.getElementById('terms3').checked = isChecked;
    });

    // 가입하기 버튼 클릭 이벤트
    document.querySelector("form").addEventListener("submit", function (e) {
        e.preventDefault(); // 폼 기본 제출 이벤트 방지(기본 submit 이벤트 방지)

        let submitBtn = document.getElementsByTagName("button")[0];
        let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");


        // 입력값 검증
        let loginId = document.getElementById('id');
        let password = document.getElementById('password')
        let confirmPassword = document.querySelectorAll("input[type='password']")[1] // 비밀번호 확인
        let name = document.getElementById('name')
        let email = document.getElementById('email')
        let gender = document.getElementById('gender')
        let phone = document.getElementById('phone')

        // 아이디 입력 확인
        if (!loginId.value) {
            alert('아이디를 입력해주세요.');
            loginId.focus()
            return;
        }

        // 비밀번호 조건 확인
        if (password.value.length < 8 || !password.value.match(/[a-zA-Z]/) || !password.value.match(/[\W_]/)) {
            alert('비밀번호는 8자 이상의 영어,특수문자를 포함하여 설정해주세요');
            password.focus()
            return;
        }

        // 비밀번호 일치 확인
        if (password.value !== confirmPassword.value) {
            alert('비밀번호가 일치하지 않습니다.');
            confirmPassword.focus()
            return;
        }

        // 이름 조건 확인
        if (name.value.length < 2 || name.value.length > 10 || !name.value.match(/^[a-zA-Z가-힣]+$/)) {
            alert('이름을 2자 이상 10자 이하의 한글/영어로 설정해주세요');
            name.focus()
            return;
        }

        // 이메일 입력 확인
        if (!email.value) {
            alert('이메일을 입력해주세요.');
            email.focus()
            return;
        }
        // 약관 동의 확인
        if (!document.getElementById('terms1').checked || !document.getElementById('terms2').checked || !document.getElementById('terms3').checked) {
            alert('모든 약관에 동의해주세요.');
            return;
        }
        
        if(gender.value === ""){
            alert('성별을 골라주세요');
            gender.focus()
            return;
        }
        // 모든 검증 통과 후 실제 제출 로직
        // 이 부분에서 서버로 데이터를 제출하는 로직을 추가하세요.
        //alert('회원가입이 완료되었습니다.');
        //let form = document.getElementById('my-form');
        //form.submit();
        signUp();
        async function signUp() {
            submitBtn.disabled = true
            const headers = {
                "Content-Type": "application/json;charset=UTF-8",
                "x-requested-with": "XMLHttpRequest",
                "X-CSRF-TOKEN" : csrfToken
            };
            const body = {loginId: loginId.value, password: password.value, name: name.value, email: email.value, gender: gender.value, phone: phone.value};
            try {
                const response = await fetch("https://dlpoma.store/user/signup", {
                    method: "POST", headers: headers, body: JSON.stringify(body)
                });
                let responseText = await response.text()
                alert(responseText)
                if (response.status === 200)
                    window.location.href = "/user/signin";
            } catch (error) {
                console.error("fetch error")
            }
            submitBtn.disabled = false
        }

    });
});

