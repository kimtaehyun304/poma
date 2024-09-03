let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

//현재 페이지에 속한 nav를 굵게 표시
// 현재 URL을 가져옵니다.
let currentURL = window.location.href;
if(currentURL.includes("social")) {
    // 실행할 코드 작성
    let element = document.getElementById("social-match")
    element.classList.add("selected"); // "selected" 클래스 추가
}

// 매치 신청 버튼 클릭 이벤트
let submitBtn = document.querySelectorAll(".submitBtn")
submitBtn.forEach((target) => target.addEventListener("click", joinMatch));

async function joinMatch() {

    let socialMatchId = this.parentElement.getElementsByClassName("socialMatchId")[0].textContent
    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/social/sports' 부분을 추출합니다.
    let regex = /\/socialMatch\/(\w+)/;
    let sports = currentUrl.match(regex)[1];
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    const body = socialMatchId
    try {
        const response = await fetch(`https://dlpoma.store/socialMatch/${socialMatchId}`, {
            method: "POST", headers: headers, body: JSON.stringify(body)
        });

        if (response.status === 201) {
            window.location.reload()
        }
        else if(response.status === 401){
            let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
            if(result) window.location.href = "/user/signin"
        }
        else {
            let data = await response.text();
            alert(data)
        }
    } catch (error) {
        alert("에러가 발생했습니다")
    }
}

let cancelBtn = document.querySelectorAll(".CancelBtn")
cancelBtn.forEach((target) => target.addEventListener("click", cancelMatch));

async function cancelMatch() {
    let socialMatchId = this.parentElement.getElementsByClassName("socialMatchId")[0].textContent
    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/social/sports' 부분을 추출합니다.
    let regex = /\/socialMatch\/(\w+)/;
    let sports = currentUrl.match(regex)[1];
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    const body = socialMatchId
    try {
        const response = await fetch(`https://dlpoma.store/socialMatch/${socialMatchId}`, {
            method: "DELETE", headers: headers, body: body
        });

        if (response.status === 200) {
            window.location.reload()
        }
        else if(response.status === 401){
            let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
            if(result) window.location.href = "/user/signin"
        }
        else {
            let data = await response.text();
            alert(data)
        }
    } catch (error) {
        alert("에러가 발생했습니다")
    }
}