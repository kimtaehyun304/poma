let adminBtns = document.querySelectorAll(".admin-btn")
let userBtns = document.querySelectorAll(".user-btn")
adminBtns.forEach((target) => target.addEventListener("click", grant));
userBtns.forEach((target) => target.addEventListener("click", grant));

async function grant() {
    //유저 ID
    let pkId = this.parentElement.parentElement.firstElementChild.textContent
    //부여할 권한
    let authority = this.textContent
    //형제 element 찾으면 복잡함
    let authorityStatus = this.parentElement.parentElement.children[4]

    let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    const body = {id: pkId, authority: authority};
    try {
        const response = await fetch("https://dlpoma.store/admins", {
            method: "POST", headers: headers, body: JSON.stringify(body)
        });
        if (response.status === 201){
            authorityStatus.textContent = authority;
        }
        else if(response.status === 401){
            let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
            if(result) window.location.href = "/user/signin"
        }
        else{
            let responseText = await response.text();
            alert(responseText)
        }
    } catch (error) {
        alert("서버 에러 발생!")
    }
}
