<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<%@ include file="../top.jsp"%>
<!-- mypage에서 회원탈퇴 페이지 -->
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2"
	crossorigin="anonymous"></script>
<div class="container" align="center"
	class="col-lg-9 my-4 mb-4 border border-dark rounded-lg">
	<h2 class="text-left">
		<b>기본 정보</b>
	</h2>
<c:if test="${empty cartList}">
<table class="table table-bordered">
	<tr align="center">
	<td><h2>결제하실 상품이 없습니다.</h2></td>
	</tr>
	<tr  align="center">
	<td><button class="btn btn-dark mb-3" onClick="location.href='goProduct.product'" style="width: 756px; background:#091835;">용품 대여하러  가기</button></td>
	</tr>
</table>
</c:if>

	<c:forEach items="${cartList}" var="mdto">
		<table class="table table-bordered">
			<tr>
				<th scope="col" width="30%" class="text-center">상점명</th>
				<td width="70%">WithCar(${mdto.agencyDTO.agency_name})</td>
			</tr>

			<tr>
				<th scope="col" width="30%" class="text-center">용품명</th>
				<td width="200" width="70%">${mdto.productDTO.prod_name }</td>

			</tr>
			<tr>
				<th scope="col" width="30%" class="text-center">대여기간</th>
				<td width="200" width="70%">${mdto.cart_from }~ ${mdto.cart_to }</td>

			</tr>
			<tr>
				<th scope="col" width="30%" class="text-center">물품갯수</th>
				<td width="200" width="70%">${mdto.cart_prodCount}개</td>
			</tr>
			
			<tr>
				<th scope="col" width="30%" class="text-center">
				사용할 포인트
				</th>
				<td width="200" width="70%" class="text-success">
				<form action="myPageCheckOut.myPage" method="post">
				 <input type="hidden" name= "cart_num" value="${mdto.cart_num }">
       			 <input type="hidden" name= "rental_price" value="${mdto.productDTO.prod_price}">
       			  <input type="hidden" name= "mem_num" value="${mdto.memberDTO.mem_num}">
       			  
       			  <c:if test="${mdto.memberDTO.mem_point <= 0}">
        		  사용 가능한 포인트가 없습니다.<br>
        		  <input type="number" name="rental_usePoint"
								value="0" pattern="[0-9]+" min="0" max="0">
        		 </c:if>
        		 
        		 <c:if test="${mdto.memberDTO.mem_point > 0}">
        		  사용가능 포인트 : ${mdto.memberDTO.mem_point}<br>
        		  <input type="number" name="rental_usePoint"
								value="0" pattern="[0-9]+" min="0" max="3000">
								</c:if>
        		 <button class="btn btn-lg btn-dark btn-block text-uppercase" style="background:#091835"
			type="submit" >결제하기</button>
         </form>
				</td>
			</tr>
		</table>
	</c:forEach>
</div>

<%@ include file="../bottom.jsp"%>