// 상품 검색
function clothSearch() {
    const guestSearchText = document.getElementById("guestSearchText").value;
    window.location.href="/guest/category?category=search&searchText=" + guestSearchText;
}

//*** ProductDetail ***//
// 1) 선택한 컬러에 해당하는 재고의 사이즈 목록 가져오기
function stockSizesGet() {
    const colorSelector = document.getElementById("colorSelector").value;
    const clothId = document.getElementById("clothId").value;

    $.ajax({
        type: 'get',
        url: '/api/stocks/size?colorCode=' + colorSelector + "&clothId=" + clothId,
        success: (result) => {
            //AJAX 성공시 실행 코드(* sizeSelector ID)
            const sizeSelector = document.getElementById("sizeSelector");

            // 1. sizeSelector Clear
            sizeSelector.innerHTML = `<option value = "0">사이즈 선택</option>`;
            // 2. sizeSelector Option 추가
            result.forEach(data => {
                const option = document.createElement('option');
                option.innerHTML = data.sizeName;
                option.value = data.sizeCode;
                sizeSelector.append(option);
            })
        }, error:function(e) {
            alert("error: " + e);
        }
    });
}
// 2) 재고 가져오기
function stockGet() {
    const colorSelector = document.getElementById("colorSelector").value;
    const sizeSelector = document.getElementById("sizeSelector").value;

    // ajax로 Color/Size의 재고 가져와서 화면 보여주기
    $.ajax({
        type: 'GET',
        url: '/api/stocks/color/size?colorCode=' + colorSelector + '&sizeCode=' + sizeSelector,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            const stockOrderAmountSelectBox = document.getElementById("stockOrderAmountSelectBox");
            stockOrderAmountSelectBox.innerHTML
                  = '<tr class="border-top-1-ccc">' +
                    '    <td><b>color/size</b></td>' +
                    '    <td>' +
                    '        <input type = "hidden" id="stockId" value = "' + result.stock.stockId + '">' +
                    '        <b>' + result.color.colorName + '</b>/<b>' + result.size.sizeName + '</b>' +
                    '    </td>' +
                    '</tr>' +
                    '<tr>' +
                    '    <td><b>수량</b></td>' +
                    '    <td><input type = "number" class="w-50 py-1" id = "count" min = "1"' +
                    '               max = "' + result.stock.stockMaxCount + '"value = "1"' +
                    '               oninput = "detailPriceUpdate(this.value);"></td>' +
                    '</tr>' +
                    '<tr class="border-bottom-1-ccc">' +
                    '    <td><b>가격</b></td>' +
                    '    <td><input type = "hidden" id="stockSalePrice" value = "' + result.stock.salePrice + '">' +
                    '    <label id="stockTotalPrice">' + result.stock.salePrice + '원</label></td>' +
                    '</tr>';
        }, error:function(e) {
            alert("실패 에러남");
        }
    });
}
// 3) 가져온 재고 구매 수량 선택시 가격도 바꿔줌
function detailPriceUpdate(count){
    const stockTotalPrice = document.getElementById("stockTotalPrice");
    const stockSalePrice = document.getElementById("stockSalePrice").value;
    const totalPrice = stockSalePrice*count;
    stockTotalPrice.innerHTML = totalPrice + '원';
};

