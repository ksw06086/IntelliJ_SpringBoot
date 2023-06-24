// *** 비밀번호 정규식 ***
// 1) 최소 하나 이상의 소문자, 하나의 대문자, 하나의 숫자, 하나의 특수문자, 최소 8 자리
// 2) 비밀번호 6 자리 이상, 모든 요구 사항을 충족하거나 숫자만 빼고 나머지를 충족하는 경우
// 3) 비밀번호 6 자리 이상, 최소 하나 이상의 대소문자, 하나의 숫자, 하나의 특수문자
let strongPassword = new RegExp('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{8,})');
let mediumStrongPassword = new RegExp('((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{6,}))|((?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9])(?=.{8,}))');
let mediumPassword = new RegExp('(((?=.*[a-z])|(?=.*[A-Z]))(?=.*[0-9])(?=.*[^A-Za-z0-9])(?=.{6,}))');
// *** 휴대폰 번호 정규식 *** //
let regPhone = /^01([0|1|2|7|8|9])?-([0-9]{3,4})?-([0-9]{4})$/;
// *** 숫자 정규식 *** //
let numberCheck = /^[0-9]+$/;


// *** Register(회원가입) *** //
// ID 중복검사
function idCheck(id){
    const idForm = document.getElementById("IDForm");
    const idDiv = document.getElementById("IDdiv");

    $.ajax({
        url: '/api/userCheck/' + id,
        type: 'GET',
        success: (result) => {
            //AJAX 성공시 실행 코드
            if(id === ""){
                idDiv.previousElementSibling.className = "form-control is-invalid";
                idDiv.className = "invalid-feedback"
                idDiv.innerText = "입력해주세요.";
            } else if(id.length > 30){
                idDiv.previousElementSibling.className = "form-control is-invalid";
                idDiv.className = "invalid-feedback"
                idDiv.innerText = "id는 30자가 넘어가면 안됩니다.";
            } else if(result === ""){
                idDiv.previousElementSibling.className = "form-control is-valid";
                idDiv.className = "valid-feedback";
                idDiv.innerText = "사용가능한 아이디입니다.";
            } else {
                idDiv.previousElementSibling.className = "form-control is-invalid";
                idDiv.className = "invalid-feedback"
                idDiv.innerText = "중복된 아이디입니다. 사용이 불가능합니다.";
            }
        }
    });
}
// PWD 보안 강함, 보통, 약함
function pwdCheck(pwd){
    const pwdDiv = document.getElementById("pwdDiv");
    if(pwd === ""){
        pwdDiv.previousElementSibling.className = "form-control is-invalid";
        pwdDiv.className = "invalid-feedback"
        pwdDiv.style.color = "red";
        pwdDiv.textContent = "입력해주세요.";
    } else if(strongPassword.test(pwd)){
        pwdDiv.previousElementSibling.className = "form-control is-valid";
        pwdDiv.className = "valid-feedback";
        pwdDiv.style.color = "green";
        pwdDiv.textContent = "강함";
    } else if(mediumPassword.test(pwd)){
        pwdDiv.previousElementSibling.className = "form-control is-valid";
        pwdDiv.className = "valid-feedback";
        pwdDiv.style.color = "blue";
        pwdDiv.textContent = "보통";
    } else {
        pwdDiv.previousElementSibling.className = "form-control is-invalid";
        pwdDiv.className = "invalid-feedback"
        pwdDiv.style.color = "red";
        pwdDiv.textContent = "위험(영문, 특수문자, 숫자를 1가지 이상씩 넣어주세요)";
    }
}
// PWDChk 일치 확인
function pwdChkCheck(pwdChk){
    const pwd = document.getElementById("pwdInput");
    const pwdChkDiv = document.getElementById("pwdChkDiv");
    if(pwd.value !== pwdChk){
        pwdChkDiv.previousElementSibling.className = "form-control is-invalid";
        pwdChkDiv.className = "invalid-feedback"
        pwdChkDiv.style.color = "red";
        pwdChkDiv.textContent = "비밀번호가 일치하지 않습니다.";
    } else {
        pwdChkDiv.previousElementSibling.className = "form-control is-valid";
        pwdChkDiv.className = "valid-feedback";
        pwdChkDiv.style.color = "green";
        pwdChkDiv.textContent = "비밀번호가 일치합니다.";
    }
}

