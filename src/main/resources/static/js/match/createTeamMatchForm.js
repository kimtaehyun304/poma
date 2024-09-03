
// 매치 신청 버튼 클릭 이벤트
document.querySelector("#applyButton").addEventListener("click", function (e) {
    joinMatch();
});

async function joinMatch() {
    let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    let teamMatchId = document.getElementById('teamMatchId').textContent;
    let sports = document.getElementById('sports').textContent.toLowerCase();
    let userIdList = Array.from(document.querySelectorAll('input[name="userIdList"]:checked')).map(input => input.value);
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    try {
        const response = await fetch(`https://dlpoma.store/teamMatch/${teamMatchId}/user/new`, {
            method: "POST", headers: headers, body: JSON.stringify(userIdList)
        });

        let data = await response.text()
        if (response.status === 201) {
            alert(data)
            window.location.href = document.referrer; //예
            //window.location.href = "/team/" + sports
        } else {
            alert(data)
        }

    } catch (error) {
        console.error("에러가 발생했습니다")
    }
}
