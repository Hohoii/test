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
			<td>�̸�</td>
			<td>�̸���</td>
			<td>��ȭ��ȣ</td>
			<td></td>
		</tr>
		<!-- result�� contoller�� addObject�� ���� �����´�. -->
		<c:forEach items="${result}" var="member">
			<tr>
				<td><input type="text" name="name" id="name" value="${member._name}" readonly="readonly"></td>
				<td><input type="text" name="email" id="email" value="${member._email}"></td>
				<td><input type="text" name="phone" id="phone" value="${member._phone}"></td>
				<td><input type="button" value="����" onclick="location.href='/mybatis/update?name=${member._name}'"> 
				<input type="button" value="����"></td>
			</tr>
		</c:forEach>
	</table>

</form>
</body>
</html>