<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"
%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath }" />

<c:set var="articlesList" value="${articlesMap.articlesList }" />
<c:set var="totArticles" value="${articlesMap.totArticles }" />
<c:set var="section" value="${articlesMap.section }" />
<c:set var="pageNum" value="${articlesMap.pageNum }" />

<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Articles List 조회결과</title>
	<style>
		.cls1{margin: auto; text-decoration: none;}
		.cls2{margin: auto; text-align: center; font-size: 22px;}
		table{margin: auto; width: 80%; text-align: center; 
					border: 1px solid black; border-collapse: collapse;}
					
		tr,td{border: 1px solid black;}
		caption{font-size:22px; }
		
		.no-uline{text-decoration: none;}
		.sel-page{text-decoration: none; color:red; }
		.cls3{margin: auto; text-align: center; text-decoration: none;}
		
	</style>
</head>
<body>
	<table>
		<caption>Articles List 조회결과</caption>
		<tr bgcolor="lightgreen">
			<td>글번호</td>
			<td>작성자</td>
			<td>제목</td>
			<td>작성일</td>
		</tr>
		<c:choose>
			<c:when test="${articlesList == null }">
			<tr height="10">
				<td colspan="4"><span style="font-size: 9pt;">등록된 글이 없습니다.</span></td>
			</tr>
			</c:when>
			<c:when test="${articlesList != null }">
				<c:forEach var="article" items="${articlesList }" varStatus="articleNum">
				<tr>
					<td width="5%">${articleNum.count }</td>
					<td width="10%">${article.id }</td>
					<td align="left" width="35%">
						<span style="padding-right: 30px"></span>
						<c:choose>
							<c:when test="${article.level > 1 }">
								<c:forEach begin="1" end="${article.level }" step="1">
									<span style="padding-left: 20px"></span>
								</c:forEach>
								<span style="font-size:12px;">[답변]</span>
								<a class="cls1" href="${contextPath }/board04/viewArticle.do?
																			articleNO=${article.articleNO}">${article.title }</a>
							</c:when>
							<c:otherwise>
								<a class="cls1" href="${contextPath }/board04/viewArticle.do?
																			articleNO=${article.articleNO}">${article.title }</a>
							</c:otherwise>
						</c:choose>
					</td>
					<td width="10%">
						<fmt:formatDate value="${article.writeDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
				</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</table>
	<br>
	<div class="cls3">
		<c:if test="${totArticles != null }" >
			<c:choose>
				<c:when test="${totArticles > 100 }">
					<c:forEach var="page" begin="1" end="10" step="1">
						<c:if test="${section>1 && page==1 }">
							<a class="no-uline" href="${contextPath }/board04/listArticles.do?
							section=${section-1 }&pageNum=${(section-1)*10+1 }">&nbsp; pre</a>
						</c:if>
						<a class="no-uline" href="${contextPath }/board04/listArticles.do?
							section=${section }&pageNum=${(section-1)*10+page }"></a>
					</c:forEach>
				</c:when>
				
				<c:when test="${totArticles == 100 }" >
					<c:forEach var="page" begin="1" end="10" step="1">
						<a class="no-uline" href="#">${page }</a>
					</c:forEach>
				</c:when>
				
				<c:when test="${totArticles < 100 }">
					<c:forEach var="page" begin="1" end="${totArticles/10 + 1 }" step="1">
						<c:choose>
							<c:when test="${page==pageNum }">
									<a class="sel-page" href="${contextPath }/board04/listArticles.do?
									section=${section}&pageNum=${page}">${page }</a>
							</c:when>
							<c:otherwise>
								<a class="no-uline" href="${contextPath }/board04/listArticles.do?
									section=${section}&pageNum=${page}">${page }</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
	</div>
	<br><br>
	<p class="cls2">
		<a class="cls1" href="${contextPath }/board04/articleForm.do">글쓰기</a></p>
</body>
</html>