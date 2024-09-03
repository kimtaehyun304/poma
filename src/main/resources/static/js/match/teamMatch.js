let currentURL = window.location.href;
if (currentURL.includes("team")) {
    // 실행할 코드 작성
    let element = document.getElementById("team-match")
    element.classList.add("selected"); // "selected" 클래스 추가
}

// 매치 신청 버튼 클릭 이벤트
let submitBtn = document.querySelectorAll(".submitBtn")
submitBtn.forEach((target) => target.addEventListener("click", joinMatch));

async function joinMatch() {
    let teamMatchId = this.parentElement.getElementsByClassName("teamMatchId")[0].textContent
    
    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/team/sports' 부분을 추출합니다.
    let regex = /\/teamMatch\/(\w+)/;
    let sports = currentUrl.match(regex)[1];


    try {
        const response = await fetch(`https://dlpoma.store/teamMatch/${teamMatchId}/user/new`, {
            method: "GET"
        });

        if (response.status === 200) {
            window.location.href = `/teamMatch/${teamMatchId}/user/new`;
        }
        else if(response.status === 401){
            let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
            if(result) window.location.href = "/user/signin"
        }
        else {
            let responseText = await response.text()
            alert(responseText)
        }

    } catch (error) {
        console.error("에러가 발생했습니다")
    }
}

// 일괄 취소 버튼 클릭 이벤트
let cancelBtn = document.querySelectorAll(".CancelBtn")
cancelBtn.forEach((target) => target.addEventListener("click", cancelMatch));

async function cancelMatch() {
    let teamMatchId = this.parentElement.getElementsByClassName("teamMatchId")[0].textContent

    // 현재 URL을 가져옵니다.
    let currentUrl = window.location.pathname;
    // 정규 표현식을 사용하여 '/team/sports' 부분을 추출합니다.
    let regex = /\/teamMatch\/(\w+)/;
    let sports = currentUrl.match(regex)[1];
    let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };

    try {
        const response = await fetch(`https://dlpoma.store/teamMatch/${teamMatchId}/user`, {
            method: "DELETE", headers: headers
        });

        if (response.status === 200) {
            window.location.reload()
        }
        else if(response.status === 401){
            let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
            if(result) window.location.href = "/user/signin"
        }
        else {
            let responseText = await response.text()
            alert(responseText)
        }

    } catch (error) {
        console.error("에러가 발생했습니다")
    }
}