//*** Cart ***//
// 장바구니 추가
function cartAdd() {
    const stockId = document.getElementById("stockId").value;
    const count = document.getElementById("count").value;
    const formData = new FormData();
    formData.append("stockId", stockId);
    formData.append("count", count);

    // 메인 이미지 update 하기
    $.ajax({
        type:"POST",
        url: "/api/cart",
        processData: false,
        contentType: false,
        data: formData,
        success: function(result){
            alert("장바구니에 해당 상품이 추가되었습니다.");
            window.location.href="/guest/cartList";
        },
        err: function(err){
            alert("장바구니 추가 실패");
        }
    })
}
// 장바구니 삭제
function cartDel(cartNum) {
    if(confirm(cartNum + "번 장바구니를 정말 삭제하시겠습니까?")){
        // cart 1개 삭제하는 Ajax 함수
        $.ajax({
            type: 'DELETE',
            url: '/api/carts/' + cartNum,
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert(cartNum + "번 장바구니가 삭제되었습니다.");
                // 화면에서 해당 요소 삭제해줌
                document.getElementById('tr_' + cartNum).remove();
                // Total Price 업데이트
                totalPriceUpdate();
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    } else {
        alert(cartNum + '번 삭제가 취소되었습니다.');
    }
}
// 장바구니 checkbox 선택목록 삭제 Or 장바구니 비우기
function cartAllOrCheckDel(switchStr) {
    let msg = '';
    if(switchStr === 'check'){ msg = "선택된 목록들을 정말 삭제하시겠습니까?" }
    else if(switchStr === 'all'){ msg = "장바구니를 정말 비우시겠습니까?" }

    if(confirm(msg)) {
        const cartNums = document.getElementsByName("cartNums"); // 화면에 있는 모든 Checkbox(cartNums)
        let cartDelList = []; // 삭제된 cartNums

        // checkBox에 체크되었는지 확인 후 삭제
        for (let i = 0; i < cartNums.length; i++) {
            if (switchStr === 'check') {
                if (cartNums[i].checked === true) {
                    let cartNum = cartNums[i].value;
                    cartDelAjax(cartNum);
                    cartDelList.push(cartNum);
                }
            } else if (switchStr === 'all') {
                let cartNum = cartNums[i].value;
                cartDelAjax(cartNum);
                cartDelList.push(cartNum);
            }
        }

        // 삭제되었음을 alert 띄워줌
        alert(cartDelList + "번 장바구니가 삭제되었습니다.");

        // 장바구니의 모든 목록을 삭제해준 것이라면 장바구니 목록이 없다는 Text를 띄워줌
        if(cartDelList.length === cartNums.length){
            document.getElementById("cartSizeZeroMsg").className = 'text-center';
        }

        // 화면에서 해당 요소 삭제해줌
        for (let i = 0; i < cartDelList.length; i++) {
            document.getElementById('tr_' + cartDelList[i]).remove();
        }

        // Total Price 업데이트
        totalPriceUpdate();

    }

    // stock 1개 삭제하는 Ajax 함수
    function cartDelAjax(cartNum){
        $.ajax({
            type: 'DELETE',
            url: '/api/carts/' + cartNum,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// 장바구니 checkbox 한번에 체크 함수
function allCartCheck() {
    const cartCheckAll = document.getElementById("cartCheckAll");
    const cartNums = document.getElementsByName("cartNums");
    // 해당 페이지의 모든 stock CheckBox에 체크
    if(cartCheckAll.checked === true){
        for (let i = 0; i < cartNums.length; i++) {
            cartNums[i].checked = true;
        }
    } else {
        for (let i = 0; i < cartNums.length; i++) {
            cartNums[i].checked = false;
        }
    }
}
// 장바구니 checkBox 버튼 클릭 시 totalPrice 바꿔주기
function totalPriceUpdate() {
    // 값을 넣어줄 Block 들
    const cartSelectTotalText = document.getElementById("cartSelectTotalText"); // 상품구매금액 0 + 배송비 0 = 합계: KRW 0
    const totalSalePriceView = document.getElementById("totalSalePriceView");   // Total 상품 금액
    const totalDeliPriceView = document.getElementById("totalDeliPriceView");   // Total 배송비 금액
    const totalPriceView = document.getElementById("totalPriceView");           // Total 구매 금액

    // 값을 가져올 Block 들
    const cartNums = document.getElementsByName("cartNums"); // 화면에 있는 모든 Checkbox(cartNums)
    let totalSalePrice = 0, totalDeliPrice = 0, totalPrice = 0;

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < cartNums.length; i++) {
        if(cartNums[i].checked === true){
            const salePrice = document.getElementById("salePrice_" + cartNums[i].value).textContent.split(' ')[1]; // 장바구니 상품 단가 금액
            const cartCount = document.getElementById("cartCount_" + cartNums[i].value).value; // 장바구니 상품 수량
            const deliPrice = parseInt(document.getElementById("deliPrice_" + cartNums[i].value).textContent.replace(/원/, '')); // 장바구니 상품 배송비
            totalSalePrice  += salePrice * cartCount; // 상품단가 * 수량
            totalDeliPrice  += deliPrice;             // 상품 배송비
            totalPrice      += totalSalePrice + totalDeliPrice;     // 상품 가격 + 배송비
        }
    }

    // Total 값 Text 띄워주기
    cartSelectTotalText.innerText = '상품구매금액 ' + totalSalePrice +
                                    ' + 배송비 ' + totalDeliPrice +
                                    ' = 합계: KRW ' + totalPrice;
    totalSalePriceView.innerText = 'KRW ' + totalSalePrice;
    totalDeliPriceView.innerText = '+ KRW ' + totalDeliPrice;
    totalPriceView.innerText = '= KRW ' + totalPrice;
}
// 장바구니 numberInput 버튼 값 변경 시 totalPrice 바꿔주기
function cartCountChange(updateCartNum) {
    const cartCount = document.getElementById(updateCartNum).value;
    const salePrice = document.getElementById("salePrice_" + (updateCartNum.split('_')[1])).textContent.split(' ')[1];
    const deliPrice = document.getElementById("deliPrice_" + (updateCartNum.split('_')[1])).textContent.replace(/원/, '');
    const totalPrice = document.getElementById("totalPrice_" + (updateCartNum.split('_')[1]));
    const plusPay = salePrice/100;
    const plusPayBlock = document.getElementById("plusPay_" + (updateCartNum.split('_')[1]));
    totalPrice.innerText = 'KSW ' + (salePrice * cartCount + parseInt(deliPrice));
    plusPayBlock.innerText = (plusPay * cartCount) + '원';

    totalPriceUpdate();
}