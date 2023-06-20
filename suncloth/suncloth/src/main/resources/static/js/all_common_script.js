// checkbox 한번에 체크 함수
function allCheck(listName) {
    const checkAllButton = document.getElementById(listName + "CheckAll");
    const checkIds = document.getElementsByName(listName + "Ids");
    // 해당 페이지의 모든 stock CheckBox에 체크
    if(checkAllButton.checked === true){
        for (let i = 0; i < checkIds.length; i++) {
            checkIds[i].checked = true;
        }
    } else {
        for (let i = 0; i < checkIds.length; i++) {
            checkIds[i].checked = false;
        }
    }
}
/* checkbox 제거 시에 all 체크 제거 함수 */
function allCheckRemove(listName) {
    const checkAllButton = document.getElementById(listName + "CheckAll");
    const checkIds = document.getElementsByName(listName + "Ids");
    let checkBoxCount = 0;
    // check가 true인 checkBox 개수
    for (let i = 0; i < checkIds.length; i++) {
        if(checkIds[i].checked) { checkBoxCount += 1; }
    }

    if(checkBoxCount === checkIds.length){ checkAllButton.checked = true; }
    else { checkAllButton.checked = false; }
}

