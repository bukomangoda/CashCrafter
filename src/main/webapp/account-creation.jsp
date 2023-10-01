<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	
	<title>Account Creation</title>
	
	<!-- Include jQuery -->
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	
	<!-- Include Bootstrap JavaScript -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	
	<!-- Font Icon -->
	<link rel="stylesheet" href="fonts/material-icon/css/material-design-iconic-font.min.css">
		
	<!-- Include Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
	<!-- Main CSS -->
	<link rel="stylesheet" href="css/style.css">	
</head>

<body>
<%-- <%
    HttpServletRequest newRequest = (HttpServletRequest) pageContext.getRequest();

    Cookie[] cookies = newRequest.getCookies();

    String usernameCookieName = "username";

    String usernameCookieValue = null;

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(usernameCookieName)) {
                usernameCookieValue = cookie.getValue();
            }  
        }
    }

    if (usernameCookieValue == null) {
    	 response.sendRedirect("login.jsp");
    }
    
%> --%>
 <jsp:include page="header.jsp" />
	<div class="main" style="background: #8ad0da69">
		<!-- Account form -->
		<section class="signup">
			<div class="container" style="margin-top: 10%; background: #4b6e7942;">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title">Account Creations</h2>
						<form method="post" class="register-form" id="register-form" action="CreateAccount">
							<div class="form-group" style="margin-bottom: 0px">
								<select class="form-control" id="accountType" name="accountType" onchange="showFixedDepositMessage()">
									<option value="default">Select account type</option>
	                                   <option value="savings">Savings</option>
	                                   <option value="loans">Loans</option>
	                                   <option value="fd">Fixed Deposits</option>
	                             </select>
							</div>
							<span id="fixedDepositMessage" style="display: none; color: red;">
    							<small>*Fixed deposits are for (12) months. 5.5% interest per annum will be added.</small>
							</span>

							<div class="form-group" style="margin-top: 25px">
								<label for="deposit-amount"><i class="zmdi zmdi-money-box zmdi-hc-2x px-1"></i></label> 
								<input type="number" name="deposit-amount" id="deposit-amount" step="0.01" min="0.01" placeholder="Enter initial deposit amount."/>
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signup" id="signup"
									class="form-submit" value="Create" />
							</div>
						</form>
					</div>
					<div class="signup-image">
						<figure>
							<img src="images/account-creation.png" alt="sing up image">
						</figure>
					</div>
				</div>
			</div>
		</section>
	</div>
	
	<script>
	    function showFixedDepositMessage() {
	        var accountType = document.getElementById("accountType").value;
	        var fixedDepositMessageDiv = document.getElementById("fixedDepositMessage");
	
	        if (accountType === "fd") {
	            // Show the message if "Fixed Deposits" is selected
	            fixedDepositMessageDiv.style.display = "block";
	        } else {
	            // Hide the message for other account types
	            fixedDepositMessageDiv.style.display = "none";
	        }
	    }
	</script>
	
</body>
</html>
