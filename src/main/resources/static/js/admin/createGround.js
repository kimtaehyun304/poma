// 가입하기 버튼 클릭 이벤트
document.querySelector("form").addEventListener("submit", function (e) {
    let location = document.getElementsByName("location")[0].value
    let name = document.getElementsByName('name')[0].value;
    let price = document.getElementsByName('price')[0].value;
    e.preventDefault(); // 폼 기본 제출 이벤트 방지

    // 입력값 검증
    if (!location) {
        alert('위치를 입력해주세요.');
        return;
    }

    if (!name) {
        alert('이름을 입력해주세요.');
        return;
    }
    crateGround();

    async function crateGround() {
        let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

        const headers = {
            "Content-Type": "application/json;charset=UTF-8",
            "x-requested-with": "XMLHttpRequest",
            "X-CSRF-TOKEN" : csrfToken
        };
        const body = {location: location, name: name, price: price};
        try {
            const response = await fetch("https://dlpoma.store/ground", {
                method: "POST", headers: headers, body: JSON.stringify(body)
            });
            if (response.status === 201)
                window.location.href = "/grounds"
            else if(response.status === 401){
                let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                if(result) window.location.href = "/user/signin"
            }
            else{
                let responseText = await response.text();
                alert(responseText)
            }
        } catch (error) {
            console.error("fetch error")
        }
    }
});

