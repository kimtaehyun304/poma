
//수정 버튼 클릭 이벤트
document.querySelector("#applyButton").addEventListener("click", function (e) {
    editMembers();
});

async function editMembers() {
    let teamMatchId = document.getElementById('teamMatchId').textContent;
    let sports = document.getElementById('sports').textContent.toLowerCase();
    let userIdList = Array.from(document.querySelectorAll('input[name="userIdList"]:checked')).map(input => input.value);
    let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const headers = {
        "Content-Type": "application/json;charset=UTF-8",
        "x-requested-with": "XMLHttpRequest",
        "X-CSRF-TOKEN" : csrfToken
    };
    const API_URL = window.location.origin;
    try {
        const response = await fetch(`${API_URL}/teamMatch/${teamMatchId}/user/edit`, {
            method: "POST", headers: headers, body: JSON.stringify(userIdList)
        });

        let data = await response.text()
        if (response.status === 200) {
            alert(data)
            window.location.href = document.referrer;
        } else {
            alert(data)
        }

    } catch (error) {
       alert(error)
    }
}
