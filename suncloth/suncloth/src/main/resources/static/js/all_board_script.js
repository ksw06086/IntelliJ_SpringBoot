//**** Board 게시글 관련 스크립트 ****//
let searchType = '제목';                                        // 게시글 검색 타입 (제목, 내용, 작성자)
let searchInput = '';                                          // 게시글 검색 텍스트(Text)
let pageSize = 10;                                             // 게시글 페이지 당 글 갯수
let writeState = '', contentState = '';                        // 답글상태, 문의구분
let firstDay = '', lastDay = '';                               // 시작, 끝 날짜
let boardState = '';                                           // 게시물 분류
let clothId = 0;                                               // board ClothId
// --- 게시글 검색 버튼 클릭 --- //
function boardsGet(){
    // 검색 요소 Parameter 변수
    let name = document.getElementById("title").value;
    boardState = document.getElementById("title").value;
    searchType = document.getElementById("searchType").value;
    searchInput = document.getElementById("searchInput").value;
    pageSize = document.getElementById("pageSize").value;
    // 검색 요소 - Q&A일때만
    if(boardState === "Q&A"){
        boardState = "Q%26A";
        writeState = document.getElementById("writeState").value;
        contentState = document.getElementById("contentState").value;
    }
    // 검색 요소 - 날짜 변수
    const dayType = document.getElementById("dayType").value;
    if(dayType !== ""){
        const todayDate = new Date(); const date = new Date(); date.setDate(todayDate.getDate() - dayType);
        lastDay = todayDate.toISOString().substring(0, 10); firstDay = date.toISOString().substring(0, 10);
    }

    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/boards?searchType=' + searchType + '&searchInput=' + searchInput + '&size=' + pageSize +
            '&writeState=' + writeState + '&contentState=' + contentState +
            "&firstDay=" + firstDay + "&lastDay=" + lastDay + "&boardState=" + boardState,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const boardListTBody = document.getElementById("boardListTBody_" + name);
            boardListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText_" + name);
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "td-py10 td-px15";
                tr.id = data.num;
                // Q&A일 경우
                let boardTdTopList = '', boardTdBottomList = '', refImage = '';
                if(data.board.boardState === "FAQ" || data.board.boardState === "Q&A")      // 게시글 분류/문의구분 : FAQ, Q&A
                { boardTdTopList += '<td>' + data.board.contentState + '</td>'; }
                if(data.board.boardState === "REVIEW" || data.board.boardState === "Q&A")   // 상품 이미지 : Review, Q&A
                { boardTdTopList += '<td><img src="/uploadMainImageView/' + data.boardCloth.clothId + '" alt="" width = "50px" height = "60px"></td>'; }
                if(data.board.boardState === "Q&A")                                         // 게시글 답변상태 : Q&A
                { boardTdBottomList += '<td>' + data.board.writeState + '</td>'; }
                if(data.board.boardState === "REVIEW")                                      // 게시글 방문횟수 : Review
                { boardTdBottomList += '<td>' + data.board.readCnt + '</td>'; }
                if(data.board.refLevel > 0)                                                 // 답글 여부 확인
                { refImage += '&nbsp;<img src = "${project}ascloimage/re.png" width = "20" height = "15">'}
                // td 태그 추가
                tr.innerHTML =  '<!-- 게시글 NO : All -->' +
                    '<th scope="row">' + data.board.num + '</th>' +
                    boardTdTopList +
                    '<!-- 게시글 제목 : All -->' +
                    '<td><a href="/board/boardView?name=' + boardState + '&num=' + data.board.num + '">' +
                    refImage + data.board.subject + '</a></td>' +
                    '<!-- 게시글 작성자 : All -->' +
                    '<td>' + data.user.username + '</td>' +
                    '<!-- 게시글 작성날짜 : All -->\n' +
                    '<td>' + data.board.regDate + '</td>' +
                    boardTdBottomList;
                boardListTBody.append(tr);
            })

            // 3) 페이지 이동 버튼 보여주는 ListView의 footer 부분
            const pagingBlock = document.getElementById("pagingBlock_" + name);
            // < 버튼 부분 추가
            let pagingStr = '<li class="page-item ' + ((1 === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="boardPaging(this.id, \'' + name + '\')" id="' + (result.pageObject.pageable.pageNumber-1) + '">&lt;</a>\n' +
                '</li>\n';
            // 1,2,... 버튼 부분 추가
            for (let i = result.startPage; i < result.endPage+1; i++) {
                pagingStr += '<li class="page-item ' + ((i === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                    '    <a class="page-link" href="#;" onclick="boardPaging(this.id, \'' + name + '\')" id="' + (i - 1) + '">' + i + '</a>\n' +
                    '</li>\n';
            }
            // > 버튼 부분 추가
            pagingStr += '<li class="page-item ' + ((result.pageObject.totalPages === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="boardPaging(this.id, \'' + name + '\')" id="' + (result.pageObject.pageable.pageNumber+1) + '">&gt;</a>\n' +
                '</li>';
            pagingBlock.innerHTML = pagingStr;
        }, error:function(e) {
            alert("검색 조건이 올바르지 않습니다. 재확인 후 검색해주세요.");
        }
    });
}
// --- 페이지 넘기기 --- //
function boardPaging(page, name){
    pageSize = document.getElementById("pageSize").value;
    boardState = name;
    if(boardState === "Q&A") { boardState = "Q%26A"; }
    if(document.getElementById("clothId") != null){
        clothId = document.getElementById("clothId").value;
    }

    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/boards?searchType=' + searchType + '&searchInput=' + searchInput + '&size=' + pageSize +
            '&writeState=' + writeState + '&contentState=' + contentState + '&clothId' + clothId +
            "&firstDay=" + firstDay + "&lastDay=" + lastDay + "&boardState=" + boardState + "&page=" + page,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const boardListTBody = document.getElementById("boardListTBody_" + name);
            boardListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText_" + name);
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "td-py10 td-px15";
                tr.id = data.num;
                // Q&A일 경우
                let boardTdTopList = '', boardTdBottomList = '', refImage = '', boardTdWriter = '';
                if(data.board.boardState === "FAQ" || data.board.boardState === "Q&A")      // 게시글 분류/문의구분 : FAQ, Q&A
                { boardTdTopList += '<td>' + data.board.contentState + '</td>'; }
                if(data.board.boardState === "REVIEW" || data.board.boardState === "Q&A")   // 상품 이미지 : Review, Q&A
                {
                    if (data.boardCloth !== null) {
                        boardTdTopList += '<td><img src="/uploadMainImageView/' + data.boardCloth.clothId + '" alt="" width = "50px" height = "60px"></td>';
                    } else {
                        boardTdTopList += '<td><div class="bg-secondary-subtle w-50px h-60px mx-auto fs-75">No image</div></td>';
                    }
                }
                if(data.board.boardState === "REPLY")                                       // 댓글 작성자 위치, new 아이콘 : REPLY
                {
                    boardTdTopList += '<td>';
                    if(data.board.regDate === dateFormat(new Date()))
                    {
                        boardTdTopList += '<img src="/icon/new.png" alt="이미지 없음" width = "20px" height = "20px"/>';
                    }
                    boardTdTopList += data.user.username + '</td>';
                    boardTdBottomList += '<td class="text-end"><input type = "button" class="whiteButton" ' +
                        'name = "replyDel" value = "삭제" onclick = "boardOneDel(' + data.board.num + ')"></td>'
                }
                else { boardTdWriter = '<td>' + data.user.username + '</td>'; }
                if(data.board.boardState === "Q&A")                                         // 게시글 답변상태 : Q&A
                { boardTdBottomList += '<td>' + data.board.writeState + '</td>'; }
                if(data.board.boardState === "REVIEW")                                      // 게시글 방문횟수 : Review
                { boardTdBottomList += '<td>' + data.board.readCnt + '</td>'; }
                if(data.board.refLevel > 0)                                                 // 답글 여부 확인
                { refImage += '&nbsp;<img src = "${project}ascloimage/re.png" width = "20" height = "15">';}
                // td 태그 추가
                tr.innerHTML =  '<!-- 게시글 NO : All -->' +
                    '<td>' + data.board.num + '</td>' +
                    boardTdTopList +
                    '<!-- 게시글 제목 : All -->' +
                    '<td><a href="/board/boardView?name=' + boardState + '&num=' + data.board.num + '">' +
                    refImage + data.board.subject + '</a></td>' +
                    '<!-- 게시글 작성자 : All -->' +
                    boardTdWriter +
                    '<!-- 게시글 작성날짜 : All -->\n' +
                    '<td>' + data.board.regDate + '</td>' +
                    boardTdBottomList;
                boardListTBody.append(tr);
            })

            // 3) 페이지 이동 버튼 보여주는 ListView의 footer 부분
            const pagingBlock = document.getElementById("pagingBlock_" + name);
            // < 버튼 부분 추가
            let pagingStr = '<li class="page-item ' + ((1 === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="boardPaging(this.id, \'' + name + '\')" id="' + (result.pageObject.pageable.pageNumber-1) + '">&lt;</a>\n' +
                '</li>\n';
            // 1,2,... 버튼 부분 추가
            for (let i = result.startPage; i < result.endPage+1; i++) {
                pagingStr += '<li class="page-item ' + ((i === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                    '    <a class="page-link" href="#;" onclick="boardPaging(this.id, \'' + name + '\')" id="' + (i - 1) + '">' + i + '</a>\n' +
                    '</li>\n';
            }
            // > 버튼 부분 추가
            pagingStr += '<li class="page-item ' + ((result.pageObject.totalPages === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="boardPaging(this.id, \'' + name + '\')" id="' + (result.pageObject.pageable.pageNumber+1) + '">&gt;</a>\n' +
                '</li>';
            pagingBlock.innerHTML = pagingStr;
        }, error:function(e) {
            alert("페이지 기능 에러남");
        }
    });
}
// --- Q&A, REVIEW 글쓰기 : 상품 선택하기 --- //
// 1. popup 창 열기 //
function withItemListPage() {
    /*
	 * window.open("파일명", "윈도우명", "창에 대한 속성")\
	 * url = "주소?속성 = " + 속성값;   -> get방식
	 */
    const url = "/popup/withItemsSelect";
    window.open(url, "withItem", "menubar=no, width=1000, height = 600");
}
// 2. popup 창에서 상품명 클릭 //
function withItemSelected(clothId) {
    opener.document.getElementById("imageDiv").innerHTML = '<img src="/uploadMainImageView/' + clothId + '" class="w-100 h-100"/>';
    opener.document.getElementById("clothId").value = clothId;
    opener.document.getElementById("clothName").innerText = document.getElementById("clothName_" + clothId).textContent;
    self.close();
}
// --- Q&A 글보기 : Pwd 입력 후 확인 버튼 --- //
function boardPwdCheck(boardPwd) {
    const pwdInput = document.getElementById("pwdInput").value;
    if(pwdInput !== boardPwd) {
        alert("글의 비밀번호와 다릅니다. 다시 입력해주세요.");
    } else {
        document.getElementById("QnADoor").className = 'body d-none';
        document.getElementById("boardBody").className = 'body';
    }
}
// Board 게시판 비밀글 여부 radioButton Event
function boardTextTypeChange(textType) {
    if(textType === "open") {
        document.getElementById("pwd").setAttribute("disabled", "disabled");
    } else {
        document.getElementById("pwd").removeAttribute("disabled");
    }
}

// Board 게시판 추가하기
function boardAdd(userRole){
    let boardInputForm = new FormData(document.getElementById("boardInputForm"));
    let boardState = document.getElementById("boardState").value;
    if(boardState === 'Q&A'){ boardState = 'Q%26A'; }
    let hrefURL = ''
    if(userRole === 'host'){ hrefURL = "/host/boardList?name=" + boardState; }
    if(userRole === 'guest'){ hrefURL = "/board/boardList?name=" + boardState; }
    if(userRole === 'reply'){
        boardState = "REPLY"; boardInputForm.append("boardState", boardState);
    }
    $.ajax({
        type:"POST",
        url: "/api/board",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: boardInputForm,
        success: function(result){
            alert(boardState + " 게시글이 등록되었습니다.");
            boardFilesAdd(result.num);
            if(userRole === 'reply'){ boardPaging(null, "REPLY"); }
            else{ window.location.href=hrefURL; }
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
        }
    });
}
// 게시판 이미지 파일들 삽입
function boardFilesAdd(boardNum) {
    const boardImageInput = document.getElementById("boardFile");
    const formData = new FormData();
    if(boardImageInput === null || boardImageInput.files.length === 0){ return false; } // 게시판 이미지 파일 선택 안했으면 그냥 넘기기
    for (let i = 0; i < boardImageInput.files.length; i++) {
        // formData 에 'subImages' 이라는 키값으로 subImageFile 값을 append 시킨다.
        formData.append('boardImages', boardImageInput.files[i]);
    }
    formData.append("boardNum", boardNum);

    // 게시판 이미지 재삽입하기
    $.ajax({
        type:"POST",
        url: "/api/boardFiles",
        processData: false,
        contentType: false,
        data: formData,
        success: function(result){
            alert("게시판 이미지 삽입 성공");
        },
        err: function(err){
            alert("게시판 이미지 삽입 실패");
        }
    })
}
// 게시판 이미지 파일들 삭제
function boardFilesDel() {
    const boardFileIds = document.getElementsByName("boardFileIds"); // boardFileIds 게시판이미지들

    // 서브 이미지 삭제
    for (let i = 0; i < boardFileIds.length; i++) {
        subFileDelAjax(boardFileIds[i].value);
    }

    // subFile 1개 삭제하는 Ajax 함수
    function subFileDelAjax(boardFileId){
        $.ajax({
            type: 'DELETE',
            url: '/api/boardFiles/' + boardFileId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// Board 게시판 수정하기
function boardUpdate(userRole){
    let boardInputForm = new FormData(document.getElementById("boardInputForm"));
    let boardState = document.getElementById("boardState").value;
    if(boardState === 'Q&A'){ boardState = 'Q%26A'; }
    let hrefURL = ''
    if(userRole === 'host'){ hrefURL = "/host/boardList?name=" + boardState; }
    if(userRole === 'guest'){ hrefURL = "/board/boardList?name=" + boardState; }
    const boardSubject = document.getElementById("boardSubject").value;
    const boardNum = document.getElementById("boardNum");
    const baseBoardFileYN = document.getElementById("baseBoardFileYN");

    // api/board에서 추가와 수정 둘다 가능함(save함수)
    $.ajax({
        type:"POST",
        url: "/api/board",
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        data: boardInputForm,
        success: function(result){
            if(baseBoardFileYN.checked === false){
                boardFilesDel();
                boardFilesAdd(boardNum.value);
            }
            alert(boardSubject + " 게시글이 수정되었습니다.");
            window.location.href=hrefURL;
        },
        err: function(err){
            console.log("err:", err);
            alert("실패");
            return false;
        }
    });
}
// Board 게시판 삭제하기
function boardDel() {
    const boardNums = document.getElementsByName("boardNums"); // 화면에 있는 모든 Checkbox(boardNums)
    let boardDelList = []; // 삭제된 clothIds

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < boardNums.length; i++) {
        if(boardNums[i].checked === true){
            boardDelAjax(boardNums[i].value);
            boardDelList.push(boardNums[i].value);
        }
    }

    // 삭제되었음을 alert 띄워줌
    alert(boardDelList + "번 게시글이 삭제되었습니다.");

    // 화면에서 해당 요소 삭제해줌
    for (let i = 0; i < boardDelList.length; i++) {
        document.getElementById(boardDelList[i]).remove();
    }

    // board 1개 삭제하는 Ajax 함수
    function boardDelAjax(boardNum){
        $.ajax({
            type: 'DELETE',
            url: '/api/boards/' + boardNum,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// Board 게시판 한개 삭제하기
function boardOneDel(boardNum) {
    $.ajax({
        type: 'DELETE',
        url: '/api/boards/' + boardNum,
        success: (result) => {
            //AJAX 성공시 실행 코드
            alert(boardNum + "번 글이 삭제되었습니다.");
            boardPaging(null, "REPLY");
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}
// Board checkbox 한번에 체크 함수
function allBoardCheck() {
    const boardCheckAll = document.getElementById("boardCheckAll");
    const boardNums = document.getElementsByName("boardNums");
    // 해당 페이지의 모든 stock CheckBox에 체크
    if(boardCheckAll.checked === true){
        for (let i = 0; i < boardNums.length; i++) {
            boardNums[i].checked = true;
        }
    } else {
        for (let i = 0; i < boardNums.length; i++) {
            boardNums[i].checked = false;
        }
    }
}
