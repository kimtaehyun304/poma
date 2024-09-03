
//수정 버튼 클릭 이벤트
document.querySelector("#applyButton").addEventListener("click", function (e) {
    editMembers();
});

async function editMembers() {
    let mercenaryMatchId = document.getElementById('matchId').textContent;
    let sports = document.getElementById('sports').textContent.toLowerCase();
    let memberIdList = Array.from(document.querySelectorAll('input[name="memberIdList"]:checked')).map(input => input.value);
    let mercenaryIdList = Array.from(document.querySelectorAll('input[name="mercenaryIdList"]:checked')).map(input => input.value);
    let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };

    let body = {'memberIdList': memberIdList, 'mercenaryIdList': mercenaryIdList}
    try {
        const response = await fetch(`https://dlpoma.store/mercenaryMatch/${mercenaryMatchId}/user/edit`, {
            method: "POST", headers: headers, body: JSON.stringify(body)
        });

        let data = await response.text()
        if (response.status === 200) {
            alert(data)
            window.location.href = document.referrer;
        } else {
            alert(data)
        }

    } catch (error) {
        console.error("에러가 발생했습니다")
    }
}
