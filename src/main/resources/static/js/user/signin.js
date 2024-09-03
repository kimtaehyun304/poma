let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

//일반 계정 로그인
let loginForm = document.querySelector(".loginForm");

if(loginForm != null) {
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault(); //기존 submit 이벤트 실행 막음
        const loginId = document.getElementById("id")
        const password = document.getElementById("password")

        if (!loginId.value.trim()) {
            alert("아이디를 입력해주세요.");
            loginId.focus(); // 사용자가 다시 입력할 수 있도록 아이디 입력란에 포커스를 맞춤
            return;
        }
        if (!password.value.trim()) {
            alert("비밀번호를 입력해주세요.");
            password.focus(); // 사용자가 다시 입력할 수 있도록 비밀번호 입력란에 포커스를
            // 맞춤
            return;
        }
        login()

        async function login() {


            let submitBtn = document.getElementsByTagName("button")[0]
            submitBtn.disabled = true
            const headers = {
                "Content-Type": "application/json;charset=UTF-8",
                "x-requested-with": "XMLHttpRequest",
                "X-CSRF-TOKEN": csrfToken
            };
            const body = {loginId: loginId.value, password: password.value,};
            try {
                const response = await fetch("https://dlpoma.store/user/signin", {
                    method: "POST", headers: headers, body: JSON.stringify(body)
                });

                if (response.status === 200)
                    window.location.href = document.referrer
                else {
                    let responseText = await response.text()
                    alert(responseText)
                }
            } catch (error) {
                console.error("fetch error")
            }
            submitBtn.disabled = false
        }
    });
}
//손님 계정 로그인
let isRunning = false;
let guest = document.querySelectorAll(".GuestLink");

if(guest != null) {
    guest.forEach((target) => target.addEventListener("click", function (event) {
        event.preventDefault()
        if (isRunning === false) login()

        async function login() {
            isRunning = true
            let loginId;
            if (event.target.textContent === "손님1")
                loginId = "guest"
            else if (event.target.textContent === "손님2")
                loginId = "guest2"

            const headers = {
                "Content-Type": "application/json;charset=UTF-8",
                "x-requested-with": "XMLHttpRequest",
                "X-CSRF-TOKEN": csrfToken
            };
            const body = {loginId: loginId};
            try {
                const response = await fetch("https://dlpoma.store/user/signin", {
                    method: "POST", headers: headers, body: JSON.stringify(body)
                });

                if (response.status === 200)
                    window.location.href = document.referrer
                else {
                    let responseText = await response.text()
                    alert(responseText)
                }
            } catch (error) {
                console.error("fetch error")
            }
            isRunning = false;
        }
    }));
}




