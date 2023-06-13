// 상품 검색
function clothSearch() {
    const guestSearchText = document.getElementById("guestSearchText").value;
    window.location.href="/guest/category?category=search&searchText=" + guestSearchText;
}