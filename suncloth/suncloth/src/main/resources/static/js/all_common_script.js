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

// *** 상품 검색 관련 Script *** //
let searchType = '상품명';                                      // 상품 검색 타입 (상품명, 상품번호)
let searchInput = '';                                          // 상품 검색 텍스트(Text)
let iconList = [];                                             // 아이콘 checkBox 리스트
let brandId = 0, mainCategoryId = 0, subCategoryId = 0;        // 브랜드, 메인 카테고리, 서브 카테고리 ID
let firstDay = '', lastDay = '';                               // 시작, 끝 날짜
// --- 상품 검색 버튼 클릭 --- //
function clothsGet(){
    // 검색 요소 Parameter 변수
    searchType = document.getElementById("searchType").value;
    searchInput = document.getElementById("searchInput").value;
    iconList = []; document.getElementsByName("icon").forEach(icon => {
        if(icon.checked){ iconList.push(icon.value); }
    });
    brandId = document.getElementById("brandId").value;
    mainCategoryId = document.getElementById("mainCategorySelector").value;
    subCategoryId = document.getElementById("subCategorySelector").value;
    firstDay = document.getElementById("firstDay").value;
    lastDay = document.getElementById("lastDay").value;

    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/cloths?searchType=' + searchType + '&searchInput=' + searchInput + '&brandId=' + brandId +
            '&mainCategoryId=' + mainCategoryId + '&subCategoryId=' + subCategoryId +
            "&icons=" + iconList + "&firstDay=" + firstDay + "&lastDay=" + lastDay,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const productListTBody = document.getElementById("productListTBody");
            productListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText");
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "text-center td-py10 td-px15";
                tr.id = data.clothId;
                // icon 체크
                let icon = '';
                if(data.mainCategory.mainName == 'onlySuncloth'){ icon += '<img src="/icon/only.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'best'){ icon += '<img src="/icon/best.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'hot'){ icon += '<img src="/icon/hot.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'minPrice'){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                if(data.cloth.regDate == new Date()){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                // td 태그 추가
                tr.innerHTML =  '<td><input type = "checkbox" name = "clothIds" value = "' + data.cloth.clothId + '"></td>' +
                    '<td>' +
                    '<img src="/uploadMainImageView/' + data.cloth.clothId + '" alt="이미지 없음" width = "50px" height = "60px">' +
                    '</td>' +
                    '<td>' + data.cloth.clothId + '</td>' +
                    '<td>' + data.subCategory.subName + '</td>' +
                    '<td class="text-start">' + icon +
                    '<a href="/host/productInput?clothId=' + data.cloth.clothId + '">' + data.cloth.clothName +
                    '</a></td>' +
                    '<td>' + data.cloth.basePrice + '</td>' +
                    '<td>' + data.cloth.deliPrice + '</td>' +
                    '<td>' + data.cloth.regDate + '</td>' +
                    '<td><a href="/host/stockList?clothId=' + data.cloth.clothId +'"><input type = "button" value = "재고 관리" name = "csInput" class="whiteButton"/></a></td>';
                productListTBody.append(tr);
            })

            // 3) 페이지 이동 버튼 보여주는 ListView의 footer 부분
            const pagingBlock = document.getElementById("pagingBlock");
            // < 버튼 부분 추가
            let pagingStr = '<li class="page-item ' + ((1 === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber-1) + '">&lt;</a>\n' +
                '</li>\n';
            // 1,2,... 버튼 부분 추가
            for (let i = result.startPage; i < result.endPage+1; i++) {
                pagingStr += '<li class="page-item ' + ((i === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                    '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (i - 1) + '">' + i + '</a>\n' +
                    '</li>\n';
            }
            // > 버튼 부분 추가
            pagingStr += '<li class="page-item ' + ((result.pageObject.totalPages === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber+1) + '">&gt;</a>\n' +
                '</li>';
            pagingBlock.innerHTML = pagingStr;
        }, error:function(e) {
            alert("검색 조건이 올바르지 않습니다. 재확인 후 검색해주세요.");
        }
    });
}
// --- 페이지 넘기기 --- //
function clothPaging(page){
    // ajax로 검색결과의 상품 가져와서 목록 Update
    $.ajax({
        type: 'GET',
        url: '/api/cloths?searchType=' + searchType + '&searchInput=' + searchInput + '&brandId=' + brandId +
            '&mainCategoryId=' + mainCategoryId + '&subCategoryId=' + subCategoryId +
            "&icons=" + iconList + "&firstDay=" + firstDay + "&lastDay=" + lastDay + "&page=" + page,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const productListTBody = document.getElementById("productListTBody");
            productListTBody.innerHTML = '';

            // 1) 가져온 목록 개수 뿌려주는 ListView의 Head 부분
            const getPageCountText = document.getElementById("getPageCountText");
            getPageCountText.innerHTML = '검색 ' + result.pageObject.numberOfElements + '건 /'
                + ' 총 ' + result.pageObject.totalElements + '건';

            // 2) 가져온 목록 뿌려주는 ListView의 Body 부분
            result.contentList.forEach(data => {
                // tr 태그
                const tr = document.createElement('tr');
                tr.className = "text-center td-py10 td-px15";
                tr.id = data.clothId;
                // icon 체크
                let icon = '';
                if(data.mainCategory.mainName == 'onlySuncloth'){ icon += '<img src="/icon/only.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'best'){ icon += '<img src="/icon/best.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'hot'){ icon += '<img src="/icon/hot.png" alt="이미지 없음" width = "45px" height = "18px">'}
                if(data.cloth.icon == 'minPrice'){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                if(data.cloth.regDate == new Date()){ icon += '<img src="/icon/minPrice.png" alt="이미지 없음" width = "30px" height = "30px">'}
                // td 태그 추가
                tr.innerHTML =  '<td><input type = "checkbox" name = "clothIds" value = "' + data.cloth.clothId + '"></td>' +
                    '<td>' +
                    '<img src="/uploadMainImageView/' + data.cloth.clothId + '" alt="이미지 없음" width = "50px" height = "60px">' +
                    '</td>' +
                    '<td>' + data.cloth.clothId + '</td>' +
                    '<td>' + data.subCategory.subName + '</td>' +
                    '<td class="text-start">' + icon +
                    '<a href="/host/productInput?clothId=' + data.cloth.clothId + '">' + data.cloth.clothName +
                    '</a></td>' +
                    '<td>' + data.cloth.basePrice + '</td>' +
                    '<td>' + data.cloth.deliPrice + '</td>' +
                    '<td>' + data.cloth.regDate + '</td>' +
                    '<td><a href="/host/stockList?clothId=' + data.cloth.clothId +'"><input type = "button" value = "재고 관리" name = "csInput" class="whiteButton"/></a></td>';
                productListTBody.append(tr);
            })

            // 3) 페이지 이동 버튼 보여주는 ListView의 footer 부분
            const pagingBlock = document.getElementById("pagingBlock");
            // < 버튼 부분 추가
            let pagingStr = '<li class="page-item ' + ((1 === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber-1) + '">&lt;</a>\n' +
                '</li>\n';
            // 1,2,... 버튼 부분 추가
            for (let i = result.startPage; i < result.endPage+1; i++) {
                pagingStr += '<li class="page-item ' + ((i === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                    '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (i - 1) + '">' + i + '</a>\n' +
                    '</li>\n';
            }
            // > 버튼 부분 추가
            pagingStr += '<li class="page-item ' + ((result.pageObject.totalPages === result.pageObject.pageable.pageNumber+1)?'disabled':'') + '">\n' +
                '    <a class="page-link" href="#;" onclick="clothPaging(this.id)" id="' + (result.pageObject.pageable.pageNumber+1) + '">&gt;</a>\n' +
                '</li>';
            pagingBlock.innerHTML = pagingStr;
        }, error:function(e) {
            alert("페이지 기능 에러남");
        }
    });
}