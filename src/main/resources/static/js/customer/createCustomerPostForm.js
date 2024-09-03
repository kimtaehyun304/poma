let saveBtn = document.querySelector("div button")

// 가입하기 버튼 클릭 이벤트
if (saveBtn != null) {
    saveBtn.addEventListener("click", function (e) {
        let titleElement = document.getElementsByName("title")[0]
        let contentElement = document.getElementsByName('content')[0]
        let secretElement = document.getElementsByName('secret')[0]

        if (titleElement.value.length > 50) {
            alert('제목은 50자 아래로 입력해주세요.');
            titleElement.focus()
            return;
        }

        if (contentElement.value.length > 1000) {
            alert('내용은 1000자 아래로 입력해주세요.');
            contentElement.focus()
            return;
        }

        save();

        async function save() {
            let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
            const headers = {
                "Content-Type": "application/json;charset=UTF-8",
                "x-requested-with": "XMLHttpRequest",
                "X-CSRF-TOKEN" : csrfToken
            };
            const body = {
                title: titleElement.value,
                content: contentElement.value,
                secretElement: secretElement.value
            };
            try {
                const response = await fetch("https://dlpoma.store/customer", {
                    method: "POST", headers: headers, body: JSON.stringify(body)
                });
                if (response.status === 201)
                    window.location.href = "/customer/ok"
                else if(response.status === 401){
                    let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                    if(result) window.location.href = "/user/signin"
                }
                else {
                    let responseText = await response.text();
                    alert(responseText)
                }
            } catch (error) {
                console.error("fetch error")
            }
        }
    });
}