// SMS 인증 번호 전송
function SMSFormal(){
    const keyCheckForm = document.getElementById("keyCheckForm");
    const keyHiddenInput = document.getElementById("keyNumber");
    let hp = document.getElementById("hpInput").value;
    if(!regPhone.test(hp)){
        alert("번호입력이 잘못되었습니다. 다시 입력해주세요.(ex 010-1234-5678)");
    } else {
        $.ajax({
            url: '/smsApi/send-one/' + hp.replace(/-/g, ''),
            type: 'GET',
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert(result); // 인증번호 가져온거 alert 띄워줌
                alert("인증번호가 발송되었습니다.");
                keyCheckForm.className = "";
                keyHiddenInput.value = result;
            }
        });
    }
}
// SMS 인증 번호 일치 확인
function SMSFormalCheck() {
    const keyHiddenInput = document.getElementById("keyNumber");
    const formalInput = document.getElementById("formalInput");
    const formalDiv = document.getElementById("formalDiv");
    if(keyHiddenInput.value === formalInput.value) {
        formalDiv.style.color = "green";
        formalDiv.textContent = "비밀번호가 일치합니다.";
    } else {
        formalDiv.style.color = "red";
        formalDiv.textContent = "비밀번호가 일치하지 않습니다.";
    }
}

// Email urlSelect 시 동작 함수
function urlSelected() {
    const urlSelect = document.getElementById("urlSelect");
    const urlCodeInput = document.getElementById("urlCodeInput");
    if(urlSelect.value === "0"){
        urlCodeInput.value = "";
        urlCodeInput.focus();
        return false;
    } else {
        urlCodeInput.value = urlSelect.value;
        return false;
    }
}

/* checkbox 한번에 체크 함수 */
function allAgreeCheck() {
    const allAgree = document.getElementById("allAgree");
    const useAgree = document.getElementById("useAgree");
    const dataAgree = document.getElementById("dataAgree");
    if(allAgree.checked){
        useAgree.checked = true;
        dataAgree.checked = true;
    } else {
        useAgree.checked = false;
        dataAgree.checked = false;
    }
}
/* checkbox 제거 시에 all체크 제거 함수 */
function agreeCheck() {
    const allAgree = document.getElementById("allAgree");
    const useAgree = document.getElementById("useAgree");
    const dataAgree = document.getElementById("dataAgree");
    if(!useAgree.checked){ allAgree.checked = false; }
    else { allAgree.checked = dataAgree.checked; }
}

// 우편번호 버튼 클릭 기능
function addressSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = ''; // 최종 주소 변수
            var extraAddr = ''; // 조합형 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                fullAddr = data.roadAddress;

            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                fullAddr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
            if(data.userSelectedType === 'R'){
                //법정동명이 있을 경우 추가한다.
                if(data.bname !== ''){
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if(data.buildingName !== ''){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('addressInput').value = data.zonecode; //5자리 새우편번호 사용
            document.getElementById('subAddressInput').value = fullAddr;

            // 커서를 상세주소 필드로 이동한다.
            document.getElementById('detailAddressInput').focus();
        }
    }).open();
}

// Register 폼 제출 전 확인 script
function registerCheck() {
    // 아이디 입력 확인(30자 이하여야함)
    if (document.getElementById("IDdiv").className === "invalid-feedback"){
        document.getElementById("IDdiv").innerText = "사용가능한 아이디여야합니다.";
        document.getElementById("idInput").focus();
        return false;
    }

    // 패스워드 입력 확인(강함, 보통인지)
    if (document.getElementById("pwdDiv").className === "invalid-feedback") {
        document.getElementById("pwdDiv").previousElementSibling.className = "form-control is-invalid";
        document.getElementById("pwdDiv").className = "invalid-feedback"
        document.getElementById("pwdDiv").style.color = "red";
        document.getElementById("pwdDiv").textContent = "패스워드는 강함/보통 정도여야합니다.";
        document.getElementById("pwdDiv").focus();
        return false;
    }

    // 패스워드 Chk 확인
    if(document.getElementById("pwdInput").value !== document.getElementById("pwdChkInput").value){
        document.getElementById("pwdChkDiv").previousElementSibling.className = "form-control is-invalid";
        document.getElementById("pwdChkDiv").className = "invalid-feedback"
        document.getElementById("pwdChkDiv").style.color = "red";
        document.getElementById("pwdChkDiv").textContent = "비밀번호가 일치하지 않습니다.";
        document.getElementById("pwdChkInput").focus();
        return false;
    }

    // 휴대폰 인증번호 확인
    if(document.getElementById("keyCheckForm").className === "d-none") { // 휴대폰 인증 확인
        alert("휴대폰 인증을 해주세요");
        document.getElementById("hpInput").focus();
        return false;
    } else if(document.getElementById("keyNumber").value !== document.getElementById("formalInput").value) { // 인증번호 확인
        alert("인증번호를 확인해주세요.");
        const formalDiv = document.getElementById("formalDiv");
        formalDiv.style.color = "red";
        formalDiv.textContent = "인증번호를 확인해주세요.";
        document.getElementById("formalInput").focus();
        return false;
    }

}


