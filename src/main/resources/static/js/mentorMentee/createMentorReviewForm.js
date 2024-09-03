let currentURL = window.location.href;
if (currentURL.includes("reviews")) {
    // 실행할 코드 작성
    let element = document.getElementById("mentor-mentee")
    element.classList.add("selected"); // "selected" 클래스 추가
}

let sports = document.querySelector("input[name='sports']")
let reviewForm = document.querySelector("#review-form")
let mentorIdSelect = document.querySelector("select[name='mentorId']")
let scoreSelect = document.querySelector("select[name='score']")
let textarea = document.querySelector("textarea")

reviewForm.addEventListener("submit", function (e) {
        e.preventDefault()
        let url = new URL(e.target.action).pathname
        let mentorIdSelectedOption = mentorIdSelect.options[mentorIdSelect.selectedIndex];
        let scoreSelectedOption = scoreSelect.options[scoreSelect.selectedIndex];

        if (mentorIdSelectedOption.textContent === "검색 결과가 없습니다" || mentorIdSelectedOption.textContent === "멘토를 선택하세요") {
            alert('멘토를 선택하세요')
            return
        }
        if (textarea.value === '') {
            alert('후기를 입력하세요')
            return
        }

        save()
        async function save() {
            let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
            const headers = {
                "Content-Type": "application/json;charset=UTF-8",
                "x-requested-with": "XMLHttpRequest",
                "X-CSRF-TOKEN" : csrfToken
            };

            const body = {
                mentorId: mentorIdSelectedOption.value,
                score: scoreSelectedOption.value,
                description: textarea.value,
                sports: sports.value
            };
            try {
                const response = await fetch("https://dlpoma.store/reviews/form", {
                    method: "POST", headers: headers, body: JSON.stringify(body)
                });

                if (response.status === 201)
                    window.location.href = `/reviews/users/${mentorIdSelectedOption.value}?sports=${sports.value}`
                else if(response.status === 401){
                    let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                    if(result) window.location.href = "/user/signin"
                }
                else {
                    let responseText = await response.text()
                    alert(responseText)
                }
            } catch (error) {
                console.error("fetch error")
            }

        }
    }
)

let searchForm = document.querySelector(".searchForm")
searchForm.addEventListener("submit", function (e) {
    e.preventDefault()
    getMentors()
    async function getMentors() {
        let name = document.querySelector("input[name='name']").value

        try {
            const response = await fetch(`https://dlpoma.store/api/mentors?name=${name}`, {
                method: "GET"
            });

            if (response.status === 200) {
                let mentorSelect = document.querySelector("select[id='mentor-name']")
                let json = await response.json()
                mentorSelect.textContent = ''
                for (let user of json) {
                    let option = document.createElement('option');
                    option.value = user.id
                    option.text = user.name
                    mentorSelect.append(option)
                }

                if(json.length === 0){
                    let option = document.createElement('option');
                    option.text = "검색 결과가 없습니다"
                    mentorSelect.append(option)
                }

                mentorSelect.click()

            } else {
                let responseText = await response.text()
                alert(responseText)
            }
        } catch (error) {
            console.error("에러가 발생했습니다")
        }
    }
})