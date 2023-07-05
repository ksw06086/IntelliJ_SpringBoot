// *** Board 게시판 관련 Script *** //

// Board 게시판 수정하기
function boardUpdate(){
    let boardInputForm = new FormData(document.getElementById("boardInputForm"));
    const boardState = document.getElementById("boardState").value;
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
            alert(boardSubject + "게시글이 수정되었습니다.");
            window.location.href="/host/boardList?name=" + boardState;
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