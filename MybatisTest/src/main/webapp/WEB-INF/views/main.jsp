<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*,com.test.mybatis.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>main</title>
</head
>
<body>
<form  method="post" action="/mybatis/insert">
	<table border="1">
		<tr>
			<td>이름</td>
			<td>이메일</td>
			<td>전화번호</td>
			<td></td>
		</tr>

		<tr>
			<td><input type="text" name="name" id="name"></td>
			<td><input type="text" name="email" id="email"></td>
			<td><input type="text" name="phone" id="phone"></td>
			<td colspan="2" align="center">
			<input type="submit" value="추가">
			</td>

		</tr>
		<!-- result는 contoller의 addObject로 부터 가져온다. -->
		<c:forEach items="${result}" var="member">
			<tr>
				<td><input type="text" name="name" id="name" value="${member._name}" readonly="readonly"></td>
				<td><input type="text" name="email" id="email" value="${member._email}"></td>
				<td><input type="text" name="phone" id="phone" value="${member._phone}"></td>
			</tr>
		</c:forEach>
		<tr>
		<td><input type="button" value="수정" onclick="location.href='/mybatis/updateform'"></td>
		<td><input type="button" value="삭제"></td>
		</tr>
	</table>

</form>
</body>
</html>