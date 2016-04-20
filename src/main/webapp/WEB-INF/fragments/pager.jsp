<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<nav>
	<ul class="pagination pagination-lg">
		<c:choose>
			<c:when test="${page.hasPrevious()}">
				<li>
					<spring:url value="" var="prev">
						<spring:param name="q" value="${query}" />
						<spring:param name="page" value="${page.number - 1}"></spring:param>
						<spring:param name="size" value="${page.size}"></spring:param>
					</spring:url>
					<a href="${prev}">Previous</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="disabled"> <a href="#"> Previous</a> </li>
			</c:otherwise>
		</c:choose>


		<c:forEach var="index" begin="1" end="${page.totalPages}">
			<c:choose>
				<c:when test="${(index-1) == page.number}">
					<li class="active"><a href="#">${index}</a></li>
				</c:when>
				<c:otherwise>
					<spring:url value="" var="next">
						<spring:param name="q" value="${query}" />
						<spring:param name="page" value="${index-1}"></spring:param>
						<spring:param name="size" value="${page.size}"></spring:param>
					</spring:url>
					<li><a href="${next}">${index}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:choose>
			<c:when test="${page.hasNext()}">
					<li>
						<spring:url value="" var="next">
							<spring:param name="q" value="${query}" />
							<spring:param name="page" value="${page.number + 1}"></spring:param>
							<spring:param name="size" value="${page.size}"></spring:param>
						</spring:url>
						<a href="${next}">Next</a>
					</li>
			</c:when>
			<c:otherwise>
				<li class="disabled"> <a href="#"> Next</a> </li>
			</c:otherwise>
		</c:choose>
	</ul>
</nav>
