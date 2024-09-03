let currentUrl = window.location.pathname;
let regex = /\/customers\/(\w+)/;
let postId = currentUrl.match(regex)[1];
let csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");

// 답글 버튼 클릭 이벤트 (자식 댓글 저장 로직 포함)
let clickEvent = function (e) {
    let findForm = e.target.parentElement.querySelector('form')
    //console.log(childTextarea)
    if(findForm === null){
        let form = document.createElement('form')
        let replyTextarea = document.createElement('textarea');
        replyTextarea.className = "form-control childTextarea"
        replyTextarea.placeholder = "답글 작성란 입니다"
        form.append(replyTextarea)

        let input = document.createElement('input');
        input.type = 'submit'
        input.value = '저장'
        input.className = 'btn btn-primary w-50 m-3'
        form.append(input)
        e.target.parentElement.append(form)

        form.addEventListener('submit', function (e){
            e.preventDefault()
            if (form.querySelector("textarea").value === '') {
                alert('답글을 작성하세요')
                return
            }

            if(form.querySelector("textarea").value.length > 500){
                alert('500자 아래로 적어주세요')
                return
            }

            input.disabled = true;
            saveChild()
            async function saveChild() {

                const headers = {
                    "Content-Type": "application/json;charset=UTF-8",
                    "x-requested-with": "XMLHttpRequest",
                    "X-CSRF-TOKEN" : csrfToken
                };

                const body = {
                    content: form.querySelector("textarea").value,
                    parentId: e.target.parentElement.querySelector('.commentId').textContent
                };

                try {
                    const response = await fetch(`https://dlpoma.store/customers/${postId}/comments`, {
                        method: "POST", headers: headers, body: JSON.stringify(body)
                    });

                    if (response.status === 201){
                        let reply = document.createElement('div');
                        reply.className = 'ml-5'
                        let name = document.createElement('span');
                        name.textContent = '손님'
                        name.className = 'mr-1'
                        reply.append(name)
                        let createdAt = document.createElement('span');
                        let today = new Date();
                        createdAt.textContent = today.toLocaleString()
                        reply.append(createdAt)
                        let content = document.createElement('div');
                        content.textContent = form.querySelector("textarea").value
                        reply.append(content)

                        form.before(reply)
                        form.querySelector("textarea").value = ''
                    }
                    //window.location.reload()
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
                input.disabled = false
            }
        })
        e.target.textContent = "답글 작성 취소"
    }else{
        findForm.remove()
        e.target.textContent = "답글 작성"
    }
}

//부모 댓글 저장
let form = document.querySelector(".parentForm")
form.addEventListener("submit", function (e) {
        e.preventDefault()
        if (document.querySelector(".parentTextarea").value === '') {
            alert('댓글을 작성하세요')
            return
        }
        if(form.querySelector(".parentTextarea").value.length > 500){
            alert('500자 아래로 적어주세요')
            return
        }
        saveParent()
        form.querySelector("input[type=submit]").disabled = true
        async function saveParent() {

            const headers = {
                "Content-Type": "application/json;charset=UTF-8",
                "x-requested-with": "XMLHttpRequest",
                "X-CSRF-TOKEN" : csrfToken
            };

            const body = {
                content: document.querySelector(".parentTextarea").value
            };

            try {
                const response = await fetch(`https://dlpoma.store/customers/${postId}/comments`, {
                    method: "POST", headers: headers, body: JSON.stringify(body)
                });

                if (response.status === 201) {
                    /*
                    let today = new Date();
                    let comment = `<div>
                        <span class='mr-1'>손님</span>
                        <span>${today.toLocaleString()}</span>
                        <span class='Underline-blue replyBtn user-select-none Pe-auto'>답글 작성</span>
                        <div>${form.querySelector("textarea").value}</div>
                    </div>`
                    form.parentElement.append(new DOMParser().parseFromString(comment, 'text/html').body.firstElementChild)
                    document

                    //form.parentElement.append(comment)
                    //form.parentElement.innerHTML += comment
                    form.querySelector("textarea").value = ''
                             */
                    let comment = document.createElement('div');
                    let notice = document.createElement('div');
                    notice.textContent = '댓글 저장 완료!'
                    notice.className = 'text-success'
                    comment.append(notice)
                    let commentId = document.createElement('span');
                    commentId.textContent = await response.json()
                    commentId.className = "d-none commentId"
                    comment.append(commentId)
                    let name = document.createElement('span');
                    name.textContent = '손님'
                    name.className = 'mr-1'
                    comment.append(name)
                    let createdAt = document.createElement('span');
                    let today = new Date();
                    createdAt.textContent = today.toLocaleString()
                    comment.append(createdAt)
                    let replyByn = document.createElement('span');
                    replyByn.textContent = '답글 작성'
                    replyByn.className = "Underline-blue replyBtn user-select-none Pe-auto ml-1"
                    comment.append(replyByn)
                    replyByn.addEventListener('click', clickEvent)
                    let content = document.createElement('div');
                    content.textContent = form.querySelector("textarea").value
                    comment.append(content)

                    form.append(comment)
                    form.querySelector("textarea").value = ''

                } else if (response.status === 401) {
                    let result = confirm('로그인 해주세요! 로그인 페이지로 갈까요?')
                    if (result) window.location.href = "/user/signin"
                }
                else {
                    let responseText = await response.text()
                    alert(responseText)
                }
            } catch (error) {
                console.error("fetch error")
            }
        }
        form.querySelector("input[type=submit]").disabled = false
    }
)

// 답글 버튼 클릭 이벤트 리스너 추가
let replyBtn = document.querySelectorAll(".replyBtn")
replyBtn.forEach((target) => target.addEventListener("click", clickEvent));
