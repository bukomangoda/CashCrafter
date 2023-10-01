 <%
    HttpServletRequest newRequest = (HttpServletRequest) pageContext.getRequest();

    Cookie[] cookies = newRequest.getCookies();

    String usernameCookieName = "username"; 
    String accountTypeCookieName = "accountType";
    String accountNumberCookieName = "accountNumber";
    
    String username = null;
    String accountType = null;
    String accountNumber = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(usernameCookieName)) {
                username = cookie.getValue();
            }
            if (cookie.getName().equals(accountTypeCookieName)) {
            	accountType = cookie.getValue();
            }
            if (cookie.getName().equals(accountNumberCookieName)) {
            	accountNumber = cookie.getValue();
            }
        }
    }
%> 
<nav
		class="navbar navbar-expand-lg bg-secondary text-uppercase fixed-top"
		id="mainNav">
	 <div class="container" style="width: 100%">
			<a class="navbar-brand" href="#page-top">FMS</a>
			<button
				class="navbar-toggler text-uppercase font-weight-bold bg-primary text-white rounded"
				type="button" data-bs-toggle="collapse"
				data-bs-target="#navbarResponsive" aria-controls="navbarResponsive"
				aria-expanded="false" aria-label="Toggle navigation">
				Menu <i class="fas fa-bars"></i>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
				<ul class="navbar-nav ms-auto">
					 <% if (accountNumber == null) { %>
					 	<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="account-creation.jsp">Account Creation</a></li>
					 <% } %>
					 
					  <% if (accountType == "loans") { %>
					 	<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="loan-payment.jsp">Loan Re-payment</a></li>
					 <% } %>
					 
					  <% if (accountType == "fd") { %>
					 	<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="fixed-deposit.jsp">Fixed Deposit</a></li>
					 <% } %>
					
					<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="transaction-operations.jsp">Transaction Operations</a></li>
					<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="balance-inquiry.jsp">Balance Inquiry</a></li>
					<li class="nav-item mx-0 mx-lg-1"><a
						class="nav-link py-3 px-0 px-lg-3 rounded" href="login.jsp" onclick="logout()">Logout</a></li>
				</ul>
				<p style="text-transform: none;">Hello, <%= username %>!</p>	
			</div>
		</div>
</nav>

<script>
function logout() {
    // Get all cookies
    var cookies = document.cookie.split(';');

    // Iterate through cookies and delete them
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf('=');
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    }

    // Redirect to the logout page
    window.location.href = "login.jsp";
}
</script>
	
	
	