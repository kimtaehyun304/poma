// 매치 신청 버튼 클릭 이벤트

let myTr = document.querySelectorAll(".myTr")
myTr.forEach((target) => target.addEventListener("click", validateUser));

async function validateUser() {
    let postId = this.firstElementChild.textContent
    const API_URL = window.location.origin;
    try {
        const response = await fetch(`${API_URL}/customers/${postId}`, {
            method: "GET"
        });
        let responseText = await response.text()
        if (response.status === 200) {
            window.location.href = `/customers/${postId}`
        } else {
            alert(responseText)
        }
    } catch (error) {
        console.error("에러가 발생했습니다")
    }

}