//*** Category(Search) ***//
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
    const clothId = document.getElementById("clothId").value;

    // ajax로 Color/Size의 재고 가져와서 화면 보여주기
    $.ajax({
        type: 'GET',
        url: '/api/stocks/color/size?colorCode=' + colorSelector + '&sizeCode=' + sizeSelector + '&clothId=' + clothId,
        success: (result) => {
            //AJAX 성공시 실행 코드(* mainCategoriesSelector Class)
            document.getElementById("buyBtn").value = result.stock.stockId;
            document.getElementById("cartAddBtn").value = result.stock.stockId;

            const stockOrderAmountSelectBox = document.getElementById("stockOrderAmountSelectBox");
            stockOrderAmountSelectBox.innerHTML
                  = '<tr class="border-top-1-ccc">' +
                    '    <td><b>color/size</b></td>' +
                    '    <td>' +
                    '        <input type = "hidden" id="stockId_' + result.stock.stockId + '" value = "' + result.stock.stockId + '">' +
                    '        <b>' + result.color.colorName + '</b>/<b>' + result.size.sizeName + '</b>' +
                    '    </td>' +
                    '</tr>' +
                    '<tr>' +
                    '    <td><b>수량</b></td>' +
                    '    <td><input type = "number" class="w-50 py-1" id = "count_' + result.stock.stockId + '" min = "1"' +
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
// 한개의 stock 정보를 가지고 주문페이지로 이동
function stockOrderFormMove(id) {
    if(id === ''){ alert("구매할 상품을 선택해주세요."); return false; }

    const stockId = document.getElementById("stockId_" + id); // 재고 목록들(stockIds)
    const count = document.getElementById("count_" + id); // 주문할 수량 가져오기(counts)
    let stockIdList = []; let countList = [];
    stockIdList.push(stockId.value); countList.push(count.value);

    window.location.href="/guest/orderForm?stockIds=" + stockIdList + "&counts=" + countList;
}
// checkbox 선택 stockList 가지고 주문페이지로 이동
function stockListOrderFormMove() {
    const cartIds = document.getElementsByName("cartIds"); // 화면에 있는 모든 Checkbox(cartNums)
    let stockIdList = []; let countList = [];

    // checkBox에 체크되었는지 확인 후 stockIdList, countList 에 값 추가해주기
    for (let i = 0; i < cartIds.length; i++) {
        if (cartIds[i].checked === true) {
            let cartId = cartIds[i].value;
            const stockId = document.getElementById("stockId_" + cartId); // 재고 목록들(stockIds)
            const count = document.getElementById("count_" + cartId); // 주문할 수량 가져오기(counts)

            stockIdList.push(stockId.value);
            countList.push(count.value);
        }
    }

    window.location.href="/guest/orderForm?stockIds=" + stockIdList + "&counts=" + countList;
}

//*** Cart ***//
// 장바구니 추가
function cartAdd(id) {
    if(id === ''){ alert("장바구니에 추가할 상품을 선택해주세요."); return false; }

    const stockId = document.getElementById("stockId_" + id).value; // 재고 목록들(stockIds)
    const count = document.getElementById("count_" + id).value; // 주문할 수량 가져오기(counts)
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
function cartDel(cartId) {
    if(confirm(cartId + "번 장바구니를 정말 삭제하시겠습니까?")){
        // cart 1개 삭제하는 Ajax 함수
        $.ajax({
            type: 'DELETE',
            url: '/api/carts/' + cartId,
            success: (result) => {
                //AJAX 성공시 실행 코드
                alert(cartId + "번 장바구니가 삭제되었습니다.");
                // 화면에서 해당 요소 삭제해줌
                document.getElementById('tr_' + cartId).remove();
                // Total Price 업데이트
                totalPriceUpdate();
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    } else {
        alert(cartId + '번 삭제가 취소되었습니다.');
    }
}
// 장바구니 checkbox 선택목록 삭제 Or 장바구니 비우기
function cartAllOrCheckDel(switchStr) {
    let msg = '';
    if(switchStr === 'check'){ msg = "선택된 목록들을 정말 삭제하시겠습니까?" }
    else if(switchStr === 'all'){ msg = "장바구니를 정말 비우시겠습니까?" }

    if(confirm(msg)) {
        const cartIds = document.getElementsByName("cartIds"); // 화면에 있는 모든 Checkbox(cartNums)
        let cartDelList = []; // 삭제된 cartNums

        // checkBox에 체크되었는지 확인 후 삭제
        for (let i = 0; i < cartIds.length; i++) {
            if (switchStr === 'check') {
                if (cartIds[i].checked === true) {
                    let cartId = cartIds[i].value;
                    cartDelAjax(cartId);
                    cartDelList.push(cartId);
                }
            } else if (switchStr === 'all') {
                let cartId = cartIds[i].value;
                cartDelAjax(cartId);
                cartDelList.push(cartId);
            }
        }

        // 삭제되었음을 alert 띄워줌
        alert(cartDelList + "번 장바구니가 삭제되었습니다.");

        // 장바구니의 모든 목록을 삭제해준 것이라면 장바구니 목록이 없다는 Text를 띄워줌
        if(cartDelList.length === cartIds.length){
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
    function cartDelAjax(cartId){
        $.ajax({
            type: 'DELETE',
            url: '/api/carts/' + cartId,
            success: (result) => {
                //AJAX 성공시 실행 코드
            }, error:function(e) {
                alert("error: " + e);
            }
        });
    }
}
// 장바구니 checkBox 버튼 클릭 시 totalPrice 바꿔주기
function cartTotalPriceUpdate(checkType) {
    // 값을 넣어줄 Block 들
    const cartSelectTotalText = document.getElementById("cartSelectTotalText"); // 상품구매금액 0 + 배송비 0 = 합계: KRW 0
    const totalSalePriceView = document.getElementById("totalSalePriceView");   // Total 상품 금액
    const totalDeliPriceView = document.getElementById("totalDeliPriceView");   // Total 배송비 금액
    const totalPriceView = document.getElementById("totalPriceView");           // Total 구매 금액

    // 값을 가져올 Block 들
    const cartIds = document.getElementsByName("cartIds"); // 화면에 있는 모든 Checkbox(cartNums)
    let totalSalePrice = 0, totalDeliPrice = 0, totalPrice = 0;

    // 모두 체크가 되어있다면 전체 CheckBox 체크 해주기
    if(checkType === 'allCheck') { allCheck('cart'); }
    else { allCheckRemove('cart'); }

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < cartIds.length; i++) {
        if(cartIds[i].checked === true){
            const salePrice = document.getElementById("salePrice_" + cartIds[i].value).textContent.split(' ')[1]; // 장바구니 상품 단가 금액
            const cartCount = document.getElementById("count_" + cartIds[i].value).value; // 장바구니 상품 수량
            const deliPrice = parseInt(document.getElementById("deliPrice_" + cartIds[i].value).textContent.replace(/원/, '')); // 장바구니 상품 배송비
            totalSalePrice  += salePrice * cartCount; // 상품단가 * 수량
            totalDeliPrice  += deliPrice;             // 상품 배송비
            totalPrice      += salePrice * cartCount + deliPrice;     // 상품 가격 + 배송비
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
function CountChangeUpdate(updateId, page) {
    const Count = document.getElementById(updateId).value;
    const salePrice = document.getElementById("salePrice_" + (updateId.split('_')[1])).textContent.split(' ')[1];
    const deliPrice = document.getElementById("deliPrice_" + (updateId.split('_')[1])).textContent.replace(/원/, '');
    const totalPrice = document.getElementById("totalPrice_" + (updateId.split('_')[1]));
    const plusPayBlock = document.getElementById("plusPay_" + (updateId.split('_')[1]));
    const plusPay = (plusPayBlock.textContent === '0원')?0:salePrice/100;
    totalPrice.innerText = 'KSW ' + (salePrice * Count + parseInt(deliPrice));
    plusPayBlock.innerText = (plusPay * Count) + '원';

    if(page === 'cartList') { cartTotalPriceUpdate('oneCheck'); }
    else if(page === 'orderForm') { orderTotalPriceUpdate(); }
}

//*** OrderForm ***//
// 주문 Form에서 수량 증감시 TotalPrice 업데이트
function orderTotalPriceUpdate() {
    // 값을 넣어줄 Block 들
    const orderSelectTotalText = document.getElementById("orderSelectTotalText");   // 상품구매금액 0 + 배송비 0 = 합계: KRW 0
    const totalOrderPriceView = document.getElementById("totalOrderPriceView");     // Total 상품 금액
    const totalUseMileageView = document.getElementById("totalUseMileageView");     // Total 배송비 금액
    const totalPriceView = document.getElementById("totalPriceView");               // Total 구매 금액
    const finishTotalPriceView = document.getElementById("finishTotalPriceView");   // 최종 Total 구매 금액
    const addStockMileage = document.getElementById("addStockMileage");             // 추가 될 적립금
    const totalMileage = document.getElementById("totalMileage");                   // 총 추가 될 적립금

    // 값을 가져올 Block 들
    const stockIds = document.getElementsByName("stockIds"); // 화면에 있는 모든 Checkbox(cartNums)
    const useMileage = document.getElementById("useMileage").value; // 사용가능한 적립금 가져오기
    let totalSalePrice = 0, totalDeliPrice = 0, totalPrice = 0, mileage = 0;

    // checkBox에 체크되었는지 확인 후 삭제
    for (let i = 0; i < stockIds.length; i++) {
        const salePrice = document.getElementById("salePrice_" + stockIds[i].value).textContent.split(' ')[1]; // 상품 단가 금액
        const orderCount = document.getElementById("count_" + stockIds[i].value).value; // 상품 수량
        const deliPrice = parseInt(document.getElementById("deliPrice_" + stockIds[i].value).textContent.replace(/원/, '')); // 상품 배송비
        const stockMileage = parseInt(document.getElementById("plusPay_" + stockIds[i].value).textContent.replace(/원/, '')); // 상품 적립금
        totalSalePrice  += salePrice * orderCount;              // 상품단가 * 수량
        totalDeliPrice  += deliPrice;                           // 상품 배송비
        totalPrice      += salePrice * orderCount + deliPrice;  // 상품 가격 + 배송비
        mileage         += stockMileage;                        // 상품 적립금
    }

    // Total 값 Text 띄워주기
    orderSelectTotalText.innerText = '상품구매금액 ' + totalSalePrice +
        ' + 배송비 ' + totalDeliPrice +
        ' = 합계: KRW ' + totalPrice;
    totalOrderPriceView.innerText = 'KRW ' + totalPrice;
    totalUseMileageView.innerText = '- KRW ' + (useMileage === ''?0:useMileage);
    totalPriceView.innerText = '= KRW ' + (totalPrice-useMileage);
    finishTotalPriceView.innerText = (totalPrice-useMileage) + '';
    addStockMileage.innerText = mileage + '원';
    totalMileage.innerText = mileage + '원';
}
// 배송지 radioButton 별 작동 함수
function newDeliveryAddress() {
    document.getElementById("newReset").click();
}
function getMyDeliveryAddress(userId) {
    $.ajax({
        url: '/api/users/' + userId,
        type: 'GET',
        success: (result) => {
            //AJAX 성공시 실행 코드
            const addressInput = document.getElementById("addressInput");
            const subAddressInput = document.getElementById("subAddressInput");
            const detailAddressInput = document.getElementById("detailAddressInput");
            const baseHPInput = document.getElementById("baseHPInput");
            const secondHPInput = document.getElementById("secondHPInput");
            const thirdHPInput = document.getElementById("thirdHPInput");
            const idNameInput = document.getElementById("idNameInput");
            const urlCodeInput = document.getElementById("urlCodeInput");

            addressInput.value = result.addressNum;
            subAddressInput.value = result.addressSub;
            detailAddressInput.value = result.addressDetail;
            baseHPInput.value = result.hp.split('-')[0];
            secondHPInput.value = result.hp.split('-')[1];
            thirdHPInput.value = result.hp.split('-')[2];
            idNameInput.value = result.emailIdName;
            urlCodeInput.value = result.emailUrlCode;

        }
    });
}

// 결제 적립금 지정
function useMileageInput() {
    const useMileage = document.getElementById("useMileage"); // 사용가능한 적립금 가져오기
    const usableMileage = parseInt(document.getElementById("usableMileage").textContent); // 사용가능한 적립금 가져오기
    const totalOrderPriceView = parseInt(document.getElementById("totalOrderPriceView").textContent.split(' ')[1]);
    if(!numberCheck.test(useMileage.value) && useMileage.value != ''){
        alert("숫자만 입력 가능합니다.");
        useMileage.value = '';
    }
    if(useMileage.value > totalOrderPriceView){
        useMileage.value = totalOrderPriceView;
    }
    if(useMileage.value > usableMileage){
        useMileage.value = usableMileage;
    }

    orderTotalPriceUpdate();
}
function useMileageCheck() {
    const useMileage = document.getElementById("useMileage"); // 사용가능한 적립금 가져오기
    if(useMileage.value < 100 && useMileage.value !== '' && useMileage.value !== '0'){
        alert("적립금은 100원 이상부터 사용가능합니다.");
        useMileage.value = 0;
        orderTotalPriceUpdate();
    }
}