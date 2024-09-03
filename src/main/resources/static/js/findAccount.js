let submitBtn =  document.querySelector('button[type="submit"]');

submitBtn.onclick = function (event) {
    event.preventDefault(); // 폼 제출을 방지하여 페이지 새로고침 방지

    let email = document.getElementById("email").value; // 이메일 입력값 가져오기

    if (validateEmail(email)) { // 이메일 형식 검증
        send(email)
    } else {
        alert("올바른 이메일 주소를 입력해주세요.");
    }
};

function validateEmail(email) {
    var re = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/; // 간단한 이메일 형식 검증 정규식
    return re.test(String(email).toLowerCase());
}

async function send(email) {
    submitBtn.disabled = true;
    let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

    const headers = {
        "Content-Type": "text/plain;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    const body = email;
    try {
        const response = await fetch("https://dlpoma.store/user/findAccount", {
            method: "POST", headers: headers, body: body
        });
        let responseText = await response.text();
        alert(responseText)
    } catch (error) {
        console.error("fetch error")
    }
    submitBtn.disabled = false;
}