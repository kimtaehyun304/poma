document.getElementById("joinBtn").addEventListener("click", function (e) {
    let urlSearch = new URLSearchParams(location.search);
    let sports = urlSearch.get('sports')
    let name = document.getElementsByName('name')[0].value;

    async function createTeam() {
        let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

        const headers = {
            "Content-Type": "application/json;charset=UTF-8",
            "x-requested-with": "XMLHttpRequest",
            "X-CSRF-TOKEN" : csrfToken
        };
        const body = name;
        try {
            const response = await fetch("https://dlpoma.store/team", {
                method: "POST", headers: headers, body: body
            });
            const responseText = await response.text();
            alert(responseText)
            if (response.status === 200) window.location.href = `/teams?sports=${sports}`;
            else if(response.status === 401){
                let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                if(result) window.location.href = "/user/signin"
            }
        } catch (error) {
            console.error("fetch error")
        }
    }

    createTeam()
})