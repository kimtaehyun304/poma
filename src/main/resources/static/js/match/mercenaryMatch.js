let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

// 현재 URL을 가져옵니다.
let currentURL = window.location.href;
if(currentURL.includes("mercenaryMatch")) {
    // 실행할 코드 작성
    let element = document.getElementById("mercenary-match")
    element.classList.add("selected"); // "selected" 클래스 추가
}


let joinMercenaryBtn = document.querySelectorAll(".joinMercenaryBtn")
joinMercenaryBtn.forEach((target) => target.addEventListener("click", joinAsMercenary))

async function joinAsMercenary() {
    let mercenaryMatchId = this.parentElement.parentElement.parentElement.getElementsByClassName("mercenaryMatchId")[0].textContent
    let teamId = this.parentElement.getElementsByClassName("teamId")[0].textContent

    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    //const body = {mercenaryMatchId: mercenaryMatchId, teamId: teamId};
    const body = teamId;
    try {
        const response = await fetch(`https://dlpoma.store/mercenaryMatch/${mercenaryMatchId}/user/mercenary`, {
            method: "POST", headers: headers, body: body
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
        alert("서버 에러 발생!")
    }
}


let cancelMercenaryBtn = document.querySelectorAll(".cancelMercenaryBtn")
cancelMercenaryBtn.forEach((target) => target.addEventListener("click", cancelMercenary))

async function cancelMercenary() {
    let mercenaryMatchId = this.parentElement.parentElement.parentElement.getElementsByClassName("mercenaryMatchId")[0].textContent
    let teamId = this.parentElement.getElementsByClassName("teamId")[0].textContent

    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    //const body = {mercenaryMatchId: mercenaryMatchId, teamId: teamId};
    const body = teamId;
    try {
        const response = await fetch(`https://dlpoma.store/mercenaryMatch/${mercenaryMatchId}/user/mercenary`, {
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
        alert("서버 에러 발생!")
    }
}



let submitBtn = document.querySelectorAll(".submitBtn")
submitBtn.forEach((target) => target.addEventListener("click", joinMatch));

async function joinMatch() {
    let mercenaryMatchId = this.parentElement.parentElement.parentElement.parentElement.parentElement.getElementsByClassName("mercenaryMatchId")[0].textContent

    try {
        const response = await fetch(`https://dlpoma.store/mercenaryMatch/${mercenaryMatchId}/user/new`, {

        });

        if (response.status === 200) {
            window.location.href = `/mercenaryMatch/${mercenaryMatchId}/user/new`;
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
    let mercenaryMatchId = this.parentElement.parentElement.getElementsByClassName("mercenaryMatchId")[0].textContent

    try {
        const headers = {
            "Content-Type": "application/json;charset=UTF-8",
            "x-requested-with": "XMLHttpRequest",
            "X-CSRF-TOKEN" : csrfToken
        };

        const response = await fetch(`https://dlpoma.store/mercenaryMatch/${mercenaryMatchId}/user`, {
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